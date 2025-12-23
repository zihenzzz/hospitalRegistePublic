<template>
  <view class="container">
    <!-- 账户余额卡片 -->
    <view class="balance-card">
      <view class="balance-info">
        <text class="balance-label">账户余额（元）</text>
        <text class="balance-amount">{{ totalBalance }}</text>
      </view>
      <button class="btn-recharge" @click="recharge">充值</button>
    </view>

    <!-- 充值记录 -->
    <view class="record-list">
      <view class="list-header">
        <text class="header-title">充值记录</text>
      </view>
      
      <view 
        class="record-item card"
        v-for="(item, index) in recordList" 
        :key="index"
      >
        <view class="record-left">
          <view class="record-icon" :class="'status-' + item.payStatus">
            <text v-if="item.payStatus === '1'">💰</text>
            <text v-else-if="item.payStatus === '0'">⏰</text>
            <text v-else>↩</text>
          </view>
          <view class="record-info">
            <view class="record-title">
              <text v-if="item.payStatus === '1'">充值成功</text>
              <text v-else-if="item.payStatus === '0'">待支付</text>
              <text v-else>已退款</text>
            </view>
            <view class="record-time">{{ formatTime(item.payTime) }}</view>
            <view class="record-type">
              <text v-if="item.payType === '1'">微信支付</text>
              <text v-else-if="item.payType === '2'">支付宝</text>
              <text v-else>其他</text>
            </view>
          </view>
        </view>
        
        <view class="record-right">
          <text class="amount" :class="'status-' + item.payStatus">
            {{ item.payStatus === '2' ? '-' : '+' }}{{ item.payAmount }}
          </text>
        </view>
      </view>
    </view>

    <!-- 空状态 -->
    <u-empty 
      v-if="!loading && recordList.length === 0" 
      text="暂无充值记录"
      mode="list"
    />

    <!-- 加载中 -->
    <u-loading-icon v-if="loading" mode="circle" />
  </view>
</template>

<script>
import { getPayList } from '@/api/api.js'

export default {
  data() {
    return {
      loading: false,
      totalBalance: 0,
      recordList: []
    }
  },
  onLoad() {
    this.loadData()
  },
  onShow() {
    // 每次显示页面时刷新数据
    this.loadData()
  },
  methods: {
    // 加载数据
    async loadData() {
      try {
        this.loading = true
        const res = await getPayList({
          pageNum: 1,
          pageSize: 100
        })
        this.recordList = res.rows || []
        
        // 计算总余额（简化处理，实际应从后端获取）
        if (this.recordList.length > 0) {
          this.totalBalance = this.recordList[0].balance || 0
        }
      } catch (error) {
        console.error('加载充值记录失败', error)
      } finally {
        this.loading = false
      }
    },
    
    // 充值
    recharge() {
      uni.showModal({
        title: '充值',
        content: '请输入充值金额',
        editable: true,
        placeholderText: '请输入金额',
        success: (res) => {
          if (res.confirm) {
            const amount = parseFloat(res.content)
            if (isNaN(amount) || amount <= 0) {
              uni.showToast({
                title: '请输入正确的金额',
                icon: 'none'
              })
              return
            }
            
            // 这里应该调用支付接口
            uni.showToast({
              title: '充值功能开发中',
              icon: 'none'
            })
          }
        }
      })
    },
    
    // 格式化时间
    formatTime(time) {
      if (!time) return ''
      const date = new Date(time)
      const year = date.getFullYear()
      const month = String(date.getMonth() + 1).padStart(2, '0')
      const day = String(date.getDate()).padStart(2, '0')
      const hour = String(date.getHours()).padStart(2, '0')
      const minute = String(date.getMinutes()).padStart(2, '0')
      return `${year}-${month}-${day} ${hour}:${minute}`
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

.balance-card {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 40rpx;
  border-radius: 20rpx;
  margin-bottom: 30rpx;
  display: flex;
  justify-content: space-between;
  align-items: center;
  box-shadow: 0 10rpx 30rpx rgba(102, 126, 234, 0.3);
  
  .balance-info {
    .balance-label {
      display: block;
      font-size: 26rpx;
      color: rgba(255, 255, 255, 0.8);
      margin-bottom: 10rpx;
    }
    
    .balance-amount {
      display: block;
      font-size: 56rpx;
      font-weight: bold;
      color: #fff;
    }
  }
  
  .btn-recharge {
    padding: 20rpx 40rpx;
    background: rgba(255, 255, 255, 0.2);
    border: 2rpx solid rgba(255, 255, 255, 0.6);
    border-radius: 40rpx;
    color: #fff;
    font-size: 28rpx;
  }
}

.record-list {
  .list-header {
    padding: 20rpx 0;
    
    .header-title {
      font-size: 32rpx;
      font-weight: bold;
      color: #333;
    }
  }
  
  .record-item {
    padding: 30rpx;
    margin-bottom: 20rpx;
    display: flex;
    justify-content: space-between;
    align-items: center;
    
    .record-left {
      display: flex;
      align-items: center;
      flex: 1;
      
      .record-icon {
        width: 80rpx;
        height: 80rpx;
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        margin-right: 20rpx;
        font-size: 40rpx;
        
        &.status-1 {
          background: #e8f5e8;
        }
        
        &.status-0 {
          background: #fff4e5;
        }
        
        &.status-2 {
          background: #f0f0f0;
        }
      }
      
      .record-info {
        flex: 1;
        
        .record-title {
          font-size: 30rpx;
          color: #333;
          font-weight: bold;
          margin-bottom: 8rpx;
        }
        
        .record-time {
          font-size: 24rpx;
          color: #999;
          margin-bottom: 6rpx;
        }
        
        .record-type {
          font-size: 24rpx;
          color: #666;
        }
      }
    }
    
    .record-right {
      .amount {
        font-size: 36rpx;
        font-weight: bold;
        
        &.status-1 {
          color: #67c23a;
        }
        
        &.status-0 {
          color: #ff9900;
        }
        
        &.status-2 {
          color: #f56c6c;
        }
      }
    }
  }
}
</style>

