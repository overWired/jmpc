package org.overwired.jmpc.esl;

import org.bff.javampd.player.Player;
import org.bff.javampd.playlist.Playlist;
import org.overwired.jmpc.domain.app.PlayerStatus;
import org.overwired.jmpc.domain.app.Track;
import org.overwired.jmpc.sal.MediaPlayerDaemonSAL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class PlayerStatusESL {

    private final ConversionService conversionService;
    private final MediaPlayerDaemonSAL sal;

    @Autowired
    public PlayerStatusESL(final ConversionService conversionService, final MediaPlayerDaemonSAL sal) {
        this.conversionService = conversionService;
        this.sal = sal;
    }

    /**
     * Gets the current status of the player.
     *
     * @return the status
     */
    public PlayerStatus playerStatus() {
        Player player = sal.getPlayer();

        return PlayerStatus.builder()
                           .currentSong(conversionService.convert(player.getCurrentSong(), Track.class))
                           .status(conversionService.convert(player.getStatus(), String.class))
                           .playlist(convertPlaylist(sal.getPlaylist()))
                           .build();
    }

    private List<Track> convertPlaylist(final Playlist playlist) {
        return playlist.getSongList()
                       .stream()
                       .skip(theCurrentSongOf(playlist))
                       .map(song -> conversionService.convert(song, Track.class))
                       .collect(Collectors.toList());
    }

    /**
     * Determine how many songs in the queue (playlist) should be skipped.
     *
     * @param playlist the playlist to interrogate
     * @return 1 if there is a currentSong, indicating to skip the first song in the playlist; 0 to keep all songs.
     */
    private int theCurrentSongOf(final Playlist playlist) {
        return (null == playlist.getCurrentSong()) ? 0 : 1;
    }

}
