package org.overwired.jmpc.service;

import lombok.Setter;
import lombok.experimental.Accessors;
import org.overwired.jmpc.domain.app.MusicPlayer;
import org.overwired.jmpc.domain.view.MusicPlayerView;
import org.overwired.jmpc.esl.MusicPlayerESL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

/**
 * A business service providing access to the music player.
 */
@Accessors(chain = true)
@Service
@Setter
public class MusicPlayerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MusicPlayerService.class);

    @Autowired
    private ConversionService conversionService;
    @Autowired
    private MusicPlayerESL musicPlayerESL;

    public MusicPlayerView player() {
        LOGGER.trace("retrieving a MusicPlayerView object");
        return conversionService.convert(applicationMusicPlayer(), MusicPlayerView.class);
    }

    private MusicPlayer applicationMusicPlayer() {
        return musicPlayerESL.musicPlayer();
    }

}
