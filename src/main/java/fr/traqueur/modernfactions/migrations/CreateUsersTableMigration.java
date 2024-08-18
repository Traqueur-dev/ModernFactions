package fr.traqueur.modernfactions.migrations;

import fr.maxlego08.sarah.database.Migration;
import fr.traqueur.modernfactions.api.dto.UserDTO;

public class CreateUsersTableMigration extends Migration {

    private final String tableName;

    public CreateUsersTableMigration(String tableName) {
        this.tableName = tableName;
    }

    @Override
    public void up() {
        createOrAlter("%prefix%" + tableName, UserDTO.class);
    }
}
