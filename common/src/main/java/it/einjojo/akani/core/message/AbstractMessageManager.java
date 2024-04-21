package it.einjojo.akani.core.message;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import it.einjojo.akani.core.api.message.Language;
import it.einjojo.akani.core.api.message.MessageManager;
import it.einjojo.akani.core.api.message.MessageStorage;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.Map;

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
        return hotMessages.get(key).replace(PREFIX_PLACEHOLDER, prefix);
    }



}
