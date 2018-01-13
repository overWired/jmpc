package org.overwired.jmpc.service.converter;

import lombok.Setter;
import org.overwired.jmpc.domain.app.Track;
import org.overwired.jmpc.domain.view.ViewTrack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Converts an application domain Track object into a view domain ViewTrack object.
 */
@Component
@Setter
public class TrackToViewTrackConverter implements Converter<Track, ViewTrack> {

    @Autowired
    ViewTrack.ViewTrackBuilder viewTrackBuilder;

    @Override
    public ViewTrack convert(Track track) {
        return viewTrackBuilder.artist(track.getArtist())
                               .path(track.getPath())
                               .title(track.getTitle())
                               .build();
    }

}
