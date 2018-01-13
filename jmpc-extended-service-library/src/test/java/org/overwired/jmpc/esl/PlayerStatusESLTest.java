package org.overwired.jmpc.esl;

import static org.bff.javampd.player.Player.Status.STATUS_PAUSED;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.verify;
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

    private static final String EXPECTED_CURRENT_SONG = "Current Song";
    private static final String PAUSED = "paused";

    private PlayerStatusESL esl;
    private PlayerStatus expectedStatus;
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
    private PlayerStatus.PlayerStatusBuilder mockPlayerStatusBuilder;
    @Mock
    private Playlist mockPlaylist;
    @Mock
    private MediaPlayerDaemonSAL mockSal;

    @Before
    public void setUp() throws Exception {
        currentTrack = Track.builder().title("current song").build();
        nextTrack = Track.builder().title("next song").build();
        expectedStatus = PlayerStatus.builder().build();


        when(mockSal.getPlayer()).thenReturn(mockPlayer);
        when(mockPlayer.getCurrentSong()).thenReturn(mockCurrentSong);
        when(mockConversionService.convert(mockCurrentSong, Track.class)).thenReturn(currentTrack);
        when(mockPlayerStatusBuilder.currentSong(any(Track.class))).thenReturn(mockPlayerStatusBuilder);

        when(mockPlayer.getStatus()).thenReturn(STATUS_PAUSED);
        when(mockConversionService.convert(STATUS_PAUSED, String.class)).thenReturn(PAUSED);
        when(mockPlayerStatusBuilder.status(anyString())).thenReturn(mockPlayerStatusBuilder);

        when(mockSal.getPlaylist()).thenReturn(mockPlaylist);
        when(mockPlaylist.getSongList()).thenReturn(Collections.singletonList(mockNextSong));
        when(mockPlayerStatusBuilder.playlist(anyListOf(Track.class))).thenReturn(mockPlayerStatusBuilder);
        when(mockConversionService.convert(mockNextSong, Track.class)).thenReturn(nextTrack);

        when(mockPlayerStatusBuilder.build()).thenReturn(expectedStatus);

        esl = new PlayerStatusESL(mockConversionService, mockSal);
        esl.setPlayerStatusBuilder(mockPlayerStatusBuilder);
    }

    @Test
    public void shouldReturnMusicPlayer() throws Exception {
        when(mockSal.getPlayer()).thenReturn(mockPlayer);
        PlayerStatus actualStatus = esl.playerStatus();
        assertEquals("PlayerStatus shouldn't be null", expectedStatus, actualStatus);
        verify(mockPlayerStatusBuilder).currentSong(currentTrack);
        verify(mockPlayerStatusBuilder).status(PAUSED);
        verify(mockPlayerStatusBuilder).playlist(Arrays.asList(nextTrack));
    }

}
