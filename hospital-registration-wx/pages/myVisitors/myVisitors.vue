<template>
  <view class="container">
    <!-- 就诊人列表 -->
    <view class="visitors-list">
      <view 
        class="visitor-item card"
        v-for="(item, index) in visitorsList" 
        :key="index"
      >
        <view class="visitor-info">
          <view class="name-tag">
            <text class="name">{{ item.name }}</text>
            <text class="tag" v-if="item.isDefault">默认</text>
          </view>
          <view class="detail-row">
            <text class="label">性别：</text>
            <text class="value">{{ item.gender === '1' ? '男' : '女' }}</text>
          </view>
          <view class="detail-row">
            <text class="label">手机号：</text>
            <text class="value">{{ item.phone }}</text>
          </view>
          <view class="detail-row">
            <text class="label">身份证号：</text>
            <text class="value">{{ formatIdCard(item.idCard) }}</text>
          </view>
        </view>
        
        <view class="visitor-actions">
          <button class="btn-edit" size="mini" @click="editVisitor(item)">
            <text class="icon">✎</text>
          </button>
          <button class="btn-delete" size="mini" @click="deleteVisitor(item, index)">
            <text class="icon">🗑</text>
          </button>
        </view>
      </view>
    </view>

    <!-- 添加按钮 -->
    <view class="add-visitor">
      <button class="btn-add" @click="addVisitor">
        <text class="icon">+</text>
        <text>添加就诊人</text>
      </button>
    </view>

    <!-- 空状态 -->
    <u-empty 
      v-if="visitorsList.length === 0" 
      text="暂无就诊人"
      mode="list"
    >
      <button slot="bottom" class="btn-add-empty" @click="addVisitor">
        添加就诊人
      </button>
    </u-empty>

    <!-- 编辑/添加弹窗 -->
    <u-popup v-model="showDialog" mode="center" border-radius="20">
      <view class="dialog">
        <view class="dialog-title">{{ dialogTitle }}</view>
        <view class="dialog-content">
          <view class="form-item">
            <text class="form-label">姓名</text>
            <input 
              class="form-input" 
              v-model="formData.name" 
              placeholder="请输入姓名"
            />
          </view>
          <view class="form-item">
            <text class="form-label">性别</text>
            <radio-group class="form-radio" @change="onGenderChange">
              <label>
                <radio value="1" :checked="formData.gender === '1'" />男
              </label>
              <label>
                <radio value="2" :checked="formData.gender === '2'" />女
              </label>
            </radio-group>
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
          <view class="form-item">
            <label class="form-checkbox">
              <checkbox :checked="formData.isDefault" @change="onDefaultChange" />
              <text>设为默认就诊人</text>
            </label>
          </view>
        </view>
        <view class="dialog-footer">
          <button class="btn-dialog btn-cancel" @click="closeDialog">取消</button>
          <button class="btn-dialog btn-confirm" @click="saveVisitor">确定</button>
        </view>
      </view>
    </u-popup>
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
    // 加载就诊人列表
    loadVisitors() {
      const visitors = uni.getStorageSync(config.cacheKeys.visitors) || []
      this.visitorsList = visitors
    },
    
    // 保存就诊人列表
    saveVisitors() {
      uni.setStorageSync(config.cacheKeys.visitors, this.visitorsList)
    },
    
    // 添加就诊人
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
    
    // 编辑就诊人
    editVisitor(item) {
      this.isEdit = true
      this.dialogTitle = '编辑就诊人'
      this.currentIndex = this.visitorsList.indexOf(item)
      this.formData = { ...item }
      this.showDialog = true
    },
    
    // 删除就诊人
    deleteVisitor(item, index) {
      uni.showModal({
        title: '提示',
        content: '确定要删除该就诊人吗？',
        success: (res) => {
          if (res.confirm) {
            this.visitorsList.splice(index, 1)
            this.saveVisitors()
            uni.showToast({
              title: '删除成功',
              icon: 'success'
            })
          }
        }
      })
    },
    
    // 保存就诊人
    saveVisitor() {
      // 验证
      if (!this.formData.name) {
        uni.showToast({
          title: '请输入姓名',
          icon: 'none'
        })
        return
      }
      
      if (!this.formData.phone || !/^1[3-9]\d{9}$/.test(this.formData.phone)) {
        uni.showToast({
          title: '请输入正确的手机号',
          icon: 'none'
        })
        return
      }
      
      if (!this.formData.idCard || !/^[1-9]\d{5}(18|19|20)\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\d|3[01])\d{3}[\dXx]$/.test(this.formData.idCard)) {
        uni.showToast({
          title: '请输入正确的身份证号',
          icon: 'none'
        })
        return
      }
      
      // 如果设为默认，其他都取消默认
      if (this.formData.isDefault) {
        this.visitorsList.forEach(item => {
          item.isDefault = false
        })
      }
      
      if (this.isEdit) {
        // 编辑
        this.visitorsList[this.currentIndex] = { ...this.formData }
      } else {
        // 添加
        this.visitorsList.push({ ...this.formData })
      }
      
      this.saveVisitors()
      this.closeDialog()
      
      uni.showToast({
        title: this.isEdit ? '修改成功' : '添加成功',
        icon: 'success'
      })
    },
    
    // 关闭弹窗
    closeDialog() {
      this.showDialog = false
    },
    
    // 性别选择
    onGenderChange(e) {
      this.formData.gender = e.detail.value
    },
    
    // 默认选择
    onDefaultChange(e) {
      this.formData.isDefault = e.detail.value.length > 0
    },
    
    // 格式化身份证号
    formatIdCard(idCard) {
      if (!idCard) return ''
      return idCard.substring(0, 6) + '********' + idCard.substring(14)
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

.visitors-list {
  .visitor-item {
    padding: 30rpx;
    margin-bottom: 20rpx;
    display: flex;
    justify-content: space-between;
    
    .visitor-info {
      flex: 1;
      
      .name-tag {
        display: flex;
        align-items: center;
        margin-bottom: 15rpx;
        
        .name {
          font-size: 32rpx;
          font-weight: bold;
          color: #333;
          margin-right: 15rpx;
        }
        
        .tag {
          padding: 4rpx 16rpx;
          background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
          color: #fff;
          font-size: 22rpx;
          border-radius: 20rpx;
        }
      }
      
      .detail-row {
        font-size: 26rpx;
        color: #666;
        margin-bottom: 8rpx;
        
        .label {
          color: #999;
        }
      }
    }
    
    .visitor-actions {
      display: flex;
      flex-direction: column;
      justify-content: center;
      
      button {
        width: 60rpx;
        height: 60rpx;
        padding: 0;
        margin-bottom: 20rpx;
        border-radius: 12rpx;
        display: flex;
        align-items: center;
        justify-content: center;
        
        .icon {
          font-size: 32rpx;
        }
        
        &.btn-edit {
          background: #e8f4ff;
          color: #409eff;
        }
        
        &.btn-delete {
          background: #fff1f0;
          color: #f56c6c;
          margin-bottom: 0;
        }
      }
    }
  }
}

.add-visitor {
  margin-top: 30rpx;
  
  .btn-add {
    width: 100%;
    padding: 30rpx 0;
    background: #fff;
    border: 2rpx dashed #667eea;
    border-radius: 16rpx;
    color: #667eea;
    font-size: 30rpx;
    display: flex;
    align-items: center;
    justify-content: center;
    
    .icon {
      font-size: 40rpx;
      margin-right: 10rpx;
    }
  }
}

.btn-add-empty {
  padding: 24rpx 60rpx;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
  border-radius: 40rpx;
  font-size: 28rpx;
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
    padding: 30rpx;
    max-height: 800rpx;
    overflow-y: auto;
    
    .form-item {
      margin-bottom: 30rpx;
      
      .form-label {
        display: block;
        font-size: 28rpx;
        color: #333;
        margin-bottom: 15rpx;
      }
      
      .form-input {
        width: 100%;
        padding: 20rpx;
        background: #f5f5f5;
        border-radius: 8rpx;
        font-size: 28rpx;
      }
      
      .form-radio {
        display: flex;
        
        label {
          margin-right: 40rpx;
          font-size: 28rpx;
          color: #333;
        }
      }
      
      .form-checkbox {
        display: flex;
        align-items: center;
        font-size: 28rpx;
        color: #333;
        
        checkbox {
          margin-right: 10rpx;
        }
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

