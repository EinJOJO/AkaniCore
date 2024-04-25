package it.einjojo.akani.core.paper;

import it.einjojo.akani.core.api.message.Language;
import it.einjojo.akani.core.api.message.MessageStorage;
import it.einjojo.akani.core.message.AbstractMessageManager;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class PaperMessageManager extends AbstractMessageManager<CommandSender> {

    public PaperMessageManager(Language language, MiniMessage miniMessage, MessageStorage storage) {
        super(language, miniMessage, storage);
    }


    @Override
    public void sendMessage(CommandSender commandSender, String key, @Nullable Function<String, String> modifier) {
        commandSender.sendMessage(message(key, modifier));
    }

}
