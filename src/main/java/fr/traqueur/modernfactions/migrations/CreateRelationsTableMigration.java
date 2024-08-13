package fr.traqueur.modernfactions.migrations;

import fr.maxlego08.sarah.database.Migration;

public class CreateRelationsTableMigration extends Migration {

    private final String tableName;

    public CreateRelationsTableMigration(String tableName) {
        this.tableName = tableName;
    }


    @Override
    public void up() {
        create("%prefix%" + tableName, table -> {
            table.uuid("unique_id").primary();
            table.string("faction_emitter", 255);
            table.string("faction_receiver", 255);
            table.string("relation", 16);
        });
    }
}
