package pub.silence.antigenius.command.subcommand;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

public abstract class SubCommand {
    private int permissionLevel = 0;
    public String shortName = null;
    // Override it if necessary.
    // Resign this subcommand by default.
    public LiteralArgumentBuilder<ServerCommandSource> build(String alias){
        return CommandManager.literal(alias).requires(this::hasPermission).executes(this::executeCommand);
    }
    // Must Override.
    // Default executed handler.
    public abstract int executeCommand(CommandContext<ServerCommandSource> commandContext);

    // Better Not Override.
    // Returns command availability from CommandSource
    public boolean hasPermission(ServerCommandSource source){
        return source.hasPermissionLevel(this.permissionLevel);
    }
    // Better Not Override.
    // Set command availability for normal player or not.
    public void enabled(boolean ifEnabled){
        this.permissionLevel = ifEnabled ? 0 : 2;
    }
    // Better Not Override.
    // Returns command availability for normal player.
    public boolean isEnabled(){
        return this.permissionLevel < 2;
    }
    // Better Not Override.
    // Set permission level.
    public void setPermissionLevel(int level){
        this.permissionLevel = level;
    }
    // Better Not Override.
    // Get permission level.
    public int getPermissionLevel(){
        return this.permissionLevel;
    }
    
}
