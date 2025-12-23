<template>
  <view class="container">
    <!-- 支付结果 -->
    <view class="result-card card">
      <view class="result-icon" :class="'status-' + payStatus">
        <text v-if="payStatus === 'success'" class="icon">✓</text>
        <text v-else-if="payStatus === 'pending'" class="icon">⏰</text>
        <text v-else class="icon">✕</text>
      </view>
      <view class="result-text">
        <text v-if="payStatus === 'success'">支付成功</text>
        <text v-else-if="payStatus === 'pending'">支付处理中</text>
        <text v-else>支付失败</text>
      </view>
      <view class="result-desc" v-if="payStatus === 'success'">
        <text>您的订单已支付成功</text>
      </view>
    </view>

    <!-- 订单信息 -->
    <view class="order-info card">
      <view class="info-title">订单信息</view>
      <view class="info-list">
        <view class="info-item">
          <text class="label">订单号</text>
          <text class="value">{{ orderId || '--' }}</text>
        </view>
        <view class="info-item">
          <text class="label">支付金额</text>
          <text class="value fee">¥{{ payAmount || '0.00' }}</text>
        </view>
        <view class="info-item">
          <text class="label">支付方式</text>
          <text class="value">{{ payTypeText }}</text>
        </view>
        <view class="info-item">
          <text class="label">支付时间</text>
          <text class="value">{{ payTime || '--' }}</text>
        </view>
      </view>
    </view>

    <!-- 操作按钮 -->
    <view class="action-buttons">
      <button class="btn-primary" @click="goHome" v-if="payStatus === 'success'">
        返回首页
      </button>
      <button class="btn-secondary" @click="viewOrder" v-if="payStatus === 'success'">
        查看订单
      </button>
      <button class="btn-primary" @click="retryPay" v-if="payStatus === 'fail'">
        重新支付
      </button>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      payStatus: 'success', // success, fail, pending
      orderId: '',
      payAmount: 0,
      payType: '1', // 1-微信, 2-支付宝
      payTime: ''
    }
  },
  computed: {
    payTypeText() {
      const types = {
        '1': '微信支付',
        '2': '支付宝',
        '3': '现金'
      }
      return types[this.payType] || '其他'
    }
  },
  onLoad(options) {
    if (options.status) {
      this.payStatus = options.status
    }
    if (options.orderId) {
      this.orderId = options.orderId
    }
    if (options.amount) {
      this.payAmount = parseFloat(options.amount)
    }
    if (options.payType) {
      this.payType = options.payType
    }
    
    // 格式化支付时间
    const now = new Date()
    this.payTime = `${now.getFullYear()}-${String(now.getMonth() + 1).padStart(2, '0')}-${String(now.getDate()).padStart(2, '0')} ${String(now.getHours()).padStart(2, '0')}:${String(now.getMinutes()).padStart(2, '0')}`
  },
  methods: {
    // 返回首页
    goHome() {
      uni.switchTab({
        url: '/pages/index/index'
      })
    },
    
    // 查看订单
    viewOrder() {
      uni.navigateTo({
        url: '/pages/registrationRecord/registrationRecord'
      })
    },
    
    // 重新支付
    retryPay() {
      uni.navigateBack()
    }
  }
}
</script>

<style lang="scss" scoped>
.container {
  min-height: 100vh;
  padding: 20rpx;
  background: #f5f5f5;
}

.result-card {
  padding: 60rpx 30rpx;
  text-align: center;
  margin-bottom: 20rpx;
  
  .result-icon {
    width: 120rpx;
    height: 120rpx;
    border-radius: 50%;
    margin: 0 auto 30rpx;
    display: flex;
    align-items: center;
    justify-content: center;
    
    .icon {
      font-size: 60rpx;
      color: #fff;
    }
    
    &.status-success {
      background: linear-gradient(135deg, #67c23a 0%, #44a01c 100%);
    }
    
    &.status-pending {
      background: linear-gradient(135deg, #ff9900 0%, #ff6600 100%);
    }
    
    &.status-fail {
      background: linear-gradient(135deg, #f56c6c 0%, #d32f2f 100%);
    }
  }
  
  .result-text {
    font-size: 36rpx;
    font-weight: bold;
    color: #333;
    margin-bottom: 15rpx;
  }
  
  .result-desc {
    font-size: 28rpx;
    color: #999;
  }
}

.order-info {
  padding: 30rpx;
  margin-bottom: 40rpx;
  
  .info-title {
    font-size: 32rpx;
    font-weight: bold;
    color: #333;
    margin-bottom: 20rpx;
    padding-bottom: 20rpx;
    border-bottom: 1rpx solid #f0f0f0;
  }
  
  .info-list {
    .info-item {
      display: flex;
      justify-content: space-between;
      margin-bottom: 20rpx;
      font-size: 28rpx;
      
      .label {
        color: #999;
      }
      
      .value {
        color: #333;
        
        &.fee {
          color: #f56c6c;
          font-weight: bold;
          font-size: 32rpx;
        }
      }
    }
  }
}

.action-buttons {
  padding: 0 20rpx;
  
  button {
    width: 100%;
    padding: 28rpx 0;
    border-radius: 40rpx;
    font-size: 32rpx;
    margin-bottom: 20rpx;
    
    &.btn-primary {
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      color: #fff;
    }
    
    &.btn-secondary {
      background: #fff;
      color: #667eea;
      border: 2rpx solid #667eea;
    }
  }
}
</style>


