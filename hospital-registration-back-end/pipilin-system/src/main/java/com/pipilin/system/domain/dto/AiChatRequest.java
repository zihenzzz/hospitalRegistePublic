package com.pipilin.system.domain.dto;

import java.io.Serializable;

/**
 * AI对话请求DTO
 */
public class AiChatRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 用户消息 */
    private String message;
    
    /** 会话ID（用于多轮对话） */
    private String sessionId;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
