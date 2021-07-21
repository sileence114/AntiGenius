package pub.silence.antigenius.command.subcommand;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.util.Formatting;
import pub.silence.antigenius.command.AntigeniusCommand;
import pub.silence.antigenius.utils.config.Language;

public class HelpCommand extends SubCommand {
    
    private static final HelpCommand INSTANCE = new HelpCommand();
    private HelpCommand(){
        super.shortName = "h";
    }
    public static HelpCommand getInstance() {
        return INSTANCE;
    }
    
    @Override
    public int executeCommand(CommandContext<ServerCommandSource> commandContext) {
        commandContext.getSource().sendFeedback(
            new LiteralText("AntiGenius //").setStyle(new Style().setColor(Formatting.AQUA)).append(
                    new LiteralText(
                        " --- " + Language.get("command.help.head") + " ---"
                    ).setStyle(new Style().setColor(Formatting.GOLD))
            ),
            false
        );
        SubCommand subCommand = null;
        for(String subCommandName : AntigeniusCommand.subCommandsMap.keySet()){
            subCommand = AntigeniusCommand.subCommandsMap.get(subCommandName);
            if(commandContext.getSource().hasPermissionLevel(subCommand.getPermissionLevel())){
                LiteralText feedBack = new LiteralText("");
                feedBack.append(new LiteralText("/antigenius ").setStyle(new Style().setColor(Formatting.GRAY)));
                feedBack.append(
                    null == subCommand.shortName ?
                    new LiteralText(subCommandName).setStyle(new Style().setColor(Formatting.AQUA)):
                    new LiteralText("(").setStyle(new Style().setColor(Formatting.GRAY)).append(
                        new LiteralText(subCommand.shortName).setStyle(new Style().setColor(Formatting.AQUA))
                    ).append("|").append(
                        new LiteralText(subCommandName).setStyle(new Style().setColor(Formatting.AQUA))
                    ).append(")")
                );
                feedBack.append(
                    new LiteralText(" - " + Language.get("command." + subCommandName + ".help"))
                );
                commandContext.getSource().sendFeedback(feedBack, false);
            }
        }
        return 0;
    }
}
