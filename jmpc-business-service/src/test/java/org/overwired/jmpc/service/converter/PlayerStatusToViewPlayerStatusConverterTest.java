package org.overwired.jmpc.service.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.overwired.jmpc.domain.app.PlayerStatus;
import org.overwired.jmpc.domain.app.Track;
import org.overwired.jmpc.domain.view.ViewPlayerStatus;
import org.overwired.jmpc.domain.view.ViewTrack;

import java.util.Collections;

/**
 * Tests the PlayerStatusToViewPlayerStatusConverter class.
 */
@RunWith(MockitoJUnitRunner.class)
public class PlayerStatusToViewPlayerStatusConverterTest {

    private static final String STATUS = "playing";

    private PlayerStatusToViewPlayerStatusConverter converter;
    private Track currentTrack;
    private ViewTrack currentViewTrack;
    private Track nextTrack;
    private ViewTrack nextViewTrack;
    private PlayerStatus playerStatus = null;

    @Mock
    private TrackToViewTrackConverter mockTrackConverter;

    @Before
    public void setup() {
        converter = new PlayerStatusToViewPlayerStatusConverter(mockTrackConverter);

        currentTrack = Track.builder().title("current").build();
        nextTrack = Track.builder().title("next").build();
        currentViewTrack = ViewTrack.builder().title("next").build();
        nextViewTrack = ViewTrack.builder().title("vNext").build();
    }

    @Test
    public void shouldConvertNullMusicPlayerToEmptyMusicPlayerView() throws Exception {
        assertNotNull("result should not be null", converter.convert(playerStatus));
    }

    @Test
    public void shouldConvertValidMusicPlayerToEquivalentMusicPlayerView() {
        playerStatus = setUpPlayerStatus();
        when(mockTrackConverter.convert(currentTrack)).thenReturn(currentViewTrack);
        when(mockTrackConverter.convert(nextTrack)).thenReturn(nextViewTrack);

        ViewPlayerStatus viewPlayerStatus = converter.convert(playerStatus);
        assertNotNull("result should not be null", viewPlayerStatus);
        assertEquals(currentViewTrack, viewPlayerStatus.getCurrentSong());
        assertEquals(STATUS, viewPlayerStatus.getStatus());
        assertEquals(Collections.singletonList(nextViewTrack), viewPlayerStatus.getPlaylist());
    }

    private PlayerStatus setUpPlayerStatus() {
        return PlayerStatus.builder()
                           .currentSong(currentTrack)
                           .status(STATUS)
                           .playlist(Collections.singletonList(nextTrack))
                           .build();
    }

}
