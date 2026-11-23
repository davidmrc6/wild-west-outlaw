package wildwestoutlaw;

import java.io.InputStream;
import java.io.BufferedInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;

public class Sound {

    private static final String MUSIC = "/sound/background music.wav";
    private static final float VOLUME = -20.0f; // decibels

    private final Map<String, AudioFormat> formats = new HashMap<>();
    private final Map<String, byte[]> data = new HashMap<>();
    private Clip music;
    private boolean errorReported = false;

    // Sounds load and play on this thread, so the game never waits for them
    private final ExecutorService soundThread = Executors.newSingleThreadExecutor(task -> {
        Thread thread = new Thread(task, "sound");
        thread.setDaemon(true);
        return thread;
    });

    public Sound() {
        soundThread.execute(this::loadAll);
    }

    public void play(SoundEffect effect) {
        soundThread.execute(() -> {
            Clip clip = open(effect.path);
            if (clip != null) {
                clip.start();
            }
        });
    }

    public void playMusic() {
        soundThread.execute(() -> {
            stopMusicNow();
            music = open(MUSIC);
            if (music != null) {
                music.loop(Clip.LOOP_CONTINUOUSLY);
            }
        });
    }

    public void stopMusic() {
        soundThread.execute(this::stopMusicNow);
    }

    private void stopMusicNow() {
        if (music != null) {
            music.stop();
            music = null;
        }
    }

    // Loads every sound once and starts the audio system early (its first start is slow)
    private void loadAll() {
        load(MUSIC);
        for (SoundEffect effect : SoundEffect.values()) {
            load(effect.path);
        }
        try {
            AudioSystem.getClip().close();
        } catch (Exception e) {
            reportError(null, e);
        }
    }

    private void load(String path) {
        try (InputStream in = getClass().getResourceAsStream(path)) {
            if (in == null) {
                throw new IllegalStateException("Missing game file: " + path);
            }
            try (AudioInputStream ais = AudioSystem.getAudioInputStream(new BufferedInputStream(in))) {
                formats.put(path, ais.getFormat());
                data.put(path, ais.readAllBytes());
            }
        } catch (Exception e) {
            reportError(path, e);
        }
    }

    Clip open(String path) {
        byte[] sound = data.get(path);
        if (sound == null) {
            return null;
        }
        try {
            Clip clip = AudioSystem.getClip();
            clip.open(formats.get(path), sound, 0, sound.length);

            if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                volume.setValue(VOLUME);
            }

            // Give the audio line back once the clip has finished or was stopped
            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    clip.close();
                }
            });
            return clip;

        } catch (Exception e) {
            reportError(path, e);
            return null;
        }
    }

    // e.g. no audio device: the game keeps running without sound
    private void reportError(String path, Exception e) {
        if (!errorReported) {
            System.err.println("Could not play sound" + (path != null ? " " + path : "") + ": " + e);
            errorReported = true;
        }
    }
}
