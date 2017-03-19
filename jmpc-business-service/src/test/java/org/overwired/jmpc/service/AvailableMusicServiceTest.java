package org.overwired.jmpc.service;

import static org.hamcrest.Matchers.emptyCollectionOf;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.overwired.jmpc.test.TestResources.loadProperties;

import org.bff.javampd.exception.MPDConnectionException;
import org.bff.javampd.exception.MPDDatabaseException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.overwired.jmpc.domain.app.Track;
import org.overwired.jmpc.domain.view.Card;
import org.overwired.jmpc.esl.AvailableMusicESL;
import org.overwired.jmpc.test.MapToTrackConverter;
import org.springframework.core.convert.converter.Converter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Tests the AvailableMusicService class.
 */
@RunWith(MockitoJUnitRunner.class)
public class AvailableMusicServiceTest {

    private static final String PACKAGE = "org/overwired/jmpc/test";

    private AvailableMusicService service;
    private AvailableMusicESL mockEsl;
    private List<Track> tracks;

    @Before
    public void setup() throws MPDDatabaseException, MPDConnectionException {
        Converter<Map<String, String>, Track> converter = new MapToTrackConverter();
        // The order of these tracks (intermixing artists) is vital to this test; please do not reorder the tracks.
        tracks = Arrays.asList(
                converter.convert(loadProperties(PACKAGE + "/WhoMadeWho.properties")),
                converter.convert(loadProperties(PACKAGE + "/Photograph.properties")),
                converter.convert(loadProperties(PACKAGE + "/YouShookMeAllNightLong.properties"))
        );
        mockEsl = mock(AvailableMusicESL.class);

        service = new AvailableMusicService();
        service.setEsl(mockEsl);
    }

    @Test
    public void shouldReturnEachCardWithMusicFromTheSameArtist() throws Exception {
        when(mockEsl.availableMusic()).thenReturn(tracks);

        List<Card> cards = service.availableMusic();
        assertThat("returned list of cards was null or empty", cards, not(emptyCollectionOf(Card.class)));
        assertEquals("wrong number of cards returned", 2, cards.size());
    }

    @Test
    public void shouldReturnOneSpecialCardWhenNoTracksAreAvailable() throws Exception {
        when(mockEsl.availableMusic()).thenReturn(Collections.emptyList());

        List<Card> cards = service.availableMusic();
        assertThat("returned list of cards was null or empty", cards, not(emptyCollectionOf(Card.class)));
        assertEquals("expected one special 'no music found' card", 1, cards.size());
        assertEquals(AvailableMusicService.NO_MUSIC_FOUND, cards.get(0).getArtist());
    }

}
