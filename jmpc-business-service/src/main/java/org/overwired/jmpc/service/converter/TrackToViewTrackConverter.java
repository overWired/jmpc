package org.overwired.jmpc.service.converter;

import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.overwired.jmpc.domain.app.Track;
import org.overwired.jmpc.domain.view.ViewTrack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Base64;

/**
 * Converts an application domain Track object into a view domain ViewTrack object.
 */
@Component
@Setter
public class TrackToViewTrackConverter implements Converter<Track, ViewTrack> {

    private static final Logger LOGGER = LoggerFactory.getLogger(TrackToViewTrackConverter.class);

    @Override
    public ViewTrack convert(final Track _track) {
        LOGGER.trace("track: {}", _track);
        Track track = nonNullTrack(_track);

        ViewTrack viewTrack = ViewTrack.builder()
                                       .artist(track.getArtist())
                                       .id(viewTrackIdFor(track))
                                       .title(track.getTitle())
                                       .build();

        LOGGER.trace("viewTrack: {}", viewTrack);
        return viewTrack;
    }

    private String viewTrackIdFor(Track track) {
        String id = track.getPath();
        if (StringUtils.isNotEmpty(id)) {
            try {
                String base64TrackId = Base64.getUrlEncoder()
                                             .encodeToString(track.getPath().getBytes("UTF-8"));
                id = URLEncoder.encode(base64TrackId, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                LOGGER.error("unable to URLEncode the id (path) for track: {}", track);
            }
        }
        return id;
    }

    private Track nonNullTrack(Track track) {
        return (null == track) ? Track.builder().artist("N / A").title("N / A").build() : track;
    }

}
