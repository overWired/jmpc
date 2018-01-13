package org.overwired.jmpc.esl;

import lombok.Setter;
import org.bff.javampd.player.Player;
import org.bff.javampd.playlist.Playlist;
import org.overwired.jmpc.domain.app.PlayerStatus;
import org.overwired.jmpc.domain.app.Track;
import org.overwired.jmpc.sal.MediaPlayerDaemonSAL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * The Extended Service Library for the Music Player.
 */
@Repository
public class PlayerStatusESL {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlayerStatusESL.class);

    private final ConversionService conversionService;
    private final MediaPlayerDaemonSAL sal;
    @Autowired
    @Setter
    private PlayerStatus.PlayerStatusBuilder playerStatusBuilder;

    @Autowired
    public PlayerStatusESL(ConversionService conversionService, MediaPlayerDaemonSAL sal) {
        this.conversionService = conversionService;
        this.sal = sal;
    }

    public PlayerStatus playerStatus() {
        Player player = sal.getPlayer();

        return playerStatusBuilder.currentSong(conversionService.convert(player.getCurrentSong(), Track.class))
                                  .status(conversionService.convert(player.getStatus(), String.class))
                                  .playlist(convertPlaylist(sal.getPlaylist()))
                                  .build();
    }

    private List<Track> convertPlaylist(final Playlist playlist) {
        return playlist.getSongList().stream()
                       .map(song -> conversionService.convert(song, Track.class))
                       .collect(Collectors.toList());
    }

}
