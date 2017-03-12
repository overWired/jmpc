package org.overwired.jmpc.esl;

import lombok.Setter;
import org.bff.javampd.MPD;
import org.bff.javampd.exception.MPDConnectionException;
import org.bff.javampd.exception.MPDDatabaseException;
import org.bff.javampd.exception.MPDResponseException;
import org.bff.javampd.objects.MPDSong;
import org.overwired.jmpc.domain.app.Track;
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
@Setter
public class AvailableMusicESL {

    private static final Logger LOGGER = LoggerFactory.getLogger(AvailableMusicESL.class);

    @Autowired
    private ConversionService conversionService;
    @Autowired
    private MPD.Builder mpdBuilder;

    public List<Track> availableMusic() throws MPDConnectionException, MPDDatabaseException {
        LOGGER.trace("retrieving songs from MPD database");
        MPD mpd = openConnection();
        Collection<MPDSong> mpdSongs = mpd.getDatabase().listAllSongs();
        LOGGER.trace("found {} songs", mpdSongs.size());
        closeConnection(mpd);

        List<Track> tracks = new ArrayList<>(mpdSongs.size());
        for (MPDSong mpdSong : mpdSongs) {
            tracks.add(conversionService.convert(mpdSong, Track.class));
        }

        LOGGER.trace("sorting {} tracks", tracks.size());
        Collections.sort(tracks);
        return tracks;
    }


    private void closeConnection(MPD mpd) {
        try {
            if (null != mpd && mpd.isConnected()) {
                mpd.close();
            }
        } catch (MPDResponseException e) {
            LOGGER.info("failed to close MPD connection: moving on...", e); // INFO since we're ignoring it.
        }
    }

    private MPD openConnection() throws MPDConnectionException {
        return mpdBuilder.build();
    }

}
