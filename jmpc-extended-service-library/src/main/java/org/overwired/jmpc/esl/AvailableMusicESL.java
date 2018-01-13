package org.overwired.jmpc.esl;

import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.bff.javampd.server.MPDConnectionException;
import org.bff.javampd.song.MPDSong;
import org.bff.javampd.song.SongSearcher;
import org.overwired.jmpc.domain.app.Track;
import org.overwired.jmpc.sal.MediaPlayerDaemonSAL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * The Extended Service Library for Available Music.
 */
@Repository
public class AvailableMusicESL {

    private static final Logger LOGGER = LoggerFactory.getLogger(AvailableMusicESL.class);

    private final ConversionService conversionService;
    @Setter
    private int numberOfAttempts = 2;
    private final MediaPlayerDaemonSAL sal;

    @Autowired
    public AvailableMusicESL(ConversionService conversionService, MediaPlayerDaemonSAL sal) {
        this.conversionService = conversionService;
        this.sal = sal;
    }

    public List<Track> availableMusic() throws MPDConnectionException {
        LOGGER.trace("retrieving songs from MPD database");
        Collection<MPDSong> mpdSongs = Collections.emptyList();
        // Retrying is probably bad form, but MPD seems to be a little flaky getting all songs.
        for (int attemptsRemaining = numberOfAttempts; attemptsRemaining > 0; attemptsRemaining--) {
            mpdSongs = sal.getSongSearcher().search(SongSearcher.ScopeType.ANY, "");
            if (CollectionUtils.isEmpty(mpdSongs)) {
                LOGGER.warn("song retrieval failed (or no songs present). attemptsRemaining={}", attemptsRemaining);
            } else {
                attemptsRemaining = 0;
            }
        }
        LOGGER.debug("found {} songs", mpdSongs.size());

        List<Track> tracks = new ArrayList<>(mpdSongs.size());
        for (MPDSong mpdSong : mpdSongs) {
            tracks.add(conversionService.convert(mpdSong, Track.class));
        }

        LOGGER.trace("sorting {} tracks", tracks.size());
        Collections.sort(tracks);
        return tracks;
    }

}
