package com.pipilin.system.service;

import com.pipilin.system.domain.dto.AiChatRequest;
import com.pipilin.system.domain.dto.AiChatResponse;

/**
 * AI智能助手服务接口
 */
public interface IAiAssistantService {
    
    /**
     * 处理用户对话
     * @param request 对话请求
     * @param userId 当前用户ID
     * @return AI响应
     */
    AiChatResponse chat(AiChatRequest request, Long userId);
    
    /**
     * 执行确认操作（如确认挂号）
     * @param action 操作类型
     * @param data 操作数据
     * @param userId 当前用户ID
     * @return 执行结果
     */
    AiChatResponse executeAction(String action, Object data, Long userId);
}
