package pub.silence.antigenius.command.subcommand;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.ServerCommandSource;
import pub.silence.antigenius.AntiGenius;

public class LookupCommand extends SubCommands {
    private static final LookupCommand INSTANCE = new LookupCommand();
    private LookupCommand(){}
    public static LookupCommand getInstance() {
        return INSTANCE;
    }
    
    @Override
    public int executeCommand(CommandContext<ServerCommandSource> commandContext) {
        AntiGenius.info("LookupCommand Executes");
        return 0;
    }
}
