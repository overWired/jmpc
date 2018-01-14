package org.overwired.jmpc.sal;

import org.bff.javampd.playlist.Playlist;

/**
 * Get an (MPD) Playlist.
 */
public interface PlaylistFactory {
    /**
     * Get a Playlist.
     * @return the playlist.
     */
    Playlist getPlaylist();
}
