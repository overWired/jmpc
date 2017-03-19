package org.overwired.jmpc.esl.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.bff.javampd.objects.MPDSong;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.overwired.jmpc.domain.app.Track;
import org.overwired.jmpc.esl.converter.MPDSongToTrackConverter;

/**
 * Tests the MPDSongToTrackConverter class.
 */
@RunWith(MockitoJUnitRunner.class)
public class MPDSongToTrackConverterTest {

    private static final String ALBUM = "Album";
    private static final String ARTIST = "Artist";
    private static final String TITLE = "Title";
    private static final int TRACK = 1;

    private MPDSongToTrackConverter converter;
    private MPDSong mpdSong;
    private Track expectedTrack;
    private Track.TrackBuilder mockTrackBuilder;

    @Before
    public void setUp() throws Exception {
        mockTrackBuilder = mock(Track.TrackBuilder.class);

        setupMpdSong();
        converter = new MPDSongToTrackConverter();
        converter.setTrackBuilder(mockTrackBuilder);
    }

    @Test
    public void shouldReturnNullWhenGivenNull() throws Exception {
        assertNull("null input should cause null output", converter.convert(null));
    }

    @Test
    public void shouldConvertMpdSongIntoTrack() throws Exception {
        expectedTrack = Track.builder().build();
        when(mockTrackBuilder.album(ALBUM)).thenReturn(mockTrackBuilder);
        when(mockTrackBuilder.artist(ARTIST)).thenReturn(mockTrackBuilder);
        when(mockTrackBuilder.title(TITLE)).thenReturn(mockTrackBuilder);
        when(mockTrackBuilder.trackNumber(TRACK)).thenReturn(mockTrackBuilder);
        when(mockTrackBuilder.build()).thenReturn(expectedTrack);

        assertEquals(expectedTrack, converter.convert(mpdSong));
        verify(mockTrackBuilder).album(ALBUM);
        verify(mockTrackBuilder).artist(ARTIST);
        verify(mockTrackBuilder).title(TITLE);
        verify(mockTrackBuilder).trackNumber(TRACK);
    }

    private void setupMpdSong() {
        mpdSong = new MPDSong();
        mpdSong.setAlbumName(ALBUM);
        mpdSong.setArtistName(ARTIST);
        mpdSong.setTitle(TITLE);
        mpdSong.setTrack(TRACK);
    }

}
