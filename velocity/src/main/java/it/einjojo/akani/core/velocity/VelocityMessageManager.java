package it.einjojo.akani.core.velocity;

import com.velocitypowered.api.proxy.Player;
import it.einjojo.akani.core.api.message.Language;
import it.einjojo.akani.core.api.message.MessageStorage;
import it.einjojo.akani.core.message.AbstractMessageManager;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class VelocityMessageManager extends AbstractMessageManager<Player> {


    public VelocityMessageManager(Language language, MiniMessage miniMessage, MessageStorage storage) {
        super(language, miniMessage, storage);
    }

    @Override
    public void sendMessage(Player player, String key, @Nullable Function<String, String> modifier) {
        player.sendMessage(message(key, modifier));
    }
}
