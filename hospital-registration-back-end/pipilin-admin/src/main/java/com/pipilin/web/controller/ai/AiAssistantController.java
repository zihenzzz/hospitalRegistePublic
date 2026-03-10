package com.pipilin.web.controller.ai;

import com.pipilin.common.core.controller.BaseController;
import com.pipilin.common.core.domain.AjaxResult;
import com.pipilin.common.utils.SecurityUtils;
import com.pipilin.system.domain.dto.AiChatRequest;
import com.pipilin.system.domain.dto.AiChatResponse;
import com.pipilin.system.service.IAiAssistantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * AI智能助手控制器
 */
@Tag(name = "AI智能助手")
@RestController
@RequestMapping("/ai/assistant")
public class AiAssistantController extends BaseController {

    @Autowired
    private IAiAssistantService aiAssistantService;

    /**
     * AI对话接口
     */
    @Operation(summary = "AI对话")
    @PostMapping("/chat")
    public AjaxResult chat(@RequestBody AiChatRequest request) {
        Long userId = SecurityUtils.getUserId();
        AiChatResponse response = aiAssistantService.chat(request, userId);
        return AjaxResult.success(response);
    }

    /**
     * 执行确认操作
     */
    @Operation(summary = "执行确认操作")
    @PostMapping("/execute")
    public AjaxResult execute(@RequestBody Map<String, Object> params) {
        Long userId = SecurityUtils.getUserId();
        String action = (String) params.get("action");
        Object data = params.get("data");
        AiChatResponse response = aiAssistantService.executeAction(action, data, userId);
        return AjaxResult.success(response);
    }
}
