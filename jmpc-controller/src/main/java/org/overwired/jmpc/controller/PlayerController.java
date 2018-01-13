package org.overwired.jmpc.controller;

import lombok.Setter;
import org.overwired.jmpc.domain.view.ViewPlayerStatus;
import org.overwired.jmpc.service.MusicPlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Provide access to the music status.
 */
@RequestMapping("/player")
@RestController
@Setter
public class PlayerController {

    @Autowired
    private MusicPlayerService musicPlayerService;

    @RequestMapping("/status")
    public ViewPlayerStatus player() {
        return musicPlayerService.status();
    }

}
