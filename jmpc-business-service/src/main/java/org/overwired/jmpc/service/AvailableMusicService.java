package org.overwired.jmpc.service;

import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.bff.javampd.exception.MPDConnectionException;
import org.bff.javampd.exception.MPDDatabaseException;
import org.overwired.jmpc.domain.app.Track;
import org.overwired.jmpc.domain.view.Card;
import org.overwired.jmpc.domain.view.Cards;
import org.overwired.jmpc.esl.AvailableMusicESL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * A business service providing access to available music.
 */
@Service
@Setter
public class AvailableMusicService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AvailableMusicService.class);
    public static final String NO_MUSIC_FOUND = "No Music Found";

    @Autowired
    private AvailableMusicESL esl;

    public Cards availableMusic() throws MPDDatabaseException, MPDConnectionException {
        List<Track> tracks = esl.availableMusic();
        List<Card> cardList = createCards(tracks);
        Cards cards = new Cards(cardList);
        LOGGER.trace("returning available music cards: {}", cards);
        return cards;
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

    private String zeroPaddedCardId(int trackIndex) {
        return StringUtils.leftPad(String.valueOf(trackIndex), 2, '0');
    }

}
