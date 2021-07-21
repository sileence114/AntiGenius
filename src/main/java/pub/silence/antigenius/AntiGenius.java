package pub.silence.antigenius;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pub.silence.antigenius.utils.RegistryGetter;
import pub.silence.antigenius.utils.config.Config;
import pub.silence.antigenius.utils.config.Language;


public interface AntiGenius{
    
    // Remember: Use [-Dfabric.log.level=debug] to show debug level on console.
    Logger LOGGER = LogManager.getLogger(AntiGenius.class);
    int CONFIG_VERSION = 0;
    int DATABASE_VERSION = 0;
    
    class InstanceManger{
        public static AntiGenius instance = null;
    }
    static AntiGenius getInstance() {
        return InstanceManger.instance;
    }
    
    default void onLoad(){
        AntiGenius.LOGGER.info(String.format(
            "Loading AntiGenius ... \n" +
            "     █████╗   ██████╗     \n" +
            "    ██╔══██╗ ██╔════╝     AntiGenius v%s (Config v%d DataBase v%d)\n" +
            "    ███████║ ██║  ███╗    Running on %s v%s\n" +
            "    ██╔══██║ ██║   ██║    MineCraft %s\n" +
            "    ██║  ██║ ╚██████╔╝    ",
            this.getAntiGeniusVersion(), this.CONFIG_VERSION, this.DATABASE_VERSION,
            this.getPlatFormName(), this.getPlatFormVersion(),
            this.gatGameVersion()
        ));
        Language.initialize();
        Config.initialize();
        Language.setLanguage(Config.getString("language"));
    }
    
    List<String> worlds = Collections.synchronizedList(new ArrayList<>());
    default void onInitializeWorld() {
        worlds.addAll(Objects.requireNonNull(RegistryGetter.get("DIMENSION_TYPE")));
    }
    
    String getAntiGeniusVersion();
    String getPlatFormName();
    String getPlatFormVersion();
    String gatGameVersion();
    Path getWorkingDir();
}