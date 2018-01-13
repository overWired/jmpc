package org.overwired.jmpc.controller;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.overwired.jmpc.domain.view.ViewCard;
import org.overwired.jmpc.service.AvailableMusicService;

import java.util.Collections;
import java.util.List;

/**
 * Tests the AvailableMusicController class.
 */
@RunWith(MockitoJUnitRunner.class)
public class AvailableMusicControllerTest {

    private List<ViewCard> viewCards;
    private AvailableMusicController controller;
    private AvailableMusicService mockAvailableMusicService;

    @Before
    public void setUp() throws Exception {
        viewCards = Collections.emptyList();
        mockAvailableMusicService = mock(AvailableMusicService.class);
        controller = new AvailableMusicController();
        controller.setMusicService(mockAvailableMusicService);
    }

    @Test
    public void getCards() throws Exception {
        when(mockAvailableMusicService.availableMusic()).thenReturn(viewCards);
        assertEquals(viewCards, controller.availableMusic());
    }

}
