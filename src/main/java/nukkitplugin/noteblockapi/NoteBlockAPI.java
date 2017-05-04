package nukkitplugin.noteblockapi;

import java.util.ArrayList;
import java.util.HashMap;

import cn.nukkit.Player;
import cn.nukkit.plugin.PluginBase;
import nukkitplugin.noteblockapi.player.SongPlayer;

public class NoteBlockAPI extends PluginBase {
	private static NoteBlockAPI						instance;
	public HashMap<String, ArrayList<SongPlayer>>	playingSongs	= new HashMap<String, ArrayList<SongPlayer>>();
	public HashMap<String, Byte>					playerVolume	= new HashMap<String, Byte>();

	public static NoteBlockAPI getInstance() {
		return instance;
	}

	public static boolean isReceivingSong(Player player) {
		return !(instance.playingSongs.get(player.getName()) == null || instance.playingSongs.get(player.getName()).isEmpty());
	}

	public static void stopPlaying(Player player) {
		if (instance.playingSongs.get(player.getName()) != null)
			instance.playingSongs.get(player.getName()).stream().forEach(songPlayer -> songPlayer.removePlayer(player));
	}

	public static void setPlayerVolume(Player player, byte volume) {
		instance.playerVolume.put(player.getName(), volume);
	}

	public static byte getPlayerVolume(Player player) {
		Byte volume = instance.playerVolume.get(player.getName());
		if (volume == null) {
			volume = 100;
			instance.playerVolume.put(player.getName(), volume);
		}
		return volume;
	}

	@Override
	public void onEnable() {
		instance = this;
	}
}
