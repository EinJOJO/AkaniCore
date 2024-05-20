package it.einjojo.akani.core.economy;

import it.einjojo.akani.core.api.messaging.BrokerService;

public class CoinsEconomyManager extends CommonAbstractEconomyManager {
    private static final String INVALIDATE_MESSAGE_ID = "deleco";
    private static final String UPDATE_MESSAGE_ID = "upeco";

    public CoinsEconomyManager(BrokerService brokerService, CommonEconomyStorage storage) {
        super(brokerService, storage);
    }

    @Override
    String invalidateMessageId() {
        return INVALIDATE_MESSAGE_ID;
    }


    @Override
    String updateMessageId() {
        return UPDATE_MESSAGE_ID;
    }
}
