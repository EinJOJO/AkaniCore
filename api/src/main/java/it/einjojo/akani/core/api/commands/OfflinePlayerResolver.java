package it.einjojo.akani.core.api.commands;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.resolver.ArgumentResolver;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import it.einjojo.akani.core.api.AkaniCore;
import it.einjojo.akani.core.api.network.Server;
import it.einjojo.akani.core.api.player.AkaniOfflinePlayer;

public class OfflinePlayerResolver<SENDER> extends ArgumentResolver<SENDER, AkaniOfflinePlayer> {

    private final AkaniCore core;

    public OfflinePlayerResolver(AkaniCore core) {
        this.core = core;
    }

    @Override
    public SuggestionResult suggest(Invocation<SENDER> invocation, Argument<AkaniOfflinePlayer> argument, SuggestionContext context) {
        return core.networkManager().servers().stream().map(Server::name).collect(SuggestionResult.collector());
    }

    @Override
    protected ParseResult<AkaniOfflinePlayer> parse(Invocation<SENDER> invocation, Argument<AkaniOfflinePlayer> context, String argument) {
        if (argument == null || argument.isEmpty()) {
            return ParseResult.failure("Spieler nicht gefunden");
        }
        AkaniOfflinePlayer corePlayer = core.playerManager().loadPlayerByName(argument).join().orElse(null);
        if (corePlayer == null) {
            return ParseResult.failure("Spieler nicht gefunden");
        }
        return ParseResult.success(corePlayer);
    }
}
