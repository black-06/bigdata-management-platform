package com.bmp.search.core.provider;

import com.bmp.commons.enums.SubjectType;
import com.bmp.dao.dto.SubjectID;
import com.bmp.search.core.Item;
import com.bmp.search.core.SearchRequest;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class Providers implements Provider {
    public static final Providers Instance;

    static {
        List<Provider> providers = new ArrayList<>();
        for (SubjectType type : SubjectType.values()) {
            providers.add(new SubjectProvider(type));
        }
        providers.add(TagProvider.Instance);
        Instance = new Providers(providers);
    }

    private final List<Provider> providers;

    @Override
    public List<Item> recall(SearchRequest request) {
        Map<SubjectID, Item> items = new HashMap<>();
        for (Provider provider : providers) {
            for (Item item : provider.recall(request)) {
                items.put(SubjectID.of(item.getSubject()), item);
            }
        }
        return new ArrayList<>(items.values());
    }
}
