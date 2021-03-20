package com.kappa.service.impl;

import com.kappa.service.DraftStoreService;
import com.kappa.model.entity.DraftMessageBlock;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("heroku")
public class InMemoryDraftStoreServiceImpl implements DraftStoreService {

    private final Map<String, Object> map = new ConcurrentHashMap<>();

    @Override
    public Map<String, Object> getMap(String mapName) {
        return this.map;
    }

    @Override
    public void updateDraft(Map<String, Object> map, String conversationId, DraftMessageBlock draft) {
        map.replace(conversationId, draft);
    }

    @Override
    public void clearDraft(Map<String, Object> map, String conversationId) {
        map.remove(conversationId);
    }
}
