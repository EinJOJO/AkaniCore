package it.einjojo.akani.core.velocity;

import com.velocitypowered.api.proxy.Player;
import it.einjojo.akani.core.api.message.Language;
import it.einjojo.akani.core.api.message.MessageStorage;
import it.einjojo.akani.core.message.AbstractMessageManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class VelocityMessageManager extends AbstractMessageManager<Player> {
    private static final MiniMessage minimessage = MiniMessage.miniMessage();

    public VelocityMessageManager(Language language, MessageStorage storage) {
        super(language, storage);
    }

    @Override
    public Component message(@NotNull String key, @Nullable Function<String, String> modifier) {
        return minimessage.deserialize(plainMessage(key, modifier));
    }

    @Override
    public void sendMessage(Player player, String key, @Nullable Function<String, String> modifier) {
        player.sendMessage(message(key, modifier));
    }
}
