package org.overwired.jmpc.test;

import org.bff.javampd.song.MPDSong;
import org.springframework.core.convert.converter.Converter;

import java.util.Map;

/**
 * Converts a Map (assuming the proper contents) into an MPDSong.
 */
public class MapToMPDSongConverter implements Converter<Map<String, String>, MPDSong> {

    private static final String ALBUM = "album";
    private static final String ARTIST = "artist";
    private static final String TITLE = "title";
    private static final String TRACK = "track";

    @Override
    public MPDSong convert(Map<String, String> source) {
        String title = source.get(TITLE);
        String filename = source.get(ARTIST) + "_" + source.get(ALBUM) + "_" + title;

        MPDSong mpdSong = new MPDSong(filename, title);
        mpdSong.setAlbumName(source.get(ALBUM));
        mpdSong.setArtistName(source.get(ARTIST));
        mpdSong.setTrack(Integer.valueOf(source.get(TRACK)));
        return mpdSong;
    }

}
