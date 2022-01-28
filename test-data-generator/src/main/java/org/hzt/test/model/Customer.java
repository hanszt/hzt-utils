package org.hzt.test.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Customer {

    private final String id;
    private final String name;
    private final List<BankAccount> bankAccounts;

    public Customer(String id, String name) {
        this(id, name, new ArrayList<>());
    }
    public Customer(String id, String name, List<BankAccount> bankAccounts) {
        this.id = id;
        this.name = name;
        this.bankAccounts = Collections.unmodifiableList(bankAccounts);
    }

    @Override
    public boolean equals(Object o) {
        return this == o || (o instanceof Customer other &&
                Objects.equals(id, other.id) &&
                Objects.equals(name, other.name) &&
                Objects.equals(bankAccounts, other.bankAccounts));
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, bankAccounts);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<BankAccount> getBankAccounts() {
        return bankAccounts;
    }
}
