package pub.silence.antigenius.command.subcommand;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import pub.silence.antigenius.permission.PermissionManager;

public abstract class SubCommands {
    public int permissionLevel = 0;
    public LiteralArgumentBuilder<ServerCommandSource> build(String alias){
        return CommandManager.literal(alias)
                             .requires(this::hasPermission)
                             .executes(this::executeCommand);
    }
    
    public boolean hasPermission(ServerCommandSource source){
        return PermissionManager.canUseCommand(source, permissionLevel);
    }
    
    public void enabled(boolean ifEnabled){
        permissionLevel = ifEnabled ? 0 : 2;
    }
    public boolean isEnabled(){
        return permissionLevel >= 2;
    }
    
    public abstract int executeCommand(CommandContext<ServerCommandSource> commandContext);
}
