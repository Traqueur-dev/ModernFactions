package fr.traqueur.modernfactions.api.dto;

import java.util.UUID;

public record UserDTO(UUID unique_id, String name, UUID faction, String role) {}
