package org.overwired.jmpc.application;

import org.apache.commons.lang3.StringUtils;
import org.bff.javampd.MPD;
import org.bff.javampd.exception.MPDConnectionException;
import org.overwired.jmpc.domain.app.Track;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;

/**
 * Configure the JukeboxMPC application.
 */
@Configuration
public class JmpcConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(JmpcConfiguration.class);

    @Bean
    public MPD.Builder mpdBuilder(@Value("${mpd.host}") String host,
                   @Value("${mpd.port}") int port,
                   @Value("${mpd.password:}") String password) throws UnknownHostException {

        LOGGER.debug("returning MPD.Builder: host='{}', port={}, password='{}", host, port, password);
        return new MPD.Builder().server(host).port(port).password(StringUtils.trimToNull(password));
    }

    @Bean
    public Track.TrackBuilder trackBuilder() {
        return Track.builder();
    }

}
