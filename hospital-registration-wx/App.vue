<script>
export default {
  onLaunch: function() {
    console.log('App Launch')
    
    // 检查更新
    this.checkUpdate()
    
    // 获取系统信息
    this.getSystemInfo()
  },
  onShow: function() {
    console.log('App Show')
  },
  onHide: function() {
    console.log('App Hide')
  },
  methods: {
    // 检查小程序更新
    checkUpdate() {
      // #ifdef MP-WEIXIN
      const updateManager = uni.getUpdateManager()
      updateManager.onCheckForUpdate(function(res) {
        if (res.hasUpdate) {
          updateManager.onUpdateReady(function() {
            uni.showModal({
              title: '更新提示',
              content: '新版本已经准备好，是否重启应用？',
              success: function(res) {
                if (res.confirm) {
                  updateManager.applyUpdate()
                }
              }
            })
          })
          updateManager.onUpdateFailed(function() {
            uni.showModal({
              title: '更新提示',
              content: '新版本下载失败',
              showCancel: false
            })
          })
        }
      })
      // #endif
    },
    
    // 获取系统信息
    getSystemInfo() {
      uni.getSystemInfo({
        success: (res) => {
          this.$store.commit('app/SET_SYSTEM_INFO', res)
        }
      })
    }
  }
}
</script>

<style lang="scss">
/* 引入uView基础样式 */
/* 注意：如果index.scss为空，可以不引入，uView组件会自带样式 */

/* 全局样式 */
page {
  background-color: #f5f5f5;
  font-size: 28rpx;
  color: #333;
}

/* 清除按钮默认样式 */
button {
  margin: 0;
  padding: 0;
  border: none;
  background: none;
  line-height: normal;
}

button::after {
  border: none;
}

/* 通用样式 */
.container {
  width: 100%;
  min-height: 100vh;
  box-sizing: border-box;
}

.page-content {
  padding: 20rpx;
}

/* 通用间距 */
.mt-10 { margin-top: 10rpx; }
.mt-20 { margin-top: 20rpx; }
.mt-30 { margin-top: 30rpx; }
.mb-10 { margin-bottom: 10rpx; }
.mb-20 { margin-bottom: 20rpx; }
.mb-30 { margin-bottom: 30rpx; }

/* 通用卡片 */
.card {
  background: #fff;
  border-radius: 16rpx;
  padding: 24rpx;
  margin-bottom: 20rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
}

/* Flex布局 */
.flex {
  display: flex;
}

.flex-center {
  display: flex;
  align-items: center;
  justify-content: center;
}

.flex-between {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.flex-1 {
  flex: 1;
}
</style>

