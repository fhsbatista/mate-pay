package com.matepay.wallet_core;

public class Exceptions extends Exception {
    public static class AccountAlreadyBelongsToOtherClient extends Exceptions {}
    public static class AmountMustBePresent extends Exceptions {}
    public static class AccountFromMustBePresent extends Exceptions {}
    public static class AccountToMustBePresent extends Exceptions {}
    public static class NotEnoughBalance extends Exceptions {}
    public static class ClientNotFound extends Exceptions {}
    public static class AccountNotFound extends Exceptions {}
    public static class TransactionNotFound extends Exceptions {}

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
