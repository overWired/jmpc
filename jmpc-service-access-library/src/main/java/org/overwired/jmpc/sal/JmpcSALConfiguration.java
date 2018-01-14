package org.overwired.jmpc.sal;

import org.apache.commons.lang3.StringUtils;
import org.bff.javampd.server.MPD;
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
public class JmpcSALConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(JmpcSALConfiguration.class);
    private static final String PROTOTYPE = "prototype";

    @Bean
    @Scope(PROTOTYPE)
    public MPD.Builder mpdBuilder(@Value("${mpd.host}") String host,
                                  @Value("${mpd.port}") int port,
                                  @Value("${mpd.password:}") String password) throws UnknownHostException {

        LOGGER.debug("returning MPD.Builder: host='{}', port={}, password='{}", host, port, password);
        return new MPD.Builder().server(host).port(port).password(StringUtils.trimToNull(password));
    }

}
