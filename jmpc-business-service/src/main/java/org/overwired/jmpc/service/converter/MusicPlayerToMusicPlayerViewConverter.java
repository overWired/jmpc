package org.overwired.jmpc.service.converter;

import org.overwired.jmpc.domain.app.MusicPlayer;
import org.overwired.jmpc.domain.view.MusicPlayerView;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Converts a MusicPlayer object into a MusicPlayerView object.
 */
@Component
public class MusicPlayerToMusicPlayerViewConverter implements Converter<MusicPlayer, MusicPlayerView> {
    @Override
    public MusicPlayerView convert(MusicPlayer source) {
        MusicPlayerView.MusicPlayerViewBuilder builder = MusicPlayerView.builder();
        if (null != source) {
            builder.currentSong(source.getCurrentSong()).status(source.getStatus());
        }
        return builder.build();
    }
}
