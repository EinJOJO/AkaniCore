package it.einjojo.akani.core.economy;

import it.einjojo.akani.core.api.messaging.BrokerService;

public class RubinEconomyManager extends CommonAbstractEconomyManager implements ThalerEconomyManager {
    public RubinEconomyManager(BrokerService brokerService, CommonEconomyStorage storage) {
        super(brokerService, storage);
    }

    @Override
    String invalidateMessageId() {
        return "delrubi";
    }

    @Override
    String updateMessageId() {
        return "uprubi";
    }
}
