package org.overwired.jmpc.esl;

import lombok.extern.slf4j.Slf4j;
import org.overwired.jmpc.domain.app.PlayerStatus;
import org.overwired.jmpc.domain.app.Track;
import org.overwired.jmpc.domain.app.constant.LogMessage;
import org.overwired.jmpc.esl.publisher.JukeboxEventPublisher;
import org.overwired.jmpc.sal.MediaPlayerDaemonSAL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Repository;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * The Extended Service Library for the Music Player.
 */
@Repository
@Slf4j
public class PlayerESL {

    private final ConversionService conversionService;
    private final JukeboxEventPublisher eventPublisher;
    private final PlayerStatusESL playerStatusESL;
    private final MediaPlayerDaemonSAL sal;

    @Autowired
    public PlayerESL(final ConversionService conversionService,
                     final JukeboxEventPublisher eventPublisher,
                     final PlayerStatusESL playerStatusESL,
                     final MediaPlayerDaemonSAL sal) {
        this.conversionService = conversionService;
        this.eventPublisher = eventPublisher;
        this.playerStatusESL = playerStatusESL;
        this.sal = sal;
    }

    public void play(final String trackId) throws FileNotFoundException {
        log.trace("received a request to play {}", trackId);
        try {
            sal.getPlaylist().addSong(trackId);
        } catch (Exception e) {
            throw new FileNotFoundException("unable to play track '" + trackId + "'");
        }
        sal.getPlayer().play();
    }

    /**
     * Gets the current status of the player.
     *
     * @return the status
     */
    public PlayerStatus playerStatus() {
        return playerStatusESL.playerStatus();
    }

    /**
     * Get the unfiltered list of upcoming tracks.
     *
     * @return the list of upcoming tracks.
     */
    public List<Track> playlist() {
        return sal.getPlaylist()
                  .getSongList()
                  .stream()
                  .map(song -> conversionService.convert(song, Track.class))
                  .collect(Collectors.toList());
    }

    public void subscribe(final Consumer<PlayerStatus> statusPublisher) {
        log.debug("subscribing to player status events");
        eventPublisher.subscribe(event ->  {
            log.debug("handing event {} {}", event.getName(), LogMessage.DETAILS_AT_TRACE);
            log.trace(LogMessage.DETAIL_PREFIX, event);
            statusPublisher.accept(event.getPlayerStatus());
        });
    }

}
