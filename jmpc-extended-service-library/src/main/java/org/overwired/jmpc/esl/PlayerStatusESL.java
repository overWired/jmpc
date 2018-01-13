package org.overwired.jmpc.esl;

import lombok.AllArgsConstructor;
import lombok.Setter;
import org.bff.javampd.player.Player;
import org.bff.javampd.song.MPDSong;
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
@AllArgsConstructor
@Repository
@Setter
public class PlayerStatusESL {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlayerStatusESL.class);
    @Autowired
    PlayerStatus.PlayerStatusBuilder playerStatusBuilder;
    @Autowired
    private ConversionService conversionService;
    @Autowired
    private MediaPlayerDaemonSAL sal;

    public PlayerStatus playerStatus() {
        Player player = sal.getPlayer();

        return playerStatusBuilder.currentSong(conversionService.convert(player.getCurrentSong(), Track.class))
                                  .status(conversionService.convert(player.getStatus(), String.class))
                                  .playlist(playlist())
                                  .build();
    }

    private List<Track> playlist() {
        List<MPDSong> songList = sal.getPlaylist().getSongList();
        List<Track> tracks = songList.stream()
                                     .map(song -> conversionService.convert(song, Track.class))
                                     .collect(Collectors.toList());
        return tracks;
    }

}
