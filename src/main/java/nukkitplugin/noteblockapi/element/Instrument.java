package nukkitplugin.noteblockapi.element;

import cn.nukkit.level.sound.NoteBoxSound;

public class Instrument {
	public static byte getInstrument(byte instrument) {
		switch (instrument) {
			case 1:
				return NoteBoxSound.INSTRUMENT_BASS_DRUM;
			case 2:
				return NoteBoxSound.INSTRUMENT_CLICK;
			case 3:
				return NoteBoxSound.INSTRUMENT_TABOUR;
			case 4:
				return NoteBoxSound.INSTRUMENT_BASS;
			default:
				return NoteBoxSound.INSTRUMENT_PIANO;
		}
	}
}
