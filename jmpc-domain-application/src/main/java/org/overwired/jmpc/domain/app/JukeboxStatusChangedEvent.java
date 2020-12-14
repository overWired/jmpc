package org.overwired.jmpc.domain.app;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class JukeboxStatusChangedEvent {
    String name;
    PlayerStatus playerStatus;
}
