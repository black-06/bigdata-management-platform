package com.bmp.catalog.controller;

import com.bmp.commons.result.Result;
import com.bmp.dao.entity.Collection;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.collections4.CollectionUtils;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class CollectionControllerTest {

    private static final Logger logger = LoggerFactory.getLogger(CollectionControllerTest.class);

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper mapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT, true)
            .findAndRegisterModules();

    @Test
    void createCollection() {
        Collection collection = new Collection();
        collection.setName("test collection");
        String json = assertDoesNotThrow(() -> mapper.writeValueAsString(collection));
        logger.info(json);

        byte[] resp = assertDoesNotThrow(() -> mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/collection")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsByteArray()
        );
        Result<Collection> result = assertDoesNotThrow(() -> mapper.readValue(resp, new TypeReference<Result<Collection>>() {
        }));
        assertTrue(result.isSuccess());

        queryCollectionList();

        updateCollection(result.getData().getId());
    }

    void queryCollectionList() {
        byte[] resp = assertDoesNotThrow(() -> mockMvc.perform(MockMvcRequestBuilders.get("/collection"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsByteArray()
        );
        Result<List<Collection>> result = assertDoesNotThrow(() -> mapper.readValue(resp, new TypeReference<Result<List<Collection>>>() {
        }));
        assertTrue(result.isSuccess());
        assertTrue(CollectionUtils.isNotEmpty(result.getData()));
    }

    void updateCollection(int id) {
        Collection collection = new Collection().setId(id).setName("").setDescription("hello");
        String json = assertDoesNotThrow(() -> mapper.writeValueAsString(collection));
        byte[] resp = assertDoesNotThrow(() -> mockMvc
                .perform(MockMvcRequestBuilders.
                        patch("/collection/{value}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsByteArray()
        );
        Result<Collection> result = assertDoesNotThrow(() -> mapper.readValue(resp, new TypeReference<Result<Collection>>() {
        }));
        assertTrue(result.isSuccess());
        assertEquals(id, result.getData().getId());
        assertEquals("hello", result.getData().getDescription());
    }
}