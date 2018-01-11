package org.overwired.jmpc.esl.converter;

import org.bff.javampd.player.Player;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Converts a Player.Status object into a String.
 */
@Component
public class PlayerStatusToStringConverter implements Converter<Player.Status, String> {

    private static final String UNRECOGNIZED = "unrecognized status";

    @Override
    public String convert(Player.Status source) {
        String status = UNRECOGNIZED;
        switch (source) {
            case STATUS_PAUSED:
                status = "paused";
                break;
            case STATUS_PLAYING:
                status = "playing";
                break;
            case STATUS_STOPPED:
                status = "stopped";
                break;
        }
        return status;
    }
}
