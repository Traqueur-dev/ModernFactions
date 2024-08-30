package fr.traqueur.modernfactions.api.storage.cache;

import fr.traqueur.modernfactions.api.storage.Data;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface Cache<T extends Data> {

    void add(T object);

    void remove(UUID id);

    Optional<T> get(UUID id);

    List<T> values();

    void tag(T data);
}
