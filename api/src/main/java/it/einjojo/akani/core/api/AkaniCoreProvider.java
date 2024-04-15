package it.einjojo.akani.core.api;

import org.jetbrains.annotations.ApiStatus;

/**
 * Get the instance of AkaniCore.
 */
public class AkaniCoreProvider {

    private static AkaniCore instance;

    @ApiStatus.Internal
    public static void register(AkaniCore core) {
        instance = core;
    }

    @ApiStatus.Internal
    public static void unregister() {
        instance = null;
    }

    /**
     * @return the instance of AkaniCore
     */
    public static AkaniCore get() {
        if (instance == null) {
            throw new NotLoadedException();
        }
        return instance;
    }

    public AkaniCoreProvider() {
        throw new UnsupportedOperationException("This class cannot be instantiated");
    }

    public static class NotLoadedException extends RuntimeException {
        public static final String MESSAGE = "AkaniCore is not loaded!";

        public NotLoadedException() {
            super(MESSAGE);
        }
    }

}
