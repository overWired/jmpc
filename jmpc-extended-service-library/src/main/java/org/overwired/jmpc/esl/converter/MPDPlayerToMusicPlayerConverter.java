package org.overwired.jmpc.esl.converter;

import lombok.AllArgsConstructor;
import lombok.Setter;
import org.bff.javampd.player.MPDPlayer;
import org.bff.javampd.song.MPDSong;
import org.overwired.jmpc.domain.app.MusicPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Converts an MPD Player object into a MusicPlayer object.
 */
@AllArgsConstructor
@Component
@Setter
public class MPDPlayerToMusicPlayerConverter implements Converter<MPDPlayer, MusicPlayer> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MPDPlayerToMusicPlayerConverter.class);

    @Autowired
    MusicPlayer.MusicPlayerBuilder builder;
    @Autowired
    private PlayerStatusToStringConverter playerStatusConverter;

    public MusicPlayer convert(MPDPlayer source) {
        if (null != source) {
            try {
                MPDSong currentSong = source.getCurrentSong();
                String title;
                if (null == currentSong) {
                    LOGGER.info("no current song");
                    title = "N / A";
                } else {
                    title = currentSong.getTitle();
                }
                builder.currentSong(title);
                builder.status(playerStatusConverter.convert(source.getStatus()));
            } catch (NullPointerException e) {
                LOGGER.error("failed to access player data", e);
            }
        }
        return builder.build();
    }

}
