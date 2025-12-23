<template>
  <view class="container">
    <!-- 状态卡片 -->
    <view class="status-card card">
      <view class="status-icon" :class="'status-' + detail.status">
        <text v-if="detail.status === '0'" class="icon">⏰</text>
        <text v-else-if="detail.status === '1'" class="icon">✓</text>
        <text v-else class="icon">✕</text>
      </view>
      <view class="status-text">
        <text v-if="detail.status === '0'">待就诊</text>
        <text v-else-if="detail.status === '1'">已就诊</text>
        <text v-else>已取消</text>
      </view>
    </view>

    <!-- 就诊信息 -->
    <view class="info-section card">
      <view class="section-title">就诊信息</view>
      <view class="info-list">
        <view class="info-item">
          <text class="label">医生</text>
          <text class="value">{{ detail.doctorName }}</text>
        </view>
        <view class="info-item">
          <text class="label">科室</text>
          <text class="value">{{ detail.sectionName }}</text>
        </view>
        <view class="info-item">
          <text class="label">就诊时间</text>
          <text class="value">
            {{ detail.registrationDate }} 
            <text v-if="detail.registrationTime === '1'">上午</text>
            <text v-else-if="detail.registrationTime === '2'">下午</text>
            <text v-else>晚上</text>
          </text>
        </view>
        <view class="info-item">
          <text class="label">挂号费</text>
          <text class="value fee">¥{{ detail.registrationFee }}</text>
        </view>
      </view>
    </view>

    <!-- 患者信息 -->
    <view class="info-section card">
      <view class="section-title">患者信息</view>
      <view class="info-list">
        <view class="info-item">
          <text class="label">姓名</text>
          <text class="value">{{ detail.patientName }}</text>
        </view>
        <view class="info-item">
          <text class="label">手机号</text>
          <text class="value">{{ detail.phone }}</text>
        </view>
        <view class="info-item" v-if="detail.idCard">
          <text class="label">身份证号</text>
          <text class="value">{{ detail.idCard }}</text>
        </view>
      </view>
    </view>

    <!-- 订单信息 -->
    <view class="info-section card">
      <view class="section-title">订单信息</view>
      <view class="info-list">
        <view class="info-item">
          <text class="label">预约单号</text>
          <text class="value">{{ detail.registrationId }}</text>
        </view>
        <view class="info-item">
          <text class="label">预约时间</text>
          <text class="value">{{ formatTime(detail.createTime) }}</text>
        </view>
        <view class="info-item" v-if="detail.remark">
          <text class="label">备注</text>
          <text class="value">{{ detail.remark }}</text>
        </view>
      </view>
    </view>

    <!-- 底部按钮 -->
    <view class="footer-actions" v-if="detail.status === '0'">
      <button class="btn-cancel" @click="cancelRegistration">取消挂号</button>
    </view>
  </view>
</template>

<script>
import { getRegistrationDetail, delRegistration } from '@/api/api.js'

export default {
  data() {
    return {
      registrationId: '',
      detail: {}
    }
  },
  onLoad(options) {
    if (options.id) {
      this.registrationId = options.id
      this.loadDetail()
    }
  },
  methods: {
    // 加载详情
    async loadDetail() {
      try {
        const res = await getRegistrationDetail(this.registrationId)
        this.detail = res.data || {}
      } catch (error) {
        console.error('加载详情失败', error)
        uni.showToast({
          title: '加载失败',
          icon: 'none'
        })
      }
    },
    
    // 格式化时间
    formatTime(time) {
      if (!time) return ''
      return time.replace('T', ' ')
    },
    
    // 取消挂号
    cancelRegistration() {
      uni.showModal({
        title: '提示',
        content: '确定要取消该挂号吗？',
        success: async (res) => {
          if (res.confirm) {
            try {
              await delRegistration(this.registrationId)
              uni.showToast({
                title: '取消成功',
                icon: 'success'
              })
              setTimeout(() => {
                uni.navigateBack()
              }, 1500)
            } catch (error) {
              console.error('取消挂号失败', error)
            }
          }
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.container {
  min-height: 100vh;
  padding: 20rpx;
  padding-bottom: 120rpx;
  background: #f5f5f5;
}

.status-card {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 60rpx 30rpx;
  margin-bottom: 20rpx;
  
  .status-icon {
    width: 120rpx;
    height: 120rpx;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    margin-bottom: 20rpx;
    
    .icon {
      font-size: 60rpx;
      color: #fff;
    }
    
    &.status-0 {
      background: linear-gradient(135deg, #ff9900 0%, #ff6600 100%);
    }
    
    &.status-1 {
      background: linear-gradient(135deg, #67c23a 0%, #44a01c 100%);
    }
    
    &.status-2 {
      background: linear-gradient(135deg, #909399 0%, #6c6f73 100%);
    }
  }
  
  .status-text {
    font-size: 36rpx;
    font-weight: bold;
    color: #333;
  }
}

.info-section {
  padding: 30rpx;
  margin-bottom: 20rpx;
  
  .section-title {
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
      margin-bottom: 24rpx;
      
      &:last-child {
        margin-bottom: 0;
      }
      
      .label {
        font-size: 28rpx;
        color: #999;
        min-width: 150rpx;
      }
      
      .value {
        font-size: 28rpx;
        color: #333;
        flex: 1;
        text-align: right;
        
        &.fee {
          color: #f56c6c;
          font-weight: bold;
          font-size: 32rpx;
        }
      }
    }
  }
}

.footer-actions {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 20rpx;
  background: #fff;
  box-shadow: 0 -2rpx 10rpx rgba(0, 0, 0, 0.05);
  
  .btn-cancel {
    width: 100%;
    padding: 28rpx 0;
    background: #fff;
    color: #f56c6c;
    border: 2rpx solid #f56c6c;
    border-radius: 40rpx;
    font-size: 32rpx;
  }
}
</style>


