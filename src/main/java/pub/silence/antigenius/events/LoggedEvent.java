package pub.silence.antigenius.events;

import pub.silence.antigenius.AntiGenius;

public interface LoggedEvent {
    static <T extends LoggedEvent> void onEvent(T event){
        AntiGenius.LOGGER.info(event.toString());
    }
}
