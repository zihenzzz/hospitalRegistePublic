<template>
  <view class="container">
    <!-- 用户信息卡片 -->
    <view class="user-section">
      <view class="user-bg"></view>
      <view class="user-card">
        <view class="user-info">
          <view class="avatar-wrap">
            <image :src="userInfo.avatar || defaultAvatar" class="avatar"></image>
            <view class="avatar-badge" v-if="isLogin">
              <u-icon name="checkmark" color="#fff" size="20"></u-icon>
            </view>
          </view>
          <view class="info">
            <text class="nickname">{{ userInfo.nickName || '未登录' }}</text>
            <text class="phone" v-if="isLogin">{{ formatPhone(userInfo.phonenumber) }}</text>
            <text class="phone" v-else @click="toLogin">点击登录账号</text>
          </view>
        </view>
        <view class="user-action" @click="isLogin ? toEditInfo() : toLogin()">
          <text>{{ isLogin ? '编辑资料' : '立即登录' }}</text>
          <u-icon name="arrow-right" color="#fff" size="28"></u-icon>
        </view>
      </view>
      
      <!-- 统计数据 -->
      <view class="stats-card" v-if="isLogin">
        <view class="stat-item">
          <text class="stat-value">{{ stats.registration || 0 }}</text>
          <text class="stat-label">挂号次数</text>
        </view>
        <view class="stat-divider"></view>
        <view class="stat-item">
          <text class="stat-value">{{ stats.visitors || 0 }}</text>
          <text class="stat-label">就诊人</text>
        </view>
        <view class="stat-divider"></view>
        <view class="stat-item">
          <text class="stat-value">¥{{ stats.balance || '0.00' }}</text>
          <text class="stat-label">账户余额</text>
        </view>
      </view>
    </view>

    <!-- 功能菜单 -->
    <view class="menu-section">
      <view class="menu-group">
        <view class="group-title">
          <view class="title-icon"></view>
          <text>我的服务</text>
        </view>
        <view class="menu-list">
          <view class="menu-item" @click="toPage('/pages/registrationRecord/registrationRecord')">
            <view class="menu-left">
              <view class="menu-icon" style="background: linear-gradient(135deg, #10b981, #34d399);">
                <text>📋</text>
              </view>
              <text class="menu-name">挂号记录</text>
            </view>
            <u-icon name="arrow-right" color="#cbd5e1" size="28"></u-icon>
          </view>
          <view class="menu-item" @click="toPage('/pages/myVisitors/myVisitors')">
            <view class="menu-left">
              <view class="menu-icon" style="background: linear-gradient(135deg, #6366f1, #8b5cf6);">
                <text>👥</text>
              </view>
              <text class="menu-name">就诊人管理</text>
            </view>
            <u-icon name="arrow-right" color="#cbd5e1" size="28"></u-icon>
          </view>
          <view class="menu-item" @click="toPage('/pages/payRecord/payRecord')">
            <view class="menu-left">
              <view class="menu-icon" style="background: linear-gradient(135deg, #059669, #10b981);">
                <text>💰</text>
              </view>
              <text class="menu-name">充值记录</text>
            </view>
            <u-icon name="arrow-right" color="#cbd5e1" size="28"></u-icon>
          </view>
        </view>
      </view>

      <view class="menu-group">
        <view class="group-title">
          <view class="title-icon"></view>
          <text>更多功能</text>
        </view>
        <view class="menu-list">
          <view class="menu-item" @click="toPage('/pages/aiAssistant/aiAssistant')">
            <view class="menu-left">
              <view class="menu-icon" style="background: linear-gradient(135deg, #f59e0b, #fbbf24);">
                <text>🤖</text>
              </view>
              <text class="menu-name">AI智能助手</text>
            </view>
            <view class="menu-tag">NEW</view>
            <u-icon name="arrow-right" color="#cbd5e1" size="28"></u-icon>
          </view>
          <view class="menu-item" @click="handleMenuClick('collect')">
            <view class="menu-left">
              <view class="menu-icon" style="background: linear-gradient(135deg, #ec4899, #f472b6);">
                <text>⭐</text>
              </view>
              <text class="menu-name">我的收藏</text>
            </view>
            <u-icon name="arrow-right" color="#cbd5e1" size="28"></u-icon>
          </view>
          <view class="menu-item" @click="handleMenuClick('evaluate')">
            <view class="menu-left">
              <view class="menu-icon" style="background: linear-gradient(135deg, #8b5cf6, #a78bfa);">
                <text>💬</text>
              </view>
              <text class="menu-name">我的评价</text>
            </view>
            <u-icon name="arrow-right" color="#cbd5e1" size="28"></u-icon>
          </view>
        </view>
      </view>

      <view class="menu-group">
        <view class="group-title">
          <view class="title-icon"></view>
          <text>其他</text>
        </view>
        <view class="menu-list">
          <view class="menu-item" @click="handleMenuClick('about')">
            <view class="menu-left">
              <view class="menu-icon" style="background: linear-gradient(135deg, #64748b, #94a3b8);">
                <text>ℹ️</text>
              </view>
              <text class="menu-name">关于我们</text>
            </view>
            <u-icon name="arrow-right" color="#cbd5e1" size="28"></u-icon>
          </view>
          <view class="menu-item" @click="handleMenuClick('feedback')">
            <view class="menu-left">
              <view class="menu-icon" style="background: linear-gradient(135deg, #0ea5e9, #38bdf8);">
                <text>📝</text>
              </view>
              <text class="menu-name">意见反馈</text>
            </view>
            <u-icon name="arrow-right" color="#cbd5e1" size="28"></u-icon>
          </view>
        </view>
      </view>
    </view>

    <!-- 退出登录 -->
    <view class="logout-section" v-if="isLogin">
      <button class="btn-logout" @click="handleLogout">退出登录</button>
    </view>

    <!-- 底部安全间距 -->
    <view class="safe-bottom"></view>
  </view>
</template>

<script>
import config from '@/config/app.js'
import { getUserInfo, logout } from '@/api/user.js'

export default {
  data() {
    return {
      userInfo: {},
      defaultAvatar: '/static/default-avatar.png',
      isLogin: false,
      stats: {
        registration: 0,
        visitors: 0,
        balance: '0.00'
      }
    }
  },
  onShow() {
    this.checkLogin()
    if (this.isLogin) {
      this.loadUserInfo()
    }
  },
  methods: {
    checkLogin() {
      const token = uni.getStorageSync(config.cacheKeys.token)
      this.isLogin = !!token
      if (this.isLogin) {
        this.userInfo = uni.getStorageSync(config.cacheKeys.userInfo) || {}
      }
    },
    
    async loadUserInfo() {
      try {
        const res = await getUserInfo()
        if (res.code === 200) {
          this.userInfo = res.user
          uni.setStorageSync(config.cacheKeys.userInfo, res.user)
          // 更新统计数据
          if (res.user.balance) {
            this.stats.balance = res.user.balance
          }
        }
      } catch (error) {
        console.error('获取用户信息失败', error)
      }
    },
    
    formatPhone(phone) {
      if (!phone) return ''
      return phone.replace(/(\d{3})\d{4}(\d{4})/, '$1****$2')
    },
    
    toLogin() {
      uni.showToast({ title: '请使用微信授权登录', icon: 'none' })
    },
    
    toEditInfo() {
      uni.navigateTo({ url: '/pages/user/info/info' })
    },
    
    toPage(url) {
      if (!this.isLogin && url !== '/pages/aiAssistant/aiAssistant') {
        uni.showToast({ title: '请先登录', icon: 'none' })
        return
      }
      uni.navigateTo({ url: url })
    },
    
    handleMenuClick(type) {
      uni.showToast({ title: '功能开发中', icon: 'none' })
    },
    
    handleLogout() {
      uni.showModal({
        title: '提示',
        content: '确定要退出登录吗？',
        confirmColor: '#10b981',
        success: (res) => {
          if (res.confirm) {
            uni.removeStorageSync(config.cacheKeys.token)
            uni.removeStorageSync(config.cacheKeys.userInfo)
            this.isLogin = false
            this.userInfo = {}
            uni.showToast({ title: '已退出登录', icon: 'success' })
          }
        }
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

.user-section {
  position: relative;
  padding-bottom: 24rpx;
  
  .user-bg {
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    height: 320rpx;
    background: linear-gradient(135deg, $primary 0%, $primary-light 100%);
  }
  
  .user-card {
    position: relative;
    margin: 60rpx 32rpx 0;
    padding: 32rpx;
    display: flex;
    justify-content: space-between;
    align-items: center;
    
    .user-info {
      display: flex;
      align-items: center;
      
      .avatar-wrap {
        position: relative;
        
        .avatar {
          width: 120rpx;
          height: 120rpx;
          border-radius: 50%;
          border: 4rpx solid rgba(255, 255, 255, 0.3);
        }
        
        .avatar-badge {
          position: absolute;
          bottom: 0;
          right: 0;
          width: 36rpx;
          height: 36rpx;
          background: #10b981;
          border-radius: 50%;
          display: flex;
          align-items: center;
          justify-content: center;
          border: 4rpx solid #fff;
        }
      }
      
      .info {
        margin-left: 24rpx;
        
        .nickname {
          font-size: 36rpx;
          font-weight: bold;
          color: #fff;
          display: block;
          margin-bottom: 8rpx;
        }
        
        .phone {
          font-size: 28rpx;
          color: rgba(255, 255, 255, 0.8);
        }
      }
    }
    
    .user-action {
      display: flex;
      align-items: center;
      gap: 8rpx;
      padding: 16rpx 24rpx;
      background: rgba(255, 255, 255, 0.2);
      border-radius: 32rpx;
      
      text {
        font-size: 26rpx;
        color: #fff;
      }
    }
  }
  
  .stats-card {
    position: relative;
    margin: 24rpx 32rpx 0;
    padding: 32rpx;
    background: #fff;
    border-radius: 24rpx;
    display: flex;
    align-items: center;
    box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.04);
    
    .stat-item {
      flex: 1;
      display: flex;
      flex-direction: column;
      align-items: center;
      
      .stat-value {
        font-size: 36rpx;
        font-weight: bold;
        color: $dark;
        margin-bottom: 8rpx;
      }
      
      .stat-label {
        font-size: 24rpx;
        color: $gray;
      }
    }
    
    .stat-divider {
      width: 1rpx;
      height: 60rpx;
      background: #e2e8f0;
    }
  }
}

.menu-section {
  padding: 0 32rpx;
  
  .menu-group {
    margin-bottom: 24rpx;
    
    .group-title {
      display: flex;
      align-items: center;
      gap: 12rpx;
      margin-bottom: 16rpx;
      padding-left: 8rpx;
      
      .title-icon {
        width: 6rpx;
        height: 28rpx;
        background: linear-gradient(180deg, $primary, $primary-light);
        border-radius: 3rpx;
      }
      
      text {
        font-size: 28rpx;
        font-weight: 600;
        color: $dark;
      }
    }
    
    .menu-list {
      background: #fff;
      border-radius: 24rpx;
      overflow: hidden;
      box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.04);
      
      .menu-item {
        display: flex;
        align-items: center;
        padding: 28rpx 24rpx;
        border-bottom: 1rpx solid #f1f5f9;
        
        &:last-child {
          border-bottom: none;
        }
        
        &:active {
          background: #f8fafc;
        }
        
        .menu-left {
          flex: 1;
          display: flex;
          align-items: center;
          gap: 20rpx;
          
          .menu-icon {
            width: 72rpx;
            height: 72rpx;
            border-radius: 18rpx;
            display: flex;
            align-items: center;
            justify-content: center;
            
            text {
              font-size: 32rpx;
            }
          }
          
          .menu-name {
            font-size: 30rpx;
            color: $dark;
          }
        }
        
        .menu-tag {
          padding: 6rpx 12rpx;
          background: linear-gradient(135deg, #ef4444, #f87171);
          border-radius: 8rpx;
          font-size: 20rpx;
          color: #fff;
          margin-right: 16rpx;
        }
      }
    }
  }
}

.logout-section {
  padding: 40rpx 32rpx;
  
  .btn-logout {
    width: 100%;
    height: 96rpx;
    line-height: 96rpx;
    background: #fff;
    color: #ef4444;
    border: 2rpx solid #fecaca;
    border-radius: 48rpx;
    font-size: 32rpx;
    font-weight: 500;
    
    &:active {
      background: #fef2f2;
    }
  }
}

.safe-bottom {
  height: calc(40rpx + env(safe-area-inset-bottom));
}
</style>
