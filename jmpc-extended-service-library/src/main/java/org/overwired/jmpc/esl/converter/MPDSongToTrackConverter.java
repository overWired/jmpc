package org.overwired.jmpc.esl.converter;

import lombok.Setter;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.bff.javampd.objects.MPDSong;
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

    public Track convert(MPDSong source) {
        if (null != source) {
            // MPDSong's own toString method embeds newlines.  Screw that.
            LOGGER.trace("converting MPDSong to Track: {}", ReflectionToStringBuilder.toString(source));
            trackBuilder.artist(source.getArtistName())
                    .album(source.getAlbumName())
                    .title(source.getTitle())
                    .trackNumber(source.getTrack());
        } else {
            LOGGER.trace("source is null, returning an unpopulated track");
        }
        return trackBuilder.build();
    }

}
