package org.overwired.jmpc.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.overwired.jmpc.domain.view.MusicPlayerView;
import org.overwired.jmpc.service.MusicPlayerService;

/**
 * Tests the MusicPlayerController class.
 */
@RunWith(MockitoJUnitRunner.class)
public class MusicPlayerControllerTest {

    private MusicPlayerController playerController;
    @Mock
    private MusicPlayerService mockMusicPlayerService;
    private MusicPlayerView musicPlayerView;

    @Before
    public void setup() throws Exception {
        playerController = new MusicPlayerController();
        playerController.setMusicPlayerService(mockMusicPlayerService);

        musicPlayerView = MusicPlayerView.builder().status("status").currentSong("currentSong").build();
        when(mockMusicPlayerService.player()).thenReturn(musicPlayerView);
    }

    @Test
    public void shouldUsePlayerServiceToGetPlayer() throws Exception {
        assertEquals("wrong musicPlayerView object returned", musicPlayerView, playerController.player());
    }

}
