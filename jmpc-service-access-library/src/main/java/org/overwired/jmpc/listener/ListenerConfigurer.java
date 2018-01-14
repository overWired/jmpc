package org.overwired.jmpc.listener;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.bff.javampd.monitor.StandAloneMonitor;
import org.bff.javampd.player.PlayerBasicChangeListener;
import org.bff.javampd.playlist.PlaylistBasicChangeListener;
import org.overwired.jmpc.sal.StandAloneMonitorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Configure application listeners after Spring context startup.
 */
@Component
@RequiredArgsConstructor
public class ListenerConfigurer {
    private static final Logger LOGGER = LoggerFactory.getLogger(ListenerConfigurer.class);

    private final StandAloneMonitorFactory monitorFactory; // will be autowired as a constructor argument
    @Autowired(required = false)
    @Setter
    private List<PlayerBasicChangeListener> playerBasicChangeListeners;
    @Autowired(required = false)
    @Setter
    private List<PlaylistBasicChangeListener> playlistBasicChangeListeners;

    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {
        LOGGER.trace("handling ContextRefreshedEvent from {}", event.getSource());
        StandAloneMonitor monitor = monitorFactory.getMonitor();
        registerPlayerBasicChangeListeners(monitor);
        registerPlaylistBasicChangeListeners(monitor);
        monitor.start();
    }

    private void registerPlayerBasicChangeListeners(final StandAloneMonitor monitor) {
        if (CollectionUtils.isEmpty(playerBasicChangeListeners)) {
            LOGGER.debug("no playerBasicChangeListeners found to be registered");
        } else {
            LOGGER.debug("registering {} playerBasicChangeListeners", playerBasicChangeListeners.size());
            playerBasicChangeListeners.forEach(monitor::addPlayerChangeListener);
        }
    }

    private void registerPlaylistBasicChangeListeners(final StandAloneMonitor monitor) {
        if (CollectionUtils.isEmpty(playlistBasicChangeListeners)) {
            LOGGER.debug("no playlistBasicChangeListeners found to be registered");
        } else {
            LOGGER.debug("registering {} playlistBasicChangeListeners", playlistBasicChangeListeners.size());
            playlistBasicChangeListeners.forEach(monitor::addPlaylistChangeListener);
        }
    }

}
