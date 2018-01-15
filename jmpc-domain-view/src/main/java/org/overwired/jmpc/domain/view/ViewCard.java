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
    String index1;
    String index2;
    ViewTrack track1;
    ViewTrack track2;
}
