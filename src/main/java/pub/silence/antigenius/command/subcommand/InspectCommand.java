package pub.silence.antigenius.command.subcommand;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.ServerCommandSource;
import pub.silence.antigenius.AntiGenius;

public class InspectCommand extends SubCommand {
    
    private static final InspectCommand INSTANCE = new InspectCommand();
    private InspectCommand(){
        super.shortName = "i";
    }
    public static InspectCommand getInstance() {
        return INSTANCE;
    }
    @Override
    public int executeCommand(CommandContext<ServerCommandSource> commandContext) {
        AntiGenius.info("InspectCommand Executes");
        return 0;
    }
    
}
