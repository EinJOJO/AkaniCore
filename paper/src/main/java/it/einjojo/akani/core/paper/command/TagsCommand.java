package it.einjojo.akani.core.paper.command;

import it.einjojo.akani.core.api.tags.TagManager;
import it.einjojo.akani.core.paper.tags.TagSelectGui;
import it.einjojo.akani.util.commands.BaseCommand;
import it.einjojo.akani.util.commands.MessageKeys;
import it.einjojo.akani.util.commands.PaperCommandManager;
import it.einjojo.akani.util.commands.annotation.Default;
import it.einjojo.akani.util.commands.annotation.Optional;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TagsCommand extends BaseCommand {
    private final TagManager tagManager;
    private final MiniMessage miniMessage;
    public TagsCommand(PaperCommandManager commandManager, TagManager tagManager, MiniMessage miniMessage) {
        this.tagManager = tagManager;
        this.miniMessage = miniMessage;
        commandManager.registerCommand(this);
    }

    @Default
    public void openTagsSelectGui(CommandSender sender, @Optional Player otherPlayer) {
        Player target;
        if (otherPlayer == null && sender instanceof Player) {
            target = (Player) sender;
        } else {
            if (!sender.hasPermission("akani.core.tags.open-other")) {
                getCurrentCommandIssuer().sendError(MessageKeys.PERMISSION_DENIED);
                return;
            }
            if (otherPlayer == null) {
                sendMessage(sender, "<red>Bitte gib einen Spieler an,, für den die Tagauswahl geöffnet werden soll.");
                return;
            }
            target = otherPlayer;
        }
        new TagSelectGui(target, tagManager).open();
    }

    public void sendMessage(CommandSender cs, String miniMessageString, TagResolver... resolver) {
        cs.sendMessage(miniMessage.deserialize(miniMessageString, resolver));
    }



}


