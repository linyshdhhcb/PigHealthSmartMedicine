package com.linyi.pig.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linyi.pig.entity.ConversationSession;
import com.linyi.pig.mapper.ConversationSessionMapper;
import com.linyi.pig.service.ConversationSessionService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ConversationSessionServiceImpl extends ServiceImpl<ConversationSessionMapper, ConversationSession>
        implements ConversationSessionService {

    @Override
    public List<ConversationSession> listMySessions() {
        LambdaQueryWrapper<ConversationSession> qw = new LambdaQueryWrapper<>();
        qw.eq(ConversationSession::getUserId, Integer.valueOf(StpUtil.getLoginId().toString()))
                .orderByDesc(ConversationSession::getUpdateTime);
        return this.list(qw);
    }

    @Override
    public ConversationSession createSession(String title, String modelName) {
        ConversationSession s = ConversationSession.builder()
                .userId(Integer.valueOf(StpUtil.getLoginId().toString()))
                .title(title == null || title.isBlank() ? "新对话" : title)
                .status(1)
                .modelName(modelName)
                .createTime(new Date())
                .updateTime(new Date())
                .build();
        this.save(s);
        return s;
    }

    @Override
    public boolean closeSession(Long sessionId) {
        ConversationSession s = this.getById(sessionId);
        if (s == null)
            return false;
        s.setStatus(2);
        s.setUpdateTime(new Date());
        return this.updateById(s);
    }
}
