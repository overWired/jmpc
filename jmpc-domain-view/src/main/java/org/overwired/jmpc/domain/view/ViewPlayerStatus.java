package org.overwired.jmpc.domain.view;

import lombok.Builder;
import lombok.Singular;
import lombok.Value;

import java.util.List;

/**
 * The view domain object representing the player.
 */
@Builder
@Value
public class ViewPlayerStatus {
    ViewTrack currentSong;
    String status;
    @Singular("playlistItem")
    List<ViewTrack> playlist;
}
