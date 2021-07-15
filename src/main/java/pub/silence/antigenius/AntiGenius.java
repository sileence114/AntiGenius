package pub.silence.antigenius;

import java.nio.file.Path;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pub.silence.antigenius.config.Config;
import pub.silence.antigenius.config.Language;
import pub.silence.antigenius.platform.AntiGeniusInstanceManger;
import pub.silence.antigenius.utils.CollectionUtils;
import pub.silence.antigenius.utils.RegistryGetter;


public interface AntiGenius{
    
    // Remember: Use [-Dfabric.log.level=debug] to show debug level on console.
    Logger LOGGER = LogManager.getLogger(AntiGenius.class);
    
    static AntiGenius getInstance() {
        return AntiGeniusInstanceManger.getInstance();
    }
    
    default void onLoad(){
        StringBuilder msg = new StringBuilder(":>\n\n");
        msg.append("     █████╗   ██████╗    ").append('\n');
        msg.append("    ██╔══██╗ ██╔════╝    ").append('\n');
        msg.append("    ███████║ ██║  ███╗   ").append("AntiGenius v").append(getInstance().getAntiGeniusVersion()).append('\n');
        msg.append("    ██╔══██║ ██║   ██║   ").append("Running on ").append(getInstance().getPlatFormName()).append(" v").append(getInstance().getPlatFormVersion()).append('\n');
        msg.append("    ██║  ██║ ╚██████╔╝   ").append("MineCraft v").append(getInstance().gatGameVersion()).append('\n');
        AntiGenius.LOGGER.info(msg);
        Language.initialize();
        Config.initialize();
        Language.setLanguage(Config.getString("language"));
    }
    
    default void onInitializeWorld() {
        CollectionUtils.printSet(
            RegistryGetter.dimensions(),
            "========== dimensions =========="
        );
        CollectionUtils.printSet(
            RegistryGetter.blocks(),
            "========== blocks =========="
        );
        CollectionUtils.printSet(
            RegistryGetter.items(),
            "========== items =========="
        );
        AntiGenius.LOGGER.info("");
    }
    
    String getAntiGeniusVersion();
    String getPlatFormName();
    String getPlatFormVersion();
    String gatGameVersion();
    Path getWorkingDir();
}