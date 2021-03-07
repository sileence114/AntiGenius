package pub.silence.antigenius.permission;

import net.minecraft.server.command.ServerCommandSource;

public class PermissionManager {
    public static boolean canUseCommand(ServerCommandSource source, Object commandLevel){
        if(commandLevel instanceof Boolean){
            return (Boolean) commandLevel;
        }
        String commandLevelString = commandLevel.toString();
        switch (commandLevelString){
            case "true":
                return true;
            case "false":
                return false;
            case "ops":
                return source.hasPermissionLevel(2);
            case "0":
            case "1":
            case "2":
            case "3":
            case "4":
                return source.hasPermissionLevel(Integer.parseInt(commandLevelString));
        }
            return false;
    }
    
}
