<template>
  <view class="ai-container">
    <!-- 头部 -->
    <view class="ai-header">
      <view class="header-bg"></view>
      <view class="header-content">
        <view class="back-btn" @click="goBack">
          <u-icon name="arrow-left" color="#fff" size="40"></u-icon>
        </view>
        <view class="header-info">
          <view class="avatar">
            <text class="avatar-emoji">🤖</text>
          </view>
          <view class="info">
            <text class="name">AI智能助手</text>
            <view class="status">
              <view class="status-dot"></view>
              <text>在线</text>
            </view>
          </view>
        </view>
        <view class="header-action" @click="clearMessages">
          <u-icon name="trash" color="#fff" size="36"></u-icon>
        </view>
      </view>
    </view>

    <!-- 消息区域 -->
    <scroll-view 
      class="message-area" 
      scroll-y 
      :scroll-top="scrollTop"
      scroll-with-animation
      :scroll-into-view="scrollIntoView"
    >
      <view class="message-list">
        <view 
          v-for="(msg, index) in messages" 
          :key="index" 
          :class="['message-item', msg.type]"
          :id="'msg-' + index"
        >
          <view class="msg-avatar">
            <text v-if="msg.type === 'user'" class="avatar-emoji">👤</text>
            <text v-else class="avatar-emoji">🤖</text>
          </view>
          <view class="msg-bubble">
            <text class="msg-text">{{ msg.content }}</text>
            
            <!-- 排班数据展示 -->
            <view v-if="msg.actionData && msg.intentType === 'QUERY_SCHEDULING'" class="data-cards">
              <view v-for="item in msg.actionData" :key="item.schedulingId" class="schedule-card" @click="selectSchedule(item)">
                <view class="card-top">
                  <text class="doctor-name">{{ item.doctorName }}</text>
                  <text class="section-tag">{{ item.sectionName }}</text>
                </view>
                <view class="card-middle">
                  <view class="info-item">
                    <u-icon name="calendar" color="#64748b" size="28"></u-icon>
                    <text>{{ formatDate(item.schedulingDate) }}</text>
                  </view>
                  <view class="info-item">
                    <u-icon name="clock" color="#64748b" size="28"></u-icon>
                    <text>{{ item.timeSlot }}</text>
                  </view>
                </view>
                <view class="card-bottom">
                  <text class="fee">¥{{ item.registrationFee }}</text>
                  <view class="remain-tag" :class="{ 'full': item.residualNumber <= 0 }">
                    {{ item.residualNumber > 0 ? '余' + item.residualNumber + '号' : '已约满' }}
                  </view>
                </view>
              </view>
            </view>
            
            <!-- 挂号记录展示 -->
            <view v-if="msg.actionData && msg.intentType === 'QUERY_REGISTRATION'" class="data-cards">
              <view v-for="item in msg.actionData" :key="item.registrationId" class="reg-card">
                <view class="reg-top">
                  <text class="reg-no">{{ item.registrationNo }}</text>
                  <view :class="['status-badge', 'status-' + item.status]">
                    {{ getStatusText(item.status) }}
                  </view>
                </view>
                <view class="reg-info">
                  <text class="reg-doctor">{{ item.sectionName }} · {{ item.doctorName }}</text>
                  <text class="reg-time">{{ formatDate(item.registrationDate) }} {{ item.timeSlot }}</text>
                </view>
              </view>
            </view>
            
            <!-- 确认操作按钮 -->
            <view v-if="msg.needConfirm && !msg.confirmed" class="action-btns">
              <button class="btn-primary" @click="confirmAction(msg, index)">
                {{ msg.confirmAction === 'CREATE_REGISTRATION' ? '确认挂号' : '确认取消' }}
              </button>
              <button class="btn-secondary" @click="cancelConfirm(msg, index)">取消</button>
            </view>
            
            <text class="msg-time">{{ formatTime(msg.time) }}</text>
          </view>
        </view>
        
        <!-- 加载中 -->
        <view v-if="loading" class="message-item ai">
          <view class="msg-avatar">
            <text class="avatar-emoji">🤖</text>
          </view>
          <view class="msg-bubble">
            <view class="typing-indicator">
              <view class="dot"></view>
              <view class="dot"></view>
              <view class="dot"></view>
            </view>
          </view>
        </view>
      </view>
    </scroll-view>

    <!-- 快捷操作 -->
    <view class="quick-section">
      <scroll-view scroll-x class="quick-scroll" :show-scrollbar="false">
        <view class="quick-list">
          <view 
            v-for="(action, index) in quickActions" 
            :key="index" 
            class="quick-chip"
            @click="sendQuickMessage(action.text)"
          >
            <text class="chip-emoji">{{ action.emoji }}</text>
            <text class="chip-text">{{ action.label }}</text>
          </view>
        </view>
      </scroll-view>
    </view>

    <!-- 输入区域 -->
    <view class="input-section">
      <view class="input-wrapper">
        <input 
          class="msg-input" 
          v-model="inputMessage" 
          placeholder="输入您的问题..."
          placeholder-class="input-placeholder"
          confirm-type="send"
          @confirm="sendMessage"
          :disabled="loading"
        />
        <view class="send-btn" :class="{ 'active': inputMessage.trim() }" @click="sendMessage">
          <u-icon name="arrow-up" color="#fff" size="40"></u-icon>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
import { aiChat, aiExecuteAction } from '@/api/api.js'

export default {
  data() {
    return {
      inputMessage: '',
      loading: false,
      scrollTop: 0,
      scrollIntoView: '',
      sessionId: '',
      messages: [],
      quickActions: [
        { emoji: '🏥', label: '查看科室', text: '有哪些科室可以挂号？' },
        { emoji: '📋', label: '我的挂号', text: '查看我的挂号记录' },
        { emoji: '🤒', label: '症状咨询', text: '我最近头痛，应该挂什么科？' },
        { emoji: '❓', label: '就诊指南', text: '第一次来看病，流程是什么？' },
        { emoji: '👨‍⚕️', label: '找医生', text: '帮我推荐一位内科医生' }
      ]
    }
  },
  onLoad() {
    this.sessionId = 'wx_' + Date.now() + '_' + Math.random().toString(36).substr(2, 9)
    this.addWelcomeMessage()
  },
  methods: {
    goBack() {
      uni.navigateBack()
    },
    
    clearMessages() {
      uni.showModal({
        title: '提示',
        content: '确定要清空对话记录吗？',
        success: (res) => {
          if (res.confirm) {
            this.messages = []
            this.sessionId = 'wx_' + Date.now() + '_' + Math.random().toString(36).substr(2, 9)
            this.addWelcomeMessage()
          }
        }
      })
    },
    
    addWelcomeMessage() {
      this.messages.push({
        type: 'ai',
        content: '您好！我是AI智能助手 👋\n\n我可以帮您：\n• 根据症状推荐科室\n• 查询医生排班\n• 预约挂号\n• 查看挂号记录\n• 取消预约\n\n请问有什么可以帮您的？',
        time: new Date(),
        intentType: 'WELCOME'
      })
    },
    
    async sendMessage() {
      if (!this.inputMessage.trim() || this.loading) return
      
      const userMessage = this.inputMessage.trim()
      this.inputMessage = ''
      
      this.messages.push({
        type: 'user',
        content: userMessage,
        time: new Date()
      })
      
      this.scrollToBottom()
      this.loading = true
      
      try {
        const res = await aiChat({
          message: userMessage,
          sessionId: this.sessionId
        })
        
        if (res.code === 200) {
          const data = res.data
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
    
    selectSchedule(item) {
      if (item.residualNumber <= 0) {
        uni.showToast({ title: '该时段已约满', icon: 'none' })
        return
      }
      uni.navigateTo({
        url: `/pages/subscribeInfo/subscribeInfo?schedulingId=${item.schedulingId}&doctorName=${item.doctorName}&date=${item.schedulingDate}&fee=${item.registrationFee}`
      })
    },
    
    async confirmAction(msg, index) {
      this.loading = true
      
      try {
        const res = await aiExecuteAction({
          action: msg.confirmAction,
          data: msg.confirmData
        })
        
        if (res.code === 200) {
          const data = res.data
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
    
    scrollToBottom() {
      this.$nextTick(() => {
        this.scrollIntoView = 'msg-' + (this.messages.length - 1)
        this.scrollTop = 99999
      })
    },
    
    formatTime(date) {
      if (!date) return ''
      const d = new Date(date)
      return d.toLocaleTimeString('zh-CN', { hour: '2-digit', minute: '2-digit' })
    },
    
    formatDate(date) {
      if (!date) return ''
      const d = new Date(date)
      return `${d.getMonth() + 1}月${d.getDate()}日`
    },
    
    getStatusText(status) {
      const texts = { '0': '待就诊', '1': '已就诊', '2': '已取消', '3': '已过期' }
      return texts[status] || '未知'
    }
  }
}
</script>

<style lang="scss" scoped>
$primary: #10b981;
$primary-light: #34d399;
$primary-dark: #059669;
$dark: #0f172a;
$gray: #64748b;

.ai-container {
  display: flex;
  flex-direction: column;
  height: 100vh;
  background: #f1f5f9;
}

.ai-header {
  position: relative;
  
  .header-bg {
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    height: 200rpx;
    background: linear-gradient(135deg, $primary 0%, $primary-light 100%);
  }
  
  .header-content {
    position: relative;
    display: flex;
    align-items: center;
    padding: 60rpx 32rpx 32rpx;
    
    .back-btn {
      width: 72rpx;
      height: 72rpx;
      background: rgba(255, 255, 255, 0.2);
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
    }
    
    .header-info {
      flex: 1;
      display: flex;
      align-items: center;
      margin-left: 24rpx;
      
      .avatar {
        width: 88rpx;
        height: 88rpx;
        background: rgba(255, 255, 255, 0.2);
        border-radius: 22rpx;
        display: flex;
        align-items: center;
        justify-content: center;
        
        .avatar-emoji { font-size: 44rpx; }
      }
      
      .info {
        margin-left: 20rpx;
        
        .name {
          font-size: 34rpx;
          font-weight: bold;
          color: #fff;
          display: block;
        }
        
        .status {
          display: flex;
          align-items: center;
          margin-top: 8rpx;
          
          .status-dot {
            width: 14rpx;
            height: 14rpx;
            background: #4ade80;
            border-radius: 50%;
            margin-right: 10rpx;
          }
          
          text {
            font-size: 24rpx;
            color: rgba(255, 255, 255, 0.8);
          }
        }
      }
    }
    
    .header-action {
      width: 72rpx;
      height: 72rpx;
      background: rgba(255, 255, 255, 0.2);
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
    }
  }
}

.message-area {
  flex: 1;
  padding: 24rpx;
}

.message-list {
  display: flex;
  flex-direction: column;
  gap: 32rpx;
}

.message-item {
  display: flex;
  align-items: flex-start;
  
  &.user {
    flex-direction: row-reverse;
    
    .msg-avatar {
      background: linear-gradient(135deg, #ecfdf5, #d1fae5);
      margin-left: 16rpx;
      margin-right: 0;
    }
    
    .msg-bubble {
      background: linear-gradient(135deg, $primary 0%, $primary-light 100%);
      border-radius: 28rpx 28rpx 8rpx 28rpx;
      
      .msg-text { color: #fff; }
      .msg-time { color: rgba(255, 255, 255, 0.7); }
    }
  }
  
  &.ai {
    .msg-avatar {
      background: linear-gradient(135deg, $primary, $primary-light);
    }
    
    .msg-bubble {
      background: #fff;
      border-radius: 28rpx 28rpx 28rpx 8rpx;
      box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.04);
    }
  }
  
  .msg-avatar {
    width: 72rpx;
    height: 72rpx;
    border-radius: 20rpx;
    display: flex;
    align-items: center;
    justify-content: center;
    flex-shrink: 0;
    margin-right: 16rpx;
    
    .avatar-emoji { font-size: 36rpx; }
  }
  
  .msg-bubble {
    max-width: 540rpx;
    padding: 28rpx;
    
    .msg-text {
      font-size: 30rpx;
      line-height: 1.7;
      color: $dark;
      white-space: pre-wrap;
    }
    
    .msg-time {
      font-size: 22rpx;
      color: #94a3b8;
      margin-top: 16rpx;
      display: block;
      text-align: right;
    }
  }
}

.data-cards {
  margin-top: 24rpx;
  display: flex;
  flex-direction: column;
  gap: 16rpx;
}

.schedule-card {
  background: #f8fafc;
  border-radius: 16rpx;
  padding: 24rpx;
  border: 2rpx solid #e2e8f0;
  
  &:active { background: #f1f5f9; }
  
  .card-top {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 16rpx;
    
    .doctor-name {
      font-size: 30rpx;
      font-weight: bold;
      color: $dark;
    }
    
    .section-tag {
      font-size: 24rpx;
      color: $primary;
      background: #ecfdf5;
      padding: 6rpx 16rpx;
      border-radius: 8rpx;
    }
  }
  
  .card-middle {
    display: flex;
    gap: 32rpx;
    margin-bottom: 16rpx;
    
    .info-item {
      display: flex;
      align-items: center;
      gap: 8rpx;
      
      text {
        font-size: 26rpx;
        color: $gray;
      }
    }
  }
  
  .card-bottom {
    display: flex;
    justify-content: space-between;
    align-items: center;
    
    .fee {
      font-size: 32rpx;
      font-weight: bold;
      color: #f59e0b;
    }
    
    .remain-tag {
      font-size: 24rpx;
      color: $primary;
      background: #d1fae5;
      padding: 8rpx 16rpx;
      border-radius: 8rpx;
      
      &.full {
        color: #ef4444;
        background: #fee2e2;
      }
    }
  }
}

.reg-card {
  background: #f8fafc;
  border-radius: 16rpx;
  padding: 24rpx;
  border: 2rpx solid #e2e8f0;
  
  .reg-top {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 16rpx;
    
    .reg-no {
      font-size: 26rpx;
      color: $gray;
    }
    
    .status-badge {
      font-size: 24rpx;
      padding: 6rpx 16rpx;
      border-radius: 8rpx;
      
      &.status-0 { background: #fef3c7; color: #d97706; }
      &.status-1 { background: #d1fae5; color: #059669; }
      &.status-2 { background: #e5e7eb; color: #6b7280; }
      &.status-3 { background: #fee2e2; color: #dc2626; }
    }
  }
  
  .reg-info {
    display: flex;
    flex-direction: column;
    gap: 8rpx;
    
    .reg-doctor {
      font-size: 28rpx;
      font-weight: 500;
      color: $dark;
    }
    
    .reg-time {
      font-size: 26rpx;
      color: $gray;
    }
  }
}

.action-btns {
  display: flex;
  gap: 16rpx;
  margin-top: 24rpx;
  
  button {
    flex: 1;
    height: 72rpx;
    line-height: 72rpx;
    font-size: 28rpx;
    border-radius: 36rpx;
    border: none;
    
    &.btn-primary {
      background: linear-gradient(135deg, $primary, $primary-light);
      color: #fff;
    }
    
    &.btn-secondary {
      background: #f1f5f9;
      color: $gray;
    }
  }
}

.typing-indicator {
  display: flex;
  gap: 10rpx;
  padding: 12rpx 0;
  
  .dot {
    width: 18rpx;
    height: 18rpx;
    border-radius: 50%;
    background: #94a3b8;
    animation: typing 1.4s infinite ease-in-out;
    
    &:nth-child(1) { animation-delay: 0s; }
    &:nth-child(2) { animation-delay: 0.2s; }
    &:nth-child(3) { animation-delay: 0.4s; }
  }
}

@keyframes typing {
  0%, 60%, 100% { transform: translateY(0); opacity: 0.4; }
  30% { transform: translateY(-10rpx); opacity: 1; }
}

.quick-section {
  background: #fff;
  border-top: 1rpx solid #e2e8f0;
  padding: 20rpx 0;
  
  .quick-scroll { white-space: nowrap; }
  
  .quick-list {
    display: inline-flex;
    padding: 0 24rpx;
    gap: 16rpx;
  }
  
  .quick-chip {
    display: inline-flex;
    align-items: center;
    gap: 10rpx;
    padding: 16rpx 24rpx;
    background: #f1f5f9;
    border-radius: 36rpx;
    
    &:active { background: #e2e8f0; }
    
    .chip-emoji { font-size: 28rpx; }
    .chip-text { font-size: 26rpx; color: $gray; }
  }
}

.input-section {
  background: #fff;
  padding: 20rpx 24rpx;
  padding-bottom: calc(20rpx + env(safe-area-inset-bottom));
  border-top: 1rpx solid #e2e8f0;
  
  .input-wrapper {
    display: flex;
    align-items: center;
    gap: 16rpx;
    
    .msg-input {
      flex: 1;
      height: 88rpx;
      background: #f1f5f9;
      border-radius: 44rpx;
      padding: 0 32rpx;
      font-size: 30rpx;
    }
    
    .input-placeholder { color: #94a3b8; }
    
    .send-btn {
      width: 88rpx;
      height: 88rpx;
      border-radius: 50%;
      background: #e2e8f0;
      display: flex;
      align-items: center;
      justify-content: center;
      transition: all 0.3s;
      
      &.active {
        background: linear-gradient(135deg, $primary, $primary-light);
      }
      
      &:active { transform: scale(0.95); }
    }
  }
}
</style>
