package org.overwired.jmpc.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.overwired.jmpc.domain.app.PlayerStatus;
import org.overwired.jmpc.domain.app.Track;
import org.overwired.jmpc.domain.view.ViewPlayerStatus;
import org.overwired.jmpc.esl.PlayerESL;
import org.springframework.core.convert.ConversionService;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Collections;

/**
 * Tests the MusicPlayerService class.
 */
@RunWith(MockitoJUnitRunner.class)
public class MusicPlayerServiceTest {

    private static final String TRACK_ID = "trackId";

    private String encodedTrackId;
    private MusicPlayerService musicPlayerService;
    private PlayerStatus playerStatus;
    private ViewPlayerStatus viewPlayerStatus;

    @Mock
    private ConversionService mockConversionService;
    @Mock
    private PlayerESL mockPlayerESL;

    @Before
    public void setup() throws Exception {
        encodedTrackId = Base64.getUrlEncoder().encodeToString(TRACK_ID.getBytes("UTF-8"));

        musicPlayerService = new MusicPlayerService(mockConversionService, mockPlayerESL);

        playerStatus = PlayerStatus.builder().build();
        viewPlayerStatus = ViewPlayerStatus.builder().build();

        when(mockPlayerESL.playerStatus()).thenReturn(playerStatus);
        when(mockConversionService.convert(playerStatus, ViewPlayerStatus.class)).thenReturn(viewPlayerStatus);
    }

    @Test
    public void should_return_player_status() throws Exception {
        assertEquals("wrong ViewPlayerStatus object returned", viewPlayerStatus, musicPlayerService.status());
        verify(mockConversionService).convert(playerStatus, ViewPlayerStatus.class);
    }

    @Test
    public void should_play_track() throws UnsupportedEncodingException, FileNotFoundException {
        //Base64.getUrlEncoder().encodeToString(TRACK_ID.getBytes("UTF-8"))
        musicPlayerService.play(encodedTrackId);
        verify(mockPlayerESL).play(TRACK_ID);
    }

    @Test
    public void should_not_enqueue_duplicate_consecutive_tracks()
            throws UnsupportedEncodingException, FileNotFoundException {

        when(mockPlayerESL.playlist()).thenReturn(Collections.emptyList())
                                      .thenReturn(Collections.singletonList(Track.builder().path(TRACK_ID).build()));

        musicPlayerService.play(encodedTrackId);
        musicPlayerService.play(encodedTrackId);
        verify(mockPlayerESL).play(TRACK_ID); // just once!
    }

}
