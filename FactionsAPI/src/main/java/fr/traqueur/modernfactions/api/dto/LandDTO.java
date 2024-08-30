package fr.traqueur.modernfactions.api.dto;

import fr.maxlego08.sarah.Column;

import java.util.UUID;

public record LandDTO(@Column(value = "unique_id", primary = true) UUID unique_id,
                      String faction,
                      UUID world,
                      int x,
                      int z) {}
