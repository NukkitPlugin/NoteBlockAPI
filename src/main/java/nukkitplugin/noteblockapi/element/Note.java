package nukkitplugin.noteblockapi.element;

import cn.nukkit.Player;
import cn.nukkit.level.Position;
import cn.nukkit.level.sound.NoteBoxSound;
import cn.nukkit.math.Vector3;

public class Note {
	private byte	instrument;
	private byte	key;

	public Note(byte instrument, byte key) {
		this.instrument = instrument;
		this.key = key;
	}

	public byte getInstrument() {
		return instrument;
	}

	public void setInstrument(byte instrument) {
		this.instrument = instrument;
	}

	public byte getKey() {
		return key;
	}

	public void setKey(byte key) {
		this.key = key;
	}

	public void playSound(Position pos) {
		NoteBoxSound sound = new NoteBoxSound(pos, instrument, key - 33);
		pos.getLevel().addSound(sound);
	}

	public void playSound(Player player, Vector3 vec) {
		NoteBoxSound sound = new NoteBoxSound(vec, instrument, key - 33);
		player.getLevel().addSound(sound, player);
	}
}
