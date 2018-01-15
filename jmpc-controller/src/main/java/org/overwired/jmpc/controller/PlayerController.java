package org.overwired.jmpc.controller;

import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.overwired.jmpc.domain.view.ViewPlayerStatus;
import org.overwired.jmpc.service.MusicPlayerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;

/**
 * Provide access to the music status.
 */
@RequestMapping("/player")
@RestController
@Setter
public class PlayerController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlayerController.class);

    @Autowired
    private MusicPlayerService musicPlayerService;

    @RequestMapping("/status")
    public ViewPlayerStatus player() {
        return musicPlayerService.status();
    }

    @RequestMapping("/play")
    public void play(@RequestParam String trackId) throws FileNotFoundException {
        LOGGER.debug("received a request to play {}", trackId);
        if (StringUtils.isNotEmpty(trackId)) {
            musicPlayerService.play(trackId);
        } else {
            throw new FileNotFoundException("cannot play track: missing parameter 'trackId'");
        }
    }

}
