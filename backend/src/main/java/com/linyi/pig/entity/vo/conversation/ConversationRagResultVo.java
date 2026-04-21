package com.linyi.pig.entity.vo.conversation;

import com.linyi.pig.entity.vo.knowledge.KnowledgeChunkView;
import lombok.Data;

import java.util.List;

@Data
public class ConversationRagResultVo {
    private Long sessionId;
    private String prompt;
    private String aiResponse;
    private List<KnowledgeChunkView> chunks;
}
