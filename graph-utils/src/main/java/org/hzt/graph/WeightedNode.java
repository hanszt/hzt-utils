package org.hzt.graph;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WeightedNode<T> implements Node<T> {

    private final int weight;
    private final IntegerProperty cost = new SimpleIntegerProperty(Integer.MAX_VALUE);

    private final ObjectProperty<WeightedNode<T>> predecessor = new SimpleObjectProperty<>();

    private final List<WeightedNode<T>> neighbors = new ArrayList<>();
    private final T payload;

    public WeightedNode(T payload, int weight) {
        this.payload = payload;
        this.weight = weight;
    }

    public int getWeight() {
        return weight;
    }

    public int getCost() {
        return cost.get();
    }

    public IntegerProperty costProperty() {
        return cost;
    }

    public void updateCost(WeightedNode<T> current) {
        int newCost = current.getCost() + weight;
        if (newCost < cost.get()) {
            setCost(newCost);
        }
    }

    public void setCost(int cost) {
        this.cost.set(cost);
    }

    public List<WeightedNode<T>> getNeighbors() {
        return Collections.unmodifiableList(neighbors);
    }

    public boolean addNeighbor(WeightedNode<T> neighbor) {
        return neighbors.add(neighbor);
    }

    @Override
    public T getPayload() {
        return payload;
    }

    public WeightedNode<T> getPredecessor() {
        return predecessor.get();
    }

    public ObjectProperty<WeightedNode<T>> predecessorProperty() {
        return predecessor;
    }

}
