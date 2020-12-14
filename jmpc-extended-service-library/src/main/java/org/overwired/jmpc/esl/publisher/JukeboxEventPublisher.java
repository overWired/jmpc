package org.overwired.jmpc.esl.publisher;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.bff.javampd.monitor.StandAloneMonitor;
import org.bff.javampd.player.PlayerBasicChangeEvent;
import org.bff.javampd.player.PlayerBasicChangeListener;
import org.bff.javampd.playlist.Playlist;
import org.bff.javampd.playlist.PlaylistBasicChangeEvent;
import org.bff.javampd.playlist.PlaylistBasicChangeListener;
import org.bff.javampd.song.MPDSong;
import org.overwired.jmpc.domain.app.JukeboxStatusChangedEvent;
import org.overwired.jmpc.domain.app.constant.LogMessage;
import org.overwired.jmpc.esl.PlayerStatusESL;
import org.overwired.jmpc.esl.listener.JukeboxStatusChangedEventListener;
import org.overwired.jmpc.sal.MediaPlayerDaemonSAL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

/**
 * Publishes Jukebox events to any interested subscribers.
 */
@Component
@Slf4j
public class JukeboxEventPublisher implements PlayerBasicChangeListener, PlaylistBasicChangeListener {

    private final PlayerStatusESL playerStatusESL;
    private final List<JukeboxStatusChangedEventListener> statusChangedEventListeners;
    private final MediaPlayerDaemonSAL sal;

    @Autowired
    public JukeboxEventPublisher(final PlayerStatusESL playerStatusESL, MediaPlayerDaemonSAL sal) {
        this.playerStatusESL = playerStatusESL;
        this.sal = sal;
        this.statusChangedEventListeners = new LinkedList<>();
        register();
    }

    @Override
    public void playerBasicChange(PlayerBasicChangeEvent playerBasicChangeEvent) {
        PlayerBasicChangeEvent.Status source = playerBasicChangeEvent.getStatus();
        log.trace("received PlayerBasicChangeEvent: {}", source);
        final JukeboxStatusChangedEvent event =
                JukeboxStatusChangedEvent.builder()
                                         .name(source.name())
                                         .playerStatus(playerStatusESL.playerStatus())
                                         .build();
        statusChangedEventListeners.forEach(listener -> deliverEvent(event, listener));
    }

    @Override
    public void playlistBasicChange(PlaylistBasicChangeEvent playlistBasicChangeEvent) {
        PlaylistBasicChangeEvent.Event source = playlistBasicChangeEvent.getEvent();
        log.trace("received PlaylistBasicChangeEvent: {}", source);
        final JukeboxStatusChangedEvent event =
                JukeboxStatusChangedEvent.builder()
                                         .name(source.name())
                                         .playerStatus(playerStatusESL.playerStatus())
                                         .build();
        statusChangedEventListeners.forEach(listener -> deliverEvent(event, listener));
    }

    /**
     * Subscribe to {@link org.overwired.jmpc.domain.app.JukeboxStatusChangedEvent}s.
     *
     * @param listener the listener to subscribe
     */
    public synchronized void subscribe(@NonNull final JukeboxStatusChangedEventListener listener) {
        if (!statusChangedEventListeners.contains(listener)) {
            statusChangedEventListeners.add(listener);
        }
    }

    private void deliverEvent(final JukeboxStatusChangedEvent event, final JukeboxStatusChangedEventListener listener) {
        try {
            log.debug("delivering event: {} {}", event.getName(), LogMessage.DETAILS_AT_TRACE);
            log.trace(LogMessage.DETAIL_PREFIX, event);
            listener.handle(event);
        } catch (Exception exception) {
            log.error("statusChangedEventListener failed to handle event: {}", event, exception);
        }
    }

    private void register() {
        StandAloneMonitor monitor = sal.getMonitor();
        monitor.addPlayerChangeListener(this);
        monitor.addPlaylistChangeListener(this);
        monitor.start();
    }

    private void handleSongChange() {
        MPDSong current = playlist().getCurrentSong();
        MPDSong upcoming = playlist().getSongList().stream().findFirst().orElse(null);
        log.debug("current: {}, upcoming: {}", titleOf(current), titleOf(upcoming));
    }

    private Playlist playlist() {
        return sal.getPlaylist();
    }

    private String titleOf(MPDSong song) {
        return (null == song) ? "N/A" : song.getTitle();
    }

}
