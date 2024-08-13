package fr.traqueur.modernfactions.api.dto;

import fr.traqueur.modernfactions.api.storage.Primary;

import java.util.UUID;

public record RelationDTO(@Primary UUID unique_id, String faction_emitter, String faction_receiver, String relation) {}
