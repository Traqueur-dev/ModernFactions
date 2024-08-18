package fr.traqueur.modernfactions.migrations;

import fr.maxlego08.sarah.database.Migration;
import fr.traqueur.modernfactions.api.dto.RelationDTO;

public class CreateRelationsTableMigration extends Migration {

    private final String tableName;

    public CreateRelationsTableMigration(String tableName) {
        this.tableName = tableName;
    }


    @Override
    public void up() {
        createOrAlter("%prefix%" + tableName, RelationDTO.class);
    }
}
