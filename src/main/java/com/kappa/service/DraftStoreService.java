package com.kappa.service;

import com.kappa.model.entity.DraftMessageBlock;
import java.util.Map;

public interface DraftStoreService {

    Map<String, Object> getMap(String mapName);

    void updateDraft(Map<String, Object> map, String conversationId, DraftMessageBlock draft) throws InterruptedException;

    void clearDraft(Map<String, Object> map, String conversationId) throws InterruptedException;
}
