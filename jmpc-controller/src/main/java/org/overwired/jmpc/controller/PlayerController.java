package org.overwired.jmpc.controller;

import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.overwired.jmpc.domain.view.ViewPlayerStatus;
import org.overwired.jmpc.service.MusicPlayerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.io.FileNotFoundException;
import java.net.URI;

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
    public ResponseEntity<Void> play(@RequestParam String trackId) throws FileNotFoundException {
        LOGGER.debug("received a request to play {}", trackId);
        if (StringUtils.isNotEmpty(trackId)) {
            musicPlayerService.play(trackId);
        } else {
            throw new FileNotFoundException("cannot play track: missing parameter 'trackId'");
        }
        // TOTAL HACK to try and redirect to the Juke until I learn how to do this with AJAX and not leave the page.
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/"));
        return new ResponseEntity<Void>(headers, HttpStatus.FOUND);
    }

}
