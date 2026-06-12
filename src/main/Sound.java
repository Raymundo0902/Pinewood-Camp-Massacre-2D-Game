package main;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.net.URL;

public class Sound {

    Clip clip;
    URL soundURL[] = new URL[30];
    FloatControl fc;
    int volumeScale = 3;
    float volume;

    public Sound() {

        // NEW CONFIGURATION

        soundURL[0] = getClass().getResource("/sound/gameintro.wav");
        soundURL[1] = getClass().getResource("/sound/nightAmbience.wav");
        soundURL[2] = getClass().getResource("/sound/grabkeys.wav");
        soundURL[3] = getClass().getResource("/sound/osstartup.wav");
        soundURL[4] = getClass().getResource("/sound/typingSE.wav");
        soundURL[5] = getClass().getResource("/sound/dialogueBox.wav");
        soundURL[6] = getClass().getResource("/sound/arcadeSelect.wav");
        soundURL[7] = getClass().getResource("/sound/err.wav");
        soundURL[8] = getClass().getResource("/sound/receiveDamage.wav");
        soundURL[9] = getClass().getResource("/sound/grabBag.wav");
        soundURL[10] = getClass().getResource("/sound/unlock.wav");
        soundURL[11] = getClass().getResource("/sound/Elevatormusic.wav");
        soundURL[12] = getClass().getResource("/sound/cursorSelect.wav");
        soundURL[13] = getClass().getResource("/sound/monchase.wav");
        soundURL[14] = getClass().getResource("/sound/monchaseclose.wav");
        soundURL[15] = getClass().getResource("/sound/loudknocking.wav");
        soundURL[16] = getClass().getResource("/sound/cursorMovement.wav");
    }

    public void setFile(int i) {
        try {

            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
            clip = AudioSystem.getClip();
            clip.open(ais);
            // accepts the range -80f to 6f. -80f being no sound.
            fc = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
            checkVolume();

        } catch (Exception e) {
        }
    }
    public void play () {

        clip.start();
    }
    public void loop() {

        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }
    public void stop() {

        clip.stop();
    }
    public void checkVolume() {

        switch(volumeScale) {
            // these adjustments make it change proportionally.
            case 0: volume = -80f; break;
            case 1: volume = -20f; break;
            case 2: volume = -12f; break;
            case 3: volume = -5f; break;
            case 4: volume = 1f; break;
            case 5: volume = 6f; break;
        }
        fc.setValue(volume);
    }
}
