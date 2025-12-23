<template>
  <view class="container">
    <!-- 科室信息 -->
    <view class="section-info card" v-if="sectionName">
      <text class="section-title">{{ sectionName }}</text>
      <text class="section-desc">请选择医生和就诊时间</text>
    </view>

    <!-- 日期选择 -->
    <view class="date-selector card">
      <view class="date-list">
        <view 
          class="date-item"
          :class="{'active': selectedDate === item.date}"
          v-for="(item, index) in dateList" 
          :key="index"
          @click="selectDate(item.date)"
        >
          <text class="date-week">{{ item.week }}</text>
          <text class="date-day">{{ item.day }}</text>
        </view>
      </view>
    </view>

    <!-- 排班列表 -->
    <view class="scheduling-list">
      <view class="time-section" v-for="(timeSlot, index) in timeSlots" :key="index">
        <view class="time-header">
          <text class="time-title">{{ timeSlot.label }}</text>
          <text class="time-desc">{{ timeSlot.time }}</text>
        </view>
        
        <view class="doctor-list">
          <view 
            class="doctor-item card"
            v-for="(doctor, idx) in getSchedulingByTime(timeSlot.value)" 
            :key="idx"
            @click="selectDoctor(doctor)"
          >
            <view class="doctor-info">
              <view class="doctor-name">{{ doctor.doctorName }}</view>
              <view class="doctor-title">{{ doctor.doctorTitle || '主治医师' }}</view>
              <view class="doctor-desc">擅长：{{ doctor.specialty || '常见病诊治' }}</view>
            </view>
            <view class="doctor-action">
              <view class="available-num">
                <text v-if="doctor.availableNum > doctor.registeredNum" style="color: #67c23a;">
                  余{{ doctor.availableNum - doctor.registeredNum }}号
                </text>
                <text v-else style="color: #f56c6c;">已约满</text>
              </view>
              <view class="fee">¥{{ doctor.registrationFee }}</view>
              <button 
                class="btn-book" 
                :disabled="doctor.availableNum <= doctor.registeredNum"
                size="mini"
              >
                {{ doctor.availableNum <= doctor.registeredNum ? '约满' : '预约' }}
              </button>
            </view>
          </view>
          
          <view v-if="getSchedulingByTime(timeSlot.value).length === 0" class="empty-tip">
            <text>该时段暂无排班</text>
          </view>
        </view>
      </view>
    </view>

    <!-- 空状态 -->
    <view v-if="!loading && schedulingList.length === 0" class="empty-state">
      <view class="empty-icon">📅</view>
      <view class="empty-text">暂无排班信息</view>
    </view>

    <!-- 加载中 -->
    <view v-if="loading" class="loading-container">
      <view class="loading-spinner"></view>
    </view>
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
    // 初始化日期列表（未来7天）
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
          week: i === 0 ? '今天' : `周${week}`,
          fullDate: date
        })
      }
      
      this.selectedDate = this.dateList[0].date
    },
    
    // 选择日期
    selectDate(date) {
      this.selectedDate = date
      this.loadScheduling()
    },
    
    // 加载排班数据
    async loadScheduling() {
      try {
        this.loading = true
        const res = await getSchedulingList({
          sectionId: this.sectionId,
          schedulingDate: this.selectedDate,
          status: '0' // 只显示启用的排班
        })
        this.schedulingList = res.rows || []
      } catch (error) {
        console.error('加载排班失败', error)
        uni.showToast({
          title: '加载失败',
          icon: 'none'
        })
      } finally {
        this.loading = false
      }
    },
    
    // 根据时段筛选排班
    getSchedulingByTime(timeValue) {
      return this.schedulingList.filter(item => item.schedulingTime === timeValue)
    },
    
    // 选择医生预约
    selectDoctor(doctor) {
      if (doctor.availableNum <= doctor.registeredNum) {
        uni.showToast({
          title: '该时段已约满',
          icon: 'none'
        })
        return
      }
      
      // 跳转到预约信息填写页面
      uni.navigateTo({
        url: `/pages/subscribeInfo/subscribeInfo?schedulingId=${doctor.schedulingId}&doctorName=${doctor.doctorName}&date=${this.selectedDate}&fee=${doctor.registrationFee}`
      })
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

.section-info {
  padding: 30rpx;
  margin-bottom: 20rpx;
  
  .section-title {
    font-size: 36rpx;
    font-weight: bold;
    color: #333;
    display: block;
    margin-bottom: 10rpx;
  }
  
  .section-desc {
    font-size: 28rpx;
    color: #999;
  }
}

.date-selector {
  padding: 20rpx;
  margin-bottom: 20rpx;
  
  .date-list {
    display: flex;
    justify-content: space-between;
    
    .date-item {
      flex: 1;
      display: flex;
      flex-direction: column;
      align-items: center;
      padding: 20rpx 10rpx;
      margin: 0 5rpx;
      border-radius: 12rpx;
      background: #f5f5f5;
      transition: all 0.3s;
      
      &.active {
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        
        .date-week,
        .date-day {
          color: #fff;
        }
      }
      
      .date-week {
        font-size: 24rpx;
        color: #999;
        margin-bottom: 8rpx;
      }
      
      .date-day {
        font-size: 28rpx;
        color: #333;
        font-weight: bold;
      }
    }
  }
}

.scheduling-list {
  .time-section {
    margin-bottom: 30rpx;
    
    .time-header {
      display: flex;
      align-items: center;
      margin-bottom: 20rpx;
      
      .time-title {
        font-size: 32rpx;
        font-weight: bold;
        color: #333;
        margin-right: 15rpx;
      }
      
      .time-desc {
        font-size: 24rpx;
        color: #999;
      }
    }
    
    .doctor-list {
      .doctor-item {
        padding: 24rpx;
        margin-bottom: 20rpx;
        display: flex;
        justify-content: space-between;
        
        .doctor-info {
          flex: 1;
          
          .doctor-name {
            font-size: 32rpx;
            font-weight: bold;
            color: #333;
            margin-bottom: 8rpx;
          }
          
          .doctor-title {
            font-size: 26rpx;
            color: #666;
            margin-bottom: 12rpx;
          }
          
          .doctor-desc {
            font-size: 24rpx;
            color: #999;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
          }
        }
        
        .doctor-action {
          display: flex;
          flex-direction: column;
          align-items: flex-end;
          justify-content: space-between;
          
          .available-num {
            font-size: 24rpx;
            font-weight: bold;
          }
          
          .fee {
            font-size: 32rpx;
            color: #f56c6c;
            font-weight: bold;
            margin: 10rpx 0;
          }
          
          .btn-book {
            padding: 12rpx 32rpx;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: #fff;
            border-radius: 40rpx;
            font-size: 26rpx;
            
            &[disabled] {
              background: #e0e0e0;
              color: #999;
            }
          }
        }
      }
      
      .empty-tip {
        text-align: center;
        padding: 60rpx 0;
        color: #999;
        font-size: 28rpx;
      }
    }
  }
  
  /* 空状态样式 */
  .empty-state {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    padding: 100rpx 0;
    text-align: center;
  }
  
  .empty-icon {
    font-size: 100rpx;
    margin-bottom: 30rpx;
    opacity: 0.6;
  }
  
  .empty-text {
    font-size: 28rpx;
    color: #999;
  }
  
  /* 加载中样式 */
  .loading-container {
    display: flex;
    justify-content: center;
    align-items: center;
    padding: 60rpx 0;
  }
  
  .loading-spinner {
    width: 60rpx;
    height: 60rpx;
    border: 6rpx solid rgba(102, 126, 234, 0.2);
    border-top-color: #667eea;
    border-radius: 50%;
    animation: spin 1s linear infinite;
  }
  
  @keyframes spin {
    to {
      transform: rotate(360deg);
    }
  }
}
</style>

