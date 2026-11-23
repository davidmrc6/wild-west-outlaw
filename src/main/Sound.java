package main;

import java.net.URL;

import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

public class Sound {
    
    Clip clip;
    URL soundURL[] = new URL[30];

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
        
    }

    public void setFile(int i) {
        try {

            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
            clip = AudioSystem.getClip();
            clip.open(ais);

            FloatControl volume = (FloatControl) clip.getControl (FloatControl.Type.MASTER_GAIN);
            volume.setValue (-20.0f);

        } catch (Exception e) {}
    }

    public void play() {

        clip.start();
    }

    public void loop() {

        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stop() {

        clip.stop();
    }
}
