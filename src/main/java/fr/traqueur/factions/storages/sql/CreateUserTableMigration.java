package fr.traqueur.factions.storages.sql;

import fr.maxlego08.sarah.database.Migration;

public class CreateUserTableMigration extends Migration {

    private final String tableName;

    public CreateUserTableMigration(String tableName) {
        this.tableName = tableName;
    }

    @Override
    public void up() {
        create("%prefix%" + tableName, table -> {
            table.uuid("unique_id").primary();
        });
    }
}
