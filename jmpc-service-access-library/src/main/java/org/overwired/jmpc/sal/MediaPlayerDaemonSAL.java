package org.overwired.jmpc.sal;

import lombok.extern.slf4j.Slf4j;
import org.bff.javampd.command.CommandExecutor;
import org.bff.javampd.monitor.StandAloneMonitor;
import org.bff.javampd.player.Player;
import org.bff.javampd.playlist.Playlist;
import org.bff.javampd.server.MPD;
import org.bff.javampd.server.MPDConnectionException;
import org.bff.javampd.song.SongSearcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * A Service Access Library for MusicPlayerDaemon.
 */
@Component
@Slf4j
public class MediaPlayerDaemonSAL implements PlayerFactory, PlaylistFactory, StandAloneMonitorFactory {

    private final MPD.Builder builder;
    private MPD _mpd;
    private int connectCount = 0;

    @Autowired
    public MediaPlayerDaemonSAL(final MPD.Builder builder) {
        this.builder = builder;
    }

    private synchronized MPD mpd() throws MPDConnectionException {
        if (null == _mpd || !_mpd.isConnected()) {
            log.debug("MPD is null or disconnected - reconnecting.  count={}", ++connectCount);
            _mpd = builder.build();
            // Set cross-fade to 2 seconds
            _mpd.getPlayer().setXFade(2);
            // Commands not (currently) exposed (or that I could not find) via MPD object
            final CommandExecutor commandExecutor = _mpd.getCommandExecutor();
            // Consume mode removes songs from the queue (a.k.a. playlist) once they've been played - like a jukebox.
            log.debug("enabling MPD consume mode");
            commandExecutor.sendCommand("consume", "1");
        }
        return _mpd;
    }

    @Override
    public StandAloneMonitor getMonitor() {
        return mpd().getMonitor();
    }

    @Override
    public Player getPlayer() {
        Player player = mpd().getPlayer();
        if (null == player) {
            throw new NullPointerException("cannot access a null Player object");
        }
        return player;
    }

    @Override
    public Playlist getPlaylist() {
        return mpd().getPlaylist();
    }

    public SongSearcher getSongSearcher() {
        return mpd().getSongSearcher();
    }

    public void subscribe(Object o) {
        mpd().getMonitor()
             .addPlaylistChangeListener(event -> log.debug("received playlistChangedEvent {}", event));
    }
}
