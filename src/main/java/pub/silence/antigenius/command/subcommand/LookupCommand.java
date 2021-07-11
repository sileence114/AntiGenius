package pub.silence.antigenius.command.subcommand;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.ServerCommandSource;
import pub.silence.antigenius.AntiGenius;

public class LookupCommand extends SubCommand {
    private static final LookupCommand INSTANCE = new LookupCommand();
    private LookupCommand(){
        super.shortName = "l";
    }
    public static LookupCommand getInstance() {
        return INSTANCE;
    }
    
    @Override
    public int executeCommand(CommandContext<ServerCommandSource> commandContext) {
        AntiGenius.LOGGER.info("LookupCommand Executes");
        return 0;
    }
}
