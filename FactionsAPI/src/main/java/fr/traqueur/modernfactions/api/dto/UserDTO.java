package fr.traqueur.modernfactions.api.dto;

import fr.traqueur.modernfactions.api.storage.Primary;

import java.util.UUID;

public record UserDTO(@Primary UUID unique_id, String name, UUID faction, String role) {}
