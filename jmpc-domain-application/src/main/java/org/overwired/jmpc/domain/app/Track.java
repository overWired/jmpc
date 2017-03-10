package org.overwired.jmpc.domain.app;

import lombok.Builder;
import lombok.Value;

/**
 * Represents an audio track.
 */
@Builder
@Value
public class Track {
    String album;
    String artist;
    String title;
    int trackNumber;
}
