package nukkitplugin.noteblockapi.event;

import cn.nukkit.event.Cancellable;
import cn.nukkit.event.Event;
import cn.nukkit.event.HandlerList;
import nukkitplugin.noteblockapi.player.SongPlayer;

public class SongDestroyingEvent extends Event implements Cancellable {
	private static final HandlerList	handlers	= new HandlerList();
	private SongPlayer					song;
	private boolean						cancelled	= false;

	public SongDestroyingEvent(SongPlayer song) {
		this.song = song;
	}

	public static HandlerList getHandlers() {
		return handlers;
	}

	public SongPlayer getSongPlayer() {
		return song;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}
}
