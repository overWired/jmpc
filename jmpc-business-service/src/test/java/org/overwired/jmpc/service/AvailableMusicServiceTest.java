package org.overwired.jmpc.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.overwired.jmpc.domain.view.Card;
import org.overwired.jmpc.domain.view.Cards;
import org.overwired.jmpc.esl.AvailableMusicESL;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests the AvailableMusicService class.
 */
@RunWith(MockitoJUnitRunner.class)
public class AvailableMusicServiceTest {

    private AvailableMusicService service;
    private AvailableMusicESL mockEsl;
    private Cards cards;

    @Before
    public void setup() {
        mockEsl = mock(AvailableMusicESL.class);
        cards = new Cards(Collections.singletonList(
                Card.builder().artist("test").build()
        ));
        when(mockEsl.availableMusic()).thenReturn(cards);

        service = new AvailableMusicService();
        service.setEsl(mockEsl);
    }

    @Test
    public void availableMusic() throws Exception {
        assertEquals("wrong Cards returned", cards, service.availableMusic());
    }

}
