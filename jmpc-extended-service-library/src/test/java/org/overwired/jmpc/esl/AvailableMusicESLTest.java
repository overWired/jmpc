package org.overwired.jmpc.esl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;
import static org.overwired.jmpc.domain.app.Track.builder;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import org.bff.javampd.Database;
import org.bff.javampd.MPD;
import org.bff.javampd.exception.MPDConnectionException;
import org.bff.javampd.objects.MPDSong;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.overwired.jmpc.domain.app.Track;
import org.overwired.jmpc.domain.view.Card;
import org.overwired.jmpc.domain.view.Cards;
import org.springframework.core.convert.ConversionService;

import java.io.File;
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
    private static final Map<String, String> PHOTOGRAPH = ImmutableMap
            .of(ALBUM, "Pyromania", ARTIST, "Def Leppard", TITLE, "Photograph", TRACK, "1");
    private static final Map<String, String> WHO_MADE_WHO = ImmutableMap
            .of(ALBUM, "Who Made Who", ARTIST, "AC/DC", TITLE, "Who Made Who", TRACK, "1");
    private static final Map<String, String> YOU_SHOOK_ME = ImmutableMap
            .of(ALBUM, "Who Made Who", ARTIST, "AC/DC", TITLE, "You Shook Me All Night Long", TRACK, "2");

    private static final String WRONG_NUMBER_OF_CARDS_PRODUCED = "wrong number of cards produced";

    private AvailableMusicESL esl;
    @Mock
    private ConversionService mockConversionService;
    @Mock
    private Database mockDatabase;
    @Mock
    private MPD.Builder mockMpdBuilder;
    @Mock
    private MPD mockMPD;

    MPDSong mpdWhoMadeWho;
    MPDSong mpdPhotograph;
    MPDSong mpdYouShookMe;
    Track trackWhoMadeWho;
    Track trackPhotograph;
    Track trackYouShookMe;

    @Before
    public void setup() throws MPDConnectionException {
        mpdWhoMadeWho = createMPDSong(WHO_MADE_WHO);
        mpdPhotograph = createMPDSong(PHOTOGRAPH);
        mpdYouShookMe = createMPDSong(YOU_SHOOK_ME);
        trackWhoMadeWho = createTrack(WHO_MADE_WHO);
        trackPhotograph = createTrack(PHOTOGRAPH);
        trackYouShookMe = createTrack(YOU_SHOOK_ME);


        esl = new AvailableMusicESL();
        esl.setConversionService(mockConversionService);

        when(mockMPD.getDatabase()).thenReturn(mockDatabase);
        when(mockMpdBuilder.build()).thenReturn(mockMPD);
        esl.setMpdBuilder(mockMpdBuilder);
    }

    @Test
    public void cardsCombineArtistsAfterSortingTracks() throws Exception {
        Collection<MPDSong> songs = Lists.newArrayList(mpdWhoMadeWho, mpdPhotograph, mpdYouShookMe);
        when(mockDatabase.listAllSongs()).thenReturn(songs);

        when(mockConversionService.convert(mpdWhoMadeWho, Track.class)).thenReturn(trackWhoMadeWho);
        when(mockConversionService.convert(mpdPhotograph, Track.class)).thenReturn(trackPhotograph);
        when(mockConversionService.convert(mpdYouShookMe, Track.class)).thenReturn(trackYouShookMe);

        Cards cards = esl.availableMusic();
        assertNotNull(cards);
        List<Card> cardList = cards.getCards();
        assertEquals(WRONG_NUMBER_OF_CARDS_PRODUCED, 2, cardList.size());
    }

    @Test
    public void cardsSplitDifferentArtistsAfterSortingTracks() throws Exception {
        Collection<MPDSong> songs = Lists.newArrayList(mpdWhoMadeWho, mpdPhotograph);
        when(mockDatabase.listAllSongs()).thenReturn(songs);

        when(mockConversionService.convert(mpdWhoMadeWho, Track.class)).thenReturn(trackWhoMadeWho);
        when(mockConversionService.convert(mpdPhotograph, Track.class)).thenReturn(trackPhotograph);

        Cards cards = esl.availableMusic();
        assertNotNull(cards);
        List<Card> cardList = cards.getCards();
        assertEquals(WRONG_NUMBER_OF_CARDS_PRODUCED, 2, cardList.size());
    }

    private Track createTrack(Map<String, String> data) {
        return Track.builder().album(data.get(ALBUM))
                .artist(data.get(ARTIST))
                .title(data.get(TITLE))
                .trackNumber(Integer.valueOf(data.get(TRACK)))
                .build();
    }

    private MPDSong createMPDSong(Map<String, String> data) {
        MPDSong mpdSong = new MPDSong();
        mpdSong.setFile(data.get(ARTIST)+"_"+data.get(ALBUM)+"_"+data.get(TITLE));
        mpdSong.setAlbumName(data.get(ALBUM));
        mpdSong.setArtistName(data.get(ARTIST));
        mpdSong.setTitle(data.get(TITLE));
        mpdSong.setTrack(Integer.valueOf(data.get(TRACK)));
        return mpdSong;
    }

}
