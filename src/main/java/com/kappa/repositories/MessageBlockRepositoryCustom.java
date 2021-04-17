package com.kappa.repositories;

import com.kappa.model.entity.MessageBlock;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface MessageBlockRepositoryCustom {

    List<MessageBlock> findLastMessageBlock(
        Set<String> conversationIds, boolean isDraft);

    Map<String, List<MessageBlock>> findLastMessageBlocks(
        Set<String> conversationIds, boolean isDraft);

    List<MessageBlock> findLastMessageBlocks(
        String conversationId, boolean isDraft);

}
