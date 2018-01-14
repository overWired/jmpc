package org.overwired.jmpc.listener;

import lombok.RequiredArgsConstructor;
import org.bff.javampd.player.PlayerBasicChangeEvent;
import org.bff.javampd.player.PlayerBasicChangeListener;
import org.bff.javampd.playlist.Playlist;
import org.bff.javampd.playlist.PlaylistBasicChangeEvent;
import org.bff.javampd.playlist.PlaylistBasicChangeListener;
import org.bff.javampd.song.MPDSong;
import org.overwired.jmpc.sal.PlaylistFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Imbue MPD with a Jukebox personality, such as removing a song from the playlist once it has been played (or skipped).
 */
@Component
@RequiredArgsConstructor
public class JukeboxBehaviorEventListener implements PlayerBasicChangeListener, PlaylistBasicChangeListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(JukeboxBehaviorEventListener.class);

    private final PlaylistFactory playlistFactory;
    private AtomicReference<MPDSong> priorSong = new AtomicReference<>(null);

    @Override
    public void playerBasicChange(PlayerBasicChangeEvent playerBasicChangeEvent) {
        PlayerBasicChangeEvent.Status event = playerBasicChangeEvent.getStatus();
        LOGGER.trace("received PlayerBasicChangeEvent: {}", event);
        switch (event) {
            case PLAYER_PAUSED:
                break;
            case PLAYER_STARTED:
                break;
            case PLAYER_STOPPED:
                handleSongChange();
                break;
            case PLAYER_UNPAUSED:
                break;
            default:
                LOGGER.debug("received an unexpected PlayerBasicChangeEvent: {}", event);
        }
    }

    @Override
    public void playlistBasicChange(PlaylistBasicChangeEvent playlistBasicChangeEvent) {
        PlaylistBasicChangeEvent.Event event = playlistBasicChangeEvent.getEvent();
        LOGGER.trace("received PlaylistBasicChangeEvent: {}", event);
        switch (event) {
            case PLAYLIST_CHANGED:
                // update upcoming song list once I figure out how to push changes from web server to web browser
                break;
            case PLAYLIST_ENDED:
                // never seen this one happen
                break;
            case SONG_ADDED:
                break;
            case SONG_CHANGED:
                handleSongChange();
                break;
            case SONG_DELETED:
                break;
            default:
                LOGGER.debug("unexpected PlaylistBasicChangeEvent: {}", event);
        }
    }

    private void handleSongChange() {
        MPDSong current =  playlist().getCurrentSong();
        MPDSong previous = priorSong.getAndSet(current);
        MPDSong upcoming = playlist().getSongList().stream().findFirst().get();
        LOGGER.debug("previous: {}, current: {}, upcoming: {}",
                     titleOf(previous),
                     titleOf(current),
                     titleOf(upcoming));

        if (previousSongShouldBeRemovedFromPlaylist(previous, current)) {
            if (playlist().getSongList().contains(previous)) {
                LOGGER.trace("removing {} from playlist", previous.getTitle());
                playlist().removeSong(previous);
            }
        }
    }

    private Playlist playlist() {
        return playlistFactory.getPlaylist();
    }

    private boolean previousSongShouldBeRemovedFromPlaylist(MPDSong previous, MPDSong current) {
        // there is probably a better way to do this with the playlist ID, such as checking current.id > previous.id
        boolean remove = false;
        if (null == previous) {
            remove = false;
        } else if (null == current) {
            // previous wasn't null, so something was playing, but has ended.
            remove = true;
            // no more null checks
        } else if (!previous.getFile().equals(current.getFile())) {
            // different song, remove the previous previous from the playlist
            remove = true;
        }
        // else: nothing was playing before, and previous cannot remove nothing, so leave it false.

        LOGGER.trace("previous={}, current={}, remove: {}", filenameOf(previous), filenameOf(current), remove);
        return remove;
    }

    private String filenameOf(MPDSong song) {
        return (null == song) ? "null" : "MPDSong['" + song.getFile() + "']";
    }

    private String titleOf(MPDSong song) {
        return (null == song) ? "N/A" : song.getTitle();
    }
}
