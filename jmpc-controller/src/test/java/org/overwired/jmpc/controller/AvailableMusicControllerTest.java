package org.overwired.jmpc.controller;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.overwired.jmpc.domain.view.Cards;
import org.overwired.jmpc.service.AvailableMusicService;

import java.util.Collections;

/**
 * Tests the AvailableMusicController class.
 */
@RunWith(MockitoJUnitRunner.class)
public class AvailableMusicControllerTest {

    private Cards cards;
    private AvailableMusicController controller;
    private AvailableMusicService mockAvailableMusicService;

    @Before
    public void setUp() throws Exception {
        cards = new Cards(Collections.emptyList());
        mockAvailableMusicService = mock(AvailableMusicService.class);
        controller = new AvailableMusicController();
        controller.setMusicService(mockAvailableMusicService);
    }

    @Test
    public void getCards() throws Exception {
        when(mockAvailableMusicService.availableMusic()).thenReturn(cards);
        assertEquals(cards, controller.availableMusic());
    }

}