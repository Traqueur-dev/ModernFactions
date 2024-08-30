package fr.traqueur.modernfactions.migrations;

import fr.maxlego08.sarah.database.Migration;
import fr.traqueur.modernfactions.api.dto.LandDTO;

public class CreateLandsTableMigration extends Migration {

    private final String tableName;

    public CreateLandsTableMigration(String tableName) {
        this.tableName = tableName;
    }

    @Override
    public void up() {
        createOrAlter("%prefix%" + tableName, LandDTO.class);
    }
}
