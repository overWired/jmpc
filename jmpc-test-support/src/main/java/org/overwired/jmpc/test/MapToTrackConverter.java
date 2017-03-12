package org.overwired.jmpc.test;

import org.overwired.jmpc.domain.app.Track;
import org.springframework.core.convert.converter.Converter;

import java.util.Map;

/**
 * Converts a Map (properly populated) into a Track.
 */
public class MapToTrackConverter implements Converter<Map<String, String>, Track> {

    private static final String ALBUM = "album";
    private static final String ARTIST = "artist";
    private static final String TITLE = "title";
    private static final String TRACK = "track";

    @Override
    public Track convert(Map<String, String> source) {
        return Track.builder().album(source.get(ALBUM))
                .artist(source.get(ARTIST))
                .title(source.get(TITLE))
                .trackNumber(Integer.valueOf(source.get(TRACK)))
                .build();
    }

}
