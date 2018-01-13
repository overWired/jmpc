package org.overwired.jmpc.service.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
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

    private PlayerStatusToViewPlayerStatusConverter converter;
    private ViewPlayerStatus expectedViewPlayerStatus;

    @Mock
    private TrackToViewTrackConverter mockTrackConverter;
    @Mock
    private ViewPlayerStatus.ViewPlayerStatusBuilder mockViewPlayerStatusBuilder;

    @Before
    public void setup() {
        converter = new PlayerStatusToViewPlayerStatusConverter(mockViewPlayerStatusBuilder, mockTrackConverter);

        expectedViewPlayerStatus = ViewPlayerStatus.builder().build();

        when(mockViewPlayerStatusBuilder.currentSong(any(ViewTrack.class))).thenReturn(mockViewPlayerStatusBuilder);
        when(mockViewPlayerStatusBuilder.status(anyString())).thenReturn(mockViewPlayerStatusBuilder);
        when(mockViewPlayerStatusBuilder.playlist(anyListOf(ViewTrack.class))).thenReturn(mockViewPlayerStatusBuilder);
        when(mockViewPlayerStatusBuilder.build()).thenReturn(expectedViewPlayerStatus);
    }

    @Test
    public void shouldConvertNullMusicPlayerToEmptyMusicPlayerView() throws Exception {
        ViewPlayerStatus actualViewPlayerStatus = converter.convert(null);
        assertNotNull("result should not be null", actualViewPlayerStatus);
    }

    @Test
    public void shouldConvertValidMusicPlayerToEquivalentMusicPlayerView() {
        Track currentTrack = Track.builder().title("current").build();
        ViewTrack currentViewTrack = ViewTrack.builder().title("current").build();

        Track nextTrack = Track.builder().title("next").build();
        ViewTrack nextViewTrack = ViewTrack.builder().title("next").build();

        PlayerStatus playerStatus = PlayerStatus.builder()
                                                .currentSong(currentTrack)
                                                .status("status")
                                                .playlistItem(nextTrack).build();

        when(mockTrackConverter.convert(currentTrack)).thenReturn(currentViewTrack);
        when(mockTrackConverter.convert(nextTrack)).thenReturn(nextViewTrack);

        ViewPlayerStatus actualViewPlayerStatus = converter.convert(playerStatus);
        assertEquals(expectedViewPlayerStatus, actualViewPlayerStatus);

        verify(mockViewPlayerStatusBuilder).currentSong(currentViewTrack);
        verify(mockViewPlayerStatusBuilder).status("status");
        verify(mockViewPlayerStatusBuilder).playlist(Collections.singletonList(nextViewTrack));
    }

}
