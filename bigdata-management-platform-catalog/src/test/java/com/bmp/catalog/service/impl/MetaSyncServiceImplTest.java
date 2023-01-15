package com.bmp.catalog.service.impl;

import com.bmp.catalog.service.DatasourceService;
import com.bmp.catalog.service.MetaSyncService;
import com.bmp.commons.Validate;
import com.bmp.commons.enums.DatasourceType;
import com.bmp.commons.enums.Status;
import com.bmp.commons.result.Result;
import com.bmp.connector.hive.HiveConnectorInfo;
import com.bmp.dao.entity.Datasource;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@AutoConfigureMockMvc
public class MetaSyncServiceImplTest {
    @Autowired
    private DatasourceService datasourceService;

    @Autowired
    @SuppressWarnings("unused")
    private MetaSyncService metaSyncService;

    @Test
    void startSend() {
        Datasource datasource = new Datasource();
        datasource.setName("test_datasource");
        datasource.setStatus(Status.ACTIVE);
        datasource.setType(DatasourceType.MYSQL);
        datasource.setConnectorInfo(new HiveConnectorInfo("127.0.0.1", "3306", "root", "root"));
        datasource.setCollectionID(1);
        datasource.setSyncExecuteTime(Instant.now());
        Result<Datasource> create = datasourceService.createDatasource(datasource);

        Validate.success(create);
        Integer datasourceID = create.getData().getId();

        Awaitility.with().pollInterval(1, TimeUnit.SECONDS)
                .await().atMost(10, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    Datasource rst = datasourceService.getById(datasourceID);
                    assertNotNull(rst);
                    assertNull(rst.getSyncExecuteTime());
                });
    }
}