package pub.silence.antigenius.command;

import com.mojang.brigadier.CommandDispatcher;
import java.util.HashMap;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import pub.silence.antigenius.AntiGenius;
import pub.silence.antigenius.command.subcommand.CommandCommand;
import pub.silence.antigenius.command.subcommand.HelpCommand;
import pub.silence.antigenius.command.subcommand.InspectCommand;
import pub.silence.antigenius.command.subcommand.LookupCommand;
import pub.silence.antigenius.command.subcommand.SubCommands;


public class AntigeniusCommand {
    public static final HashMap<String, SubCommands> subCommandsMap = new HashMap<>();
    static {
        subCommandsMap.put("help", HelpCommand.getInstance());
        subCommandsMap.put("command", CommandCommand.getInstance());
        subCommandsMap.put("inspect", InspectCommand.getInstance());
        subCommandsMap.put("lookup", LookupCommand.getInstance());
    }
    
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, boolean dedicated) {
        if(!dedicated){
            AntiGenius.error("How do you get here?");
            return;
        }
        for(String alias : new String[] {"ag", "antigenius"}){
            dispatcher.register(
                CommandManager.literal(alias).requires((serverCommandSource) -> serverCommandSource.hasPermissionLevel(0))
                              .then(HelpCommand.getInstance().build("h"))
                              .then(HelpCommand.getInstance().build("help"))
                              
                              .then(CommandCommand.getInstance().build("c"))
                              .then(CommandCommand.getInstance().build("command"))
                              
                              .then(LookupCommand.getInstance().build("l"))
                              .then(LookupCommand.getInstance().build("lookup"))
                              
                              .then(InspectCommand.getInstance().build("i"))
                              .then(InspectCommand.getInstance().build("inspect"))
            );
        }
    }
}
