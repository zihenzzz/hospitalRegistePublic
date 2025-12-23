<template>
  <view class="container">
    <!-- 医生信息卡片 -->
    <view class="doctor-card card">
      <view class="doctor-header">
        <view class="doctor-info">
          <text class="doctor-name">{{ doctorName }}</text>
          <text class="section-name">{{ sectionName || '科室' }}</text>
        </view>
      </view>
      <view class="doctor-detail">
        <view class="detail-item">
          <text class="label">就诊日期：</text>
          <text class="value">{{ registrationDate }}</text>
        </view>
        <view class="detail-item">
          <text class="label">挂号费用：</text>
          <text class="value fee">¥{{ registrationFee }}</text>
        </view>
      </view>
    </view>

    <!-- 就诊人选择 -->
    <view class="visitor-section card">
      <view class="section-title">选择就诊人</view>
      <view class="visitor-list">
        <view 
          class="visitor-item"
          :class="{'active': selectedVisitor && selectedVisitor.id === item.id}"
          v-for="(item, index) in visitorsList" 
          :key="index"
          @click="selectVisitor(item)"
        >
          <view class="visitor-info">
            <text class="visitor-name">{{ item.name }}</text>
            <text class="visitor-phone">{{ item.phone }}</text>
          </view>
          <view class="visitor-check">
            <text v-if="selectedVisitor && selectedVisitor.id === item.id" class="check-icon">✓</text>
          </view>
        </view>
      </view>
      <button class="btn-add-visitor" @click="addVisitor">+ 添加就诊人</button>
    </view>

    <!-- 备注信息 -->
    <view class="remark-section card">
      <view class="section-title">备注信息（选填）</view>
      <textarea 
        v-model="remark" 
        class="remark-input" 
        placeholder="请输入备注信息"
        maxlength="200"
      />
    </view>

    <!-- 底部提交按钮 -->
    <view class="footer-actions">
      <view class="fee-info">
        <text class="fee-label">合计：</text>
        <text class="fee-amount">¥{{ registrationFee }}</text>
      </view>
      <button class="btn-submit" @click="submitRegistration" :disabled="!selectedVisitor">
        确认预约
      </button>
    </view>
  </view>
</template>

<script>
import config from '@/config/app.js'
import { createRegistration } from '@/api/api.js'

export default {
  data() {
    return {
      schedulingId: '',
      doctorName: '',
      sectionName: '',
      registrationDate: '',
      registrationFee: 0,
      selectedVisitor: null,
      visitorsList: [],
      remark: ''
    }
  },
  onLoad(options) {
    if (options.schedulingId) {
      this.schedulingId = options.schedulingId
      this.doctorName = options.doctorName || ''
      this.registrationDate = options.date || ''
      this.registrationFee = parseFloat(options.fee) || 0
    }
    this.loadVisitors()
  },
  methods: {
    // 加载就诊人列表
    loadVisitors() {
      const visitors = uni.getStorageSync(config.cacheKeys.visitors) || []
      this.visitorsList = visitors
      
      // 如果有默认就诊人，自动选择
      const defaultVisitor = visitors.find(v => v.isDefault)
      if (defaultVisitor) {
        this.selectedVisitor = defaultVisitor
      } else if (visitors.length > 0) {
        this.selectedVisitor = visitors[0]
      }
    },
    
    // 选择就诊人
    selectVisitor(visitor) {
      this.selectedVisitor = visitor
    },
    
    // 添加就诊人
    addVisitor() {
      uni.navigateTo({
        url: '/pages/myVisitors/myVisitors'
      })
    },
    
    // 提交预约
    async submitRegistration() {
      if (!this.selectedVisitor) {
        uni.showToast({
          title: '请选择就诊人',
          icon: 'none'
        })
        return
      }
      
      if (!this.schedulingId) {
        uni.showToast({
          title: '预约信息不完整',
          icon: 'none'
        })
        return
      }
      
      try {
        uni.showLoading({
          title: '提交中...'
        })
        
        const registrationData = {
          schedulingId: this.schedulingId,
          patientName: this.selectedVisitor.name,
          phone: this.selectedVisitor.phone,
          idCard: this.selectedVisitor.idCard,
          doctorName: this.doctorName,
          sectionName: this.sectionName,
          registrationDate: this.registrationDate,
          registrationTime: this.getTimeSlot(),
          registrationFee: this.registrationFee,
          remark: this.remark
        }
        
        const res = await createRegistration(registrationData)
        
        uni.hideLoading()
        
        if (res.code === 200) {
          uni.showToast({
            title: '预约成功',
            icon: 'success'
          })
          
          setTimeout(() => {
            uni.navigateBack({
              delta: 2
            })
          }, 1500)
        } else {
          uni.showToast({
            title: res.msg || '预约失败',
            icon: 'none'
          })
        }
      } catch (error) {
        uni.hideLoading()
        console.error('预约失败', error)
        uni.showToast({
          title: '预约失败，请重试',
          icon: 'none'
        })
      }
    },
    
    // 获取时段（根据时间判断）
    getTimeSlot() {
      // 这里可以根据日期和时间判断，暂时返回默认值
      return '1' // 1-上午, 2-下午, 3-晚上
    }
  }
}
</script>

<style lang="scss" scoped>
.container {
  min-height: 100vh;
  padding: 20rpx;
  padding-bottom: 160rpx;
  background: #f5f5f5;
}

.doctor-card {
  padding: 30rpx;
  margin-bottom: 20rpx;
  
  .doctor-header {
    margin-bottom: 20rpx;
    padding-bottom: 20rpx;
    border-bottom: 1rpx solid #f0f0f0;
    
    .doctor-info {
      .doctor-name {
        font-size: 36rpx;
        font-weight: bold;
        color: #333;
        display: block;
        margin-bottom: 8rpx;
      }
      
      .section-name {
        font-size: 28rpx;
        color: #999;
      }
    }
  }
  
  .doctor-detail {
    .detail-item {
      display: flex;
      justify-content: space-between;
      margin-bottom: 12rpx;
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

.visitor-section {
  padding: 30rpx;
  margin-bottom: 20rpx;
  
  .section-title {
    font-size: 32rpx;
    font-weight: bold;
    color: #333;
    margin-bottom: 20rpx;
  }
  
  .visitor-list {
    margin-bottom: 20rpx;
    
    .visitor-item {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 24rpx;
      margin-bottom: 15rpx;
      background: #f8f8f8;
      border-radius: 12rpx;
      border: 2rpx solid transparent;
      transition: all 0.3s;
      
      &.active {
        background: #e8f4ff;
        border-color: #409eff;
      }
      
      .visitor-info {
        .visitor-name {
          font-size: 30rpx;
          font-weight: bold;
          color: #333;
          display: block;
          margin-bottom: 8rpx;
        }
        
        .visitor-phone {
          font-size: 26rpx;
          color: #999;
        }
      }
      
      .visitor-check {
        width: 40rpx;
        height: 40rpx;
        border-radius: 50%;
        background: #409eff;
        display: flex;
        align-items: center;
        justify-content: center;
        
        .check-icon {
          color: #fff;
          font-size: 24rpx;
          font-weight: bold;
        }
      }
    }
  }
  
  .btn-add-visitor {
    width: 100%;
    padding: 24rpx 0;
    background: #fff;
    border: 2rpx dashed #409eff;
    border-radius: 12rpx;
    color: #409eff;
    font-size: 28rpx;
  }
}

.remark-section {
  padding: 30rpx;
  margin-bottom: 20rpx;
  
  .section-title {
    font-size: 32rpx;
    font-weight: bold;
    color: #333;
    margin-bottom: 20rpx;
  }
  
  .remark-input {
    width: 100%;
    min-height: 200rpx;
    padding: 20rpx;
    background: #f8f8f8;
    border-radius: 8rpx;
    font-size: 28rpx;
    color: #333;
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
  display: flex;
  align-items: center;
  justify-content: space-between;
  
  .fee-info {
    display: flex;
    align-items: baseline;
    
    .fee-label {
      font-size: 28rpx;
      color: #666;
      margin-right: 10rpx;
    }
    
    .fee-amount {
      font-size: 36rpx;
      font-weight: bold;
      color: #f56c6c;
    }
  }
  
  .btn-submit {
    flex: 1;
    margin-left: 20rpx;
    padding: 28rpx 0;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    color: #fff;
    border-radius: 40rpx;
    font-size: 32rpx;
    
    &[disabled] {
      background: #e0e0e0;
      color: #999;
    }
  }
}
</style>


