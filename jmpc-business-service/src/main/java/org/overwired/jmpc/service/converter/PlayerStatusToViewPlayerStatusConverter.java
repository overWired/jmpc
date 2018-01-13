package org.overwired.jmpc.service.converter;

import org.overwired.jmpc.domain.app.PlayerStatus;
import org.overwired.jmpc.domain.view.ViewPlayerStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

/**
 * Converts a app domain PlayerStatus object into a view domain ViewPlayerStatus object.
 */
@Component
public class PlayerStatusToViewPlayerStatusConverter implements Converter<PlayerStatus, ViewPlayerStatus> {

    private final ViewPlayerStatus.ViewPlayerStatusBuilder builder;
    private final TrackToViewTrackConverter trackConverter;

    @Autowired
    public PlayerStatusToViewPlayerStatusConverter(ViewPlayerStatus.ViewPlayerStatusBuilder builder,
                                                   TrackToViewTrackConverter trackConverter) {
        this.builder = builder;
        this.trackConverter = trackConverter;
    }

    @Override
    public ViewPlayerStatus convert(PlayerStatus playerStatus) {
        if (null != playerStatus) {
            builder.currentSong(trackConverter.convert(playerStatus.getCurrentSong()))
                   .status(playerStatus.getStatus())
                   .playlist(playerStatus.getPlaylist()
                                         .stream()
                                         .map(track -> trackConverter.convert(track))
                                         .collect(Collectors.toList()))
            ;
        }
        return builder.build();
    }
}
