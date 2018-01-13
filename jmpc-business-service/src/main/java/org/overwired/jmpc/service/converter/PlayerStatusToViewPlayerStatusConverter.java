package org.overwired.jmpc.service.converter;

import org.overwired.jmpc.domain.app.PlayerStatus;
import org.overwired.jmpc.domain.view.ViewPlayerStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

/**
 * Converts a app domain PlayerStatus object into a view domain ViewPlayerStatus object.
 */
@Component
public class PlayerStatusToViewPlayerStatusConverter implements Converter<PlayerStatus, ViewPlayerStatus> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlayerStatusToViewPlayerStatusConverter.class);

    private final TrackToViewTrackConverter trackConverter;

    @Autowired
    public PlayerStatusToViewPlayerStatusConverter(TrackToViewTrackConverter trackConverter) {
        this.trackConverter = trackConverter;
    }

    @Override
    public ViewPlayerStatus convert(PlayerStatus playerStatus) {
        ViewPlayerStatus viewPlayerStatus;
        ViewPlayerStatus.ViewPlayerStatusBuilder builder = ViewPlayerStatus.builder();
        if (null == playerStatus) {
            LOGGER.info("playerStatus to be converted is null");
            viewPlayerStatus = builder.build();
        } else {
            viewPlayerStatus = builder
                    .currentSong(trackConverter.convert(playerStatus.getCurrentSong()))
                    .status(playerStatus.getStatus())
                    .playlist(playerStatus.getPlaylist()
                                          .stream()
                                          .map(trackConverter::convert)
                                          .collect(Collectors.toList()))
                    .build();
        }
        return viewPlayerStatus;
    }

}
