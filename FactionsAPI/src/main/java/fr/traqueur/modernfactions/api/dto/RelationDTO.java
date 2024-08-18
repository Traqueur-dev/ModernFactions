package fr.traqueur.modernfactions.api.dto;

import fr.maxlego08.sarah.Column;

import java.util.UUID;

public record RelationDTO(@Column(value = "unique_id", primary = true) UUID unique_id, String faction_emitter, String faction_receiver, String relation) {}
