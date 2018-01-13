package org.overwired.jmpc.service;

import lombok.Setter;
import lombok.experimental.Accessors;
import org.overwired.jmpc.domain.view.ViewPlayerStatus;
import org.overwired.jmpc.esl.PlayerStatusESL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

/**
 * A business service providing access to the music status.
 */
@Service
public class MusicPlayerService {

    private final ConversionService conversionService;
    private final PlayerStatusESL playerStatusESL;

    @Autowired
    public MusicPlayerService(ConversionService conversionService, PlayerStatusESL playerStatusESL) {
        this.conversionService = conversionService;
        this.playerStatusESL = playerStatusESL;
    }

    public ViewPlayerStatus status() {
        return conversionService.convert(playerStatusESL.playerStatus(), ViewPlayerStatus.class);
    }

}
