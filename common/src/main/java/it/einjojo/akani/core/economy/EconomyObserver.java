package it.einjojo.akani.core.economy;

import it.einjojo.akani.core.api.economy.EconomyHolder;

public interface EconomyObserver {

    void onBalanceChange(EconomyHolder holder, long oldBalance, long newBalance);

}
