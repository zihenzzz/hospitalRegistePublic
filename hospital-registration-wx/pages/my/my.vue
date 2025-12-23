<template>
  <view class="container">
    <!-- 用户信息卡片 -->
    <view class="user-card">
      <view class="user-info">
        <image :src="userInfo.avatar || defaultAvatar" class="avatar"></image>
        <view class="info">
          <text class="nickname">{{ userInfo.nickName || '未登录' }}</text>
          <text class="phone">{{ userInfo.phonenumber || '点击登录' }}</text>
        </view>
      </view>
      <view class="user-actions">
        <button class="btn-login" v-if="!isLogin" @click="toLogin">登录</button>
        <button class="btn-edit" v-else @click="toEditInfo">编辑资料</button>
      </view>
    </view>

    <!-- 功能列表 -->
    <view class="menu-list">
      <view class="menu-section">
        <view class="menu-item" @click="toPage('/pages/registrationRecord/registrationRecord')">
          <view class="menu-left">
            <text class="menu-icon">📋</text>
            <text class="menu-name">挂号记录</text>
          </view>
          <text class="menu-arrow">></text>
        </view>
        <view class="menu-item" @click="toPage('/pages/myVisitors/myVisitors')">
          <view class="menu-left">
            <text class="menu-icon">👥</text>
            <text class="menu-name">就诊人管理</text>
          </view>
          <text class="menu-arrow">></text>
        </view>
        <view class="menu-item" @click="toPage('/pages/payRecord/payRecord')">
          <view class="menu-left">
            <text class="menu-icon">💰</text>
            <text class="menu-name">充值记录</text>
          </view>
          <text class="menu-arrow">></text>
        </view>
      </view>

      <view class="menu-section">
        <view class="menu-item" @click="handleMenuClick('collect')">
          <view class="menu-left">
            <text class="menu-icon">⭐</text>
            <text class="menu-name">我的收藏</text>
          </view>
          <text class="menu-arrow">></text>
        </view>
        <view class="menu-item" @click="handleMenuClick('evaluate')">
          <view class="menu-left">
            <text class="menu-icon">💬</text>
            <text class="menu-name">我的评价</text>
          </view>
          <text class="menu-arrow">></text>
        </view>
      </view>

      <view class="menu-section">
        <view class="menu-item" @click="handleMenuClick('about')">
          <view class="menu-left">
            <text class="menu-icon">ℹ️</text>
            <text class="menu-name">关于我们</text>
          </view>
          <text class="menu-arrow">></text>
        </view>
      </view>
    </view>

    <!-- 退出登录 -->
    <view class="logout-section" v-if="isLogin">
      <button class="btn-logout" @click="handleLogout">退出登录</button>
    </view>
  </view>
</template>

<script>
import config from '@/config/app.js'
import { getUserInfo, logout } from '@/api/user.js'

export default {
  data() {
    return {
      userInfo: {},
      defaultAvatar: config.defaultAvatar,
      isLogin: false
    }
  },
  onShow() {
    this.checkLogin()
    if (this.isLogin) {
      this.loadUserInfo()
    }
  },
  methods: {
    // 检查登录状态
    checkLogin() {
      const token = uni.getStorageSync(config.cacheKeys.token)
      this.isLogin = !!token
      if (this.isLogin) {
        this.userInfo = uni.getStorageSync(config.cacheKeys.userInfo) || {}
      }
    },
    
    // 加载用户信息
    async loadUserInfo() {
      try {
        const res = await getUserInfo()
        if (res.code === 200) {
          this.userInfo = res.user
          uni.setStorageSync(config.cacheKeys.userInfo, res.user)
        }
      } catch (error) {
        console.error('获取用户信息失败', error)
      }
    },
    
    // 跳转登录
    toLogin() {
      uni.showToast({
        title: '登录功能开发中',
        icon: 'none'
      })
    },
    
    // 编辑资料
    toEditInfo() {
      uni.navigateTo({
        url: '/pages/user/info/info'
      })
    },
    
    // 跳转页面
    toPage(url) {
      if (!this.isLogin) {
        uni.showToast({
          title: '请先登录',
          icon: 'none'
        })
        return
      }
      uni.navigateTo({
        url: url
      })
    },
    
    // 菜单点击
    handleMenuClick(type) {
      uni.showToast({
        title: '功能开发中',
        icon: 'none'
      })
    },
    
    // 退出登录
    handleLogout() {
      uni.showModal({
        title: '提示',
        content: '确定要退出登录吗？',
        success: (res) => {
          if (res.confirm) {
            // 清除本地存储
            uni.removeStorageSync(config.cacheKeys.token)
            uni.removeStorageSync(config.cacheKeys.userInfo)
            this.isLogin = false
            this.userInfo = {}
            uni.showToast({
              title: '已退出登录',
              icon: 'success'
            })
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
  background: #f5f5f5;
}

.user-card {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 40rpx 30rpx;
  display: flex;
  justify-content: space-between;
  align-items: center;
  
  .user-info {
    display: flex;
    align-items: center;
    
    .avatar {
      width: 120rpx;
      height: 120rpx;
      border-radius: 50%;
      margin-right: 24rpx;
      border: 4rpx solid rgba(255, 255, 255, 0.3);
    }
    
    .info {
      display: flex;
      flex-direction: column;
      
      .nickname {
        font-size: 36rpx;
        font-weight: bold;
        color: #fff;
        margin-bottom: 10rpx;
      }
      
      .phone {
        font-size: 28rpx;
        color: rgba(255, 255, 255, 0.8);
      }
    }
  }
  
  .user-actions {
    .btn-login,
    .btn-edit {
      padding: 16rpx 32rpx;
      background: rgba(255, 255, 255, 0.2);
      border: 2rpx solid rgba(255, 255, 255, 0.6);
      border-radius: 40rpx;
      color: #fff;
      font-size: 28rpx;
    }
  }
}

.menu-list {
  .menu-section {
    margin: 20rpx 0;
    background: #fff;
    border-radius: 16rpx;
    overflow: hidden;
    
    .menu-item {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 30rpx;
      border-bottom: 1rpx solid #f0f0f0;
      
      &:last-child {
        border-bottom: none;
      }
      
      .menu-left {
        display: flex;
        align-items: center;
        
        .menu-icon {
          font-size: 40rpx;
          margin-right: 20rpx;
        }
        
        .menu-name {
          font-size: 30rpx;
          color: #333;
        }
      }
      
      .menu-arrow {
        font-size: 28rpx;
        color: #999;
      }
    }
  }
}

.logout-section {
  padding: 40rpx 30rpx;
  
  .btn-logout {
    width: 100%;
    background: #fff;
    color: #f56c6c;
    border: 1rpx solid #f56c6c;
    border-radius: 40rpx;
    padding: 24rpx 0;
    font-size: 32rpx;
  }
}
</style>


