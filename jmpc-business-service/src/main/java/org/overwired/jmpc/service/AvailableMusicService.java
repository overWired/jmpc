package org.overwired.jmpc.service;

import lombok.Setter;
import org.overwired.jmpc.domain.view.Card;
import org.overwired.jmpc.domain.view.Cards;
import org.overwired.jmpc.esl.AvailableMusicESL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * A business service providing access to available music.
 */
@Service
@Setter
public class AvailableMusicService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AvailableMusicService.class);

    @Autowired
    private AvailableMusicESL esl;

    public Cards availableMusic() {
        Cards cards = esl.availableMusic();
        LOGGER.trace("returning available music cards: {}", cards);
        return cards;
    }

}
