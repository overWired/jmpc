package org.overwired.jmpc.esl.converter;

import static org.bff.javampd.Player.Status.STATUS_PLAYING;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

import org.bff.javampd.MPDPlayer;
import org.bff.javampd.objects.MPDSong;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.overwired.jmpc.domain.app.MusicPlayer;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;

/**
 * Tests the MPDPlayerToMusicPlayerConverter class.
 */
@RunWith(MockitoJUnitRunner.class)
public class MPDPlayerToMusicPlayerConverterTest {

    private static final String STATUS = "status";
    private final String CURRENT_SONG = "current song";
    @Mock
    PlayerStatusToStringConverter mockConverter;
    private Converter converter;
    @Mock
    private MusicPlayer.MusicPlayerBuilder mockBuilder;
    @Mock
    private MPDPlayer mockPlayer;
    private MusicPlayer musicPlayer;

    @Before
    public void setUp() throws Exception {
        MPDSong mpdSong = new MPDSong();
        mpdSong.setTitle(CURRENT_SONG);
        when(mockPlayer.getCurrentSong()).thenReturn(mpdSong);
        when(mockPlayer.getStatus()).thenReturn(STATUS_PLAYING);

        musicPlayer = MusicPlayer.builder().build();
        when(mockBuilder.currentSong(anyString())).thenReturn(mockBuilder);
        when(mockBuilder.status(anyString())).thenReturn(mockBuilder);
        when(mockBuilder.build()).thenReturn(musicPlayer);

        when(mockConverter.convert(STATUS_PLAYING)).thenReturn(STATUS);

        converter = new MPDPlayerToMusicPlayerConverter(mockBuilder, mockConverter);
    }

    @Test
    public void shouldConvertNullPlayerToEmptyMusicPlayer() throws Exception {
        assertNotNull("output should not be null when input is null", converter.convert(null));
        verify(mockBuilder).build();
        verifyNoMoreInteractions(mockBuilder);
    }

    @Test
    public void testWhenNoCurrentSong() throws Exception {
        when(mockPlayer.getCurrentSong()).thenReturn(null);

        assertEquals(musicPlayer, converter.convert(mockPlayer));
        verify(mockBuilder).currentSong("N / A");
        verify(mockBuilder).status(STATUS);
        verify(mockBuilder).build();
        verifyNoMoreInteractions(mockBuilder);
    }


    @Test
    public void testConvertingPopulatePlayer() throws Exception {
        assertEquals(musicPlayer, converter.convert(mockPlayer));
        verify(mockBuilder).currentSong(CURRENT_SONG);
        verify(mockBuilder).status(STATUS);
        verify(mockBuilder).build();
        verifyNoMoreInteractions(mockBuilder);
    }

}
