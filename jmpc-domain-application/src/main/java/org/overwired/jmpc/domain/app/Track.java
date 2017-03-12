package org.overwired.jmpc.domain.app;

import lombok.Builder;
import lombok.Value;
import org.apache.commons.lang3.builder.CompareToBuilder;

/**
 * Represents an audio track.
 */
@Builder
@Value
public class Track implements Comparable<Track> {
    String album;
    String artist;
    String title;
    int trackNumber;

    @Override
    public int compareTo(Track that) {
        return new CompareToBuilder()
                .append(this.artist, that.artist)
                .append(this.album, that.album)
                .append(this.trackNumber, that.trackNumber)
                .append(this.title, that.title)
                .toComparison();
    }

}
