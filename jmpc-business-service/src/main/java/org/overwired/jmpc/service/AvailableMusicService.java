package org.overwired.jmpc.service;

import org.overwired.jmpc.domain.view.Cards;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Arrays;

import static org.overwired.jmpc.domain.view.Card.builder;

/**
 * A business service providing access to available music.
 */
@Service
public class AvailableMusicService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AvailableMusicService.class);

    public Cards availableMusic() {
        int cardId = 1;
        Cards cards = new Cards(
                Arrays.asList(
                        builder().artist("AC/DC")
                                .id_a(String.valueOf(cardId++))
                                .id_b(String.valueOf(cardId++))
                                .title_a("Who Made Who")
                                .title_b("Thunderstruck")
                                .build(),
                        builder().artist("Def Leppard")
                                .id_a(String.valueOf(cardId++))
                                .id_b(String.valueOf(cardId++))
                                .title_a("Rock Rock (Till You Drop)")
                                .title_b("Photograph")
                                .build(),
                        builder().artist("Meatloaf")
                                .id_a(String.valueOf(cardId++))
                                .id_b(String.valueOf(cardId++))
                                .title_a("Midnight at the Lost and Found")
                                .title_b("Paradise by the Dashboard Light")
                                .build(),
                        builder().artist("Mötley Crüe")
                                .id_a(String.valueOf(cardId++))
                                .id_b(String.valueOf(cardId++))
                                .title_a("Wild Side")
                                .title_b("Girls, Girls, Girls")
                                .build(),
                        builder().artist("Ozzy Osbourne")
                                .id_a(String.valueOf(cardId++))
                                .id_b(String.valueOf(cardId++))
                                .title_a("Mr. Tinkertrain")
                                .title_b("I Don't Want to Change the World")
                                .build()
                )
        );
        LOGGER.trace("returning available music cards: {}", cards);
        return cards;
    }

}
