package org.overwired.jmpc.esl.converter;

import static org.junit.Assert.assertEquals;

import org.bff.javampd.player.Player;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Tests the PlayerStatusToStringConverter class.
 */
@RunWith(MockitoJUnitRunner.class)
public class PlayerStatusToStringConverterTest {

    private PlayerStatusToStringConverter converter;

    @Before
    public void setup() {
        converter = new PlayerStatusToStringConverter();
    }

    @Test
    public void shouldReturnPaused() throws Exception {
        assertEquals("paused", converter.convert(Player.Status.STATUS_PAUSED));
    }

    @Test
    public void shouldReturnPlaying() throws Exception {
        assertEquals("playing", converter.convert(Player.Status.STATUS_PLAYING));
    }

    @Test
    public void shouldReturnStopped() throws Exception {
        assertEquals("stopped", converter.convert(Player.Status.STATUS_STOPPED));
    }

}
