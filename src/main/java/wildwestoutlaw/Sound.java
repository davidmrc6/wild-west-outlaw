package wildwestoutlaw;

import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;

public class Sound {

    URL soundURL[] = new URL[30];
    private final AudioFormat[] formats = new AudioFormat[30];
    private final byte[][] data = new byte[30][];
    private Clip music;
    private boolean errorReported = false;

    // Sounds load and play on this thread, so the game never waits for them
    private final ExecutorService soundThread = Executors.newSingleThreadExecutor(task -> {
        Thread thread = new Thread(task, "sound");
        thread.setDaemon(true);
        return thread;
    });

    public Sound() {

        soundURL[0] = getClass().getResource("/sound/background music.wav");
        soundURL[1] = getClass().getResource("/sound/coin get.wav");
        soundURL[2] = getClass().getResource("/sound/coin given.wav");
        soundURL[3] = getClass().getResource("/sound/death explosion.wav");
        soundURL[4] = getClass().getResource("/sound/dialogue.wav");
        soundURL[5] = getClass().getResource("/sound/game over.wav");
        soundURL[6] = getClass().getResource("/sound/game won.wav");
        soundURL[7] = getClass().getResource("/sound/hit explosion.wav");
        soundURL[8] = getClass().getResource("/sound/player damaged.wav");
        soundURL[9] = getClass().getResource("/sound/player shot.wav");
        soundURL[10] = getClass().getResource("/sound/walk on sand.wav");
        soundURL[11] = getClass().getResource("/sound/kill monsters.wav");
        soundURL[12] = getClass().getResource("/sound/menu button.wav");
        soundURL[13] = getClass().getResource("/sound/choose button.wav");

        soundThread.execute(this::loadAll);
    }

    public void playEffect(int i) {
        soundThread.execute(() -> {
            Clip clip = open(i);
            if (clip != null) {
                clip.start();
            }
        });
    }

    public void playMusic(int i) {
        soundThread.execute(() -> {
            stopMusicNow();
            music = open(i);
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
        for (int i = 0; i < soundURL.length; i++) {
            if (soundURL[i] != null) {
                try (AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i])) {
                    formats[i] = ais.getFormat();
                    data[i] = ais.readAllBytes();
                } catch (Exception e) {
                    reportError(soundURL[i], e);
                }
            }
        }
        try {
            AudioSystem.getClip().close();
        } catch (Exception e) {
            reportError(null, e);
        }
    }

    Clip open(int i) {
        if (data[i] == null) {
            return null;
        }
        try {
            Clip clip = AudioSystem.getClip();
            clip.open(formats[i], data[i], 0, data[i].length);

            if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                volume.setValue(-20.0f);
            }

            // Give the audio line back once the clip has finished or was stopped
            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    clip.close();
                }
            });
            return clip;

        } catch (Exception e) {
            reportError(soundURL[i], e);
            return null;
        }
    }

    // e.g. no audio device: the game keeps running without sound
    private void reportError(URL sound, Exception e) {
        if (!errorReported) {
            System.err.println("Could not play sound" + (sound != null ? " " + sound : "") + ": " + e);
            errorReported = true;
        }
    }
}
