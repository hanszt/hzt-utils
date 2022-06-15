package org.hzt.graph;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Collections;
import java.util.List;

final class ObservableWeightedNodeImpl<T> implements WeightedNode<T> {

    private final T payload;
    private final ObservableList<WeightedEdge<T>> weightedEdges = FXCollections.observableArrayList();
    private final ObjectProperty<WeightedNode<T>> predecessor = new SimpleObjectProperty<>();
    private final BooleanProperty open = new SimpleBooleanProperty();
    private final BooleanProperty visited = new SimpleBooleanProperty();
    private final BooleanProperty selected = new SimpleBooleanProperty();
    private final IntegerProperty cost = new SimpleIntegerProperty();
    private final IntegerProperty heuristic = new SimpleIntegerProperty();

    ObservableWeightedNodeImpl(T payload) {
        this(payload, 0);
    }

    ObservableWeightedNodeImpl(T payload, int cost) {
        this.payload = payload;
        this.cost.set(cost);
    }

    public T getPayload() {
        return payload;
    }

    @Override
    public List<WeightedEdge<T>> getWeightedEdges() {
        return Collections.emptyList();
    }

    @Override
    public List<WeightedEdge<T>> getEdges() {
        return weightedEdges;
    }

    public ObjectProperty<WeightedNode<T>> predecessorProperty() {
        return predecessor;
    }

    @Override
    public WeightedNode<T> getPredecessor() {
        return predecessor.get();
    }

    @Override
    public void setPredecessor(WeightedNode<T> node) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setPredecessor(Node<T> node) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isOpen() {
        return open.get();
    }

    @Override
    public void setOpen(boolean open) {
        this.open.set(open);
    }

    @Override
    public boolean isVisited() {
        return visited.get();
    }

    @Override
    public void setVisited(boolean visited) {
        this.visited.set(visited);
    }

    @Override
    public boolean isSelected() {
        return this.selected.get();
    }

    @Override
    public void setSelected(boolean selected) {
        this.selected.set(selected);
    }

    public boolean addEdge(WeightedEdge<T> weightedEdge) {
        return weightedEdges.add(weightedEdge);
    }

    @Override
    public int getHeuristic() {
        return this.heuristic.get();
    }

    @Override
    public void setHeuristic(int heuristic) {
        this.heuristic.set(heuristic);
    }

    @Override
    public int getCost() {
        return cost.get();
    }

    @Override
    public void setCost(int cost) {
        this.cost.set(cost);
    }
}
