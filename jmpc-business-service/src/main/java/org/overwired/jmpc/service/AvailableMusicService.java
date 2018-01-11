package org.overwired.jmpc.service;

import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.bff.javampd.server.MPDConnectionException;
import org.overwired.jmpc.domain.app.Track;
import org.overwired.jmpc.domain.view.Card;
import org.overwired.jmpc.esl.AvailableMusicESL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A business service providing access to available music.
 */
@Service
@Setter
public class AvailableMusicService {

    public static final String NO_MUSIC_FOUND = "No Music Found";
    private static final Logger LOGGER = LoggerFactory.getLogger(AvailableMusicService.class);
    @Autowired
    private AvailableMusicESL esl;

    public List<Card> availableMusic() throws MPDConnectionException {
        List<Track> tracks = esl.availableMusic();
        List<Card> cardList = createCards(tracks);
        logCardsToBeReturned(cardList);
        return cardList;
    }

    private List<Card> createCards(List<Track> tracks) {
        List<Card> cardList;
        if (CollectionUtils.isEmpty(tracks)) {
            cardList = Collections.singletonList(Card.builder().artist(NO_MUSIC_FOUND).build());
        } else {
            cardList = new ArrayList<>(estimateNumberOfCards(tracks));
            Collections.sort(tracks);
            int trackIndex = 0;
            int numTracks = tracks.size();
            while (trackIndex < numTracks) {
                Track track = tracks.get(trackIndex);
                Card.CardBuilder builder = Card.builder()
                        .artist(track.getArtist())
                        .id_a(zeroPaddedCardId(trackIndex))
                        .title_a(track.getTitle());

                if (++trackIndex < numTracks) {
                    Track trackTwo = tracks.get(trackIndex);
                    // Only put two tracks on one card if they're by the same artist
                    if (StringUtils.equals(track.getArtist(), trackTwo.getArtist())) {
                        builder.id_b(zeroPaddedCardId(trackIndex++)).title_b(trackTwo.getTitle());
                    }
                }
                cardList.add(builder.build());
            }
        }
        return cardList;
    }

    private int estimateNumberOfCards(List<Track> tracks) {
        return Math.round(tracks.size() / 2);
    }

    private void logCardsToBeReturned(List<Card> cardList) {
        LOGGER.debug("returning {} music cards", cardList.size());
        LOGGER.trace("details of music cards: {}", cardList);
    }

    private String zeroPaddedCardId(int trackIndex) {
        return StringUtils.leftPad(String.valueOf(trackIndex), 2, '0');
    }

}
