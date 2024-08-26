package fr.traqueur.modernfactions.api.dto;

import fr.maxlego08.sarah.Column;

import java.util.UUID;

public record FactionDTO(@Column(value = "unique_id", primary = true) UUID unique_id,
                         String name,
                         String description,
                         UUID leader,
                         Integer nbLands,
                         @Column(value = "home", nullable = true)
                         String home) {}
