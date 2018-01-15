package org.overwired.jmpc.esl;

import org.bff.javampd.player.Player;
import org.bff.javampd.playlist.Playlist;
import org.overwired.jmpc.domain.app.PlayerStatus;
import org.overwired.jmpc.domain.app.Track;
import org.overwired.jmpc.sal.MediaPlayerDaemonSAL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Repository;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The Extended Service Library for the Music Player.
 */
@Repository
public class PlayerESL {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlayerESL.class);

    private final ConversionService conversionService;
    private final MediaPlayerDaemonSAL sal;

    @Autowired
    public PlayerESL(ConversionService conversionService, MediaPlayerDaemonSAL sal) {
        this.conversionService = conversionService;
        this.sal = sal;
    }

    public void play(String trackId) throws FileNotFoundException {
        LOGGER.trace("received a request to play {}", trackId);
        try {
            sal.getPlaylist().addSong(trackId);
        } catch (Exception e) {
            throw new FileNotFoundException("unable to play track '" + trackId + "'");
        }
        sal.getPlayer().play();
    }

    public PlayerStatus playerStatus() {
        // This is too much.  I am a dumb ass.
        // The ESL should have less logic, and the business service should do the filtering and combining.
        Player player = sal.getPlayer();

        return PlayerStatus.builder()
                           .currentSong(conversionService.convert(player.getCurrentSong(), Track.class))
                           .status(conversionService.convert(player.getStatus(), String.class))
                           .playlist(convertPlaylist(sal.getPlaylist()))
                           .build();
    }

    /**
     * Get the unfiltered list of upcoming tracks.
     *
     * @return the list of upcoming tracks.
     */
    public List<Track> playlist() {
        return sal.getPlaylist()
                  .getSongList()
                  .stream()
                  .map(song -> conversionService.convert(song, Track.class))
                  .collect(Collectors.toList());
    }

    private List<Track> convertPlaylist(final Playlist playlist) {
        return playlist.getSongList()
                       .stream()
                       .skip(theCurrentSongOf(playlist))
                       .map(song -> conversionService.convert(song, Track.class))
                       .collect(Collectors.toList());
    }

    /**
     * Determine if the first song in the playlist should be skipped.
     *
     * @param playlist the playilst to interrogate
     * @return 1 if there is a currentSong, indicating to skip the first song in the songlist; 0 to keep all songs.
     */
    private int theCurrentSongOf(Playlist playlist) {
        return (null == playlist.getCurrentSong()) ? 0 : 1;
    }

}
