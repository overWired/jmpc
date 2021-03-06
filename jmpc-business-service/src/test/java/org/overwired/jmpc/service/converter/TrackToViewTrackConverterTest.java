package org.overwired.jmpc.service.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
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

import java.net.URLEncoder;
import java.util.Base64;

/**
 * Tests the TrackToViewTrackConverter class.
 */
@RunWith(MockitoJUnitRunner.class)
public class TrackToViewTrackConverterTest {

    private static final String ARTIST = "artist";
    private static final String PATH = "path/to@file";
    private static final String TITLE = "title";

    TrackToViewTrackConverter converter;
    Track track;

    @Before
    public void setUp() throws Exception {
        track = Track.builder().artist(ARTIST).path(PATH).title(TITLE).build();

        converter = new TrackToViewTrackConverter();
    }

    @Test
    public void shouldProperlyConvertNullToViewTrack() throws Exception {
        ViewTrack actual = converter.convert(null);
        assertNotNull("result of conversion was null", actual);
        assertEquals("N / A", actual.getArtist());
        assertNull(actual.getId());
        assertEquals("N / A", actual.getTitle());
    }


    @Test
    public void shouldProperlyConvertTrackToViewTrack() throws Exception {
        ViewTrack actual = converter.convert(track);
        assertNotNull("result of conversion was null", actual);
        assertEquals(ARTIST, actual.getArtist());
        String expectedId = URLEncoder.encode(
                Base64.getUrlEncoder().encodeToString(PATH.getBytes("UTF-8")),"UTF-8");

        assertEquals(expectedId, actual.getId());
        assertEquals(TITLE, actual.getTitle());
    }

}
