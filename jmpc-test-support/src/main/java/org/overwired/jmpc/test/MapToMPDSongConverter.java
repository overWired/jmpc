package org.overwired.jmpc.test;

import org.bff.javampd.objects.MPDSong;
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
        MPDSong mpdSong = new MPDSong();
        mpdSong.setFile(source.get(ARTIST) + "_" + source.get(ALBUM) + "_" + source.get(TITLE));
        mpdSong.setAlbumName(source.get(ALBUM));
        mpdSong.setArtistName(source.get(ARTIST));
        mpdSong.setTitle(source.get(TITLE));
        mpdSong.setTrack(Integer.valueOf(source.get(TRACK)));
        return mpdSong;
    }

}
