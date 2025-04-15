package com.matepay.balances.domain;

public class Exceptions extends Exception {
    public static class AccountNotFound extends Exceptions {}

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
