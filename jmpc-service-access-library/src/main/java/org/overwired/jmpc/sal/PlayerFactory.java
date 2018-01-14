package org.overwired.jmpc.sal;

import org.bff.javampd.player.Player;

/**
 * Get an (MPD) Player.
 */
public interface PlayerFactory {
    /**
     * Get an (MPD) Player instance.
     * @return the player.
     */
    Player getPlayer();
}
