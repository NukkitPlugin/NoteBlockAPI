package nukkitplugin.noteblockapi.player;

import cn.nukkit.Player;
import nukkitplugin.noteblockapi.element.Note;
import nukkitplugin.noteblockapi.element.Song;

public class RadioSongPlayer extends SongPlayer {
	public RadioSongPlayer(Song song) {
		super(song);
	}

	@Override
	public void playTick(Player player, int tick) {
		song.getLayerHashMap().values().stream().forEach(layer -> {
			Note note = layer.getNote(tick);
			if (note != null)
				note.playSound(player, player.add(0, player.getEyeHeight()));
		});
	}
}
