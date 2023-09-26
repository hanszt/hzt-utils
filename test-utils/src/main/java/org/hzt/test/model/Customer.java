package org.hzt.test.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Customer extends Person {

    private final String customerId;
    private final List<BankAccount> bankAccounts;

    public Customer(final String id, final String name) {
        this(id, name, new ArrayList<>());
    }
    public Customer(final String id, final String name, final List<BankAccount> bankAccounts) {
        super(name);
        this.customerId = id;
        this.bankAccounts = Collections.unmodifiableList(bankAccounts);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        final var customer = (Customer) o;
        return Objects.equals(getCustomerId(), customer.getCustomerId()) &&
                Objects.equals(getBankAccounts(), customer.getBankAccounts());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getCustomerId(), getBankAccounts());
    }

    public String getCustomerId() {
        return customerId;
    }

    public List<BankAccount> getBankAccounts() {
        return bankAccounts;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "customerId='" + customerId + '\'' +
                ", bankAccounts=" + bankAccounts +
                '}';
    }
}
