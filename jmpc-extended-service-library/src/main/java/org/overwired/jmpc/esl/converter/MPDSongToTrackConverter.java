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

    @Autowired
    private Track.TrackBuilder trackBuilder;

    public Track convert(MPDSong mpdSong) {
        if (null != mpdSong) {
            if (LOGGER.isTraceEnabled()) {
                // MPDSong's own toString method embeds newlines.  Screw that.
                LOGGER.trace("converting MPDSong to Track: {}", ReflectionToStringBuilder.toString(mpdSong));
            }
            trackBuilder.album(mpdSong.getAlbumName())
                        .artist(mpdSong.getArtistName())
                        .path(mpdSong.getFile())
                        .title(mpdSong.getTitle())
                        .trackNumber(mpdSong.getTrack());
        } else {
            LOGGER.trace("source is null, returning an unpopulated track");
        }
        return trackBuilder.build();
    }

}
