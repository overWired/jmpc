package org.overwired.jmpc.sal;

import lombok.Setter;
import org.bff.javampd.admin.Admin;
import org.bff.javampd.player.Player;
import org.bff.javampd.playlist.Playlist;
import org.bff.javampd.server.MPD;
import org.bff.javampd.server.MPDConnectionException;
import org.bff.javampd.song.SongSearcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * A Service Access Library for MusicPlayerDaemon.
 */
@Component
public class MediaPlayerDaemonSAL {

    private static final Logger LOGGER = LoggerFactory.getLogger(MediaPlayerDaemonSAL.class);

    @Autowired
    @Setter
    private MPD.Builder builder;
    private int connectCount = 0;
    private MPD _mpd;

    private synchronized MPD mpd() throws MPDConnectionException {
        if (null == _mpd || !_mpd.isConnected()) {
            LOGGER.debug("MPD is null or disconnected - reconnecting.  count={}", ++connectCount);
            _mpd = builder.build();
        }
        return _mpd;
    }

    public Admin getAdmin() {
        return mpd().getAdmin();
    }

    public Player getPlayer() {
        Player player = mpd().getPlayer();
        if (null == player) {
            throw new NullPointerException("cannot access a null Player object");
        }
        return player;
    }

    public Playlist getPlaylist() {
        return mpd().getPlaylist();
    }

    public SongSearcher getSongSearcher() {
        return mpd().getSongSearcher();
    }

}
