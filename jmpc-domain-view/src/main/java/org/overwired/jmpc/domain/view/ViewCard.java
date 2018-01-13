package org.overwired.jmpc.domain.view;

import lombok.Builder;
import lombok.Value;

/**
 * Represents the view model for a Jukebox ViewCard.
 */
@Builder
@Value
public class ViewCard {
    String artist;
    String id_a;
    String id_b;
    String title_a;
    String title_b;
}
