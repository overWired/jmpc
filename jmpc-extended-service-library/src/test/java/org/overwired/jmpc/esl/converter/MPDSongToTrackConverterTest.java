package org.overwired.jmpc.esl.converter;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.bff.javampd.song.MPDSong;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.overwired.jmpc.domain.app.Track;

/**
 * Tests the MPDSongToTrackConverter class.
 */
@RunWith(MockitoJUnitRunner.class)
public class MPDSongToTrackConverterTest {

    private static final String ALBUM = "Album";
    private static final String ARTIST = "Artist";
    private static final String FILE_NAME = "file_name";
    private static final String TITLE = "Title";
    private static final int TRACK = 1;

    private MPDSongToTrackConverter converter;
    private MPDSong mpdSong;

    @Before
    public void setUp() throws Exception {
        setupMpdSong();
        converter = new MPDSongToTrackConverter();
    }

    @Test
    public void shouldReturnNullWhenGivenNull() throws Exception {
        assertNotNull("null input should return placeholder track", converter.convert(null));
    }

    @Test
    public void shouldConvertMpdSongIntoTrack() throws Exception {
        Track track = converter.convert(mpdSong);
        assertNotNull("converted track is null", track);
        assertEquals(ALBUM, track.getAlbum());
        assertEquals(ARTIST, track.getArtist());
        assertEquals(FILE_NAME, track.getPath());
        assertEquals(TITLE, track.getTitle());
        assertEquals(TRACK, track.getTrackNumber());
    }

    private void setupMpdSong() {
        mpdSong = new MPDSong(FILE_NAME, TITLE);
        mpdSong.setAlbumName(ALBUM);
        mpdSong.setArtistName(ARTIST);
        mpdSong.setTrack(TRACK);
    }

}
