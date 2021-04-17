package com.kappa.service;

import com.kappa.model.entity.MessageBlock;
import java.util.Map;

public interface DraftStoreService {

    Map<String, Object> getMap(String mapName);

    void updateDraft(Map<String, Object> map, String conversationId, MessageBlock draft);

    void clearDraft(Map<String, Object> map, String conversationId);
}
