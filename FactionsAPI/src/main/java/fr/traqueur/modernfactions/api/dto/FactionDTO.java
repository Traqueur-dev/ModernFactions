package fr.traqueur.modernfactions.api.dto;

import fr.traqueur.modernfactions.api.storage.Primary;

import java.util.UUID;

public record FactionDTO(@Primary UUID unique_id, String name, String description, UUID leader) {}
