package pub.silence.antigenius.command.subcommand;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import pub.silence.antigenius.command.AntigeniusCommand;

public class CommandCommand extends SubCommands{
    private static final CommandCommand INSTANCE = new CommandCommand();
    private CommandCommand(){}
    public static CommandCommand getInstance() {
        return INSTANCE;
    }
    @Override
    public LiteralArgumentBuilder<ServerCommandSource> build(String alias) {
        final LiteralArgumentBuilder<ServerCommandSource> literalArgumentBuilder = super.build(alias);
        for(String subCommandName : AntigeniusCommand.subCommandsMap.keySet()){
            literalArgumentBuilder.then(
                CommandManager.literal(subCommandName)
                              .executes((context)->querySubCommandEnable(context.getSource(), subCommandName))
                // TODO: Sub command Command could configure subcommand's permission level.
            );
        }
        return literalArgumentBuilder;
    }
    @Override
    public int executeCommand(CommandContext<ServerCommandSource> commandContext) {
        for(String subCommandName : AntigeniusCommand.subCommandsMap.keySet()){
            if(commandContext.getSource().hasPermissionLevel(AntigeniusCommand.subCommandsMap.get(subCommandName).permissionLevel)){
                commandContext.getSource().sendFeedback(new LiteralText(
                    subCommandName + " : " + AntigeniusCommand.subCommandsMap.get(subCommandName).permissionLevel
                ), false);
            }
        }
        return 0;
    }
    public int querySubCommandEnable(ServerCommandSource source, String subCommand){
        source.sendFeedback(new LiteralText(
            "SubCommand [" + subCommand + "] enabled: " + AntigeniusCommand.subCommandsMap.get(subCommand).isEnabled() +
            ". \nPermission level: " + AntigeniusCommand.subCommandsMap.get(subCommand).permissionLevel
        ), false);
        return 1;
    }
}
