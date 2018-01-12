import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.core.ConsoleAppender

appender("STDOUT", ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        pattern = "%date{ISO8601} [%-5level] %logger - %msg%n"
    }
}

logger('org.bff.javampd.server.MPDProperties', WARN)
logger('org.bff.javampd.server.MPDSocket', INFO)
root(DEBUG, ["STDOUT"])
