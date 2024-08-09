package fr.traqueur.modernfactions.api.storage;

import java.util.UUID;

public interface Data<T> {

    UUID getId();

    T toDTO();

}
