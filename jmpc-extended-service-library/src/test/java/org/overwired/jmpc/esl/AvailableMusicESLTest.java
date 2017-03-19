package org.overwired.jmpc.esl;

import static org.hamcrest.Matchers.emptyCollectionOf;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;
import static org.overwired.jmpc.test.TestResources.loadProperties;

import com.google.common.collect.Lists;
import org.bff.javampd.Database;
import org.bff.javampd.MPD;
import org.bff.javampd.exception.MPDConnectionException;
import org.bff.javampd.exception.MPDResponseException;
import org.bff.javampd.objects.MPDSong;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.overwired.jmpc.domain.app.Track;
import org.overwired.jmpc.sal.MediaPlayerDaemonSAL;
import org.overwired.jmpc.test.MapToMPDSongConverter;
import org.overwired.jmpc.test.MapToTrackConverter;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Tests the org.overwired.jmpc.esl.AvailableMusicESL class.
 */
@RunWith(MockitoJUnitRunner.class)
public class AvailableMusicESLTest {

    private static final String ALBUM = "album";
    private static final String ARTIST = "artist";
    private static final String TITLE = "title";
    private static final String TRACK = "track";
    private static final List<MPDSong> NO_SONGS = Collections.emptyList();

    private static final String PACKAGE = "org/overwired/jmpc/test";
    private static final Map<String, String> PHOTOGRAPH = loadProperties(PACKAGE + "/Photograph.properties");
    private static final Map<String, String> WHO_MADE_WHO = loadProperties(PACKAGE + "/WhoMadeWho.properties");
    private static final Map<String, String> YOU_SHOOK_ME = loadProperties(PACKAGE + "/YouShookMeAllNightLong.properties");

    private MPDSong mpdWhoMadeWho;
    private MPDSong mpdPhotograph;
    private MPDSong mpdYouShookMe;
    private Track trackWhoMadeWho;
    private Track trackPhotograph;
    private Track trackYouShookMe;
    private AvailableMusicESL esl;
    @Mock
    private ConversionService mockConversionService;
    @Mock
    private Database mockDatabase;
    @Mock
    private MPD.Builder mockMpdBuilder;
    @Mock
    private MediaPlayerDaemonSAL mockSal;
    private Collection<MPDSong> songs;

    @Before
    public void setup() throws MPDConnectionException {
        setupMPDSongs();
        setupTracks();
        when(mockConversionService.convert(mpdWhoMadeWho, Track.class)).thenReturn(trackWhoMadeWho);
        when(mockConversionService.convert(mpdPhotograph, Track.class)).thenReturn(trackPhotograph);
        when(mockConversionService.convert(mpdYouShookMe, Track.class)).thenReturn(trackYouShookMe);

        esl = new AvailableMusicESL();
        esl.setConversionService(mockConversionService);
        esl.setSal(mockSal);

        when(mockSal.getDatabase()).thenReturn(mockDatabase);
    }

    @Test
    public void retrievesSongListAndReturnsEquivalentTracks() throws Exception {
        when(mockDatabase.listAllSongs()).thenReturn(songs);

        List<Track> tracks = esl.availableMusic();
        assertThat("null or empty track list", tracks, not(emptyCollectionOf(Track.class)));
        assertEquals("wrong number of tracks produced", 3, tracks.size());
        verifyMPDInteractions(1);
        verifyConverterInteractions();
    }

    @Test
    public void testShouldRetryWhenNoSongsRetrieved() throws Exception {
        esl.setNumerOfAttempts(3);
        when(mockDatabase.listAllSongs()).thenReturn(NO_SONGS).thenReturn(NO_SONGS).thenReturn(songs);

        List<Track> tracks = esl.availableMusic();
        assertThat("null or empty track list", tracks, not(emptyCollectionOf(Track.class)));
        assertEquals("wrong number of tracks produced", 3, tracks.size());
        verifyMPDInteractions(3);
        verifyConverterInteractions();
    }

    @Test
    public void shouldLimitRetriesWhenNoSongsRetrieved() throws Exception {
        final int maxAttempts = 5;
        esl.setNumerOfAttempts(maxAttempts);
        when(mockDatabase.listAllSongs()).thenReturn(NO_SONGS);

        List<Track> tracks = esl.availableMusic();
        assertThat("expected null or empty track list", tracks, emptyCollectionOf(Track.class));
        verifyMPDInteractions(maxAttempts);
        verifyNoMoreInteractions(mockConversionService);
    }


    private void setupMPDSongs() {
        Converter<Map<String, String>, MPDSong> converter = new MapToMPDSongConverter();
        mpdWhoMadeWho = converter.convert(WHO_MADE_WHO);
        mpdPhotograph = converter.convert(PHOTOGRAPH);
        mpdYouShookMe = converter.convert(YOU_SHOOK_ME);

        songs = Lists.newArrayList(mpdWhoMadeWho, mpdPhotograph, mpdYouShookMe);
    }

    private void setupTracks() {
        Converter<Map<String, String>, Track> converter = new MapToTrackConverter();
        trackWhoMadeWho = converter.convert(WHO_MADE_WHO);
        trackPhotograph = converter.convert(PHOTOGRAPH);
        trackYouShookMe = converter.convert(YOU_SHOOK_ME);
    }

    private void verifyConverterInteractions() {
        verify(mockConversionService).convert(mpdWhoMadeWho, Track.class);
        verify(mockConversionService).convert(mpdPhotograph, Track.class);
        verify(mockConversionService).convert(mpdYouShookMe, Track.class);
    }

    private void verifyMPDInteractions(int expectedTimes) throws MPDConnectionException, MPDResponseException {
        verify(mockSal, times(expectedTimes)).getDatabase();
    }

}
