package org.hzt.test.model;

import java.math.BigDecimal;
import java.util.Objects;

public class BankAccount {

    private final String accountNumber;
    private final Customer customer;
    private BigDecimal balance;

    public BankAccount(String accountNumber, Customer customer, BigDecimal balance) {
        this.accountNumber = accountNumber;
        this.customer = customer;
        this.balance = balance;
    }

    public BankAccount(String accountNumber, Customer customer) {
        this(accountNumber, customer, BigDecimal.ZERO);
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public Customer getCustomer() {
        return customer;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public BigDecimal updateBalance(BigDecimal balance) {
        this.balance = balance;
        return this.balance;
    }

    public boolean isDutchAccount() {
        return accountNumber.startsWith("NL");
    }

    @Override
    public boolean equals(Object o) {
        return this == o || (o instanceof BankAccount other &&
                Objects.equals(accountNumber, other.accountNumber) &&
                Objects.equals(customer, other.customer) &&
                Objects.equals(balance, other.balance));
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountNumber, customer, balance);
    }

    @Override
    public String toString() {
        return "BankAccount{" +
                "accountNumber='" + accountNumber + '\'' +
                ", customer=" + customer +
                ", balance=" + balance +
                '}';
    }
}
