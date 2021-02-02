package pub.silence.antigenius.handler;

import net.minecraft.server.MinecraftServer;
import pub.silence.antigenius.AntiGenius;

public class ServerLife {
    private static ServerLife INSTANCE = new ServerLife();
    private ServerLife(){

    }
    public static ServerLife getInstance(){
        return INSTANCE;
    }
    public void onServerStarting(MinecraftServer server){
        AntiGenius.info("onServerStart");
    }
    public void onServerStopping(MinecraftServer server){
        AntiGenius.info("onStopServer");
    }
}
