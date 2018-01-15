package org.overwired.jmpc.service;

import org.apache.commons.collections4.CollectionUtils;
import org.overwired.jmpc.domain.app.Track;
import org.overwired.jmpc.domain.view.ViewPlayerStatus;
import org.overwired.jmpc.esl.PlayerESL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.util.Base64;
import java.util.List;

/**
 * A business service providing access to the music status.
 */
@Service
public class MusicPlayerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MusicPlayerService.class);

    private final ConversionService conversionService;
    private final PlayerESL playerESL;

    @Autowired
    public MusicPlayerService(ConversionService conversionService, PlayerESL playerESL) {
        this.conversionService = conversionService;
        this.playerESL = playerESL;
    }

    public ViewPlayerStatus status() {
        return conversionService.convert(playerESL.playerStatus(), ViewPlayerStatus.class);
    }

    public void play(String encodedTrackId) throws FileNotFoundException {
        String trackId = new String(Base64.getUrlDecoder().decode(encodedTrackId));
        LOGGER.trace("received a request to play {}", trackId);
        if (trackIsNotAlreadyLastInQueue(trackId)) {
            playerESL.play(trackId);
        }
    }

    private boolean trackIsNotAlreadyLastInQueue(String trackId) {
        boolean trackIsLast = false;
        final List<Track> tracks = playerESL.playlist();
        if (CollectionUtils.isNotEmpty(tracks)) {
            Track lastTrack = tracks.get(tracks.size() - 1);
            trackIsLast = lastTrack.getPath().equals(trackId);
        }
        return !trackIsLast;
    }

}
