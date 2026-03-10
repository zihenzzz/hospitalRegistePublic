<template>
  <div class="ai-assistant">
    <!-- 悬浮按钮 -->
    <div class="ai-fab" @click="toggleChat" :class="{ 'is-open': isOpen }">
      <i class="el-icon-chat-dot-round" v-if="!isOpen"></i>
      <i class="el-icon-close" v-else></i>
    </div>

    <!-- 聊天窗口 -->
    <transition name="slide-up">
      <div class="ai-chat-window" v-show="isOpen">
        <!-- 头部 -->
        <div class="chat-header">
          <div class="header-info">
            <div class="avatar">
              <i class="el-icon-service"></i>
            </div>
            <div class="info">
              <span class="name">智能助手 小医</span>
              <span class="status">在线</span>
            </div>
          </div>
          <div class="header-actions">
            <el-tooltip content="清空对话" placement="top">
              <i class="el-icon-delete" @click="clearChat"></i>
            </el-tooltip>
            <i class="el-icon-minus" @click="toggleChat"></i>
          </div>
        </div>

        <!-- 消息区域 -->
        <div class="chat-messages" ref="messagesContainer">
          <div v-for="(msg, index) in messages" :key="index" 
               :class="['message', msg.type]">
            <div class="message-avatar">
              <i :class="msg.type === 'user' ? 'el-icon-user' : 'el-icon-service'"></i>
            </div>
            <div class="message-content">
              <div class="message-text" v-html="formatMessage(msg.content)"></div>
              
              <!-- 操作数据展示 -->
              <div v-if="msg.actionData" class="action-data">
                <!-- 科室列表 -->
                <div v-if="msg.intentType === 'QUERY_SECTION'" class="data-list">
                  <div v-for="item in msg.actionData" :key="item.sectionId" class="data-item">
                    <span class="item-name">{{ item.sectionName }}</span>
                    <span class="item-desc">{{ item.sectionDesc }}</span>
                  </div>
                </div>
                
                <!-- 排班列表 -->
                <div v-if="msg.intentType === 'QUERY_SCHEDULING'" class="data-list">
                  <div v-for="item in msg.actionData" :key="item.schedulingId" class="data-item scheduling">
                    <div class="scheduling-info">
                      <span class="doctor">{{ item.doctorName }}</span>
                      <span class="section">{{ item.sectionName }}</span>
                    </div>
                    <div class="scheduling-time">
                      <span>{{ formatDate(item.schedulingDate) }}</span>
                      <span>{{ item.timeSlot }}</span>
                    </div>
                    <div class="scheduling-status">
                      <span class="fee">¥{{ item.registrationFee }}</span>
                      <span class="remain">余{{ item.residualNumber }}号</span>
                    </div>
                  </div>
                </div>
                
                <!-- 挂号记录 -->
                <div v-if="msg.intentType === 'QUERY_REGISTRATION'" class="data-list">
                  <div v-for="item in msg.actionData" :key="item.registrationId" class="data-item registration">
                    <div class="reg-header">
                      <span class="reg-no">{{ item.registrationNo }}</span>
                      <el-tag :type="getStatusType(item.status)" size="mini">
                        {{ getStatusText(item.status) }}
                      </el-tag>
                    </div>
                    <div class="reg-info">
                      <span>{{ item.sectionName }} - {{ item.doctorName }}</span>
                      <span>{{ formatDate(item.registrationDate) }} {{ item.timeSlot }}</span>
                    </div>
                  </div>
                </div>
              </div>
              
              <!-- 确认操作按钮 -->
              <div v-if="msg.needConfirm && !msg.confirmed" class="confirm-actions">
                <el-button type="primary" size="mini" @click="confirmAction(msg, index)">
                  确认{{ msg.confirmAction === 'CREATE_REGISTRATION' ? '挂号' : '取消' }}
                </el-button>
                <el-button size="mini" @click="cancelConfirm(msg, index)">取消</el-button>
              </div>
              
              <div class="message-time">{{ formatTime(msg.time) }}</div>
            </div>
          </div>
          
          <!-- 加载中 -->
          <div v-if="loading" class="message ai">
            <div class="message-avatar">
              <i class="el-icon-service"></i>
            </div>
            <div class="message-content">
              <div class="typing-indicator">
                <span></span><span></span><span></span>
              </div>
            </div>
          </div>
        </div>

        <!-- 快捷操作 -->
        <div class="quick-actions">
          <el-tag v-for="action in quickActions" :key="action.text" 
                  @click="sendQuickMessage(action.text)" 
                  effect="plain" size="small">
            {{ action.label }}
          </el-tag>
        </div>

        <!-- 输入区域 -->
        <div class="chat-input">
          <el-input v-model="inputMessage" 
                    placeholder="输入您的问题，如：我头痛想挂号" 
                    @keyup.enter.native="sendMessage"
                    :disabled="loading">
            <el-button slot="append" icon="el-icon-s-promotion" 
                       @click="sendMessage" :loading="loading">
            </el-button>
          </el-input>
        </div>
      </div>
    </transition>
  </div>
</template>

<script>
import { chat, executeAction } from '@/api/ai/assistant'

export default {
  name: 'AiAssistant',
  data() {
    return {
      isOpen: false,
      inputMessage: '',
      loading: false,
      sessionId: null,
      messages: [],
      quickActions: [
        { label: '🏥 查看科室', text: '有哪些科室可以挂号？' },
        { label: '📋 我的挂号', text: '查看我的挂号记录' },
        { label: '🤒 症状咨询', text: '我最近头痛，应该挂什么科？' },
        { label: '❓ 就诊指南', text: '第一次来看病，流程是什么？' }
      ]
    }
  },
  mounted() {
    this.sessionId = this.generateSessionId()
    this.addWelcomeMessage()
  },
  methods: {
    toggleChat() {
      this.isOpen = !this.isOpen
      if (this.isOpen) {
        this.$nextTick(() => {
          this.scrollToBottom()
        })
      }
    },
    
    open() {
      this.isOpen = true
      this.$nextTick(() => {
        this.scrollToBottom()
      })
    },
    
    generateSessionId() {
      return 'session_' + Date.now() + '_' + Math.random().toString(36).substr(2, 9)
    },
    
    addWelcomeMessage() {
      this.messages.push({
        type: 'ai',
        content: '您好！我是智能助手小医 👋\n\n我可以帮您：\n• 根据症状推荐科室\n• 查询医生排班\n• 预约挂号\n• 查看挂号记录\n• 取消预约\n\n请问有什么可以帮您的？',
        time: new Date(),
        intentType: 'WELCOME'
      })
    },
    
    async sendMessage() {
      if (!this.inputMessage.trim() || this.loading) return
      
      const userMessage = this.inputMessage.trim()
      this.inputMessage = ''
      
      // 添加用户消息
      this.messages.push({
        type: 'user',
        content: userMessage,
        time: new Date()
      })
      
      this.scrollToBottom()
      this.loading = true
      
      try {
        const response = await chat({
          message: userMessage,
          sessionId: this.sessionId
        })
        
        if (response.code === 200) {
          const data = response.data
          this.messages.push({
            type: 'ai',
            content: data.content,
            time: new Date(),
            intentType: data.intentType,
            actionData: data.actionData,
            needConfirm: data.needConfirm,
            confirmAction: data.confirmAction,
            confirmData: data.actionData,
            confirmed: false
          })
        } else {
          this.messages.push({
            type: 'ai',
            content: '抱歉，我遇到了一些问题，请稍后再试。',
            time: new Date(),
            intentType: 'ERROR'
          })
        }
      } catch (error) {
        console.error('AI对话失败:', error)
        this.messages.push({
          type: 'ai',
          content: '网络连接出现问题，请检查网络后重试。',
          time: new Date(),
          intentType: 'ERROR'
        })
      } finally {
        this.loading = false
        this.scrollToBottom()
      }
    },
    
    sendQuickMessage(text) {
      this.inputMessage = text
      this.sendMessage()
    },
    
    async confirmAction(msg, index) {
      this.loading = true
      
      try {
        const response = await executeAction({
          action: msg.confirmAction,
          data: msg.confirmData
        })
        
        if (response.code === 200) {
          const data = response.data
          this.messages[index].confirmed = true
          this.messages.push({
            type: 'ai',
            content: data.content,
            time: new Date(),
            intentType: data.intentType,
            actionData: data.actionData
          })
        }
      } catch (error) {
        console.error('执行操作失败:', error)
        this.messages.push({
          type: 'ai',
          content: '操作执行失败，请稍后重试。',
          time: new Date(),
          intentType: 'ERROR'
        })
      } finally {
        this.loading = false
        this.scrollToBottom()
      }
    },
    
    cancelConfirm(msg, index) {
      this.messages[index].confirmed = true
      this.messages.push({
        type: 'ai',
        content: '好的，已取消操作。还有其他需要帮助的吗？',
        time: new Date(),
        intentType: 'CHAT'
      })
      this.scrollToBottom()
    },
    
    clearChat() {
      this.$confirm('确定要清空对话记录吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.messages = []
        this.sessionId = this.generateSessionId()
        this.addWelcomeMessage()
      }).catch(() => {})
    },
    
    scrollToBottom() {
      this.$nextTick(() => {
        const container = this.$refs.messagesContainer
        if (container) {
          container.scrollTop = container.scrollHeight
        }
      })
    },
    
    formatMessage(content) {
      if (!content) return ''
      return content
        .replace(/\n/g, '<br>')
        .replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>')
    },
    
    formatTime(date) {
      if (!date) return ''
      const d = new Date(date)
      return d.toLocaleTimeString('zh-CN', { hour: '2-digit', minute: '2-digit' })
    },
    
    formatDate(date) {
      if (!date) return ''
      const d = new Date(date)
      return d.toLocaleDateString('zh-CN', { month: '2-digit', day: '2-digit' })
    },
    
    getStatusType(status) {
      const types = { '0': 'warning', '1': 'success', '2': 'info', '3': 'danger' }
      return types[status] || 'info'
    },
    
    getStatusText(status) {
      const texts = { '0': '待就诊', '1': '已就诊', '2': '已取消', '3': '已过期' }
      return texts[status] || '未知'
    }
  }
}
</script>


<style lang="scss" scoped>
$primary: #10B981;
$primary-light: #34D399;
$primary-dark: #059669;

.ai-assistant {
  position: fixed;
  right: 24px;
  bottom: 24px;
  z-index: 9999;
}

// 悬浮按钮
.ai-fab {
  width: 56px;
  height: 56px;
  border-radius: 50%;
  background: linear-gradient(135deg, $primary 0%, $primary-light 100%);
  box-shadow: 0 4px 16px rgba($primary, 0.4);
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  
  i {
    font-size: 24px;
    color: #fff;
    transition: transform 0.3s;
  }
  
  &:hover {
    transform: scale(1.1);
    box-shadow: 0 6px 20px rgba($primary, 0.5);
  }
  
  &.is-open {
    background: #64748B;
    box-shadow: 0 4px 12px rgba(100, 116, 139, 0.3);
  }
}

// 聊天窗口
.ai-chat-window {
  position: absolute;
  right: 0;
  bottom: 72px;
  width: 380px;
  height: 560px;
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.12);
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

// 头部
.chat-header {
  padding: 16px 20px;
  background: linear-gradient(135deg, $primary 0%, $primary-light 100%);
  display: flex;
  align-items: center;
  justify-content: space-between;
  
  .header-info {
    display: flex;
    align-items: center;
    gap: 12px;
    
    .avatar {
      width: 40px;
      height: 40px;
      border-radius: 50%;
      background: rgba(255, 255, 255, 0.2);
      display: flex;
      align-items: center;
      justify-content: center;
      
      i {
        font-size: 20px;
        color: #fff;
      }
    }
    
    .info {
      display: flex;
      flex-direction: column;
      
      .name {
        font-size: 15px;
        font-weight: 600;
        color: #fff;
      }
      
      .status {
        font-size: 12px;
        color: rgba(255, 255, 255, 0.8);
        
        &::before {
          content: '';
          display: inline-block;
          width: 6px;
          height: 6px;
          border-radius: 50%;
          background: #4ADE80;
          margin-right: 4px;
        }
      }
    }
  }
  
  .header-actions {
    display: flex;
    gap: 12px;
    
    i {
      font-size: 18px;
      color: rgba(255, 255, 255, 0.8);
      cursor: pointer;
      transition: color 0.2s;
      
      &:hover {
        color: #fff;
      }
    }
  }
}

// 消息区域
.chat-messages {
  flex: 1;
  padding: 16px;
  overflow-y: auto;
  background: #F8FAFC;
  
  &::-webkit-scrollbar {
    width: 4px;
  }
  
  &::-webkit-scrollbar-thumb {
    background: #CBD5E1;
    border-radius: 2px;
  }
}

// 消息样式
.message {
  display: flex;
  gap: 10px;
  margin-bottom: 16px;
  
  &.user {
    flex-direction: row-reverse;
    
    .message-content {
      background: linear-gradient(135deg, $primary, $primary-light);
      color: #fff;
      border-radius: 16px 16px 4px 16px;
      
      .message-time {
        color: rgba(255, 255, 255, 0.7);
      }
    }
    
    .message-avatar {
      background: #E0F2FE;
      
      i {
        color: $primary;
      }
    }
  }
  
  &.ai {
    .message-content {
      background: #fff;
      border-radius: 16px 16px 16px 4px;
      box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
    }
    
    .message-avatar {
      background: linear-gradient(135deg, $primary 0%, $primary-light 100%);
      
      i {
        color: #fff;
      }
    }
  }
  
  .message-avatar {
    width: 36px;
    height: 36px;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    flex-shrink: 0;
    
    i {
      font-size: 18px;
    }
  }
  
  .message-content {
    max-width: 260px;
    padding: 12px 16px;
    
    .message-text {
      font-size: 14px;
      line-height: 1.6;
      word-break: break-word;
    }
    
    .message-time {
      font-size: 11px;
      color: #94A3B8;
      margin-top: 6px;
      text-align: right;
    }
  }
}

// 操作数据展示
.action-data {
  margin-top: 12px;
  
  .data-list {
    display: flex;
    flex-direction: column;
    gap: 8px;
  }
  
  .data-item {
    padding: 10px 12px;
    background: #F1F5F9;
    border-radius: 8px;
    font-size: 13px;
    
    .item-name {
      font-weight: 500;
      color: #1E293B;
    }
    
    .item-desc {
      color: #64748B;
      margin-left: 8px;
    }
    
    &.scheduling {
      display: flex;
      flex-direction: column;
      gap: 6px;
      
      .scheduling-info {
        display: flex;
        gap: 8px;
        
        .doctor {
          font-weight: 500;
          color: #1E293B;
        }
        
        .section {
          color: #64748B;
        }
      }
      
      .scheduling-time {
        display: flex;
        gap: 8px;
        color: #64748B;
      }
      
      .scheduling-status {
        display: flex;
        gap: 12px;
        
        .fee {
          color: #F59E0B;
          font-weight: 500;
        }
        
        .remain {
          color: $primary;
        }
      }
    }
    
    &.registration {
      .reg-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 6px;
        
        .reg-no {
          font-weight: 500;
          color: #1E293B;
          font-size: 12px;
        }
      }
      
      .reg-info {
        display: flex;
        flex-direction: column;
        gap: 2px;
        color: #64748B;
        font-size: 12px;
      }
    }
  }
}

// 确认操作按钮
.confirm-actions {
  margin-top: 12px;
  display: flex;
  gap: 8px;
}

// 打字指示器
.typing-indicator {
  display: flex;
  gap: 4px;
  padding: 4px 0;
  
  span {
    width: 8px;
    height: 8px;
    border-radius: 50%;
    background: #94A3B8;
    animation: typing 1.4s infinite ease-in-out;
    
    &:nth-child(1) { animation-delay: 0s; }
    &:nth-child(2) { animation-delay: 0.2s; }
    &:nth-child(3) { animation-delay: 0.4s; }
  }
}

@keyframes typing {
  0%, 60%, 100% {
    transform: translateY(0);
    opacity: 0.4;
  }
  30% {
    transform: translateY(-4px);
    opacity: 1;
  }
}

// 快捷操作
.quick-actions {
  padding: 8px 16px;
  background: #fff;
  border-top: 1px solid #F1F5F9;
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  
  .el-tag {
    cursor: pointer;
    transition: all 0.2s;
    
    &:hover {
      background: rgba($primary, 0.1);
      border-color: $primary;
      color: $primary;
    }
  }
}

// 输入区域
.chat-input {
  padding: 12px 16px;
  background: #fff;
  border-top: 1px solid #F1F5F9;
  
  ::v-deep .el-input-group__append {
    background: linear-gradient(135deg, $primary, $primary-light);
    border-color: $primary;
    
    .el-button {
      color: #fff;
      
      &:hover {
        background: $primary-dark;
      }
    }
  }
}

// 动画
.slide-up-enter-active,
.slide-up-leave-active {
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.slide-up-enter,
.slide-up-leave-to {
  opacity: 0;
  transform: translateY(20px);
}
</style>
