package main;

import javax.sound.midi.*;
import java.util.ArrayList;

public class MidiSoundPlayer {

    private final ArrayList<Integer> keys;
    private Synthesizer synthesizer;
    private final MidiChannel channel;

    private final int inputValueMaximum;
    private static int CACHED_INDEX = -1;

    public MidiSoundPlayer(int inputValueMaximum) {
        this.inputValueMaximum = inputValueMaximum;

        try {
            synthesizer = MidiSystem.getSynthesizer();
            synthesizer.open();
        } catch (MidiUnavailableException e) {
            e.printStackTrace();
        }

        channel = synthesizer.getChannels()[0];

        Instrument[] instruments = synthesizer.getDefaultSoundbank().getInstruments();
        if (CACHED_INDEX == -1) {
            boolean found = false;
            int index;
            for (index = 0; index < instruments.length; index++) {
                Instrument instrument = instruments[index];
                if (instrument.getName().equals("Electric Grand Piano")) {
                    found = true;
                    break;
                }
            }
            if (!found)
                index = 2;

            CACHED_INDEX = index;
        }

        channel.programChange(instruments[CACHED_INDEX].getPatch().getProgram());

        //Set up keys
        keys = new ArrayList<>();
        for (int i = 24; i < 108; i+=12) {
            keys.add(i);
            keys.add(i + 2);
            keys.add(i + 4);
            keys.add(i + 5);
            keys.add(i + 7);
            keys.add(i + 9);
            keys.add(i + 11);
        }
    }

    private int convertToMajor(int v) {
        float n = (float) v / (float) inputValueMaximum;
        int index = (int) (n * (float) keys.size());
        index = Math.max(1, Math.min(107, index));
        return keys.get(index);
    }

    public void makeSound(int value) {
        int note = convertToMajor(value);
        channel.noteOn(note, 25);
    }
}
