package org.overwired.jmpc.service;

import static org.hamcrest.Matchers.emptyCollectionOf;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;
import static org.overwired.jmpc.test.TestResources.loadProperties;

import org.bff.javampd.server.MPDConnectionException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.overwired.jmpc.domain.app.Track;
import org.overwired.jmpc.domain.view.ViewCard;
import org.overwired.jmpc.domain.view.ViewTrack;
import org.overwired.jmpc.esl.AvailableMusicESL;
import org.overwired.jmpc.test.MapToTrackConverter;
import org.springframework.core.convert.ConversionService;
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
    @Mock
    private ConversionService mockConversionService;
    @Mock
    private AvailableMusicESL mockEsl;
    private List<Track> tracks;

    @Before
    public void setup() throws MPDConnectionException {
        Converter<Map<String, String>, Track> converter = new MapToTrackConverter();
        // The order of these tracks (intermixing artists) is vital to this test; please do not reorder the tracks.
        tracks = Arrays.asList(
                converter.convert(loadProperties(PACKAGE + "/WhoMadeWho.properties")),
                converter.convert(loadProperties(PACKAGE + "/Photograph.properties")),
                converter.convert(loadProperties(PACKAGE + "/YouShookMeAllNightLong.properties"))
        );

        tracks.forEach(track -> {
            ViewTrack viewTrack = ViewTrack.builder().title(track.getTitle()).build();
            when(mockConversionService.convert(track, ViewTrack.class)).thenReturn(viewTrack);
        });

        service = new AvailableMusicService(mockConversionService, mockEsl);
    }

    @Test
    public void shouldReturnEachCardWithMusicFromTheSameArtist() throws Exception {
        when(mockEsl.availableMusic()).thenReturn(tracks);

        List<ViewCard> viewCards = service.availableMusic();
        assertThat("returned list of viewCards was null or empty", viewCards, not(emptyCollectionOf(ViewCard.class)));
        assertEquals("wrong number of viewCards returned", 2, viewCards.size());
        tracks.forEach(track -> {
            verify(mockConversionService).convert(track, ViewTrack.class);
        });
    }

    @Test
    public void shouldReturnOneSpecialCardWhenNoTracksAreAvailable() throws Exception {
        when(mockEsl.availableMusic()).thenReturn(Collections.emptyList());

        List<ViewCard> viewCards = service.availableMusic();
        assertThat("returned list of viewCards was null or empty", viewCards, not(emptyCollectionOf(ViewCard.class)));
        assertEquals("expected one special 'no music found' card", 1, viewCards.size());
        assertEquals(AvailableMusicService.NO_MUSIC_FOUND, viewCards.get(0).getArtist());
        verifyNoMoreInteractions(mockConversionService);
    }

}
