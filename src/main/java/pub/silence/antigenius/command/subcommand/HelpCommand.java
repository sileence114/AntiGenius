package pub.silence.antigenius.command.subcommand;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import pub.silence.antigenius.command.AntigeniusCommand;

public class HelpCommand extends SubCommands{
    private static final HelpCommand INSTANCE = new HelpCommand();
    private HelpCommand(){}
    public static HelpCommand getInstance() {
        return INSTANCE;
    }
    
    @Override
    public int executeCommand(CommandContext<ServerCommandSource> commandContext) {
        for(String subCommandName : AntigeniusCommand.subCommandsMap.keySet()){
            if(commandContext.getSource().hasPermissionLevel(AntigeniusCommand.subCommandsMap.get(subCommandName).permissionLevel)){
                commandContext.getSource().sendFeedback(new LiteralText(
                    subCommandName + " - SUBCOMMAND"
                ), false);
            }
        }
        return 0;
    }
}
