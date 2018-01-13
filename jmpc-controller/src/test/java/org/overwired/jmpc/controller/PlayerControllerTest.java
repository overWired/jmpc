package org.overwired.jmpc.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.overwired.jmpc.domain.view.ViewPlayerStatus;
import org.overwired.jmpc.service.MusicPlayerService;

/**
 * Tests the PlayerController class.
 */
@RunWith(MockitoJUnitRunner.class)
public class PlayerControllerTest {

    private PlayerController playerController;
    @Mock
    private MusicPlayerService mockMusicPlayerService;
    private ViewPlayerStatus viewPlayerStatus;

    @Before
    public void setup() throws Exception {
        playerController = new PlayerController();
        playerController.setMusicPlayerService(mockMusicPlayerService);

        viewPlayerStatus = ViewPlayerStatus.builder().status("status").currentSong("currentSong").build();
        when(mockMusicPlayerService.status()).thenReturn(viewPlayerStatus);
    }

    @Test
    public void shouldUsePlayerServiceToGetPlayer() throws Exception {
        assertEquals("wrong viewPlayerStatus object returned", viewPlayerStatus, playerController.player());
    }

}
