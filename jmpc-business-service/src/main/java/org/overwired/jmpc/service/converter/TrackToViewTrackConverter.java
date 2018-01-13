package org.overwired.jmpc.service.converter;

import lombok.Setter;
import org.overwired.jmpc.domain.app.Track;
import org.overwired.jmpc.domain.view.ViewTrack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Converts an application domain Track object into a view domain ViewTrack object.
 */
@Component
@Setter
public class TrackToViewTrackConverter implements Converter<Track, ViewTrack> {

    private static final Logger LOGGER = LoggerFactory.getLogger(TrackToViewTrackConverter.class);

    @Override
    public ViewTrack convert(final Track track) {
        LOGGER.trace("track: {}", track);
        Track nonNullTrack = nonNullTrack(track);
        ViewTrack viewTrack = ViewTrack.builder()
                                       .artist(nonNullTrack.getArtist())
                                       .path(nonNullTrack.getPath())
                                       .title(nonNullTrack.getTitle())
                                       .build();

        LOGGER.trace("viewTrack: {}", viewTrack);
        return viewTrack;
    }

    private Track nonNullTrack(Track track) {
        return (null == track) ? Track.builder().artist("N / A").title("N / A").build() : track;
    }

}
