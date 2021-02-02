package com.kappa.service;

import com.kappa.model.DraftMessageBlock;
import java.util.Map;

public interface DraftStoreService {

    Map<Long, Object> getMap(String mapName);

    void updateDraft(Map<Long, Object> map, Long conversationId, DraftMessageBlock draft) throws InterruptedException;

    void clearDraft(Map<Long, Object> map, Long conversationId) throws InterruptedException;
}
