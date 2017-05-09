package nukkitplugin.noteblockapi.player;

import cn.nukkit.Player;
import cn.nukkit.level.Location;
import nukkitplugin.noteblockapi.element.Note;
import nukkitplugin.noteblockapi.element.Song;

public class PositionSongPlayer extends SongPlayer {
	private Location targetLocation;

	public PositionSongPlayer(Song song) {
		super(song);
	}

	@Override
	public void playTick(Player player, int tick) {
		if (player.getLevel().getFolderName().equals(targetLocation.getLevel().getFolderName()))
			song.getLayerHashMap().values().stream().forEach(layer -> {
				Note note = layer.getNote(tick);
				if (note != null)
					note.playSound(player, player.add(0, player.getEyeHeight()));
			});
	}

	public Location getTargetLocation() {
		return targetLocation;
	}

	public void setTargetLocation(Location targetLocation) {
		this.targetLocation = targetLocation;
	}
}
