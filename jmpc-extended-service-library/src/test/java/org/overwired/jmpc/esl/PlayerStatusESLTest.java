package org.overwired.jmpc.esl;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.bff.javampd.player.Player.Status.STATUS_PAUSED;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import org.bff.javampd.player.Player;
import org.bff.javampd.playlist.Playlist;
import org.bff.javampd.song.MPDSong;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.overwired.jmpc.domain.app.PlayerStatus;
import org.overwired.jmpc.domain.app.Track;
import org.overwired.jmpc.sal.MediaPlayerDaemonSAL;
import org.springframework.core.convert.ConversionService;

import java.util.Arrays;
import java.util.Collections;

/**
 * Tests the PlayerStatusESL class.
 */
@RunWith(MockitoJUnitRunner.class)
public class PlayerStatusESLTest {

    private static final String STATUS = "paused";

    private PlayerStatusESL esl;
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

        esl = new PlayerStatusESL(mockConversionService, mockSal);
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

}
