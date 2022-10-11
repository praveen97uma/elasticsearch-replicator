package com.phonepe.plaftorm.es.replicator.commons;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class Registry<Key, Value> {

    private final Map<Key, Value> elements = new ConcurrentHashMap<>();

    public synchronized void add(final Key key,
                                 final Value value) {
        elements.putIfAbsent(key, value);
    }

    public boolean alreadyRegistered(Key key) {
        return elements.containsKey(key);
    }

    public synchronized void remove(final Key key) {
        elements.remove(key);
    }

    public synchronized Optional<Value> get(final Key key) {
        return Optional.ofNullable(elements.get(key));
    }

    public Map<Key, Value> asMap() {
        return new HashMap<>(elements);
    }

    public Collection<Value> getAll() {
        return elements.values();
    }
}

