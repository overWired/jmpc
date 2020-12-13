package org.overwired.jmpc.sal;

import lombok.extern.slf4j.Slf4j;
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
