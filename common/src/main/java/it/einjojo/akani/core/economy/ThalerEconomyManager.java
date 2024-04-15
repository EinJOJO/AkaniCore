package it.einjojo.akani.core.economy;

import it.einjojo.akani.core.api.messaging.BrokerService;
public class ThalerEconomyManager extends CommonAbstractEconomyManager {
    private static final String INVALIDATE_MESSAGE_ID = "delecothaler";
    public ThalerEconomyManager(BrokerService brokerService, EconomyStorage storage) {
        super(brokerService, storage);
    }
    @Override
    String invalidateMessageId() {
        return INVALIDATE_MESSAGE_ID;
    }

}
