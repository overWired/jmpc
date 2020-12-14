package org.overwired.jmpc.service.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.overwired.jmpc.domain.app.PlayerStatus;
import org.overwired.jmpc.domain.app.Track;
import org.overwired.jmpc.domain.view.ViewPlayerStatus;

import java.util.Collections;

/**
 * Tests the PlayerStatusToViewPlayerStatusConverter class.
 */
@RunWith(MockitoJUnitRunner.class)
public class PlayerStatusToViewPlayerStatusConverterTest {

    private static final String STATUS = "playing";

    private PlayerStatusToViewPlayerStatusConverter converter;
    private Track currentTrack;
    private Track nextTrack;
    private PlayerStatus playerStatus = null;

    @Before
    public void setup() {
        converter = new PlayerStatusToViewPlayerStatusConverter();

        currentTrack = Track.builder().title("current").build();
        nextTrack = Track.builder().title("next").build();
    }

    @Test
    public void shouldConvertNullMusicPlayerToEmptyMusicPlayerView() throws Exception {
        assertNotNull("result should not be null", converter.convert(playerStatus));
    }

    @Test
    public void shouldConvertValidMusicPlayerToEquivalentMusicPlayerView() {
        playerStatus = setUpPlayerStatus();

        ViewPlayerStatus viewPlayerStatus = converter.convert(playerStatus);
        assertNotNull("result should not be null", viewPlayerStatus);
        assertEquals("current", viewPlayerStatus.getCurrentSong());
        assertEquals(Collections.singletonList("next"), viewPlayerStatus.getUpcomingSongs());
    }

    private PlayerStatus setUpPlayerStatus() {
        return PlayerStatus.builder()
                           .currentSong(currentTrack)
                           .status(STATUS)
                           .playlist(Collections.singletonList(nextTrack))
                           .build();
    }

}
