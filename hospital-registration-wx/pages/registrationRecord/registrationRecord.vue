<template>
  <view class="container">
    <!-- 顶部标签 -->
    <view class="tabs-section">
      <view class="tabs-bg"></view>
      <view class="tabs-wrapper">
        <view 
          class="tab-item"
          :class="{ 'active': currentStatus === item.value }"
          v-for="(item, index) in statusTabs" 
          :key="index"
          @click="changeStatus(item.value)"
        >
          <text class="tab-text">{{ item.label }}</text>
          <view class="tab-indicator" v-if="currentStatus === item.value"></view>
        </view>
      </view>
    </view>

    <!-- 挂号列表 -->
    <view class="record-list">
      <view 
        class="record-card"
        v-for="(item, index) in registrationList" 
        :key="index"
        @click="toDetail(item)"
      >
        <view class="card-header">
          <view class="doctor-info">
            <view class="doctor-avatar">
              <text>👨‍⚕️</text>
            </view>
            <view class="doctor-detail">
              <text class="doctor-name">{{ item.doctorName }}</text>
              <text class="section-name">{{ item.sectionName }}</text>
            </view>
          </view>
          <view :class="['status-tag', 'status-' + item.status]">
            <text v-if="item.status === '0'">待就诊</text>
            <text v-else-if="item.status === '1'">已就诊</text>
            <text v-else>已取消</text>
          </view>
        </view>
        
        <view class="card-body">
          <view class="info-row">
            <view class="info-item">
              <u-icon name="account" color="#64748b" size="28"></u-icon>
              <text>{{ item.patientName }}</text>
            </view>
            <view class="info-item">
              <u-icon name="calendar" color="#64748b" size="28"></u-icon>
              <text>{{ item.registrationDate }}</text>
            </view>
          </view>
          <view class="info-row">
            <view class="info-item">
              <u-icon name="clock" color="#64748b" size="28"></u-icon>
              <text>
                <text v-if="item.registrationTime === '1'">上午</text>
                <text v-else-if="item.registrationTime === '2'">下午</text>
                <text v-else>晚上</text>
              </text>
            </view>
            <view class="info-item fee">
              <text>¥{{ item.registrationFee }}</text>
            </view>
          </view>
        </view>
        
        <view class="card-footer" v-if="item.status === '0'">
          <button class="btn-cancel" @click.stop="cancelRegistration(item)">取消挂号</button>
          <button class="btn-detail" @click.stop="toDetail(item)">查看详情</button>
        </view>
      </view>
    </view>

    <!-- 空状态 -->
    <view v-if="!loading && registrationList.length === 0" class="empty-state">
      <view class="empty-icon">📋</view>
      <text class="empty-title">暂无挂号记录</text>
      <text class="empty-desc">快去预约挂号吧</text>
      <button class="btn-book" @click="toBook">立即预约</button>
    </view>

    <!-- 加载中 -->
    <view v-if="loading" class="loading-state">
      <view class="loading-spinner"></view>
      <text>加载中...</text>
    </view>

    <!-- 底部安全间距 -->
    <view class="safe-bottom"></view>
  </view>
</template>

<script>
import { getRegistrationList, delRegistration } from '@/api/api.js'

export default {
  data() {
    return {
      loading: false,
      currentStatus: 'all',
      statusTabs: [
        { label: '全部', value: 'all' },
        { label: '待就诊', value: '0' },
        { label: '已就诊', value: '1' },
        { label: '已取消', value: '2' }
      ],
      registrationList: []
    }
  },
  onLoad() {
    this.loadData()
  },
  onShow() {
    this.loadData()
  },
  onPullDownRefresh() {
    this.loadData()
    setTimeout(() => {
      uni.stopPullDownRefresh()
    }, 1000)
  },
  methods: {
    changeStatus(status) {
      this.currentStatus = status
      this.loadData()
    },
    
    async loadData() {
      try {
        this.loading = true
        const params = {
          pageNum: 1,
          pageSize: 100
        }
        
        if (this.currentStatus !== 'all') {
          params.status = this.currentStatus
        }
        
        const res = await getRegistrationList(params)
        this.registrationList = res.rows || []
      } catch (error) {
        console.error('加载挂号记录失败', error)
      } finally {
        this.loading = false
      }
    },
    
    cancelRegistration(item) {
      uni.showModal({
        title: '取消挂号',
        content: '确定要取消该挂号吗？取消后不可恢复。',
        confirmColor: '#10b981',
        success: async (res) => {
          if (res.confirm) {
            try {
              await delRegistration(item.registrationId)
              uni.showToast({ title: '取消成功', icon: 'success' })
              this.loadData()
            } catch (error) {
              console.error('取消挂号失败', error)
            }
          }
        }
      })
    },
    
    toDetail(item) {
      uni.navigateTo({
        url: `/pages/registrationDetail/registrationDetail?id=${item.registrationId}`
      })
    },
    
    toBook() {
      uni.navigateTo({
        url: '/pages/schedulingRecord/schedulingRecord'
      })
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

.container {
  min-height: 100vh;
  background: #f1f5f9;
}

.tabs-section {
  position: relative;
  
  .tabs-bg {
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    height: 120rpx;
    background: linear-gradient(135deg, $primary 0%, $primary-light 100%);
  }
  
  .tabs-wrapper {
    position: relative;
    display: flex;
    background: #fff;
    margin: 40rpx 32rpx 0;
    border-radius: 20rpx;
    padding: 8rpx;
    box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.04);
    
    .tab-item {
      flex: 1;
      display: flex;
      flex-direction: column;
      align-items: center;
      padding: 24rpx 0;
      position: relative;
      
      .tab-text {
        font-size: 28rpx;
        color: $gray;
        font-weight: 500;
        transition: color 0.3s;
      }
      
      &.active {
        .tab-text {
          color: $primary;
          font-weight: 600;
        }
      }
      
      .tab-indicator {
        position: absolute;
        bottom: 8rpx;
        width: 40rpx;
        height: 6rpx;
        background: linear-gradient(90deg, $primary, $primary-light);
        border-radius: 3rpx;
      }
    }
  }
}

.record-list {
  padding: 24rpx 32rpx;
  
  .record-card {
    background: #fff;
    border-radius: 24rpx;
    padding: 28rpx;
    margin-bottom: 20rpx;
    box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.04);
    
    &:active {
      background: #f8fafc;
    }
    
    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: flex-start;
      padding-bottom: 24rpx;
      border-bottom: 1rpx solid #f1f5f9;
      
      .doctor-info {
        display: flex;
        align-items: center;
        gap: 16rpx;
        
        .doctor-avatar {
          width: 80rpx;
          height: 80rpx;
          background: linear-gradient(135deg, #ecfdf5, #d1fae5);
          border-radius: 20rpx;
          display: flex;
          align-items: center;
          justify-content: center;
          
          text { font-size: 40rpx; }
        }
        
        .doctor-detail {
          display: flex;
          flex-direction: column;
          gap: 6rpx;
          
          .doctor-name {
            font-size: 32rpx;
            font-weight: bold;
            color: $dark;
          }
          
          .section-name {
            font-size: 26rpx;
            color: $gray;
          }
        }
      }
      
      .status-tag {
        padding: 10rpx 20rpx;
        border-radius: 12rpx;
        font-size: 24rpx;
        font-weight: 500;
        
        &.status-0 {
          background: #fef3c7;
          color: #d97706;
        }
        
        &.status-1 {
          background: #d1fae5;
          color: #059669;
        }
        
        &.status-2 {
          background: #e5e7eb;
          color: #6b7280;
        }
      }
    }
    
    .card-body {
      padding: 24rpx 0;
      
      .info-row {
        display: flex;
        justify-content: space-between;
        margin-bottom: 16rpx;
        
        &:last-child { margin-bottom: 0; }
        
        .info-item {
          display: flex;
          align-items: center;
          gap: 10rpx;
          
          text {
            font-size: 28rpx;
            color: $gray;
          }
          
          &.fee text {
            font-size: 32rpx;
            font-weight: bold;
            color: #f59e0b;
          }
        }
      }
    }
    
    .card-footer {
      display: flex;
      gap: 20rpx;
      padding-top: 24rpx;
      border-top: 1rpx solid #f1f5f9;
      
      button {
        flex: 1;
        height: 80rpx;
        line-height: 80rpx;
        border-radius: 40rpx;
        font-size: 28rpx;
        font-weight: 500;
        
        &.btn-cancel {
          background: #fff;
          color: $gray;
          border: 2rpx solid #e2e8f0;
        }
        
        &.btn-detail {
          background: linear-gradient(135deg, $primary, $primary-light);
          color: #fff;
          border: none;
        }
      }
    }
  }
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 100rpx 0;
  
  .empty-icon {
    font-size: 100rpx;
    margin-bottom: 24rpx;
  }
  
  .empty-title {
    font-size: 32rpx;
    font-weight: 500;
    color: $dark;
    margin-bottom: 12rpx;
  }
  
  .empty-desc {
    font-size: 28rpx;
    color: $gray;
    margin-bottom: 40rpx;
  }
  
  .btn-book {
    padding: 0 60rpx;
    height: 88rpx;
    line-height: 88rpx;
    background: linear-gradient(135deg, $primary, $primary-light);
    color: #fff;
    border-radius: 44rpx;
    font-size: 30rpx;
    font-weight: 500;
    border: none;
  }
}

.loading-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 80rpx 0;
  
  .loading-spinner {
    width: 64rpx;
    height: 64rpx;
    border: 6rpx solid rgba($primary, 0.2);
    border-top-color: $primary;
    border-radius: 50%;
    animation: spin 1s linear infinite;
    margin-bottom: 20rpx;
  }
  
  text {
    font-size: 28rpx;
    color: $gray;
  }
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

.safe-bottom {
  height: calc(40rpx + env(safe-area-inset-bottom));
}
</style>
