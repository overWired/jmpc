package org.overwired.jmpc.controller;

import lombok.Setter;
import org.overwired.jmpc.domain.view.MusicPlayerView;
import org.overwired.jmpc.service.MusicPlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Provide access to the music player.
 */
@RestController
@Setter
public class MusicPlayerController {

    @Autowired
    private MusicPlayerService musicPlayerService;

    @RequestMapping("/musicPlayer")
    public MusicPlayerView player() {
        return musicPlayerService.player();
    }

}
