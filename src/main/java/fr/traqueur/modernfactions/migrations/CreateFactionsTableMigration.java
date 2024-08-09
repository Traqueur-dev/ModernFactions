package fr.traqueur.modernfactions.migrations;

import fr.maxlego08.sarah.database.Migration;

public class CreateFactionsTableMigration extends Migration {

    private final String tableName;

    public CreateFactionsTableMigration(String tableName) {
        this.tableName = tableName;
    }

    @Override
    public void up() {
        create("%prefix%" + tableName, table -> {
            table.uuid("unique_id").primary();
            table.string("name", 16);
            table.string("description", 128);
            table.uuid("leader");
        });
    }
}
