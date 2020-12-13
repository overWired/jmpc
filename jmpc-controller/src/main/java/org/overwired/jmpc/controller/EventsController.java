package org.overwired.jmpc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.overwired.jmpc.domain.view.ViewPlayerStatus;
import org.overwired.jmpc.service.MusicPlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.function.Consumer;

@RestController
@Slf4j
public class EventsController {

    private final MusicPlayerService musicPlayerService;
    private final ObjectMapper objectMapper;

    @Autowired
    public EventsController(final MusicPlayerService musicPlayerService, final ObjectMapper objectMapper) {
        this.musicPlayerService = musicPlayerService;
        this.objectMapper = objectMapper;
    }

    @GetMapping(path = "/sse/status", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribeToStatus() {
        log.debug("processing subscription request for status");
        final SseEmitter emitter = new SseEmitter(86400L);
        log.trace("emitter timeout is {}", emitter.getTimeout());
        final Consumer<ViewPlayerStatus> eventConsumer = status -> {
            try {
                emitter.send(objectMapper.writeValueAsString(status));
            } catch (IOException exception) {
                log.error("failed to publish status: {}", status, exception);
            }
        };
        musicPlayerService.subscribe(eventConsumer);
        return emitter;
    }

}
