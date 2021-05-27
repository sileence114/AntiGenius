package pub.silence.antigenius.command.subcommand;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.util.Formatting;
import pub.silence.antigenius.command.AntigeniusCommand;
import pub.silence.antigenius.config.Language;

public class CommandCommand extends SubCommand {
    
    private static final CommandCommand INSTANCE = new CommandCommand();
    private CommandCommand(){
        super.shortName = "c";
        this.setPermissionLevel(2);
    }
    public static CommandCommand getInstance() {
        return INSTANCE;
    }
    @Override
    public LiteralArgumentBuilder<ServerCommandSource> build(String alias) {
        final LiteralArgumentBuilder<ServerCommandSource> literalArgumentBuilder = super.build(alias);
        for(String subCommandName : AntigeniusCommand.subCommandsMap.keySet()){
            literalArgumentBuilder.then(CommandManager.literal(
                subCommandName
            ).executes(
                context -> querySubCommandEnable(context.getSource(), subCommandName)
            ).then(
                CommandManager.argument(
                    "switch", BoolArgumentType.bool()
                ).executes(
                    context -> configCommand(context, subCommandName, BoolArgumentType.getBool(context,"switch"))
                )
            ).then(
                CommandManager.argument(
                    "permission", IntegerArgumentType.integer()
                ).executes(
                    context -> configCommand(context, subCommandName, IntegerArgumentType.getInteger(context,"permission"))
                )
            ));
        }
        return literalArgumentBuilder;
    }
    @Override
    public int executeCommand(CommandContext<ServerCommandSource> commandContext) {
        commandContext.getSource().sendFeedback(
            new LiteralText("AntiGenius //").setStyle(new Style().setColor(Formatting.AQUA)).append(
                    new LiteralText(
                        " --- " + Language.get("command.command.head") + " ---"
                    ).setStyle(new Style().setColor(Formatting.GOLD))
                ),
            false
        );
        for(String subCommandName : AntigeniusCommand.subCommandsMap.keySet()){
            this.feedBackCommandStatus(commandContext.getSource(), subCommandName, false);
        }
        return 0;
    }
    
    public int querySubCommandEnable(ServerCommandSource source, String subCommand){
        source.sendFeedback(
            new LiteralText("AntiGenius //").setStyle(new Style().setColor(Formatting.AQUA)).append(
                new LiteralText(
                    " --- " + Language.get("command.command.head") + " ---"
                ).setStyle(new Style().setColor(Formatting.GOLD))
            ),
            false
        );
        this.feedBackCommandStatus(source, subCommand, false);
        return 1;
    }
    
    public int configCommand(CommandContext<ServerCommandSource> commandContext, String subCommandName, boolean result){
        AntigeniusCommand.subCommandsMap.get(subCommandName).enabled(result);
        this.feedBackCommandStatus(commandContext.getSource(), subCommandName, true);
        return 1;
    }
    public int configCommand(CommandContext<ServerCommandSource> commandContext, String subCommandName, int permissionLevel){
        AntigeniusCommand.subCommandsMap.get(subCommandName).setPermissionLevel(
            Math.min(Math.max(permissionLevel, 0), 4)
        );
        this.feedBackCommandStatus(commandContext.getSource(), subCommandName, true);
        return 1;
    }
    
    private void feedBackCommandStatus(ServerCommandSource source, String subCommandName, boolean toOps){
        SubCommand subCommand = AntigeniusCommand.subCommandsMap.get(subCommandName);
        source.sendFeedback(
            new LiteralText("").setStyle(new Style().setColor(Formatting.GRAY)).append(" - ").append(
                subCommand.isEnabled() ?
                new LiteralText(
                    "[" + Language.get("command.enabled") + "] "
                ).setStyle(
                    new Style().setColor(Formatting.GREEN).setHoverEvent(new HoverEvent(
                        HoverEvent.Action.SHOW_TEXT,
                        new LiteralText(Language.get("command.enabled.hover"))
                    )).setClickEvent(new ClickEvent(
                        ClickEvent.Action.RUN_COMMAND,
                        "/antigenius command " + subCommandName + " false"
                    ))
                ) :
                new LiteralText(
                    "[" + Language.get("command.disabled") + "] "
                ).setStyle(
                    new Style().setColor(Formatting.RED).setHoverEvent(new HoverEvent(
                        HoverEvent.Action.SHOW_TEXT,
                        new LiteralText(Language.get("command.disabled.hover"))
                    )).setClickEvent(new ClickEvent(
                        ClickEvent.Action.RUN_COMMAND,
                        "/antigenius command " + subCommandName + " true"
                    ))
                )
            ).append(
                new LiteralText(subCommandName).setStyle(new Style().setColor(Formatting.AQUA))
            ).append(
                new LiteralText(
                    " [" + subCommand.getPermissionLevel() + "] "
                ).setStyle(
                    new Style().setHoverEvent(new HoverEvent(
                        HoverEvent.Action.SHOW_TEXT,
                        new LiteralText(Language.get("command.command.permission.level"))
                    )).setClickEvent(new ClickEvent(
                        ClickEvent.Action.SUGGEST_COMMAND,
                        "/antigenius command " + subCommandName + " " + subCommand.getPermissionLevel()
                    ))
                )
            ),
            toOps
        );
    }
}
