package com.linyi.pig.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linyi.pig.entity.ConversationSession;

import java.util.List;

public interface ConversationSessionService extends IService<ConversationSession> {
    List<ConversationSession> listMySessions();

    ConversationSession createSession(String title, String modelName);

    boolean closeSession(Long sessionId);
}
