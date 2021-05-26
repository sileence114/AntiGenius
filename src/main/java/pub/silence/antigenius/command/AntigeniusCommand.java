package pub.silence.antigenius.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import java.util.HashMap;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import pub.silence.antigenius.command.subcommand.CommandCommand;
import pub.silence.antigenius.command.subcommand.HelpCommand;
import pub.silence.antigenius.command.subcommand.InspectCommand;
import pub.silence.antigenius.command.subcommand.LookupCommand;
import pub.silence.antigenius.command.subcommand.SubCommand;


public class AntigeniusCommand {
    public static final HashMap<String, SubCommand> subCommandsMap = new HashMap<>();
    static {
        subCommandsMap.put("inspect", InspectCommand.getInstance());
        subCommandsMap.put("lookup", LookupCommand.getInstance());
        
        subCommandsMap.put("help", HelpCommand.getInstance());
        subCommandsMap.put("command", CommandCommand.getInstance());
    }
    
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        for(String alias : new String[] {"ag", "antigenius"}){
            LiteralArgumentBuilder<ServerCommandSource> subCommandTree = CommandManager.literal(alias).requires(
                (serverCommandSource) -> serverCommandSource.hasPermissionLevel(0)
            ).executes(context -> {
                if(context.getSource().hasPermissionLevel(HelpCommand.getInstance().getPermissionLevel())){
                    HelpCommand.getInstance().executeCommand(context);
                }
                return 1;
            });
            SubCommand subCommand;
            for(String subCommandName : subCommandsMap.keySet()){
                subCommand = subCommandsMap.get(subCommandName);
                subCommandTree.then(subCommand.build(subCommandName));
                if(null != subCommand.shortName){
                    subCommandTree.then(subCommand.build(subCommand.shortName));
                }
            }
            dispatcher.register(subCommandTree);
        }
    }
}
