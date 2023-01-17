package com.bmp.catalog.controller;

import com.bmp.commons.enums.DatasourceType;
import com.bmp.commons.result.Result;
import com.bmp.connector.api.ConnectorManager;
import com.bmp.connector.api.list.AssetPath;
import com.bmp.connector.hive.HiveConnectorInfo;
import com.bmp.dao.entity.Datasource;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
public class DatasourceControllerTest {
    private static final Logger logger = LoggerFactory.getLogger(DatasourceControllerTest.class);

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void createDatasource() {
        ObjectMapper mapper = new ObjectMapper();
        ConnectorManager.registerSubtypes(mapper);

        Datasource datasource = new Datasource();
        datasource.setName("test datasource");
        datasource.setType(DatasourceType.HIVE);
        datasource.setCollectionID(1);

        HiveConnectorInfo info = new HiveConnectorInfo("127.0.0.1", "3306", "root", "123");
        datasource.setConnectorInfo(info);

        List<AssetPath> paths = new ArrayList<>();
        paths.add(new AssetPath("default"));
        datasource.setSyncPaths(paths);

        datasource.setSyncExecuteTime(Instant.now());

        String json = assertDoesNotThrow(() -> mapper.writeValueAsString(datasource));
        logger.info(json);

        byte[] resp = assertDoesNotThrow(() -> mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/datasource")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsByteArray()
        );
        Result<?> result = assertDoesNotThrow(() -> mapper.readValue(resp, Result.class));
        assertTrue(result.isSuccess());
        assertNotNull(result.getData());

        // queryDatasource
        String queryResp = assertDoesNotThrow(() -> mockMvc.perform(MockMvcRequestBuilders
                        .get("/datasource/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString()
        );
        Result<?> queryRst = assertDoesNotThrow(() -> ConnectorManager.unmarshal(queryResp, Result.class));
        assertTrue(queryRst.isSuccess());
        assertNotNull(queryRst.getData());
        LinkedHashMap<?, ?> map = assertInstanceOf(LinkedHashMap.class, queryRst.getData());
        assertNotNull(map.get("connectorInfo"));
    }
}