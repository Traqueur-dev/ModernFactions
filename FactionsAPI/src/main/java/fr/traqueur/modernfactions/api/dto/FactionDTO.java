package fr.traqueur.modernfactions.api.dto;

import java.util.UUID;

public record FactionDTO(UUID unique_id, String name, String description, UUID leader) {}
