package it.einjojo.akani.core.api.litecommands;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.resolver.ArgumentResolver;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import it.einjojo.akani.core.api.AkaniCore;
import it.einjojo.akani.core.api.player.AkaniOfflinePlayer;
import it.einjojo.akani.core.api.player.AkaniPlayer;

public class OnlinePlayerResolver<SENDER> extends ArgumentResolver<SENDER, AkaniPlayer> {
    private final AkaniCore core;

    public OnlinePlayerResolver(AkaniCore core) {
        this.core = core;
    }

    @Override
    protected ParseResult<AkaniPlayer> parse(Invocation<SENDER> invocation, Argument<AkaniPlayer> context, String s) {
        if (s == null || s.isEmpty()) {
            return ParseResult.failure("Spieler nicht gefunden");
        }
        AkaniPlayer corePlayer = core.playerManager().onlinePlayerByName(s).orElse(null);
        if (corePlayer == null) {
            return ParseResult.failure("Spieler nicht online.");
        }
        return ParseResult.success(corePlayer);

    }

    @Override
    public SuggestionResult suggest(Invocation<SENDER> invocation, Argument<AkaniPlayer> argument, SuggestionContext context) {
        return core.playerManager().onlinePlayers().stream().map(AkaniOfflinePlayer::name).collect(SuggestionResult.collector());
    }
}
