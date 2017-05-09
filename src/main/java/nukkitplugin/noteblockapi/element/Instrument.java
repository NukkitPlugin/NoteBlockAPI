package nukkitplugin.noteblockapi.element;

import static cn.nukkit.level.sound.NoteBoxSound.*;

public class Instrument { // because of the difference between a and b
	public static byte getInstrument(byte instrument) {
		switch (instrument) {
			case 1: // Double Bass (wood)
				return INSTRUMENT_BASS;
			case 2: // Bass Drum (stone)
				return INSTRUMENT_BASS_DRUM;
			case 3: // Snare Drum (sand)
				return INSTRUMENT_TABOUR;
			case 4: // Click (glass)
				return INSTRUMENT_CLICK;
			default: // Piano (air)
				return INSTRUMENT_PIANO;
		}
	}
}
