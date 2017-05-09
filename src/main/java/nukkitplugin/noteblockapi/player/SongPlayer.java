package nukkitplugin.noteblockapi.player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.nukkit.Player;
import cn.nukkit.Server;
import nukkitplugin.noteblockapi.NoteBlockAPI;
import nukkitplugin.noteblockapi.element.Song;
import nukkitplugin.noteblockapi.event.SongDestroyingEvent;
import nukkitplugin.noteblockapi.event.SongEndEvent;
import nukkitplugin.noteblockapi.event.SongStoppedEvent;
import static nukkitplugin.noteblockapi.util.Interpolator.interpLinear;

public abstract class SongPlayer {
	protected Song				song;
	protected boolean			playing			= false;
	protected short				tick			= -1;
	protected ArrayList<String>	playerList		= new ArrayList<String>();
	protected boolean			autoDestroy		= false;
	protected boolean			destroyed		= false;
	protected Thread			playerThread;
	protected byte				fadeTarget		= 100;
	protected byte				volume			= 100;
	protected byte				fadeStart		= volume;
	protected int				fadeDuration	= 60;
	protected int				fadeDone		= 0;

	public SongPlayer(Song song) {
		this.song = song;
		createThread();
	}

	protected void calculateFade() {
		if (fadeDone != fadeDuration) {
			double targetVolume = interpLinear(new double[] { 0, fadeStart, fadeDuration, fadeTarget }, fadeDone);
			setVolume((byte) targetVolume);
			fadeDone++;
		}
	}

	protected void createThread() {
		playerThread = new Thread(() -> {
			while (!destroyed) {
				long startTime = System.currentTimeMillis();
				synchronized (SongPlayer.this) {
					if (playing) {
						calculateFade();
						tick++;
						if (tick > song.getLength()) {
							playing = false;
							tick = -1;
							Server.getInstance().getPluginManager().callEvent(new SongEndEvent(SongPlayer.this));
							if (autoDestroy) {
								destroy();
								return;
							}
						}
						playerList.stream().forEach(playerName -> {
							Player player = Server.getInstance().getPlayerExact(playerName);
							if (player != null)
								playTick(player, tick);
						});
					}
				}
				long duration = System.currentTimeMillis() - startTime;
				float delayMillis = song.getDelay() * 50;
				if (duration < delayMillis)
					try {
						Thread.sleep((long) (delayMillis - duration));
					} catch (InterruptedException e) {}
			}
		});
		playerThread.setPriority(Thread.MAX_PRIORITY);
		playerThread.start();
	}

	public Song getSong() {
		return song;
	}

	public boolean isPlaying() {
		return playing;
	}

	public void setPlaying(boolean playing) {
		if (!(this.playing = playing))
			Server.getInstance().getPluginManager().callEvent(new SongStoppedEvent(this));
	}

	public short getTick() {
		return tick;
	}

	public void setTick(short tick) {
		this.tick = tick;
	}

	public abstract void playTick(Player p, int tick);

	public List<String> getPlayerList() {
		return Collections.unmodifiableList(playerList);
	}

	public void addPlayer(Player player) {
		synchronized (this) {
			if (!playerList.contains(player.getName())) {
				playerList.add(player.getName());
				ArrayList<SongPlayer> songs = NoteBlockAPI.getInstance().playingSongs.get(player.getName());
				if (songs == null)
					songs = new ArrayList<SongPlayer>();
				songs.add(this);
				NoteBlockAPI.getInstance().playingSongs.put(player.getName(), songs);
			}
		}
	}

	public void removePlayer(Player player) {
		synchronized (this) {
			playerList.remove(player.getName());
			if (NoteBlockAPI.getInstance().playingSongs.get(player.getName()) != null) {
				ArrayList<SongPlayer> songs = new ArrayList<SongPlayer>(NoteBlockAPI.getInstance().playingSongs.get(player.getName()));
				songs.remove(this);
				NoteBlockAPI.getInstance().playingSongs.put(player.getName(), songs);
				if (playerList.isEmpty() && autoDestroy) {
					SongEndEvent event = new SongEndEvent(this);
					Server.getInstance().getPluginManager().callEvent(event);
					destroy();
				}
			}
		}
	}

	public boolean getAutoDestroy() {
		synchronized (this) {
			return autoDestroy;
		}
	}

	public void setAutoDestroy(boolean value) {
		synchronized (this) {
			autoDestroy = value;
		}
	}

	public byte getFadeTarget() {
		return fadeTarget;
	}

	public void setFadeTarget(byte fadeTarget) {
		this.fadeTarget = fadeTarget;
	}

	public byte getVolume() {
		return volume;
	}

	public void setVolume(byte volume) {
		this.volume = volume;
	}

	public byte getFadeStart() {
		return fadeStart;
	}

	public void setFadeStart(byte fadeStart) {
		this.fadeStart = fadeStart;
	}

	public int getFadeDuration() {
		return fadeDuration;
	}

	public void setFadeDuration(int fadeDuration) {
		this.fadeDuration = fadeDuration;
	}

	public int getFadeDone() {
		return fadeDone;
	}

	public void setFadeDone(int fadeDone) {
		this.fadeDone = fadeDone;
	}

	public void destroy() {
		synchronized (this) {
			SongDestroyingEvent event = new SongDestroyingEvent(this);
			Server.getInstance().getPluginManager().callEvent(event);
			// Server.getInstance().getScheduler().cancelTask(threadId);
			if (event.isCancelled())
				return;
			destroyed = true;
			playing = false;
			setTick((short) -1);
		}
	}
}
