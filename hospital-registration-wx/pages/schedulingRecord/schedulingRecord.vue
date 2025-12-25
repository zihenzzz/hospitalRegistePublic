<template>
  <view class="container">
    <!-- 顶部科室信息 -->
    <view class="header-section">
      <view class="header-bg"></view>
      <view class="header-content">
        <view class="section-info" v-if="sectionName">
          <text class="section-name">{{ sectionName }}</text>
          <text class="section-tip">请选择医生和就诊时间</text>
        </view>
        <view class="section-info" v-else>
          <text class="section-name">预约挂号</text>
          <text class="section-tip">选择科室开始预约</text>
        </view>
      </view>
    </view>

    <!-- 日期选择 -->
    <view class="date-section">
      <scroll-view scroll-x class="date-scroll" :show-scrollbar="false">
        <view class="date-list">
          <view 
            class="date-item"
            :class="{ 'active': selectedDate === item.date }"
            v-for="(item, index) in dateList" 
            :key="index"
            @click="selectDate(item.date)"
          >
            <text class="date-week">{{ item.week }}</text>
            <text class="date-day">{{ item.day }}</text>
            <view class="date-dot" v-if="hasScheduling(item.date)"></view>
          </view>
        </view>
      </scroll-view>
    </view>

    <!-- 排班列表 -->
    <view class="scheduling-section">
      <view class="time-group" v-for="(timeSlot, index) in timeSlots" :key="index">
        <view class="time-header">
          <view class="time-icon">
            <text v-if="timeSlot.value === '1'">🌅</text>
            <text v-else-if="timeSlot.value === '2'">☀️</text>
            <text v-else>🌙</text>
          </view>
          <view class="time-info">
            <text class="time-label">{{ timeSlot.label }}</text>
            <text class="time-range">{{ timeSlot.time }}</text>
          </view>
        </view>
        
        <view class="doctor-list">
          <view 
            class="doctor-card"
            v-for="(doctor, idx) in getSchedulingByTime(timeSlot.value)" 
            :key="idx"
            @click="selectDoctor(doctor)"
          >
            <view class="doctor-avatar">
              <text>👨‍⚕️</text>
            </view>
            <view class="doctor-info">
              <view class="doctor-top">
                <text class="doctor-name">{{ doctor.doctorName }}</text>
                <text class="doctor-title">{{ doctor.doctorTitle || '主治医师' }}</text>
              </view>
              <text class="doctor-specialty">擅长：{{ doctor.specialty || '常见病诊治' }}</text>
            </view>
            <view class="doctor-action">
              <view class="action-top">
                <text class="fee">¥{{ doctor.registrationFee }}</text>
              </view>
              <view 
                class="book-btn" 
                :class="{ 'disabled': doctor.availableNum <= doctor.registeredNum }"
              >
                <text v-if="doctor.availableNum > doctor.registeredNum">
                  余{{ doctor.availableNum - doctor.registeredNum }}号
                </text>
                <text v-else>已约满</text>
              </view>
            </view>
          </view>
          
          <view v-if="getSchedulingByTime(timeSlot.value).length === 0" class="empty-slot">
            <text>该时段暂无排班</text>
          </view>
        </view>
      </view>
    </view>

    <!-- 空状态 -->
    <view v-if="!loading && schedulingList.length === 0" class="empty-state">
      <view class="empty-icon">📅</view>
      <text class="empty-title">暂无排班信息</text>
      <text class="empty-desc">请选择其他日期或科室查看</text>
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
import { getSchedulingList } from '@/api/api.js'

export default {
  data() {
    return {
      sectionId: '',
      sectionName: '',
      loading: false,
      selectedDate: '',
      dateList: [],
      schedulingList: [],
      timeSlots: [
        { label: '上午', value: '1', time: '08:00-12:00' },
        { label: '下午', value: '2', time: '14:00-17:00' },
        { label: '晚上', value: '3', time: '18:00-21:00' }
      ]
    }
  },
  onLoad(options) {
    if (options.sectionId) {
      this.sectionId = options.sectionId
      this.sectionName = options.sectionName || ''
    }
    this.initDateList()
    this.loadScheduling()
  },
  methods: {
    initDateList() {
      const weekDays = ['日', '一', '二', '三', '四', '五', '六']
      const today = new Date()
      
      for (let i = 0; i < 7; i++) {
        const date = new Date(today)
        date.setDate(today.getDate() + i)
        
        const month = date.getMonth() + 1
        const day = date.getDate()
        const week = weekDays[date.getDay()]
        
        this.dateList.push({
          date: `${date.getFullYear()}-${month.toString().padStart(2, '0')}-${day.toString().padStart(2, '0')}`,
          day: `${month}/${day}`,
          week: i === 0 ? '今天' : (i === 1 ? '明天' : `周${week}`),
          fullDate: date
        })
      }
      
      this.selectedDate = this.dateList[0].date
    },
    
    selectDate(date) {
      this.selectedDate = date
      this.loadScheduling()
    },
    
    hasScheduling(date) {
      // 可以根据实际数据判断该日期是否有排班
      return true
    },
    
    async loadScheduling() {
      try {
        this.loading = true
        const res = await getSchedulingList({
          sectionId: this.sectionId,
          schedulingDate: this.selectedDate,
          status: '0'
        })
        this.schedulingList = res.rows || []
      } catch (error) {
        console.error('加载排班失败', error)
        uni.showToast({ title: '加载失败', icon: 'none' })
      } finally {
        this.loading = false
      }
    },
    
    getSchedulingByTime(timeValue) {
      return this.schedulingList.filter(item => item.schedulingTime === timeValue)
    },
    
    selectDoctor(doctor) {
      if (doctor.availableNum <= doctor.registeredNum) {
        uni.showToast({ title: '该时段已约满', icon: 'none' })
        return
      }
      
      uni.navigateTo({
        url: `/pages/subscribeInfo/subscribeInfo?schedulingId=${doctor.schedulingId}&doctorName=${doctor.doctorName}&date=${this.selectedDate}&fee=${doctor.registrationFee}`
      })
    }
  }
}
</script>

<style lang="scss" scoped>
$primary: #0891b2;
$primary-light: #06b6d4;
$dark: #0f172a;
$gray: #64748b;

.container {
  min-height: 100vh;
  background: #f1f5f9;
}

.header-section {
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
    padding: 60rpx 32rpx 32rpx;
    
    .section-info {
      .section-name {
        font-size: 40rpx;
        font-weight: bold;
        color: #fff;
        display: block;
        margin-bottom: 8rpx;
      }
      
      .section-tip {
        font-size: 26rpx;
        color: rgba(255, 255, 255, 0.8);
      }
    }
  }
}

.date-section {
  margin: -20rpx 32rpx 24rpx;
  background: #fff;
  border-radius: 24rpx;
  padding: 24rpx;
  box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.04);
  
  .date-scroll {
    white-space: nowrap;
  }
  
  .date-list {
    display: inline-flex;
    gap: 16rpx;
    
    .date-item {
      display: flex;
      flex-direction: column;
      align-items: center;
      padding: 20rpx 28rpx;
      border-radius: 16rpx;
      background: #f8fafc;
      position: relative;
      transition: all 0.3s;
      
      &.active {
        background: linear-gradient(135deg, $primary, $primary-light);
        
        .date-week, .date-day {
          color: #fff;
        }
      }
      
      &:active {
        transform: scale(0.98);
      }
      
      .date-week {
        font-size: 24rpx;
        color: $gray;
        margin-bottom: 8rpx;
      }
      
      .date-day {
        font-size: 30rpx;
        font-weight: bold;
        color: $dark;
      }
      
      .date-dot {
        position: absolute;
        bottom: 10rpx;
        width: 8rpx;
        height: 8rpx;
        background: #10b981;
        border-radius: 50%;
      }
    }
  }
}

.scheduling-section {
  padding: 0 32rpx;
  
  .time-group {
    margin-bottom: 32rpx;
    
    .time-header {
      display: flex;
      align-items: center;
      gap: 16rpx;
      margin-bottom: 20rpx;
      
      .time-icon {
        width: 56rpx;
        height: 56rpx;
        background: #fff;
        border-radius: 14rpx;
        display: flex;
        align-items: center;
        justify-content: center;
        box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.04);
        
        text {
          font-size: 28rpx;
        }
      }
      
      .time-info {
        .time-label {
          font-size: 32rpx;
          font-weight: bold;
          color: $dark;
          display: block;
        }
        
        .time-range {
          font-size: 24rpx;
          color: $gray;
        }
      }
    }
    
    .doctor-list {
      display: flex;
      flex-direction: column;
      gap: 16rpx;
      
      .doctor-card {
        display: flex;
        align-items: center;
        padding: 28rpx;
        background: #fff;
        border-radius: 20rpx;
        box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.04);
        
        &:active {
          background: #f8fafc;
        }
        
        .doctor-avatar {
          width: 88rpx;
          height: 88rpx;
          background: linear-gradient(135deg, #ecfeff, #f0fdfa);
          border-radius: 22rpx;
          display: flex;
          align-items: center;
          justify-content: center;
          margin-right: 20rpx;
          
          text {
            font-size: 44rpx;
          }
        }
        
        .doctor-info {
          flex: 1;
          
          .doctor-top {
            display: flex;
            align-items: center;
            gap: 12rpx;
            margin-bottom: 8rpx;
            
            .doctor-name {
              font-size: 32rpx;
              font-weight: bold;
              color: $dark;
            }
            
            .doctor-title {
              font-size: 24rpx;
              color: $primary;
              background: #ecfeff;
              padding: 4rpx 12rpx;
              border-radius: 8rpx;
            }
          }
          
          .doctor-specialty {
            font-size: 26rpx;
            color: $gray;
            display: block;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
          }
        }
        
        .doctor-action {
          display: flex;
          flex-direction: column;
          align-items: flex-end;
          gap: 12rpx;
          
          .fee {
            font-size: 36rpx;
            font-weight: bold;
            color: #f59e0b;
          }
          
          .book-btn {
            padding: 12rpx 24rpx;
            background: linear-gradient(135deg, $primary, $primary-light);
            border-radius: 32rpx;
            
            text {
              font-size: 26rpx;
              color: #fff;
              font-weight: 500;
            }
            
            &.disabled {
              background: #e2e8f0;
              
              text {
                color: $gray;
              }
            }
          }
        }
      }
      
      .empty-slot {
        padding: 48rpx;
        text-align: center;
        background: #fff;
        border-radius: 20rpx;
        
        text {
          font-size: 28rpx;
          color: $gray;
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
