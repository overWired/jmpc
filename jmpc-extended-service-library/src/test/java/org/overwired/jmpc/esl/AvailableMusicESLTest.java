package org.overwired.jmpc.esl;

import static org.hamcrest.Matchers.emptyCollectionOf;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
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
    private static final String PACKAGE = "org/overwired/jmpc/test";
    private static final Map<String, String> PHOTOGRAPH = loadProperties(PACKAGE + "/Photograph.properties");
    private static final Map<String, String> WHO_MADE_WHO = loadProperties(PACKAGE + "/WhoMadeWho.properties");
    private static final Map<String, String> YOU_SHOOK_ME = loadProperties(PACKAGE + "/YouShookMeAllNightLong.properties");

    MPDSong mpdWhoMadeWho;
    MPDSong mpdPhotograph;
    MPDSong mpdYouShookMe;
    Track trackWhoMadeWho;
    Track trackPhotograph;
    Track trackYouShookMe;
    private AvailableMusicESL esl;
    @Mock
    private ConversionService mockConversionService;
    @Mock
    private Database mockDatabase;
    @Mock
    private MPD.Builder mockMpdBuilder;
    @Mock
    private MediaPlayerDaemonSAL mockSal;

    @Before
    public void setup() throws MPDConnectionException {
        setupMPDSongs();
        setupTracks();

        esl = new AvailableMusicESL();
        esl.setConversionService(mockConversionService);
        esl.setSal(mockSal);

        when(mockSal.getDatabase()).thenReturn(mockDatabase);
    }

    @Test
    public void retrievesSongListAndReturnsEquivalentTracks() throws Exception {
        Collection<MPDSong> songs = Lists.newArrayList(mpdWhoMadeWho, mpdPhotograph, mpdYouShookMe);
        when(mockDatabase.listAllSongs()).thenReturn(songs);

        when(mockConversionService.convert(mpdWhoMadeWho, Track.class)).thenReturn(trackWhoMadeWho);
        when(mockConversionService.convert(mpdPhotograph, Track.class)).thenReturn(trackPhotograph);
        when(mockConversionService.convert(mpdYouShookMe, Track.class)).thenReturn(trackYouShookMe);

        List<Track> tracks = esl.availableMusic();
        assertThat("null or empty track list", tracks, not(emptyCollectionOf(Track.class)));
        assertEquals("wrong number of tracks produced", 3, tracks.size());
        verifyMPDInteractions();
    }

    private void setupMPDSongs() {
        Converter<Map<String, String>, MPDSong> converter = new MapToMPDSongConverter();
        mpdWhoMadeWho = converter.convert(WHO_MADE_WHO);
        mpdPhotograph = converter.convert(PHOTOGRAPH);
        mpdYouShookMe = converter.convert(YOU_SHOOK_ME);
    }

    private void setupTracks() {
        Converter<Map<String, String>, Track> converter = new MapToTrackConverter();
        trackWhoMadeWho = converter.convert(WHO_MADE_WHO);
        trackPhotograph = converter.convert(PHOTOGRAPH);
        trackYouShookMe = converter.convert(YOU_SHOOK_ME);
    }

    private void verifyMPDInteractions() throws MPDConnectionException, MPDResponseException {
        verify(mockSal).getDatabase();
    }

}
