package org.overwired.jmpc.domain.view;

import lombok.Value;

import java.util.List;

/**
 * View representing multiple cards.
 */
@Value
public class Cards {
    List<Card> cards;
}
