package it.einjojo.akani.core.api.economy;

/**
 * Exception thrown when a player does not have enough funds to perform an operation or the balance is negative.
 */
public class BadBalanceException extends Exception {
    private final Type type;

    public BadBalanceException(Type type) {
        super(type.name());
        this.type = type;

    }

    public static BadBalanceException notEnoughFunds() {
        return new BadBalanceException(Type.NOT_ENOUGH_FUNDS);
    }

    public static BadBalanceException negativeBalance() {
        return new BadBalanceException(Type.NEGATIVE_BALANCE);
    }

    public enum Type {
        NOT_ENOUGH_FUNDS,
        NEGATIVE_BALANCE
    }

}
