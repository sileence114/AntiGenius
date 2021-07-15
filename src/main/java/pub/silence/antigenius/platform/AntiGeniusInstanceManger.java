package pub.silence.antigenius.platform;

import pub.silence.antigenius.AntiGenius;

public class AntiGeniusInstanceManger{
    private static AntiGenius instance;
    public static void setInstance(AntiGenius instance) {
        AntiGeniusInstanceManger.instance = instance;
    }
    public static AntiGenius getInstance(){
        return AntiGeniusInstanceManger.instance;
    }
}
