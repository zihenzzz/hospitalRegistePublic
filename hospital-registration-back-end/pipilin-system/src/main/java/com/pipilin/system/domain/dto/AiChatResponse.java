package com.pipilin.system.domain.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * AI对话响应DTO
 */
public class AiChatResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /** AI回复内容 */
    private String content;
    
    /** 意图类型 */
    private String intentType;
    
    /** 操作数据（如挂号信息、科室列表等） */
    private Object actionData;
    
    /** 是否需要用户确认操作 */
    private boolean needConfirm;
    
    /** 确认操作的类型 */
    private String confirmAction;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getIntentType() {
        return intentType;
    }

    public void setIntentType(String intentType) {
        this.intentType = intentType;
    }

    public Object getActionData() {
        return actionData;
    }

    public void setActionData(Object actionData) {
        this.actionData = actionData;
    }

    public boolean isNeedConfirm() {
        return needConfirm;
    }

    public void setNeedConfirm(boolean needConfirm) {
        this.needConfirm = needConfirm;
    }

    public String getConfirmAction() {
        return confirmAction;
    }

    public void setConfirmAction(String confirmAction) {
        this.confirmAction = confirmAction;
    }
}
