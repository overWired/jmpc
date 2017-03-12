package org.overwired.jmpc.esl;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.bff.javampd.MPD;
import org.bff.javampd.exception.MPDConnectionException;
import org.bff.javampd.exception.MPDDatabaseException;
import org.bff.javampd.objects.MPDSong;
import org.overwired.jmpc.domain.app.Track;
import org.overwired.jmpc.domain.view.Card;
import org.overwired.jmpc.domain.view.Card.CardBuilder;
import org.overwired.jmpc.domain.view.Cards;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * The Extended Service Library for Available Music.
 */
@Repository
@Setter
public class AvailableMusicESL {

    private static final Logger LOGGER = LoggerFactory.getLogger(AvailableMusicESL.class);

    @Autowired
    private ConversionService conversionService;
    @Autowired
    private MPD.Builder mpdBuilder;
    @Getter(lazy = true)
    private final MPD mpd = initMpd();

    public Cards availableMusic() {
        List<Card> cardList = Collections.emptyList();
        try {
            List<Track> tracks = availableTracks();

            cardList = new ArrayList<>(estimateNumberOfCards(tracks));
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
        } catch (MPDDatabaseException e) {
            LOGGER.error("failed to retrieve database from MPD server");
        }
        return new Cards(cardList);
    }

    private List<Track> availableTracks() throws MPDDatabaseException {
        LOGGER.trace("retrieving songs from MPD database");
        Collection<MPDSong> mpdSongs = getMpd().getDatabase().listAllSongs();
        LOGGER.trace("found {} songs", mpdSongs.size());

        List<Track> tracks = new ArrayList<>(mpdSongs.size());
        for (MPDSong mpdSong : mpdSongs) {
            tracks.add(conversionService.convert(mpdSong, Track.class));
        }
        LOGGER.trace("sorting tracks");
        Collections.sort(tracks);
        return tracks;
    }


    private int estimateNumberOfCards(List<Track> tracks) {
        return Math.round(tracks.size() / 2);
    }

    private MPD initMpd() {
        MPD mpd = null;
        try {
            mpd = mpdBuilder.build();
        } catch (MPDConnectionException e) {
            LOGGER.error("failed to connect to MPD server", e);
        }
        return mpd;
    }

    private String zeroPaddedCardId(int trackIndex) {
        return StringUtils.leftPad(String.valueOf(trackIndex), 2, '0');
    }

}
