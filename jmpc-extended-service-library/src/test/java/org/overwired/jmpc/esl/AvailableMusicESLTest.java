import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.overwired.jmpc.domain.app.Track.builder;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.overwired.jmpc.domain.app.Track;
import org.overwired.jmpc.domain.view.Card;
import org.overwired.jmpc.domain.view.Cards;

import java.util.Arrays;
import java.util.List;

/**
 * Tests the AvailableMusicESL class.
 */
@RunWith(MockitoJUnitRunner.class)
public class AvailableMusicESLTest {

    private static final Track PHOTOGRAPH = builder().artist("Def Leppard")
            .album("Pyromania")
            .trackNumber(1)
            .title("Photograph")
            .build();
    private static final Track ROCK_ROCK = builder().artist("Def Leppard")
            .album("Pyromania")
            .trackNumber(1)
            .title("Rock Rock (Till You Drop")
            .build();
    private static final Track WHO_MADE_WHO = builder().artist("AC/DC")
            .album("Who Made Who")
            .title("Who Made Who")
            .trackNumber(1)
            .build();
    private static final Track YOU_SHOOK_ME = builder().artist("AC/DC")
            .album("Who Made Who")
            .title("You Shook Me All Night Long")
            .trackNumber(2)
            .build();
    private static final String WRONG_NUMBER_OF_CARDS_PRODUCED = "wrong number of cards produced";

    private AvailableMusicESL esl;

    @Before
    public void setup() {
        esl = new AvailableMusicESL();
    }

    @Test
    public void cardsCombineArtistsAfterSortingTracks() throws Exception {
        esl.setTracks(Arrays.asList(WHO_MADE_WHO, PHOTOGRAPH, YOU_SHOOK_ME, ROCK_ROCK));

        Cards cards = esl.availableMusic();
        assertNotNull(cards);
        List<Card> cardList = cards.getCards();
        assertEquals(WRONG_NUMBER_OF_CARDS_PRODUCED, 2, cardList.size());
    }

    @Test
    public void cardsSplitDifferentArtistsAfterSortingTracks() throws Exception {
        esl.setTracks(Arrays.asList(WHO_MADE_WHO, PHOTOGRAPH));

        Cards cards = esl.availableMusic();
        assertNotNull(cards);
        List<Card> cardList = cards.getCards();
        assertEquals(WRONG_NUMBER_OF_CARDS_PRODUCED, 2, cardList.size());
    }

}
