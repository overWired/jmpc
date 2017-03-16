package org.overwired.jmpc.sal;

import lombok.Setter;
import org.bff.javampd.Admin;
import org.bff.javampd.Database;
import org.bff.javampd.MPD;
import org.bff.javampd.Player;
import org.bff.javampd.Playlist;
import org.bff.javampd.ServerStatistics;
import org.bff.javampd.ServerStatus;
import org.bff.javampd.StandAloneMonitor;
import org.bff.javampd.exception.MPDConnectionException;
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
    private MPD _mpd;

    private synchronized MPD mpd() throws MPDConnectionException {
        if (null == _mpd || !_mpd.isConnected()) {
            _mpd = builder.build();
        }
        return _mpd;
    }

    public Admin getAdmin() throws MPDConnectionException {
        return mpd().getAdmin();
    }

    public Database getDatabase() throws MPDConnectionException {
        return mpd().getDatabase();
    }

    public StandAloneMonitor getMonitor() throws MPDConnectionException {
        return mpd().getMonitor();
    }

    public Player getPlayer() throws MPDConnectionException {
        return mpd().getPlayer();
    }

    public Playlist getPlaylist() throws MPDConnectionException {
        return mpd().getPlaylist();
    }

    public ServerStatistics getServerStatistics() throws MPDConnectionException {
        return mpd().getServerStatistics();
    }

    public ServerStatus getServerStatus() throws MPDConnectionException {
        return mpd().getServerStatus();
    }
}
