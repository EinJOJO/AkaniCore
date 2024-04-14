package it.einjojo.akani.core.api.messaging;

import org.jetbrains.annotations.ApiStatus;

import java.util.concurrent.CompletableFuture;

/**
 * This interface represents a service that can connect to a broker and publish/subscribe to channels.
 */
public interface BrokerService {
    public final String DEFAULT_CHANNEL = "default";

    /**
     * @return the name of the broker
     */
    String brokerName();

    /**
     * Connect to the broker
     */
    void connect();

    /**
     * Disconnect from the broker
     */
    void disconnect();

    /**
     * @param message the message to publish
     */
    void publish(ChannelMessage message);

    /**
     * @param channel the channel to subscribe to
     */
    void subscribe(String channel);

    /**
     * @param channel the channel to unsubscribe from
     */
    void unsubscribe(String channel);


    default boolean registerMessageProcessor(MessageProcessor processor) {
        return registerMessageProcessor(processor.processingChannel(), processor);
    }

    /**
     * Register a processor to process messages received from the broker
     *
     * @param processor the processor to register
     */
    boolean registerMessageProcessor(String channel, MessageProcessor processor);


    /**
     * Unregister a processor
     *
     * @param processor the processor to unregister
     * @return true if the processor was unregistered, false otherwise
     */
    default boolean unregisterMessageProcessor(MessageProcessor processor) {
        return unregisterMessageProcessor(processor.processingChannel(), processor);
    }

    /**
     * Unregister a processor
     *
     * @param processor the processor to unregister
     * @return true if the processor was unregistered, false otherwise
     */
    boolean unregisterMessageProcessor(String channel, MessageProcessor processor);

    /**
     * @param message the message to forward to the processors
     */
    @ApiStatus.Internal
    void forwardToProcessors(ChannelMessage message);

    /**
     * Send a request to a service
     *
     * @param message the message to send
     * @return a CompletableFuture that will be completed when the response is received
     * @throws IllegalArgumentException if the message is not a request
     * @throws IllegalStateException    if the service is not connected
     */
    CompletableFuture<ChannelMessage> publishRequest(ChannelMessage message);

    /**
     * Send a request to a service with a timeout
     *
     * @param message the message to send
     * @param timeout the timeout in milliseconds
     * @return a CompletableFuture that will be completed when the response is received
     */
    CompletableFuture<ChannelMessage> publishRequest(ChannelMessage message, long timeout);

    /**
     * Get the future for a request with the given request ID
     *
     * @param requestID the request ID
     * @return the future for the request
     */
    CompletableFuture<ChannelMessage> getFutureOfRequest(String requestID);


}
