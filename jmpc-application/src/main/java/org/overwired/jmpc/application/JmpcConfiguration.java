package org.overwired.jmpc.application;

import org.apache.commons.lang3.StringUtils;
import org.bff.javampd.server.MPD;
import org.overwired.jmpc.domain.app.PlayerStatus;
import org.overwired.jmpc.domain.app.Track;
import org.overwired.jmpc.domain.view.ViewPlayerStatus;
import org.overwired.jmpc.domain.view.ViewTrack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.net.UnknownHostException;

/**
 * Configure the JukeboxMPC application.
 */
@Configuration
public class JmpcConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(JmpcConfiguration.class);
    private static final String PROTOTYPE = "prototype";

}
