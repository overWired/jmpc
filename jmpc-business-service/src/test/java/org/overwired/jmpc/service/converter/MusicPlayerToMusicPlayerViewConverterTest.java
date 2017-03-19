package org.overwired.jmpc.service.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.overwired.jmpc.domain.app.MusicPlayer;
import org.overwired.jmpc.domain.view.MusicPlayerView;

/**
 * Tests the MusicPlayerToMusicPlayerViewConverter class.
 */
@RunWith(MockitoJUnitRunner.class)
public class MusicPlayerToMusicPlayerViewConverterTest {

    private static final String CURRENT_SONG = "current song";
    private static final String STATUS = "status";

    private MusicPlayerToMusicPlayerViewConverter converter;
    private MusicPlayer musicPlayer;

    @Before
    public void setup() {
        musicPlayer = MusicPlayer.builder().currentSong(CURRENT_SONG).status(STATUS).build();
        converter = new MusicPlayerToMusicPlayerViewConverter();
    }

    @Test
    public void shouldConvertNullMusicPlayerToEmptyMusicPlayerView() throws Exception {
        assertNotNull("result should not be null", converter.convert(null));
    }

    @Test
    public void shouldConvertValidMusicPlayerToEquivalentMusicPlayerView() {
        MusicPlayerView musicPlayerView = converter.convert(musicPlayer);

        assertNotNull("result should not be null", musicPlayerView);
        assertEquals("wrong current song", CURRENT_SONG, musicPlayerView.getCurrentSong());
        assertEquals("wrong status", STATUS, musicPlayerView.getStatus());
    }

}
