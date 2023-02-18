package com.bmp.search.core;

import com.bmp.connector.api.ConnectorManager;
import com.bmp.search.core.aggregator.Aggregators;
import com.bmp.search.core.packer.Packers;
import com.bmp.search.core.provider.Providers;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;

@Component
@RequiredArgsConstructor
public class Searcher {
    public static Duration TTL = Duration.ofHours(1);

    private final StringRedisTemplate redisTemplate;

    public SearchResponse search(SearchRequest request) {
        SortType sortType = request.getSortType();
        if (sortType == null) {
            sortType = SortType.GENERAL;
        }

        String key = marshalKey(request);

        SearchResponse response = unmarshalValue(redisTemplate.boundValueOps(key).get());
        if (response != null) {
            redisTemplate.boundValueOps(key).expire(TTL);
            if (response.getSortType() != sortType) {
                sortType.sort(response.getItems());
            }
            return response;
        }

        List<Item> items = Providers.Instance.recall(request);
        items = Packers.Instance.pack(items);
        sortType.sort(items);
        response = new SearchResponse(items, items.size(), Aggregators.Instance.aggregate(items), sortType);

        redisTemplate.boundValueOps(key).set(marshalValue(response), TTL);

        return response;
    }

    private final ObjectMapper mapper = new ObjectMapper();

    {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        ConnectorManager.registerSubtypes(mapper);
        // for PolymorphicType Subject
        mapper.activateDefaultTyping(mapper.getPolymorphicTypeValidator(), ObjectMapper.DefaultTyping.NON_CONCRETE_AND_ARRAYS);
    }

    private String marshalKey(SearchRequest request) {
        Key key = new Key();
        key.setSession(request.getSession());
        key.setQuery(request.getQuery());
        key.setFilter(request.getFilter());
        try {
            return mapper.writeValueAsString(key);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private String marshalValue(SearchResponse response) {
        try {
            return mapper.writeValueAsString(response);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private SearchResponse unmarshalValue(String value) {
        if (StringUtils.isBlank(value)) {
            return null;
        }
        try {
            return mapper.readValue(value, SearchResponse.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Data
    public static class Key {
        private String session;
        private String query;
        private SearchRequest.Filter filter;
    }
}
