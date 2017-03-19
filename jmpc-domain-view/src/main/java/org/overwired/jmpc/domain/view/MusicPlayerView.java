package org.overwired.jmpc.domain.view;

import lombok.Builder;
import lombok.Value;

/**
 * The view domain object representing the player.
 */
@Builder
@Value
public class MusicPlayerView {
    String currentSong;
    String status;
}
