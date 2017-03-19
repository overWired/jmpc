package org.overwired.jmpc.esl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import org.bff.javampd.Player;
import org.bff.javampd.exception.MPDConnectionException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.overwired.jmpc.domain.app.MusicPlayer;
import org.overwired.jmpc.sal.MediaPlayerDaemonSAL;
import org.springframework.core.convert.ConversionService;

/**
 * Tests the MusicPlayerESL class.
 */
@RunWith(MockitoJUnitRunner.class)
public class MusicPlayerESLTest {

    private MusicPlayerESL esl;
    @Mock
    private ConversionService mockConversionService;
    private MusicPlayer musicPlayer;
    @Mock
    private Player mockPlayer;
    @Mock
    private MediaPlayerDaemonSAL mockSal;

    @Before
    public void setUp() throws Exception {
        musicPlayer = MusicPlayer.builder().build();

        when(mockSal.getPlayer()).thenReturn(mockPlayer);
        when(mockConversionService.convert(null, MusicPlayer.class)).thenReturn(musicPlayer);
        when(mockConversionService.convert(mockPlayer, MusicPlayer.class)).thenReturn(musicPlayer);
        esl = new MusicPlayerESL(mockConversionService, mockSal);
    }

    @Test
    public void shouldHandleExceptionsGracefully() throws Exception {
        when(mockSal.getPlayer()).thenThrow(new MPDConnectionException("intentional test exception"));
        assertNotNull(esl.musicPlayer());
    }

    @Test
    public void shouldReturnMusicPlayer() throws Exception {
        when(mockSal.getPlayer()).thenReturn(mockPlayer);
        assertEquals(musicPlayer, esl.musicPlayer());
    }

}
