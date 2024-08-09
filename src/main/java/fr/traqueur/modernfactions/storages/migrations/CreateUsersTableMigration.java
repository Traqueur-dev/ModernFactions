package fr.traqueur.modernfactions.storages.migrations;

import fr.maxlego08.sarah.database.Migration;

public class CreateUsersTableMigration extends Migration {

    private final String tableName;

    public CreateUsersTableMigration(String tableName) {
        this.tableName = tableName;
    }

    @Override
    public void up() {
        create("%prefix%" + tableName, table -> {
            table.uuid("unique_id").primary();
            table.uuid("faction");
        });
    }
}
