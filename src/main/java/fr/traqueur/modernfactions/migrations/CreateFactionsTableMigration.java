package fr.traqueur.modernfactions.migrations;

import fr.maxlego08.sarah.database.Migration;
import fr.traqueur.modernfactions.api.dto.FactionDTO;

public class CreateFactionsTableMigration extends Migration {

    private final String tableName;

    public CreateFactionsTableMigration(String tableName) {
        this.tableName = tableName;
    }

    @Override
    public void up() {
        create("%prefix%" + tableName, FactionDTO.class);
    }
}
