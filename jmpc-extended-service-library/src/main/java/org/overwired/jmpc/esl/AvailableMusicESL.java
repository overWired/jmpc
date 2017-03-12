import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.overwired.jmpc.domain.app.Track;
import org.overwired.jmpc.domain.view.Card;
import org.overwired.jmpc.domain.view.Card.CardBuilder;
import org.overwired.jmpc.domain.view.Cards;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The Extended Service Library for Available Music.
 */
@Repository
@Setter
public class AvailableMusicESL {

    @Autowired
    private List<Track> tracks;

    public Cards availableMusic() {
        List<Card> cardList = new ArrayList<>(estimateNumberOfCards(tracks));
        Collections.sort(tracks);
        int trackIndex = 0;
        int numTracks = tracks.size();
        while (trackIndex < numTracks) {
            Track track = tracks.get(trackIndex++);
            CardBuilder builder = Card.builder()
                    .artist(track.getArtist())
                    .id_a(zeroPaddedCardId(trackIndex))
                    .title_a(track.getTitle());
            if (trackIndex < numTracks) {
                Track trackTwo = tracks.get(trackIndex);
                // Only put two tracks on one card if they're by the same artist
                if (StringUtils.equals(track.getArtist(), trackTwo.getArtist())) {
                    builder.id_b(zeroPaddedCardId(++trackIndex)).title_b(trackTwo.getTitle());
                }
            }
            cardList.add(builder.build());
        }
        return new Cards(cardList);
    }

    private int estimateNumberOfCards(List<Track> tracks) {
        return Math.round(tracks.size() / 2);
    }

    private String zeroPaddedCardId(int trackIndex) {
        return StringUtils.leftPad(String.valueOf(trackIndex), 2, '0');
    }

}
