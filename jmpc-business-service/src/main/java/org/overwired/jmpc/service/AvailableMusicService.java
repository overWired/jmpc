package org.overwired.jmpc.service;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.bff.javampd.server.MPDConnectionException;
import org.overwired.jmpc.domain.app.Track;
import org.overwired.jmpc.domain.view.ViewCard;
import org.overwired.jmpc.domain.view.ViewTrack;
import org.overwired.jmpc.esl.AvailableMusicESL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * A business service providing access to available music.
 */
@RequiredArgsConstructor
@Service
public class AvailableMusicService {

    protected static final String NO_MUSIC_FOUND = "No Music Found";
    private static final Logger LOGGER = LoggerFactory.getLogger(AvailableMusicService.class);

    private final ConversionService conversionService;
    private final AvailableMusicESL esl;

    public List<ViewCard> availableMusic() throws MPDConnectionException {
        List<Track> tracks = esl.availableMusic();
        List<ViewCard> viewCardList = createCards(tracks);
        logCardsToBeReturned(viewCardList);
        return viewCardList;
    }

    private List<ViewCard> createCards(List<Track> tracks) {
        List<ViewCard> viewCardList;
        if (CollectionUtils.isEmpty(tracks)) {
            viewCardList = Collections.singletonList(ViewCard.builder().artist(NO_MUSIC_FOUND).build());
        } else {
            viewCardList = new LinkedList<>();
            Collections.sort(tracks);
            int trackIndex = 0;
            int numTracks = tracks.size();
            while (trackIndex < numTracks) {
                Track track = tracks.get(trackIndex);
                ViewCard.ViewCardBuilder builder = ViewCard.builder()
                        .artist(track.getArtist())
                        .index1(zeroPaddedCardId(trackIndex))
                        .track1(conversionService.convert(track, ViewTrack.class));

                if (++trackIndex < numTracks) {
                    Track trackTwo = tracks.get(trackIndex);
                    // Only put two tracks on one card if they're by the same artist
                    if (StringUtils.equals(track.getArtist(), trackTwo.getArtist())) {
                        builder.index2(zeroPaddedCardId(trackIndex++))
                               .track2(conversionService.convert(trackTwo, ViewTrack.class));
                    }
                }
                viewCardList.add(builder.build());
            }
        }
        return viewCardList;
    }

    private void logCardsToBeReturned(List<ViewCard> viewCardList) {
        LOGGER.debug("returning {} music cards", viewCardList.size());
        LOGGER.trace("details of music cards: {}", viewCardList);
    }

    private String zeroPaddedCardId(int trackIndex) {
        return StringUtils.leftPad(String.valueOf(trackIndex), 2, '0');
    }

}
