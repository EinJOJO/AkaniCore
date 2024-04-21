package it.einjojo.akani.core.message;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import it.einjojo.akani.core.api.message.Language;
import it.einjojo.akani.core.api.message.MessageManager;
import it.einjojo.akani.core.api.message.MessageStorage;
import it.einjojo.akani.core.api.player.AkaniPlayer;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.util.Map;
import java.util.function.Function;

public abstract class AbstractMessageManager<T> implements MessageManager<T> {
    private static final String PREFIX_PLACEHOLDER = "%prefix%";
    private final Language language;
    private final MessageStorage storage;
    private final LoadingCache<String, String> hotMessages;
    private String prefix;

    public AbstractMessageManager(Language language, MessageStorage storage) {
        this.language = language;
        this.storage = storage;
        this.hotMessages = Caffeine.newBuilder()
                .expireAfterAccess(Duration.ofMinutes(15))
                .build((k) -> this.storage.readMessage(this.language.langKey(), k));
    }

    @Override
    public void load() {
        for (Map.Entry<String, String> entry : storage().readMessages(language().langKey()).entrySet()) {
            hotMessages.put(entry.getKey(), entry.getValue());
        }
        prefix = hotMessages.get("prefix");
        if (prefix == null) {
            throw new IllegalStateException("No prefix found for language " + language().langKey());
        }
    }

    @Override
    public Language language() {
        return language;
    }

    public MessageStorage storage() {
        return storage;
    }

    @Override
    public String plainMessage(@NotNull String key) {
        String res = hotMessages.get(key);
        return res == null ? "" : res.replace(PREFIX_PLACEHOLDER, prefix);
    }

    @Override
    public String plainMessage(@NotNull String key, @Nullable Function<String, String> modifier) {
        if (modifier == null) return plainMessage(key);
        return modifier.apply(plainMessage(key));
    }

    @Override
    public void sendMessage(AkaniPlayer player, String key) {
        sendMessage(player, key, null);
    }

    @Override
    public void sendMessage(T t, String key) {
        sendMessage(t, key, null);
    }

    @Override
    public Component message(@NotNull String key) {
        return message(key, null);
    }

    @Override
    public void sendMessage(AkaniPlayer player, String key, @Nullable Function<String, String> modifier) {
        player.sendMessage(message(key, modifier));
    }
}
