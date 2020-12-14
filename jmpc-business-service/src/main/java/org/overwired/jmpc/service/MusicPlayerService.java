package org.overwired.jmpc.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.overwired.jmpc.domain.app.PlayerStatus;
import org.overwired.jmpc.domain.app.Track;
import org.overwired.jmpc.domain.app.constant.LogMessage;
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
import java.util.function.Consumer;

/**
 * A business service providing access to the music status.
 */
@Service
@Slf4j
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

    public void subscribe(Consumer<ViewPlayerStatus> eventConsumer) {
        Consumer<PlayerStatus> statusPublisher = status -> {
            log.debug("publishing player status: currentSong={} {}",
                      status.getCurrentSong(),
                      LogMessage.DETAILS_AT_TRACE);
            log.trace(LogMessage.DETAIL_PREFIX, status);
            eventConsumer.accept(conversionService.convert(status, ViewPlayerStatus.class));
        };
        playerESL.subscribe(statusPublisher);
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
