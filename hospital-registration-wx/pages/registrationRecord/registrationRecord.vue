<template>
  <view class="container">
    <!-- 状态tabs -->
    <view class="status-tabs">
      <view 
        class="tab-item"
        :class="{'active': currentStatus === item.value}"
        v-for="(item, index) in statusTabs" 
        :key="index"
        @click="changeStatus(item.value)"
      >
        <text>{{ item.label }}</text>
      </view>
    </view>

    <!-- 挂号列表 -->
    <view class="registration-list">
      <view 
        class="registration-item card"
        v-for="(item, index) in registrationList" 
        :key="index"
        @click="toDetail(item)"
      >
        <view class="item-header">
          <view class="doctor-info">
            <text class="doctor-name">{{ item.doctorName }}</text>
            <text class="section-name">{{ item.sectionName }}</text>
          </view>
          <view class="status-badge" :class="'status-' + item.status">
            <text v-if="item.status === '0'">待就诊</text>
            <text v-else-if="item.status === '1'">已就诊</text>
            <text v-else>已取消</text>
          </view>
        </view>
        
        <view class="item-info">
          <view class="info-row">
            <text class="label">患者：</text>
            <text class="value">{{ item.patientName }}</text>
          </view>
          <view class="info-row">
            <text class="label">就诊时间：</text>
            <text class="value">
              {{ item.registrationDate }} 
              <text v-if="item.registrationTime === '1'">上午</text>
              <text v-else-if="item.registrationTime === '2'">下午</text>
              <text v-else>晚上</text>
            </text>
          </view>
          <view class="info-row">
            <text class="label">挂号费：</text>
            <text class="value fee">¥{{ item.registrationFee }}</text>
          </view>
        </view>
        
        <view class="item-footer" v-if="item.status === '0'">
          <button class="btn-cancel" size="mini" @click.stop="cancelRegistration(item)">
            取消挂号
          </button>
          <button class="btn-detail" size="mini" @click.stop="toDetail(item)">
            查看详情
          </button>
        </view>
      </view>
    </view>

    <!-- 空状态 -->
    <u-empty 
      v-if="!loading && registrationList.length === 0" 
      text="暂无挂号记录"
      mode="list"
    />

    <!-- 加载中 -->
    <u-loading-icon v-if="loading" mode="circle" />
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
    // 每次显示页面时刷新数据
    this.loadData()
  },
  methods: {
    // 切换状态
    changeStatus(status) {
      this.currentStatus = status
      this.loadData()
    },
    
    // 加载数据
    async loadData() {
      try {
        this.loading = true
        const params = {
          pageNum: 1,
          pageSize: 100
        }
        
        // 如果不是全部，添加状态筛选
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
    
    // 取消挂号
    cancelRegistration(item) {
      uni.showModal({
        title: '提示',
        content: '确定要取消该挂号吗？',
        success: async (res) => {
          if (res.confirm) {
            try {
              await delRegistration(item.registrationId)
              uni.showToast({
                title: '取消成功',
                icon: 'success'
              })
              this.loadData()
            } catch (error) {
              console.error('取消挂号失败', error)
            }
          }
        }
      })
    },
    
    // 查看详情
    toDetail(item) {
      uni.navigateTo({
        url: `/pages/registrationDetail/registrationDetail?id=${item.registrationId}`
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.container {
  min-height: 100vh;
  background: #f5f5f5;
}

.status-tabs {
  background: #fff;
  display: flex;
  padding: 20rpx 0;
  
  .tab-item {
    flex: 1;
    text-align: center;
    padding: 20rpx 0;
    font-size: 28rpx;
    color: #666;
    position: relative;
    
    &.active {
      color: #667eea;
      font-weight: bold;
      
      &::after {
        content: '';
        position: absolute;
        bottom: 0;
        left: 50%;
        transform: translateX(-50%);
        width: 60rpx;
        height: 4rpx;
        background: #667eea;
        border-radius: 2rpx;
      }
    }
  }
}

.registration-list {
  padding: 20rpx;
  
  .registration-item {
    padding: 24rpx;
    margin-bottom: 20rpx;
    
    .item-header {
      display: flex;
      justify-content: space-between;
      align-items: flex-start;
      margin-bottom: 20rpx;
      padding-bottom: 20rpx;
      border-bottom: 1rpx solid #f0f0f0;
      
      .doctor-info {
        .doctor-name {
          font-size: 32rpx;
          font-weight: bold;
          color: #333;
          display: block;
          margin-bottom: 8rpx;
        }
        
        .section-name {
          font-size: 26rpx;
          color: #999;
        }
      }
      
      .status-badge {
        padding: 8rpx 20rpx;
        border-radius: 20rpx;
        font-size: 24rpx;
        
        &.status-0 {
          background: #fff4e5;
          color: #ff9900;
        }
        
        &.status-1 {
          background: #e8f5e8;
          color: #67c23a;
        }
        
        &.status-2 {
          background: #f0f0f0;
          color: #999;
        }
      }
    }
    
    .item-info {
      .info-row {
        display: flex;
        margin-bottom: 12rpx;
        font-size: 28rpx;
        
        .label {
          color: #999;
          min-width: 150rpx;
        }
        
        .value {
          color: #333;
          flex: 1;
          
          &.fee {
            color: #f56c6c;
            font-weight: bold;
          }
        }
      }
    }
    
    .item-footer {
      display: flex;
      justify-content: flex-end;
      margin-top: 20rpx;
      padding-top: 20rpx;
      border-top: 1rpx solid #f0f0f0;
      
      button {
        padding: 12rpx 32rpx;
        border-radius: 40rpx;
        font-size: 26rpx;
        margin-left: 20rpx;
      }
      
      .btn-cancel {
        background: #fff;
        color: #666;
        border: 1rpx solid #ddd;
      }
      
      .btn-detail {
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        color: #fff;
      }
    }
  }
}
</style>

