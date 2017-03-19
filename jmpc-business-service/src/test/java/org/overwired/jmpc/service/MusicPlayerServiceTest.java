package org.overwired.jmpc.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.overwired.jmpc.domain.app.MusicPlayer;
import org.overwired.jmpc.domain.view.MusicPlayerView;
import org.overwired.jmpc.esl.MusicPlayerESL;
import org.springframework.core.convert.ConversionService;

/**
 * Tests the PlayerService class.
 */
@RunWith(MockitoJUnitRunner.class)
public class MusicPlayerServiceTest {

    private final String CURRENT_SONG = "currentSong";
    private final String STATUS = "status";

    @Mock
    private ConversionService mockConversionService;
    private MusicPlayer musicPlayer;
    @Mock
    private MusicPlayerESL mockMusicPlayerESL;
    private MusicPlayerService musicPlayerService;
    private MusicPlayerView musicPlayerView;

    @Before
    public void setup() throws Exception {
        musicPlayer = MusicPlayer.builder().currentSong(CURRENT_SONG).status(STATUS).build();
        musicPlayerView = MusicPlayerView.builder().currentSong("currentSong").status(STATUS).build();
        musicPlayerService = new MusicPlayerService()
                .setConversionService(mockConversionService)
                .setMusicPlayerESL(mockMusicPlayerESL);

        when(mockMusicPlayerESL.musicPlayer()).thenReturn(musicPlayer);
        when(mockConversionService.convert(musicPlayer, MusicPlayerView.class)).thenReturn(musicPlayerView);
    }

    @Test
    public void player() throws Exception {
        assertEquals("wrong MusicPlayerView object returned", musicPlayerView, musicPlayerService.player());
    }

}
