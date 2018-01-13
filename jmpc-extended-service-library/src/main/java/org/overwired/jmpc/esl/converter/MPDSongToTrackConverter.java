package org.overwired.jmpc.esl.converter;

import lombok.Setter;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.bff.javampd.song.MPDSong;
import org.overwired.jmpc.domain.app.Track;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Converts an MPDSong object to a Track object.
 */
@Component
@Setter
public class MPDSongToTrackConverter implements Converter<MPDSong, Track> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MPDSongToTrackConverter.class);

    public Track convert(final MPDSong mpdSong) {
        MPDSong nonNullMPDSong = nonNullMPDSong(mpdSong);
        return Track.builder()
                    .album(nonNullMPDSong.getAlbumName())
                    .artist(nonNullMPDSong.getArtistName())
                    .path(nonNullMPDSong.getFile())
                    .title(nonNullMPDSong.getTitle())
                    .trackNumber(nonNullMPDSong.getTrack())
                    .build();
    }

    private MPDSong nonNullMPDSong(final MPDSong mpdSong) {
        MPDSong nonNullMPDSong = mpdSong;
        if (null == mpdSong) {
            LOGGER.debug("mpdSong is null, returning an unpopulated track");
            nonNullMPDSong = new MPDSong("N / A", "N / A");
        }
        if (LOGGER.isTraceEnabled()) {
            // MPDSong's own toString method embeds newlines.  Screw that.
            LOGGER.trace("converting MPDSong to Track: {}", ReflectionToStringBuilder.toString(mpdSong));
        }
        return nonNullMPDSong;
    }

}
