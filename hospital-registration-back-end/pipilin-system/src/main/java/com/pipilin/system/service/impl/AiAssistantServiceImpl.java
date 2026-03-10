package com.pipilin.system.service.impl;

import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.pipilin.system.domain.*;
import com.pipilin.system.domain.dto.AiChatRequest;
import com.pipilin.system.domain.dto.AiChatResponse;
import com.pipilin.system.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * AI智能助手服务实现
 */
@Service
public class AiAssistantServiceImpl implements IAiAssistantService {
    
    private static final Logger log = LoggerFactory.getLogger(AiAssistantServiceImpl.class);
    
    @Value("${spring.ai.dashscope.api-key:${AI_DASHSCOPE_API_KEY:}}")
    private String apiKey;
    
    private ChatModel chatModel;
    
    @Autowired
    private IPSectionService sectionService;
    
    @Autowired
    private IPSchedulingService schedulingService;
    
    @Autowired
    private IPRegistrationService registrationService;
    
    @Autowired
    private ISysUserService userService;
    
    // 会话历史存储（生产环境建议使用Redis）
    private final Map<String, List<Message>> sessionHistory = new ConcurrentHashMap<>();
    
    // 系统提示词
    private static final String SYSTEM_PROMPT = """
        你是医院预约挂号系统的智能助手"小医"，负责帮助用户完成以下任务：
        
        ## 你的能力：
        1. **智能挂号**：根据用户描述的症状推荐合适的科室和医生，并帮助预约挂号
        2. **查询排班**：查询指定科室或医生的排班信息
        3. **查询挂号记录**：查询用户的历史挂号记录
        4. **取消挂号**：帮助用户取消预约
        5. **科室介绍**：介绍各科室的诊疗范围
        6. **就诊指南**：提供就诊流程、注意事项等信息
        
        ## 科室与症状对应关系：
        - 内科：发热、咳嗽、头痛、胸闷、心悸、高血压、糖尿病、胃痛、腹泻
        - 外科：外伤、骨折、肿块、疝气、阑尾炎
        - 儿科：小儿发热、咳嗽、腹泻、生长发育问题
        - 妇科：月经不调、妇科炎症、孕期检查
        - 眼科：视力下降、眼睛红肿、眼痛
        - 耳鼻喉科：耳鸣、听力下降、鼻塞、咽喉痛
        - 皮肤科：皮疹、瘙痒、痤疮、脱发
        - 口腔科：牙痛、牙龈出血、口腔溃疡
        - 骨科：腰痛、关节痛、颈椎病
        - 神经内科：头晕、失眠、手脚麻木
        
        ## 响应格式要求：
        你必须以JSON格式响应，包含以下字段：
        {
            "reply": "给用户的自然语言回复",
            "intent": "意图类型：QUERY_SECTION/QUERY_SCHEDULING/CREATE_REGISTRATION/QUERY_REGISTRATION/CANCEL_REGISTRATION/GUIDE/CHAT",
            "params": {
                "sectionName": "科室名称（如有）",
                "doctorName": "医生姓名（如有）",
                "date": "日期，格式yyyy-MM-dd（如有）",
                "timeSlot": "时间段：上午/下午/晚上（如有）",
                "registrationId": "挂号ID（如有）"
            },
            "needConfirm": false,
            "confirmData": null
        }
        
        ## 交互规则：
        1. 始终保持友好、专业的态度
        2. 如果用户描述症状，先推荐科室，再询问是否需要查看排班
        3. 挂号前必须确认：科室、医生、日期、时间段
        4. 对于敏感操作（挂号、取消）需要用户确认
        5. 如果信息不完整，主动询问缺失信息
        6. 今天的日期是：%s
        """;
    
    @PostConstruct
    public void init() {
        if (apiKey == null || apiKey.isEmpty()) {
            log.warn("AI_DASHSCOPE_API_KEY 未配置，AI助手功能将不可用");
            return;
        }
        
        try {
            DashScopeApi dashScopeApi = DashScopeApi.builder()
                .apiKey(apiKey)
                .build();
            
            this.chatModel = DashScopeChatModel.builder()
                .dashScopeApi(dashScopeApi)
                .defaultOptions(DashScopeChatOptions.builder()
                    .withModel("qwen-plus")
                    .withTemperature(0.7)
                    .withMaxToken(2000)
                    .build())
                .build();
            
            log.info("AI助手初始化成功");
        } catch (Exception e) {
            log.error("AI助手初始化失败", e);
        }
    }
    
    @Override
    public AiChatResponse chat(AiChatRequest request, Long userId) {
        AiChatResponse response = new AiChatResponse();
        
        if (chatModel == null) {
            response.setContent("AI助手暂不可用，请联系管理员配置API Key。");
            response.setIntentType("ERROR");
            return response;
        }
        
        try {
            String sessionId = request.getSessionId();
            if (sessionId == null || sessionId.isEmpty()) {
                sessionId = UUID.randomUUID().toString();
            }
            
            // 构建系统提示词（包含当前日期和系统数据）
            String systemPrompt = buildSystemPrompt(userId);
            
            // 获取或创建会话历史
            List<Message> history = sessionHistory.computeIfAbsent(sessionId, k -> new ArrayList<>());
            
            // 添加用户消息
            history.add(new UserMessage(request.getMessage()));
            
            // 限制历史长度
            if (history.size() > 20) {
                history = new ArrayList<>(history.subList(history.size() - 20, history.size()));
                sessionHistory.put(sessionId, history);
            }
            
            // 构建消息列表
            List<Message> messages = new ArrayList<>();
            messages.add(new SystemMessage(systemPrompt));
            messages.addAll(history);
            
            // 调用AI
            ChatResponse chatResponse = chatModel.call(new Prompt(messages));
            String aiResponse = chatResponse.getResult().getOutput().getText();
            
            log.info("AI原始响应: {}", aiResponse);
            
            // 解析AI响应
            response = parseAiResponse(aiResponse, userId);
            
            // 保存AI回复到历史
            history.add(new AssistantMessage(aiResponse));
            
        } catch (Exception e) {
            log.error("AI对话处理失败", e);
            response.setContent("抱歉，我遇到了一些问题，请稍后再试。如需紧急帮助，请联系人工客服。");
            response.setIntentType("ERROR");
        }
        
        return response;
    }
    
    @Override
    public AiChatResponse executeAction(String action, Object data, Long userId) {
        AiChatResponse response = new AiChatResponse();
        
        try {
            switch (action) {
                case "CREATE_REGISTRATION":
                    response = executeCreateRegistration(data, userId);
                    break;
                case "CANCEL_REGISTRATION":
                    response = executeCancelRegistration(data, userId);
                    break;
                default:
                    response.setContent("未知的操作类型");
                    response.setIntentType("ERROR");
            }
        } catch (Exception e) {
            log.error("执行操作失败: {}", action, e);
            response.setContent("操作执行失败，请稍后重试");
            response.setIntentType("ERROR");
        }
        
        return response;
    }
    
    /**
     * 构建系统提示词
     */
    private String buildSystemPrompt(Long userId) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String today = sdf.format(new Date());
        
        // 获取科室列表
        PSection sectionQuery = new PSection();
        sectionQuery.setStatus("0");
        List<PSection> sections = sectionService.selectPSectionList(sectionQuery);
        
        StringBuilder sectionInfo = new StringBuilder("\n\n## 当前可用科室：\n");
        for (PSection section : sections) {
            sectionInfo.append(String.format("- %s（%s）：%s\n", 
                section.getSectionName(), 
                section.getSectionCode(),
                section.getSectionDesc() != null ? section.getSectionDesc() : ""));
        }
        
        return String.format(SYSTEM_PROMPT, today) + sectionInfo.toString();
    }
    
    /**
     * 解析AI响应并执行相应操作
     */
    private AiChatResponse parseAiResponse(String aiResponse, Long userId) {
        AiChatResponse response = new AiChatResponse();
        
        try {
            // 尝试提取JSON
            String jsonStr = extractJson(aiResponse);
            if (jsonStr != null) {
                JSONObject json = JSON.parseObject(jsonStr);
                
                String reply = json.getString("reply");
                String intent = json.getString("intent");
                JSONObject params = json.getJSONObject("params");
                Boolean needConfirm = json.getBoolean("needConfirm");
                
                response.setContent(reply);
                response.setIntentType(intent);
                response.setNeedConfirm(needConfirm != null && needConfirm);
                
                // 根据意图执行查询操作
                if (params != null) {
                    switch (intent) {
                        case "QUERY_SECTION":
                            response.setActionData(querySections(params));
                            break;
                        case "QUERY_SCHEDULING":
                            response.setActionData(queryScheduling(params));
                            break;
                        case "QUERY_REGISTRATION":
                            response.setActionData(queryRegistrations(userId));
                            break;
                        case "CREATE_REGISTRATION":
                            if (response.isNeedConfirm()) {
                                response.setConfirmAction("CREATE_REGISTRATION");
                                response.setActionData(prepareRegistrationData(params, userId));
                            }
                            break;
                        case "CANCEL_REGISTRATION":
                            if (response.isNeedConfirm()) {
                                response.setConfirmAction("CANCEL_REGISTRATION");
                                response.setActionData(params);
                            }
                            break;
                    }
                }
            } else {
                // 如果不是JSON格式，直接返回文本
                response.setContent(aiResponse);
                response.setIntentType("CHAT");
            }
        } catch (Exception e) {
            log.warn("解析AI响应失败，返回原始文本", e);
            response.setContent(aiResponse);
            response.setIntentType("CHAT");
        }
        
        return response;
    }
    
    /**
     * 从文本中提取JSON
     */
    private String extractJson(String text) {
        int start = text.indexOf("{");
        int end = text.lastIndexOf("}");
        if (start != -1 && end != -1 && end > start) {
            return text.substring(start, end + 1);
        }
        return null;
    }
    
    /**
     * 查询科室列表
     */
    private List<PSection> querySections(JSONObject params) {
        PSection query = new PSection();
        query.setStatus("0");
        if (params.containsKey("sectionName")) {
            query.setSectionName(params.getString("sectionName"));
        }
        return sectionService.selectPSectionList(query);
    }
    
    /**
     * 查询排班信息
     */
    private List<PScheduling> queryScheduling(JSONObject params) {
        PScheduling query = new PScheduling();
        query.setStatus("0");
        
        // 根据科室名称查询科室ID
        if (params.containsKey("sectionName")) {
            PSection sectionQuery = new PSection();
            sectionQuery.setSectionName(params.getString("sectionName"));
            sectionQuery.setStatus("0");
            List<PSection> sections = sectionService.selectPSectionList(sectionQuery);
            if (!sections.isEmpty()) {
                query.setSectionId(sections.get(0).getSectionId());
            }
        }
        
        if (params.containsKey("doctorName")) {
            query.setDoctorName(params.getString("doctorName"));
        }
        
        return schedulingService.selectPSchedulingList(query);
    }
    
    /**
     * 查询用户挂号记录
     */
    private List<PRegistration> queryRegistrations(Long userId) {
        PRegistration query = new PRegistration();
        query.setPatientId(userId);
        return registrationService.selectPRegistrationList(query);
    }
    
    /**
     * 准备挂号数据
     */
    private Map<String, Object> prepareRegistrationData(JSONObject params, Long userId) {
        Map<String, Object> data = new HashMap<>();
        data.put("userId", userId);
        data.put("sectionName", params.getString("sectionName"));
        data.put("doctorName", params.getString("doctorName"));
        data.put("date", params.getString("date"));
        data.put("timeSlot", params.getString("timeSlot"));
        
        // 查询排班信息
        List<PScheduling> schedulings = queryScheduling(params);
        if (!schedulings.isEmpty()) {
            PScheduling scheduling = schedulings.get(0);
            data.put("schedulingId", scheduling.getSchedulingId());
            data.put("registrationFee", scheduling.getRegistrationFee());
            data.put("residualNumber", scheduling.getResidualNumber());
        }
        
        return data;
    }
    
    /**
     * 执行创建挂号
     */
    private AiChatResponse executeCreateRegistration(Object data, Long userId) {
        AiChatResponse response = new AiChatResponse();
        
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> regData = (Map<String, Object>) data;
            
            Long schedulingId = Long.valueOf(regData.get("schedulingId").toString());
            PScheduling scheduling = schedulingService.selectPSchedulingBySchedulingId(schedulingId);
            
            if (scheduling == null) {
                response.setContent("抱歉，该排班信息不存在，请重新选择。");
                response.setIntentType("ERROR");
                return response;
            }
            
            if (scheduling.getResidualNumber() <= 0) {
                response.setContent("抱歉，该时段号源已满，请选择其他时段。");
                response.setIntentType("ERROR");
                return response;
            }
            
            // 创建挂号记录
            PRegistration registration = new PRegistration();
            registration.setRegistrationNo(generateRegistrationNo());
            registration.setPatientId(userId);
            registration.setDoctorId(scheduling.getDoctorId());
            registration.setDoctorName(scheduling.getDoctorName());
            registration.setSectionId(scheduling.getSectionId());
            registration.setSectionName(scheduling.getSectionName());
            registration.setOutpatienId(scheduling.getOutpatienId());
            registration.setSchedulingId(schedulingId);
            registration.setRegistrationDate(scheduling.getSchedulingDate());
            registration.setTimeSlot(scheduling.getTimeSlot());
            registration.setRegistrationFee(scheduling.getRegistrationFee());
            registration.setStatus("0"); // 待就诊
            registration.setPayStatus("0"); // 未支付
            
            int result = registrationService.insertPRegistration(registration);
            
            if (result > 0) {
                // 更新排班剩余号源
                scheduling.setResidualNumber(scheduling.getResidualNumber() - 1);
                if (scheduling.getResidualNumber() <= 0) {
                    scheduling.setStatus("2"); // 已满
                }
                schedulingService.updatePScheduling(scheduling);
                
                response.setContent(String.format(
                    "🎉 挂号成功！\n\n" +
                    "📋 挂号单号：%s\n" +
                    "🏥 科室：%s\n" +
                    "👨‍⚕️ 医生：%s\n" +
                    "📅 就诊日期：%s %s\n" +
                    "💰 挂号费：%.2f元\n\n" +
                    "请按时就诊，祝您早日康复！",
                    registration.getRegistrationNo(),
                    registration.getSectionName(),
                    registration.getDoctorName(),
                    new SimpleDateFormat("yyyy-MM-dd").format(registration.getRegistrationDate()),
                    registration.getTimeSlot(),
                    registration.getRegistrationFee()
                ));
                response.setIntentType("SUCCESS");
                response.setActionData(registration);
            } else {
                response.setContent("挂号失败，请稍后重试。");
                response.setIntentType("ERROR");
            }
            
        } catch (Exception e) {
            log.error("创建挂号失败", e);
            response.setContent("挂号过程中出现错误，请稍后重试。");
            response.setIntentType("ERROR");
        }
        
        return response;
    }
    
    /**
     * 执行取消挂号
     */
    private AiChatResponse executeCancelRegistration(Object data, Long userId) {
        AiChatResponse response = new AiChatResponse();
        
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> cancelData = (Map<String, Object>) data;
            Long registrationId = Long.valueOf(cancelData.get("registrationId").toString());
            
            PRegistration registration = registrationService.selectPRegistrationByRegistrationId(registrationId);
            
            if (registration == null) {
                response.setContent("未找到该挂号记录。");
                response.setIntentType("ERROR");
                return response;
            }
            
            if (!registration.getPatientId().equals(userId)) {
                response.setContent("您没有权限取消此挂号。");
                response.setIntentType("ERROR");
                return response;
            }
            
            if ("2".equals(registration.getStatus())) {
                response.setContent("该挂号已经取消过了。");
                response.setIntentType("ERROR");
                return response;
            }
            
            // 更新挂号状态
            registration.setStatus("2"); // 已取消
            registration.setCancelTime(new Date());
            registration.setCancelReason("用户通过AI助手取消");
            
            int result = registrationService.updatePRegistration(registration);
            
            if (result > 0) {
                // 恢复排班号源
                PScheduling scheduling = schedulingService.selectPSchedulingBySchedulingId(registration.getSchedulingId());
                if (scheduling != null) {
                    scheduling.setResidualNumber(scheduling.getResidualNumber() + 1);
                    if ("2".equals(scheduling.getStatus())) {
                        scheduling.setStatus("0"); // 恢复正常
                    }
                    schedulingService.updatePScheduling(scheduling);
                }
                
                response.setContent(String.format(
                    "✅ 挂号已取消\n\n" +
                    "📋 挂号单号：%s\n" +
                    "🏥 科室：%s\n" +
                    "👨‍⚕️ 医生：%s\n\n" +
                    "如需重新预约，随时告诉我。",
                    registration.getRegistrationNo(),
                    registration.getSectionName(),
                    registration.getDoctorName()
                ));
                response.setIntentType("SUCCESS");
            } else {
                response.setContent("取消失败，请稍后重试。");
                response.setIntentType("ERROR");
            }
            
        } catch (Exception e) {
            log.error("取消挂号失败", e);
            response.setContent("取消过程中出现错误，请稍后重试。");
            response.setIntentType("ERROR");
        }
        
        return response;
    }
    
    /**
     * 生成挂号单号
     */
    private String generateRegistrationNo() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return "REG" + sdf.format(new Date()) + String.format("%04d", new Random().nextInt(10000));
    }
}
