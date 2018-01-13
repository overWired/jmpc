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
import org.overwired.jmpc.domain.view.ViewPlayerStatus;
import org.overwired.jmpc.esl.PlayerStatusESL;
import org.springframework.core.convert.ConversionService;

/**
 * Tests the MusicPlayerService class.
 */
@RunWith(MockitoJUnitRunner.class)
public class MusicPlayerServiceTest {

    private MusicPlayerService musicPlayerService;
    private PlayerStatus playerStatus;
    private ViewPlayerStatus viewPlayerStatus;

    @Mock
    private ConversionService mockConversionService;
    @Mock
    private PlayerStatusESL mockPlayerStatusESL;

    @Before
    public void setup() throws Exception {
        musicPlayerService = new MusicPlayerService(mockConversionService, mockPlayerStatusESL);

        playerStatus = PlayerStatus.builder().build();
        viewPlayerStatus = ViewPlayerStatus.builder().build();

        when(mockPlayerStatusESL.playerStatus()).thenReturn(playerStatus);
        when(mockConversionService.convert(playerStatus, ViewPlayerStatus.class)).thenReturn(viewPlayerStatus);
    }

    @Test
    public void player() throws Exception {
        assertEquals("wrong ViewPlayerStatus object returned", viewPlayerStatus, musicPlayerService.status());
        verify(mockConversionService).convert(playerStatus, ViewPlayerStatus.class);
    }

}
