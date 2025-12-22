<template>
  <view class="container">
    <!-- 顶部轮播 -->
    <view class="banner">
      <swiper class="swiper" indicator-dots autoplay circular>
        <swiper-item v-for="(item, index) in bannerList" :key="index">
          <image :src="item" mode="aspectFill" class="banner-img"></image>
        </swiper-item>
      </swiper>
    </view>

    <!-- 功能菜单 -->
    <view class="menu-grid">
      <view class="menu-item" v-for="(item, index) in menuList" :key="index" @click="handleMenuClick(item)">
        <image :src="item.icon" class="menu-icon"></image>
        <text class="menu-text">{{ item.name }}</text>
      </view>
    </view>

    <!-- 科室列表 -->
    <view class="section-list">
      <view class="section-header">
        <text class="section-title">热门科室</text>
        <text class="section-more" @click="toSectionList">更多 ></text>
      </view>
      <view class="section-content">
        <view 
          class="section-item" 
          v-for="(item, index) in sectionList" 
          :key="index"
          @click="toScheduling(item)"
        >
          <view class="section-name">{{ item.sectionName }}</view>
          <view class="section-desc">{{ item.sectionDesc }}</view>
        </view>
      </view>
    </view>

    <!-- 底部提示 -->
    <view class="footer-tip">
      <text>温馨提示：请合理安排就诊时间</text>
    </view>
  </view>
</template>

<script>
import { getSectionList } from '@/api/api.js'

export default {
  data() {
    return {
      bannerList: [
        '/static/img/banner1.jpg',
        '/static/img/banner2.jpg'
      ],
      menuList: [
        { name: '预约挂号', icon: '/static/appointmentRegister.png', url: '/pages/schedulingRecord/schedulingRecord' },
        { name: '挂号记录', icon: '/static/registrationRecord.png', url: '/pages/registrationRecord/registrationRecord' },
        { name: '门诊缴费', icon: '/static/outpatientPay.png', url: '' },
        { name: '充值记录', icon: '/static/rechargeRecord.png', url: '/pages/payRecord/payRecord' },
        { name: '就诊人', icon: '/static/addAttendanceUser.png', url: '/pages/myVisitors/myVisitors' },
        { name: '检查报告', icon: '/static/inspectionReport.png', url: '' },
        { name: '核酸检测', icon: '/static/nucleicAcidTesting.png', url: '' },
        { name: '更多', icon: '/static/other/zhiyin.jpg', url: '' }
      ],
      sectionList: [],
      loading: false
    }
  },
  onLoad() {
    this.loadSectionList()
  },
  onPullDownRefresh() {
    this.loadSectionList()
    setTimeout(() => {
      uni.stopPullDownRefresh()
    }, 1000)
  },
  methods: {
    // 加载科室列表
    async loadSectionList() {
      try {
        this.loading = true
        const res = await getSectionList({
          pageNum: 1,
          pageSize: 6,
          status: '0' // 只显示启用的科室
        })
        this.sectionList = res.rows || []
      } catch (error) {
        console.error('加载科室列表失败', error)
      } finally {
        this.loading = false
      }
    },
    
    // 菜单点击
    handleMenuClick(item) {
      if (item.url) {
        uni.navigateTo({
          url: item.url
        })
      } else {
        uni.showToast({
          title: '功能开发中',
          icon: 'none'
        })
      }
    },
    
    // 跳转到科室列表
    toSectionList() {
      uni.showToast({
        title: '功能开发中',
        icon: 'none'
      })
    },
    
    // 跳转到排班页面
    toScheduling(section) {
      uni.navigateTo({
        url: `/pages/schedulingRecord/schedulingRecord?sectionId=${section.sectionId}&sectionName=${section.sectionName}`
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

.banner {
  width: 100%;
  height: 320rpx;
  
  .swiper {
    width: 100%;
    height: 100%;
  }
  
  .banner-img {
    width: 100%;
    height: 100%;
  }
}

.menu-grid {
  display: flex;
  flex-wrap: wrap;
  background: #fff;
  padding: 20rpx;
  margin: 20rpx;
  border-radius: 16rpx;
  
  .menu-item {
    width: 25%;
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 20rpx 0;
    
    .menu-icon {
      width: 80rpx;
      height: 80rpx;
      margin-bottom: 10rpx;
    }
    
    .menu-text {
      font-size: 24rpx;
      color: #333;
    }
  }
}

.section-list {
  margin: 20rpx;
  background: #fff;
  border-radius: 16rpx;
  padding: 20rpx;
  
  .section-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20rpx;
    
    .section-title {
      font-size: 32rpx;
      font-weight: bold;
      color: #333;
    }
    
    .section-more {
      font-size: 28rpx;
      color: #999;
    }
  }
  
  .section-content {
    display: flex;
    flex-wrap: wrap;
    
    .section-item {
      width: 48%;
      margin-right: 4%;
      margin-bottom: 20rpx;
      padding: 24rpx;
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      border-radius: 12rpx;
      
      &:nth-child(2n) {
        margin-right: 0;
      }
      
      .section-name {
        font-size: 30rpx;
        font-weight: bold;
        color: #fff;
        margin-bottom: 8rpx;
      }
      
      .section-desc {
        font-size: 24rpx;
        color: rgba(255, 255, 255, 0.8);
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
      }
    }
  }
}

.footer-tip {
  text-align: center;
  padding: 40rpx 0;
  color: #999;
  font-size: 24rpx;
}
</style>

