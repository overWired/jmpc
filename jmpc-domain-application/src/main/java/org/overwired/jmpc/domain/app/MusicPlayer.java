package org.overwired.jmpc.domain.app;

import lombok.Builder;
import lombok.Value;

/**
 * An application domain object representing the music player.
 */
@Value
@Builder
public class MusicPlayer {
    String currentSong;
    String status;
}
