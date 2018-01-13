package org.overwired.jmpc.domain.view;

import lombok.Builder;
import lombok.Value;

/**
 * The view domain object representing a song.
 */
@Builder
@Value
public class ViewTrack {
    String artist;
    String path;
    String title;
}
