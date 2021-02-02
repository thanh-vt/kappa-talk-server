package com.kappa.service.impl;

import com.kappa.service.DraftStoreService;
import com.kappa.model.DraftMessageBlock;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("heroku")
public class InMemoryDraftStoreServiceImpl implements DraftStoreService {

    private final Map<Long, Object> map = new ConcurrentHashMap<>();

    @Override
    public Map<Long, Object> getMap(String mapName) {
        return this.map;
    }

    @Override
    public void updateDraft(Map<Long, Object> map, Long conversationId, DraftMessageBlock draft) {
        map.replace(conversationId, draft);
    }

    @Override
    public void clearDraft(Map<Long, Object> map, Long conversationId) {
        map.remove(conversationId);
    }
}
