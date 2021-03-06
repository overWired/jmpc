package org.overwired.jmpc.controller;

import lombok.Setter;
import org.bff.javampd.server.MPDConnectionException;
import org.overwired.jmpc.domain.view.ViewCard;
import org.overwired.jmpc.service.AvailableMusicService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Provide access to Jukebox cards.
 */
@RequestMapping("/music")
@RestController
@Setter
public class AvailableMusicController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AvailableMusicController.class);

    @Autowired
    private AvailableMusicService musicService;

    @RequestMapping("/cards")
    public List<ViewCard>  availableMusic() throws MPDConnectionException {
        LOGGER.trace("retrieving available music from the available music service");
        return musicService.availableMusic();
    }

}
