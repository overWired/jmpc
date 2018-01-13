package org.overwired.jmpc.service.converter;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.overwired.jmpc.domain.app.Track;
import org.overwired.jmpc.domain.view.ViewTrack;

/**
 * Tests the TrackToViewTrackConverter class.
 */
@RunWith(MockitoJUnitRunner.class)
public class TrackToViewTrackConverterTest {

    public static final String ARTIST = "artist";
    public static final String PATH = "path";
    public static final String TITLE = "title";
    TrackToViewTrackConverter converter;
    Track track;

    ViewTrack viewTrack;
    @Mock
    ViewTrack.ViewTrackBuilder mockViewTrackBuilder;

    @Before
    public void setUp() throws Exception {
        track = Track.builder().artist(ARTIST).path(PATH).title(TITLE).build();
        viewTrack = ViewTrack.builder().build();

        when(mockViewTrackBuilder.artist(anyString())).thenReturn(mockViewTrackBuilder);
        when(mockViewTrackBuilder.path(anyString())).thenReturn(mockViewTrackBuilder);
        when(mockViewTrackBuilder.title(anyString())).thenReturn(mockViewTrackBuilder);
        when(mockViewTrackBuilder.build()).thenReturn(viewTrack);

        converter = new TrackToViewTrackConverter();
        converter.setViewTrackBuilder(mockViewTrackBuilder);
    }

    @Test
    public void convert() throws Exception {
        ViewTrack actual = converter.convert(track);
        assertEquals(viewTrack, actual);
        verify(mockViewTrackBuilder, times(1)).artist(ARTIST);
        verify(mockViewTrackBuilder, times(1)).path(PATH);
        verify(mockViewTrackBuilder, times(1)).title(TITLE);
    }

}
