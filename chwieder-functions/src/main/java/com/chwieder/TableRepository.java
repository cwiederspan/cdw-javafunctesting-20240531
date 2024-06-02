package com.chwieder;

import com.azure.data.tables.TableClient;
import com.azure.data.tables.TableServiceClient;
import com.azure.data.tables.TableServiceClientBuilder;
import com.azure.data.tables.models.TableEntity;

public class TableRepository {
    private final TableClient _table;

    public TableRepository() {
        String connectionString = System.getenv("TableCS");
        TableServiceClient _service = new TableServiceClientBuilder().connectionString(connectionString).buildClient();
        _table = _service.getTableClient("data");
    }

    public void create(String partition, String row) {
        TableEntity entity = new TableEntity(partition, row);
        _table.createEntity(entity);
    }

    public TableEntity read(String partition, String row) {
        return _table.getEntity(partition, row);
    }

    public void delete(String partition, String row) {
        _table.deleteEntity(partition, row);
    }
}