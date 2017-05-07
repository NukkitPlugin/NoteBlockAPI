package nukkitplugin.noteblockapi.element;

import cn.nukkit.level.sound.NoteBoxSound;

public class Instrument { // because of the difference between a and b
	public static byte getInstrument(byte instrument) {
		switch (instrument) {
			case 1: // Double Bass (wood)
				return NoteBoxSound.INSTRUMENT_BASS;
			case 2: // Bass Drum (stone)
				return NoteBoxSound.INSTRUMENT_BASS_DRUM;
			case 3: // Snare Drum (sand)
				return NoteBoxSound.INSTRUMENT_TABOUR;
			case 4: // Click (glass)
				return NoteBoxSound.INSTRUMENT_CLICK;
			default: // Piano (air)
				return NoteBoxSound.INSTRUMENT_PIANO;
		}
	}
}
