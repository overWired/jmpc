package org.overwired.jmpc.esl;

import lombok.AllArgsConstructor;
import lombok.Setter;
import org.bff.javampd.player.Player;
import org.bff.javampd.server.MPDConnectionException;
import org.overwired.jmpc.domain.app.MusicPlayer;
import org.overwired.jmpc.sal.MediaPlayerDaemonSAL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Repository;

/**
 * The Extended Service Library for the Music Player.
 */
@AllArgsConstructor
@Repository
@Setter
public class MusicPlayerESL {

    private static final Logger LOGGER = LoggerFactory.getLogger(MusicPlayerESL.class);

    @Autowired
    private ConversionService conversionService;
    @Autowired
    private MediaPlayerDaemonSAL sal;

    public MusicPlayer musicPlayer() {
        Player player = null;
        try {
            player = sal.getPlayer();
        } catch (MPDConnectionException e) {
            LOGGER.error("failed to get a Player, returning a stub MusicPlayer", e);
        }
        // the converter deals with null input gracefully, so we don't need to.
        return conversionService.convert(player, MusicPlayer.class);
    }

}
