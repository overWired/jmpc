package org.overwired.jmpc.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.overwired.jmpc.domain.view.ViewPlayerStatus;
import org.overwired.jmpc.service.MusicPlayerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.FileNotFoundException;

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

        viewPlayerStatus = ViewPlayerStatus.builder().build();
        when(mockMusicPlayerService.status()).thenReturn(viewPlayerStatus);
    }

    @Test
    public void shouldUsePlayerServiceToGetPlayer() throws Exception {
        assertEquals("wrong viewPlayerStatus object returned", viewPlayerStatus, playerController.player());
    }

    @Test
    public void shouldUsePlayerSerivceToPlayTrack() throws FileNotFoundException {
        final ResponseEntity<Void> response = playerController.play("unknown");
        assertEquals(HttpStatus.FOUND, response.getStatusCode());
        assertEquals("/", response.getHeaders().get("location").get(0));
        verify(mockMusicPlayerService).play("unknown");
    }

    @Test(expected = FileNotFoundException.class)
    public void shouldRefuseToPlayIfMissingTrackId() throws FileNotFoundException {
        playerController.play(null);
    }

}
