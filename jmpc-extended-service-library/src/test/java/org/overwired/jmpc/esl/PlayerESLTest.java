package org.overwired.jmpc.esl;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.bff.javampd.player.Player.Status.STATUS_PAUSED;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.bff.javampd.player.Player;
import org.bff.javampd.playlist.Playlist;
import org.bff.javampd.server.MPDConnectionException;
import org.bff.javampd.song.MPDSong;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.overwired.jmpc.domain.app.PlayerStatus;
import org.overwired.jmpc.domain.app.Track;
import org.overwired.jmpc.sal.MediaPlayerDaemonSAL;
import org.springframework.core.convert.ConversionService;

import java.io.FileNotFoundException;

/**
 * Tests the PlayerESL class.
 */
@RunWith(MockitoJUnitRunner.class)
public class PlayerESLTest {

    private static final String STATUS = "paused";
    private static final String TRACK_ID = "trackId";

    private PlayerESL esl;
    private Track currentTrack;
    private Track nextTrack;

    @Mock
    private ConversionService mockConversionService;
    @Mock
    private MPDSong mockCurrentSong;
    @Mock
    private MPDSong mockNextSong;
    @Mock
    private Player mockPlayer;
    @Mock
    private Playlist mockPlaylist;
    @Mock
    private MediaPlayerDaemonSAL mockSal;

    @Before
    public void setUp() throws Exception {
        currentTrack = setUpTrack("current song");
        nextTrack = setUpTrack("next song");

        setUpMockSal();
        setUpMockPlayer();
        setUpMockConversionService();
        setUpMockPlaylist();

        esl = new PlayerESL(mockConversionService, mockSal);
    }

    private void setUpMockPlaylist() {
        when(mockPlaylist.getSongList()).thenReturn(asList(mockCurrentSong, mockNextSong));
    }

    private void setUpMockConversionService() {
        when(mockConversionService.convert(mockCurrentSong, Track.class)).thenReturn(currentTrack);
        when(mockConversionService.convert(STATUS_PAUSED, String.class)).thenReturn(STATUS);
        when(mockConversionService.convert(mockNextSong, Track.class)).thenReturn(nextTrack);
    }

    private void setUpMockPlayer() {
        when(mockPlayer.getCurrentSong()).thenReturn(mockCurrentSong);
        when(mockPlayer.getStatus()).thenReturn(STATUS_PAUSED);
    }

    private void setUpMockSal() {
        when(mockSal.getPlayer()).thenReturn(mockPlayer);
        when(mockSal.getPlaylist()).thenReturn(mockPlaylist);
    }

    private Track setUpTrack(String title) {
        return Track.builder().title(title).build();
    }

    @Test
    public void shouldReturnMusicPlayer() throws Exception {
        PlayerStatus playerStatus = esl.playerStatus();
        assertNotNull("PlayerStatus is null", playerStatus);
        assertEquals("wrong current track", currentTrack, playerStatus.getCurrentSong());
        assertEquals("wrong status", STATUS, playerStatus.getStatus());
        assertEquals("wrong playlist", asList(currentTrack, nextTrack), playerStatus.getPlaylist());
    }

    @Test
    public void shouldSkipCurrentSongInPlaylist() throws Exception {
        when(mockPlaylist.getCurrentSong()).thenReturn(mockCurrentSong);

        PlayerStatus playerStatus = esl.playerStatus();
        assertNotNull("PlayerStatus is null", playerStatus);
        assertEquals("wrong current track", currentTrack, playerStatus.getCurrentSong());
        assertEquals("wrong status", STATUS, playerStatus.getStatus());
        assertEquals("wrong playlist", singletonList(nextTrack), playerStatus.getPlaylist());
    }

    @Test
    public void should_play_a_valid_track() throws FileNotFoundException {
        esl.play(TRACK_ID);
        verify(mockSal).getPlaylist();
        verify(mockPlaylist).addSong(TRACK_ID);
        verify(mockSal).getPlayer();
        verify(mockPlayer).play();
    }

    @Test(expected = FileNotFoundException.class)
    public void should_throw_file_not_found_for_an_invalid_track() throws FileNotFoundException {
        doThrow(new MPDConnectionException("intentional test exception")).when(mockPlaylist).addSong(TRACK_ID);
        esl.play(TRACK_ID);
    }

    @Test
    public void should_not_filter_direct_playlist_request() {
        assertEquals("wrong playlist", asList(currentTrack, nextTrack), esl.playlist());
    }


}
