<template>
  <view class="container">
    <!-- 顶部 -->
    <view class="header-section">
      <view class="header-bg"></view>
      <view class="header-content">
        <text class="header-title">就诊人管理</text>
        <text class="header-desc">管理您的就诊人信息</text>
      </view>
    </view>

    <!-- 就诊人列表 -->
    <view class="visitors-list">
      <view 
        class="visitor-card"
        v-for="(item, index) in visitorsList" 
        :key="index"
      >
        <view class="card-main">
          <view class="visitor-avatar">
            <text>{{ item.gender === '1' ? '👨' : '👩' }}</text>
          </view>
          <view class="visitor-info">
            <view class="info-top">
              <text class="visitor-name">{{ item.name }}</text>
              <view class="default-tag" v-if="item.isDefault">默认</view>
            </view>
            <view class="info-detail">
              <text>{{ item.gender === '1' ? '男' : '女' }} · {{ formatIdCard(item.idCard) }}</text>
            </view>
            <view class="info-phone">
              <u-icon name="phone" color="#64748b" size="24"></u-icon>
              <text>{{ item.phone }}</text>
            </view>
          </view>
        </view>
        <view class="card-actions">
          <view class="action-btn edit" @click="editVisitor(item)">
            <u-icon name="edit-pen" color="#0891b2" size="36"></u-icon>
          </view>
          <view class="action-btn delete" @click="deleteVisitor(item, index)">
            <u-icon name="trash" color="#ef4444" size="36"></u-icon>
          </view>
        </view>
      </view>
    </view>

    <!-- 添加按钮 -->
    <view class="add-section">
      <button class="btn-add" @click="addVisitor">
        <u-icon name="plus" color="#0891b2" size="36"></u-icon>
        <text>添加就诊人</text>
      </button>
    </view>

    <!-- 空状态 -->
    <view v-if="visitorsList.length === 0" class="empty-state">
      <view class="empty-icon">👥</view>
      <text class="empty-title">暂无就诊人</text>
      <text class="empty-desc">添加就诊人后可快速预约挂号</text>
      <button class="btn-add-primary" @click="addVisitor">添加就诊人</button>
    </view>

    <!-- 编辑/添加弹窗 -->
    <u-popup v-model="showDialog" mode="bottom" border-radius="24">
      <view class="dialog">
        <view class="dialog-header">
          <text class="dialog-title">{{ dialogTitle }}</text>
          <view class="dialog-close" @click="closeDialog">
            <u-icon name="close" color="#64748b" size="40"></u-icon>
          </view>
        </view>
        <view class="dialog-content">
          <view class="form-item">
            <text class="form-label">姓名</text>
            <input 
              class="form-input" 
              v-model="formData.name" 
              placeholder="请输入真实姓名"
            />
          </view>
          <view class="form-item">
            <text class="form-label">性别</text>
            <view class="gender-selector">
              <view 
                class="gender-item" 
                :class="{ 'active': formData.gender === '1' }"
                @click="formData.gender = '1'"
              >
                <text class="gender-emoji">👨</text>
                <text>男</text>
              </view>
              <view 
                class="gender-item" 
                :class="{ 'active': formData.gender === '2' }"
                @click="formData.gender = '2'"
              >
                <text class="gender-emoji">👩</text>
                <text>女</text>
              </view>
            </view>
          </view>
          <view class="form-item">
            <text class="form-label">手机号</text>
            <input 
              class="form-input" 
              v-model="formData.phone" 
              type="number"
              maxlength="11"
              placeholder="请输入手机号"
            />
          </view>
          <view class="form-item">
            <text class="form-label">身份证号</text>
            <input 
              class="form-input" 
              v-model="formData.idCard" 
              maxlength="18"
              placeholder="请输入身份证号"
            />
          </view>
          <view class="form-item-checkbox">
            <view 
              class="checkbox-wrap" 
              :class="{ 'checked': formData.isDefault }"
              @click="formData.isDefault = !formData.isDefault"
            >
              <u-icon v-if="formData.isDefault" name="checkmark" color="#fff" size="24"></u-icon>
            </view>
            <text>设为默认就诊人</text>
          </view>
        </view>
        <view class="dialog-footer">
          <button class="btn-save" @click="saveVisitor">保存</button>
        </view>
      </view>
    </u-popup>

    <!-- 底部安全间距 -->
    <view class="safe-bottom"></view>
  </view>
</template>

<script>
import config from '@/config/app.js'

export default {
  data() {
    return {
      visitorsList: [],
      showDialog: false,
      dialogTitle: '',
      isEdit: false,
      currentIndex: -1,
      formData: {
        name: '',
        gender: '1',
        phone: '',
        idCard: '',
        isDefault: false
      }
    }
  },
  onLoad() {
    this.loadVisitors()
  },
  methods: {
    loadVisitors() {
      const visitors = uni.getStorageSync(config.cacheKeys.visitors) || []
      this.visitorsList = visitors
    },
    
    saveVisitors() {
      uni.setStorageSync(config.cacheKeys.visitors, this.visitorsList)
    },
    
    addVisitor() {
      this.isEdit = false
      this.dialogTitle = '添加就诊人'
      this.formData = {
        name: '',
        gender: '1',
        phone: '',
        idCard: '',
        isDefault: this.visitorsList.length === 0
      }
      this.showDialog = true
    },
    
    editVisitor(item) {
      this.isEdit = true
      this.dialogTitle = '编辑就诊人'
      this.currentIndex = this.visitorsList.indexOf(item)
      this.formData = { ...item }
      this.showDialog = true
    },
    
    deleteVisitor(item, index) {
      uni.showModal({
        title: '删除就诊人',
        content: '确定要删除该就诊人吗？',
        confirmColor: '#0891b2',
        success: (res) => {
          if (res.confirm) {
            this.visitorsList.splice(index, 1)
            this.saveVisitors()
            uni.showToast({ title: '删除成功', icon: 'success' })
          }
        }
      })
    },
    
    saveVisitor() {
      if (!this.formData.name) {
        uni.showToast({ title: '请输入姓名', icon: 'none' })
        return
      }
      
      if (!this.formData.phone || !/^1[3-9]\d{9}$/.test(this.formData.phone)) {
        uni.showToast({ title: '请输入正确的手机号', icon: 'none' })
        return
      }
      
      if (!this.formData.idCard || !/^[1-9]\d{5}(18|19|20)\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\d|3[01])\d{3}[\dXx]$/.test(this.formData.idCard)) {
        uni.showToast({ title: '请输入正确的身份证号', icon: 'none' })
        return
      }
      
      if (this.formData.isDefault) {
        this.visitorsList.forEach(item => { item.isDefault = false })
      }
      
      if (this.isEdit) {
        this.visitorsList[this.currentIndex] = { ...this.formData }
      } else {
        this.visitorsList.push({ ...this.formData })
      }
      
      this.saveVisitors()
      this.closeDialog()
      uni.showToast({ title: this.isEdit ? '修改成功' : '添加成功', icon: 'success' })
    },
    
    closeDialog() {
      this.showDialog = false
    },
    
    formatIdCard(idCard) {
      if (!idCard) return ''
      return idCard.substring(0, 6) + '********' + idCard.substring(14)
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
    
    .header-title {
      font-size: 40rpx;
      font-weight: bold;
      color: #fff;
      display: block;
      margin-bottom: 8rpx;
    }
    
    .header-desc {
      font-size: 26rpx;
      color: rgba(255, 255, 255, 0.8);
    }
  }
}

.visitors-list {
  padding: 0 32rpx;
  margin-top: -20rpx;
  
  .visitor-card {
    background: #fff;
    border-radius: 24rpx;
    padding: 28rpx;
    margin-bottom: 20rpx;
    box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.04);
    display: flex;
    justify-content: space-between;
    align-items: center;
    
    .card-main {
      display: flex;
      align-items: center;
      flex: 1;
      
      .visitor-avatar {
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
      
      .visitor-info {
        flex: 1;
        
        .info-top {
          display: flex;
          align-items: center;
          gap: 12rpx;
          margin-bottom: 8rpx;
          
          .visitor-name {
            font-size: 32rpx;
            font-weight: bold;
            color: $dark;
          }
          
          .default-tag {
            padding: 4rpx 12rpx;
            background: linear-gradient(135deg, $primary, $primary-light);
            color: #fff;
            font-size: 22rpx;
            border-radius: 8rpx;
          }
        }
        
        .info-detail {
          font-size: 26rpx;
          color: $gray;
          margin-bottom: 8rpx;
        }
        
        .info-phone {
          display: flex;
          align-items: center;
          gap: 8rpx;
          
          text {
            font-size: 26rpx;
            color: $gray;
          }
        }
      }
    }
    
    .card-actions {
      display: flex;
      flex-direction: column;
      gap: 16rpx;
      
      .action-btn {
        width: 64rpx;
        height: 64rpx;
        border-radius: 16rpx;
        display: flex;
        align-items: center;
        justify-content: center;
        
        &.edit {
          background: #ecfeff;
        }
        
        &.delete {
          background: #fef2f2;
        }
      }
    }
  }
}

.add-section {
  padding: 24rpx 32rpx;
  
  .btn-add {
    width: 100%;
    height: 96rpx;
    background: #fff;
    border: 2rpx dashed $primary;
    border-radius: 24rpx;
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 12rpx;
    
    text {
      font-size: 30rpx;
      color: $primary;
      font-weight: 500;
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
    margin-bottom: 40rpx;
  }
  
  .btn-add-primary {
    padding: 0 60rpx;
    height: 88rpx;
    line-height: 88rpx;
    background: linear-gradient(135deg, $primary, $primary-light);
    color: #fff;
    border-radius: 44rpx;
    font-size: 30rpx;
    font-weight: 500;
    border: none;
  }
}

.dialog {
  background: #fff;
  border-radius: 24rpx 24rpx 0 0;
  
  .dialog-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 32rpx;
    border-bottom: 1rpx solid #f1f5f9;
    
    .dialog-title {
      font-size: 34rpx;
      font-weight: bold;
      color: $dark;
    }
    
    .dialog-close {
      width: 64rpx;
      height: 64rpx;
      display: flex;
      align-items: center;
      justify-content: center;
    }
  }
  
  .dialog-content {
    padding: 32rpx;
    max-height: 800rpx;
    overflow-y: auto;
    
    .form-item {
      margin-bottom: 28rpx;
      
      .form-label {
        display: block;
        font-size: 28rpx;
        font-weight: 500;
        color: $dark;
        margin-bottom: 16rpx;
      }
      
      .form-input {
        width: 100%;
        height: 96rpx;
        padding: 0 24rpx;
        background: #f8fafc;
        border: 2rpx solid #e2e8f0;
        border-radius: 16rpx;
        font-size: 30rpx;
        
        &:focus {
          border-color: $primary;
          background: #fff;
        }
      }
      
      .gender-selector {
        display: flex;
        gap: 24rpx;
        
        .gender-item {
          flex: 1;
          height: 96rpx;
          display: flex;
          align-items: center;
          justify-content: center;
          gap: 12rpx;
          background: #f8fafc;
          border: 2rpx solid #e2e8f0;
          border-radius: 16rpx;
          transition: all 0.3s;
          
          &.active {
            background: #ecfeff;
            border-color: $primary;
          }
          
          .gender-emoji {
            font-size: 32rpx;
          }
          
          text:last-child {
            font-size: 28rpx;
            color: $dark;
          }
        }
      }
    }
    
    .form-item-checkbox {
      display: flex;
      align-items: center;
      gap: 16rpx;
      
      .checkbox-wrap {
        width: 44rpx;
        height: 44rpx;
        border: 2rpx solid #e2e8f0;
        border-radius: 10rpx;
        display: flex;
        align-items: center;
        justify-content: center;
        transition: all 0.3s;
        
        &.checked {
          background: $primary;
          border-color: $primary;
        }
      }
      
      text {
        font-size: 28rpx;
        color: $gray;
      }
    }
  }
  
  .dialog-footer {
    padding: 24rpx 32rpx;
    padding-bottom: calc(24rpx + env(safe-area-inset-bottom));
    
    .btn-save {
      width: 100%;
      height: 96rpx;
      line-height: 96rpx;
      background: linear-gradient(135deg, $primary, $primary-light);
      color: #fff;
      border-radius: 48rpx;
      font-size: 32rpx;
      font-weight: 500;
      border: none;
    }
  }
}

.safe-bottom {
  height: calc(40rpx + env(safe-area-inset-bottom));
}
</style>
