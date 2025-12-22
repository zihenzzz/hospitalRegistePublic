<template>
  <view class="container">
    <!-- 头像 -->
    <view class="avatar-section card">
      <view class="section-left">
        <text class="section-label">头像</text>
      </view>
      <view class="section-right">
        <image :src="userInfo.avatar || defaultAvatar" class="avatar" @click="changeAvatar" />
        <text class="arrow">></text>
      </view>
    </view>

    <!-- 个人信息 -->
    <view class="info-list">
      <view class="info-item card" @click="editField('nickName')">
        <text class="label">昵称</text>
        <view class="value-area">
          <text class="value">{{ userInfo.nickName || '未设置' }}</text>
          <text class="arrow">></text>
        </view>
      </view>
      
      <view class="info-item card">
        <text class="label">手机号</text>
        <view class="value-area">
          <text class="value">{{ userInfo.phonenumber || '未绑定' }}</text>
          <text class="arrow">></text>
        </view>
      </view>
      
      <view class="info-item card" @click="editField('gender')">
        <text class="label">性别</text>
        <view class="value-area">
          <text class="value">{{ userInfo.sex === '0' ? '男' : '女' }}</text>
          <text class="arrow">></text>
        </view>
      </view>
      
      <view class="info-item card" @click="editField('email')">
        <text class="label">邮箱</text>
        <view class="value-area">
          <text class="value">{{ userInfo.email || '未设置' }}</text>
          <text class="arrow">></text>
        </view>
      </view>
    </view>

    <!-- 修改密码 -->
    <view class="action-section">
      <button class="btn-action" @click="changePassword">修改密码</button>
    </view>

    <!-- 编辑弹窗 -->
    <u-popup v-model="showDialog" mode="center" border-radius="20">
      <view class="dialog">
        <view class="dialog-title">{{ dialogTitle }}</view>
        <view class="dialog-content">
          <input 
            v-if="editingField !== 'gender'"
            class="dialog-input" 
            v-model="editValue" 
            :placeholder="'请输入' + dialogTitle"
          />
          <radio-group v-else @change="onGenderChange">
            <label class="radio-item">
              <radio value="0" :checked="editValue === '0'" />男
            </label>
            <label class="radio-item">
              <radio value="1" :checked="editValue === '1'" />女
            </label>
          </radio-group>
        </view>
        <view class="dialog-footer">
          <button class="btn-dialog btn-cancel" @click="closeDialog">取消</button>
          <button class="btn-dialog btn-confirm" @click="saveEdit">确定</button>
        </view>
      </view>
    </u-popup>
  </view>
</template>

<script>
import config from '@/config/app.js'
import { getUserInfo, updateUserInfo } from '@/api/user.js'

export default {
  data() {
    return {
      userInfo: {},
      defaultAvatar: config.defaultAvatar,
      showDialog: false,
      dialogTitle: '',
      editingField: '',
      editValue: ''
    }
  },
  onLoad() {
    this.loadUserInfo()
  },
  methods: {
    // 加载用户信息
    async loadUserInfo() {
      try {
        // 先从缓存获取
        this.userInfo = uni.getStorageSync(config.cacheKeys.userInfo) || {}
        
        // 然后从服务器获取最新数据
        const res = await getUserInfo()
        if (res.code === 200) {
          this.userInfo = res.user
          uni.setStorageSync(config.cacheKeys.userInfo, res.user)
        }
      } catch (error) {
        console.error('获取用户信息失败', error)
      }
    },
    
    // 更换头像
    changeAvatar() {
      uni.chooseImage({
        count: 1,
        sizeType: ['compressed'],
        sourceType: ['album', 'camera'],
        success: (res) => {
          // 这里应该上传头像到服务器
          uni.showToast({
            title: '头像上传功能开发中',
            icon: 'none'
          })
        }
      })
    },
    
    // 编辑字段
    editField(field) {
      this.editingField = field
      
      switch(field) {
        case 'nickName':
          this.dialogTitle = '昵称'
          this.editValue = this.userInfo.nickName || ''
          break
        case 'gender':
          this.dialogTitle = '性别'
          this.editValue = this.userInfo.sex || '0'
          break
        case 'email':
          this.dialogTitle = '邮箱'
          this.editValue = this.userInfo.email || ''
          break
      }
      
      this.showDialog = true
    },
    
    // 性别选择
    onGenderChange(e) {
      this.editValue = e.detail.value
    },
    
    // 保存修改
    async saveEdit() {
      if (!this.editValue && this.editingField !== 'gender') {
        uni.showToast({
          title: '请输入内容',
          icon: 'none'
        })
        return
      }
      
      try {
        const updateData = {}
        
        switch(this.editingField) {
          case 'nickName':
            updateData.nickName = this.editValue
            this.userInfo.nickName = this.editValue
            break
          case 'gender':
            updateData.sex = this.editValue
            this.userInfo.sex = this.editValue
            break
          case 'email':
            updateData.email = this.editValue
            this.userInfo.email = this.editValue
            break
        }
        
        // 调用更新接口
        await updateUserInfo(updateData)
        
        // 更新缓存
        uni.setStorageSync(config.cacheKeys.userInfo, this.userInfo)
        
        this.closeDialog()
        uni.showToast({
          title: '修改成功',
          icon: 'success'
        })
      } catch (error) {
        console.error('修改失败', error)
        uni.showToast({
          title: '修改失败',
          icon: 'none'
        })
      }
    },
    
    // 关闭弹窗
    closeDialog() {
      this.showDialog = false
    },
    
    // 修改密码
    changePassword() {
      uni.showToast({
        title: '修改密码功能开发中',
        icon: 'none'
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

.avatar-section {
  padding: 30rpx;
  margin-bottom: 20rpx;
  display: flex;
  justify-content: space-between;
  align-items: center;
  
  .section-left {
    .section-label {
      font-size: 30rpx;
      color: #333;
    }
  }
  
  .section-right {
    display: flex;
    align-items: center;
    
    .avatar {
      width: 100rpx;
      height: 100rpx;
      border-radius: 50%;
      margin-right: 15rpx;
    }
    
    .arrow {
      font-size: 28rpx;
      color: #999;
    }
  }
}

.info-list {
  .info-item {
    padding: 30rpx;
    margin-bottom: 20rpx;
    display: flex;
    justify-content: space-between;
    align-items: center;
    
    .label {
      font-size: 30rpx;
      color: #333;
    }
    
    .value-area {
      display: flex;
      align-items: center;
      
      .value {
        font-size: 28rpx;
        color: #666;
        margin-right: 15rpx;
      }
      
      .arrow {
        font-size: 28rpx;
        color: #999;
      }
    }
  }
}

.action-section {
  margin-top: 40rpx;
  padding: 0 20rpx;
  
  .btn-action {
    width: 100%;
    padding: 28rpx 0;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    color: #fff;
    border-radius: 40rpx;
    font-size: 32rpx;
  }
}

.dialog {
  width: 600rpx;
  background: #fff;
  border-radius: 20rpx;
  overflow: hidden;
  
  .dialog-title {
    padding: 40rpx 30rpx 30rpx;
    text-align: center;
    font-size: 34rpx;
    font-weight: bold;
    color: #333;
    border-bottom: 1rpx solid #f0f0f0;
  }
  
  .dialog-content {
    padding: 40rpx 30rpx;
    
    .dialog-input {
      width: 100%;
      padding: 20rpx;
      background: #f5f5f5;
      border-radius: 8rpx;
      font-size: 28rpx;
    }
    
    radio-group {
      display: flex;
      justify-content: space-around;
      
      .radio-item {
        font-size: 30rpx;
        color: #333;
      }
    }
  }
  
  .dialog-footer {
    display: flex;
    border-top: 1rpx solid #f0f0f0;
    
    .btn-dialog {
      flex: 1;
      padding: 30rpx 0;
      font-size: 30rpx;
      border-radius: 0;
      
      &.btn-cancel {
        background: #fff;
        color: #666;
        border-right: 1rpx solid #f0f0f0;
      }
      
      &.btn-confirm {
        background: #fff;
        color: #667eea;
        font-weight: bold;
      }
    }
  }
}
</style>

