package org.overwired.jmpc.controller;

import org.overwired.jmpc.domain.view.Cards;
import org.overwired.jmpc.service.AvailableMusicService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Provide access to Jukebox cards.
 */
@RestController
public class CardController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CardController.class);

    @Autowired
    private AvailableMusicService musicService;

    public CardController() {
        LOGGER.trace("constructing org.overwired.jmpc.controller.CardController");
    }

    @RequestMapping("/cards")
    public Cards getCards() {
        return musicService.availableMusic();
    }

}
