package com.chwieder;

import com.azure.data.tables.TableAsyncClient;
import com.azure.data.tables.TableServiceAsyncClient;
import com.azure.data.tables.TableServiceClientBuilder;
import com.azure.data.tables.models.TableEntity;
import reactor.core.publisher.Mono;

public class TableRepositoryAsync {
    private final TableAsyncClient _table;

    public TableRepositoryAsync() {
        String connectionString = System.getenv("TableCS");
        TableServiceAsyncClient _service = new TableServiceClientBuilder().connectionString(connectionString).buildAsyncClient();
        _table = _service.getTableClient("data");
    }

    public Mono<Void> create(String partition, String row) {
        TableEntity entity = new TableEntity(partition, row);
        return _table.createEntity(entity);
    }

    public Mono<TableEntity> read(String partition, String row) {
        return _table.getEntity(partition, row);
    }

    public Mono<Void> delete(String partition, String row) {
        return _table.deleteEntity(partition, row);
    }
}