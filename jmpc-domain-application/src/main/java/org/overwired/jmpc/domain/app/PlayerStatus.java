package org.overwired.jmpc.domain.app;

import lombok.Builder;
import lombok.Singular;
import lombok.Value;

import java.util.List;

/**
 * An application domain object representing the music player.
 */
@Value
@Builder
public class PlayerStatus {
    Track currentSong;
    String status;
    @Singular("playlistItem")
    List<Track> playlist;
}
