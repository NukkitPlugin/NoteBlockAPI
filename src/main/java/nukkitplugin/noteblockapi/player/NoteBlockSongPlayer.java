package nukkitplugin.noteblockapi.player;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import nukkitplugin.noteblockapi.element.Note;
import nukkitplugin.noteblockapi.element.Song;

public class NoteBlockSongPlayer extends SongPlayer {
	private Block noteBlock;

	public NoteBlockSongPlayer(Song song) {
		super(song);
	}

	@Override
	public void playTick(Player player, int tick) {
		if (noteBlock.getId() == Block.NOTEBLOCK && player.getLevel().getFolderName().equals(noteBlock.getLevel().getFolderName()))
			song.getLayerHashMap().values().stream().forEach(layer -> {
				Note note = layer.getNote(tick);
				if (note != null)
					note.playSound(player, player.add(0, player.getEyeHeight()));
			});
	}

	public Block getNoteBlock() {
		return noteBlock;
	}

	public void setNoteBlock(Block noteBlock) {
		this.noteBlock = noteBlock;
	}
}
