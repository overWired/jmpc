package org.overwired.jmpc.esl.listener;

import org.overwired.jmpc.domain.app.JukeboxStatusChangedEvent;

/**
 * Listener that will handle {@link JukeboxStatusChangedEvent}s when they occur.
 */
@FunctionalInterface
public interface JukeboxStatusChangedEventListener {

    /**
     * Handles the event.
     *
     * @param event the event to handle
     */
    void handle(JukeboxStatusChangedEvent event);

}
