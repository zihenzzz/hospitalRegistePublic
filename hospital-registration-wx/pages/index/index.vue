<template>
  <view class="container">
    <!-- 顶部区域 -->
    <view class="header-section">
      <view class="header-bg"></view>
      <view class="header-content">
        <view class="logo-area">
          <view class="logo-icon">
            <text class="icon-text">+</text>
          </view>
          <view class="logo-text">
            <text class="title">智慧医疗</text>
            <text class="subtitle">便捷就医，健康生活</text>
          </view>
        </view>
        <view class="user-avatar" @click="toMy">
          <u-icon name="account-fill" color="#fff" size="44"></u-icon>
        </view>
      </view>
      
      <!-- 搜索框 -->
      <view class="search-bar" @click="toAiAssistant">
        <u-icon name="search" color="#94a3b8" size="36"></u-icon>
        <text class="search-placeholder">搜索科室、医生或症状...</text>
        <view class="ai-tag">
          <text>AI</text>
        </view>
      </view>
    </view>

    <!-- 功能菜单 -->
    <view class="menu-section">
      <view class="menu-grid">
        <view 
          class="menu-item" 
          v-for="(item, index) in menuList" 
          :key="index" 
          @click="handleMenuClick(item)"
        >
          <view class="menu-icon-wrap" :style="{ background: item.bgColor }">
            <text class="menu-emoji">{{ item.emoji }}</text>
          </view>
          <text class="menu-text">{{ item.name }}</text>
        </view>
      </view>
    </view>

    <!-- AI智能助手入口 -->
    <view class="ai-entry" @click="toAiAssistant">
      <view class="ai-content">
        <view class="ai-icon">
          <text class="ai-emoji">🤖</text>
        </view>
        <view class="ai-info">
          <text class="ai-title">AI智能助手</text>
          <text class="ai-desc">智能问诊、症状分析、预约挂号</text>
        </view>
      </view>
      <view class="ai-arrow">
        <u-icon name="arrow-right" color="#10b981" size="32"></u-icon>
      </view>
    </view>

    <!-- 热门科室 -->
    <view class="section-card">
      <view class="section-header">
        <view class="section-title-wrap">
          <view class="title-icon"></view>
          <text class="section-title">热门科室</text>
        </view>
        <text class="section-more" @click="toSectionList">全部科室 ></text>
      </view>
      <view class="section-grid">
        <view 
          class="section-item" 
          v-for="(item, index) in sectionList" 
          :key="index"
          @click="toScheduling(item)"
        >
          <view class="section-icon">
            <text>{{ getSectionEmoji(item.sectionName) }}</text>
          </view>
          <text class="section-name">{{ item.sectionName }}</text>
          <text class="section-count">{{ item.doctorCount || 0 }}位医生</text>
        </view>
        <view class="section-item" v-if="sectionList.length === 0" style="width: 100%; text-align: center;">
          <text class="empty-text">暂无科室数据</text>
        </view>
      </view>
    </view>

    <!-- 健康资讯 -->
    <view class="section-card">
      <view class="section-header">
        <view class="section-title-wrap">
          <view class="title-icon"></view>
          <text class="section-title">健康资讯</text>
        </view>
      </view>
      <view class="news-list">
        <view class="news-item" v-for="(item, index) in newsList" :key="index">
          <view class="news-content">
            <text class="news-title">{{ item.title }}</text>
            <text class="news-desc">{{ item.desc }}</text>
          </view>
          <view class="news-tag">{{ item.tag }}</view>
        </view>
      </view>
    </view>

    <!-- 底部安全间距 -->
    <view class="safe-bottom"></view>
  </view>
</template>

<script>
import { getSectionList } from '@/api/api.js'

export default {
  data() {
    return {
      menuList: [
        { name: '智能助手', emoji: '🤖', url: '/pages/aiAssistant/aiAssistant', bgColor: 'linear-gradient(135deg, #10b981, #34d399)' },
        { name: '预约挂号', emoji: '📅', url: '/pages/schedulingRecord/schedulingRecord', bgColor: 'linear-gradient(135deg, #6366f1, #8b5cf6)' },
        { name: '挂号记录', emoji: '📋', url: '/pages/registrationRecord/registrationRecord', bgColor: 'linear-gradient(135deg, #059669, #10b981)' },
        { name: '门诊缴费', emoji: '💳', url: '', bgColor: 'linear-gradient(135deg, #f59e0b, #fbbf24)' },
        { name: '充值记录', emoji: '💰', url: '/pages/payRecord/payRecord', bgColor: 'linear-gradient(135deg, #ec4899, #f472b6)' },
        { name: '就诊人', emoji: '👥', url: '/pages/myVisitors/myVisitors', bgColor: 'linear-gradient(135deg, #8b5cf6, #a78bfa)' },
        { name: '检查报告', emoji: '📊', url: '', bgColor: 'linear-gradient(135deg, #ef4444, #f87171)' },
        { name: '更多服务', emoji: '⚙️', url: '', bgColor: 'linear-gradient(135deg, #64748b, #94a3b8)' }
      ],
      sectionList: [],
      newsList: [
        { title: '冬季养生小贴士', desc: '寒冷季节如何保护身体健康', tag: '养生' },
        { title: '预防流感指南', desc: '科学防护，远离流感困扰', tag: '预防' },
        { title: '健康饮食建议', desc: '均衡营养，增强免疫力', tag: '饮食' }
      ],
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
    async loadSectionList() {
      try {
        this.loading = true
        const res = await getSectionList({
          pageNum: 1,
          pageSize: 8,
          status: '0'
        })
        this.sectionList = res.rows || []
      } catch (error) {
        console.error('加载科室列表失败', error)
      } finally {
        this.loading = false
      }
    },
    
    getSectionEmoji(name) {
      const emojiMap = {
        '内科': '🫀', '外科': '🔪', '儿科': '👶', '妇科': '👩',
        '眼科': '👁️', '口腔科': '🦷', '皮肤科': '🧴', '骨科': '🦴',
        '神经科': '🧠', '心血管': '❤️', '呼吸科': '🫁', '消化科': '🍽️'
      }
      for (const key in emojiMap) {
        if (name && name.includes(key)) return emojiMap[key]
      }
      return '🏥'
    },
    
    handleMenuClick(item) {
      if (item.url) {
        uni.navigateTo({ url: item.url })
      } else {
        uni.showToast({ title: '功能开发中', icon: 'none' })
      }
    },
    
    toAiAssistant() {
      uni.navigateTo({ url: '/pages/aiAssistant/aiAssistant' })
    },
    
    toMy() {
      uni.switchTab({ url: '/pages/my/my' })
    },
    
    toSectionList() {
      uni.navigateTo({ url: '/pages/schedulingRecord/schedulingRecord' })
    },
    
    toScheduling(section) {
      uni.navigateTo({
        url: `/pages/schedulingRecord/schedulingRecord?sectionId=${section.sectionId}&sectionName=${section.sectionName}`
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

.header-section {
  position: relative;
  padding: 0 0 60rpx;
  
  .header-bg {
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    height: 380rpx;
    background: linear-gradient(135deg, $primary 0%, $primary-light 100%);
    border-radius: 0 0 60rpx 60rpx;
  }
  
  .header-content {
    position: relative;
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 60rpx 32rpx 40rpx;
    
    .logo-area {
      display: flex;
      align-items: center;
      gap: 20rpx;
      
      .logo-icon {
        width: 80rpx;
        height: 80rpx;
        background: rgba(255, 255, 255, 0.2);
        border-radius: 20rpx;
        display: flex;
        align-items: center;
        justify-content: center;
        
        .icon-text {
          font-size: 48rpx;
          font-weight: bold;
          color: #fff;
        }
      }
      
      .logo-text {
        display: flex;
        flex-direction: column;
        
        .title {
          font-size: 40rpx;
          font-weight: bold;
          color: #fff;
        }
        
        .subtitle {
          font-size: 24rpx;
          color: rgba(255, 255, 255, 0.8);
          margin-top: 4rpx;
        }
      }
    }
    
    .user-avatar {
      width: 80rpx;
      height: 80rpx;
      background: rgba(255, 255, 255, 0.2);
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
    }
  }
  
  .search-bar {
    position: relative;
    margin: 0 32rpx;
    padding: 24rpx 32rpx;
    background: #fff;
    border-radius: 24rpx;
    display: flex;
    align-items: center;
    gap: 16rpx;
    box-shadow: 0 8rpx 32rpx rgba(0, 0, 0, 0.08);
    
    .search-placeholder {
      flex: 1;
      font-size: 28rpx;
      color: #94a3b8;
    }
    
    .ai-tag {
      padding: 8rpx 16rpx;
      background: linear-gradient(135deg, $primary, $primary-light);
      border-radius: 12rpx;
      
      text {
        font-size: 22rpx;
        font-weight: bold;
        color: #fff;
      }
    }
  }
}

.menu-section {
  margin: -20rpx 32rpx 24rpx;
  padding: 32rpx;
  background: #fff;
  border-radius: 24rpx;
  box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.04);
  
  .menu-grid {
    display: flex;
    flex-wrap: wrap;
    
    .menu-item {
      width: 25%;
      display: flex;
      flex-direction: column;
      align-items: center;
      padding: 20rpx 0;
      
      .menu-icon-wrap {
        width: 96rpx;
        height: 96rpx;
        border-radius: 24rpx;
        display: flex;
        align-items: center;
        justify-content: center;
        margin-bottom: 16rpx;
        
        .menu-emoji {
          font-size: 40rpx;
        }
      }
      
      .menu-text {
        font-size: 24rpx;
        color: $dark;
        font-weight: 500;
      }
    }
  }
}

.ai-entry {
  margin: 0 32rpx 24rpx;
  padding: 32rpx;
  background: linear-gradient(135deg, #ecfdf5 0%, #d1fae5 100%);
  border-radius: 24rpx;
  border: 2rpx solid rgba($primary, 0.2);
  display: flex;
  align-items: center;
  justify-content: space-between;
  
  .ai-content {
    display: flex;
    align-items: center;
    gap: 24rpx;
    
    .ai-icon {
      width: 88rpx;
      height: 88rpx;
      background: linear-gradient(135deg, $primary, $primary-light);
      border-radius: 22rpx;
      display: flex;
      align-items: center;
      justify-content: center;
      
      .ai-emoji {
        font-size: 44rpx;
      }
    }
    
    .ai-info {
      display: flex;
      flex-direction: column;
      gap: 8rpx;
      
      .ai-title {
        font-size: 32rpx;
        font-weight: bold;
        color: $dark;
      }
      
      .ai-desc {
        font-size: 24rpx;
        color: $gray;
      }
    }
  }
}

.section-card {
  margin: 0 32rpx 24rpx;
  padding: 32rpx;
  background: #fff;
  border-radius: 24rpx;
  box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.04);
  
  .section-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 28rpx;
    
    .section-title-wrap {
      display: flex;
      align-items: center;
      gap: 12rpx;
      
      .title-icon {
        width: 8rpx;
        height: 32rpx;
        background: linear-gradient(180deg, $primary, $primary-light);
        border-radius: 4rpx;
      }
      
      .section-title {
        font-size: 32rpx;
        font-weight: bold;
        color: $dark;
      }
    }
    
    .section-more {
      font-size: 26rpx;
      color: $gray;
    }
  }
  
  .section-grid {
    display: flex;
    flex-wrap: wrap;
    gap: 24rpx;
    
    .section-item {
      width: calc(25% - 18rpx);
      display: flex;
      flex-direction: column;
      align-items: center;
      padding: 24rpx 0;
      background: #f8fafc;
      border-radius: 16rpx;
      transition: all 0.3s;
      
      &:active {
        background: #f1f5f9;
        transform: scale(0.98);
      }
      
      .section-icon {
        width: 72rpx;
        height: 72rpx;
        background: linear-gradient(135deg, #ecfdf5, #d1fae5);
        border-radius: 18rpx;
        display: flex;
        align-items: center;
        justify-content: center;
        margin-bottom: 12rpx;
        
        text {
          font-size: 36rpx;
        }
      }
      
      .section-name {
        font-size: 26rpx;
        font-weight: 500;
        color: $dark;
        margin-bottom: 4rpx;
      }
      
      .section-count {
        font-size: 22rpx;
        color: $gray;
      }
    }
  }
  
  .news-list {
    .news-item {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 24rpx 0;
      border-bottom: 1rpx solid #f1f5f9;
      
      &:last-child {
        border-bottom: none;
      }
      
      .news-content {
        flex: 1;
        
        .news-title {
          font-size: 28rpx;
          font-weight: 500;
          color: $dark;
          display: block;
          margin-bottom: 8rpx;
        }
        
        .news-desc {
          font-size: 24rpx;
          color: $gray;
        }
      }
      
      .news-tag {
        padding: 8rpx 16rpx;
        background: #ecfdf5;
        border-radius: 8rpx;
        font-size: 22rpx;
        color: $primary;
      }
    }
  }
}

.safe-bottom {
  height: calc(40rpx + env(safe-area-inset-bottom));
}

.empty-text {
  font-size: 28rpx;
  color: $gray;
  padding: 40rpx 0;
}
</style>
