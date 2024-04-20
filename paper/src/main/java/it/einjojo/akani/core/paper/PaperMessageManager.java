package it.einjojo.akani.core.paper;

import it.einjojo.akani.core.api.message.Language;
import it.einjojo.akani.core.api.message.MessageStorage;
import it.einjojo.akani.core.message.AbstractMessageManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;

public class PaperMessageManager extends AbstractMessageManager<CommandSender> {
    private static final MiniMessage minimessage = MiniMessage.miniMessage();

    public PaperMessageManager(Language language, MessageStorage storage) {
        super(language, storage);
    }

    @Override
    public Component message(String key) {
        return minimessage.deserialize(plainMessage(key));
    }

    @Override
    public void sendMessage(CommandSender player, String key) {
        player.sendMessage(message(key));
    }

    public MiniMessage miniMessage() {
        return minimessage;
    }
}
