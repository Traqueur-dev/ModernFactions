package fr.traqueur.modernfactions.api.dto;

import fr.maxlego08.sarah.Column;
import java.util.UUID;

public record UserDTO(@Column(value = "unique_id", primary = true) UUID unique_id, String name, UUID faction, String role) {}
