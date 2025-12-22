# 🏥 医院预约挂号小程序 - 运行指南

## 📱 项目概述

这是一个基于 **uni-app** 框架开发的微信小程序，用于医院预约挂号系统的移动端。

## 🔍 当前项目状态

### ⚠️ **重要提示**
项目源代码文件（`.vue`、`.js`、配置文件等）目前都是**空的**，需要重新填充内容才能运行。

### 📂 项目结构（已有框架）
```
hospital-registration-wx/
├── api/                  # API接口（空文件）
├── components/           # 自定义组件（空文件）
├── config/              # 配置文件（空文件）
├── pages/               # 页面文件（空文件）
├── store/               # Vuex状态管理（空文件）
├── utils/               # 工具函数（空文件）
├── uni_modules/         # uni-app插件（✅ uview-ui已完整）
├── package.json         # 空
├── manifest.json        # 空
└── pages.json           # 空
```

## 🚀 运行步骤

### 方案1：使用HBuilderX（推荐）

#### 1. 安装HBuilderX
下载地址：https://www.dcloud.io/hbuilderx.html
- 推荐下载"标准版"或"App开发版"

#### 2. 导入项目
```
1. 打开 HBuilderX
2. 菜单：文件 → 导入 → 从本地目录导入
3. 选择：D:\github-project\hospitalRegistePublic\hospital-registration-wx
```

#### 3. 安装依赖
```
右键项目根目录 → 使用命令行窗口打开所在目录
执行：npm install
```

#### 4. 运行到微信开发者工具
```
1. 菜单：运行 → 运行到小程序模拟器 → 微信开发者工具
2. 首次运行需要配置微信开发者工具路径
3. 项目会自动编译并在微信开发者工具中打开
```

---

### 方案2：命令行 + 微信开发者工具

#### 1. 安装依赖
```bash
cd hospital-registration-wx
npm install
```

#### 2. 安装 uni-app CLI（如果没有）
```bash
npm install -g @vue/cli
```

#### 3. 编译项目
```bash
# 开发模式
npm run dev:mp-weixin

# 生产模式
npm run build:mp-weixin
```

#### 4. 使用微信开发者工具打开
```
1. 下载微信开发者工具：https://developers.weixin.qq.com/miniprogram/dev/devtools/download.html
2. 打开微信开发者工具
3. 导入项目：选择 hospital-registration-wx/unpackage/dist/dev/mp-weixin 目录
4. 填写AppID（测试可以选择"测试号"）
```

---

## 🔧 必要的配置文件

### 1. package.json（需要创建）
```json
{
  "name": "hospital-registration-wx",
  "version": "1.0.0",
  "description": "医院预约挂号微信小程序",
  "main": "main.js",
  "scripts": {
    "dev:mp-weixin": "uni build -p mp-weixin --watch",
    "build:mp-weixin": "uni build -p mp-weixin"
  },
  "dependencies": {
    "@dcloudio/uni-app": "^3.0.0",
    "uview-ui": "^2.0.0",
    "vuex": "^3.6.2"
  },
  "devDependencies": {
    "@dcloudio/uni-cli-shared": "^3.0.0",
    "@dcloudio/vite-plugin-uni": "^3.0.0"
  }
}
```

### 2. manifest.json（需要创建）
```json
{
  "name": "医院预约挂号",
  "appid": "",
  "description": "医院预约挂号小程序",
  "versionName": "1.0.0",
  "versionCode": "100",
  "transformPx": false,
  "mp-weixin": {
    "appid": "",
    "setting": {
      "urlCheck": false,
      "es6": true,
      "postcss": true,
      "minified": true
    },
    "usingComponents": true
  },
  "uniStatistics": {
    "enable": false
  }
}
```

### 3. pages.json（需要创建）
```json
{
  "pages": [
    {
      "path": "pages/index/index",
      "style": {
        "navigationBarTitleText": "首页"
      }
    },
    {
      "path": "pages/my/my",
      "style": {
        "navigationBarTitleText": "我的"
      }
    }
  ],
  "tabBar": {
    "color": "#7A7E83",
    "selectedColor": "#3cc51f",
    "borderStyle": "black",
    "backgroundColor": "#ffffff",
    "list": [
      {
        "pagePath": "pages/index/index",
        "iconPath": "static/img/home.png",
        "selectedIconPath": "static/img/home-active.png",
        "text": "首页"
      },
      {
        "pagePath": "pages/my/my",
        "iconPath": "static/img/my.png",
        "selectedIconPath": "static/img/my-active.png",
        "text": "我的"
      }
    ]
  },
  "globalStyle": {
    "navigationBarTextStyle": "black",
    "navigationBarTitleText": "医院预约挂号",
    "navigationBarBackgroundColor": "#F8F8F8",
    "backgroundColor": "#F8F8F8"
  }
}
```

### 4. config/app.js（需要创建）
```javascript
export default {
  // 后端API地址
  baseUrl: 'http://localhost:8080',
  
  // 小程序AppID
  appId: '',
  
  // 超时时间
  timeout: 30000,
  
  // 应用名称
  appName: '医院预约挂号'
}
```

---

## 📋 核心功能页面（需要开发）

根据项目结构，小程序应包含以下功能：

### 🏠 首页 (pages/index/index.vue)
- 医院信息展示
- 快速挂号入口
- 门诊预约
- 核酸检测预约
- 就诊人管理

### 📱 功能页面
1. **挂号相关**
   - 挂号记录 (registrationRecord)
   - 挂号详情 (registrationDetail)
   - 排班信息 (schedulingRecord)

2. **就诊相关**
   - 就诊信息 (subscribeInfo)
   - 就诊人管理 (myVisitors)

3. **支付相关**
   - 充值记录 (payRecord)
   - 订单支付状态 (orderPayStatus)
   - 核酸支付状态 (nucleicAcidPayStatus)

4. **个人中心**
   - 我的 (my)
   - 个人信息 (user/info)

---

## 🔗 API接口对接

小程序需要对接后端API：

### 配置请求拦截器 (utils/request.js)
```javascript
import config from '@/config/app.js'

// 请求拦截
const request = (options) => {
  return new Promise((resolve, reject) => {
    uni.request({
      url: config.baseUrl + options.url,
      method: options.method || 'GET',
      data: options.data || {},
      header: {
        'Content-Type': 'application/json',
        'Authorization': uni.getStorageSync('token') || ''
      },
      timeout: config.timeout,
      success: (res) => {
        if (res.statusCode === 200) {
          resolve(res.data)
        } else {
          reject(res)
        }
      },
      fail: (err) => {
        reject(err)
      }
    })
  })
}

export default request
```

### 主要接口（与后端对应）
```
POST /login           # 登录
POST /register        # 注册
GET  /getInfo         # 获取用户信息
GET  /captchaImage    # 验证码

GET  /system/section/list       # 科室列表
GET  /system/scheduling/list    # 排班列表
POST /system/registration       # 预约挂号
GET  /system/registration/list  # 挂号记录
```

---

## 🎯 开发建议

### 方式1：从头开发（耗时较长）
参考已有的目录结构，逐个创建页面和功能

### 方式2：使用uni-app模板（推荐）
1. 从 uni-app 插件市场下载医疗类模板
2. 根据当前项目结构调整
3. 对接现有后端API

### 方式3：恢复原始代码
如果有代码备份，恢复原始文件

---

## 📱 测试与发布

### 1. 本地测试
- 使用微信开发者工具的"编译"功能
- 测试各个页面和功能
- 检查API接口调用

### 2. 真机测试
- 点击"预览"生成二维码
- 使用微信扫码在真机上测试

### 3. 发布上线
```
1. 点击"上传"按钮
2. 填写版本号和备注
3. 登录微信公众平台提交审核
4. 审核通过后发布
```

---

## ❗ 当前问题与解决

### 问题：所有源代码文件为空

**原因**：与前端、后端项目一样，源代码文件在克隆时就是空的

**解决方案**：
1. **短期**：如果只需要后端和Web前端，可以暂时忽略小程序
2. **长期**：需要重新开发小程序页面和逻辑

**优先级建议**：
- 先确保后端API和Web前端完全正常
- 小程序可以作为后续扩展功能

---

## 📞 需要帮助？

如果需要开发小程序：
1. 可以先完成Web前端（已95%完成）
2. 小程序可以复用大部分API接口
3. 页面逻辑类似，开发相对简单

**建议步骤**：
1. ✅ 完成后端API（已完成）
2. ✅ 完成Web前端（已95%完成）
3. 📝 开发微信小程序（待定）

---

## 📚 相关文档

- uni-app官方文档：https://uniapp.dcloud.net.cn/
- 微信小程序文档：https://developers.weixin.qq.com/miniprogram/dev/framework/
- uView UI文档：https://www.uviewui.com/

---

**当前建议：先专注于完成Web前端和后端的集成测试，小程序可以作为二期功能开发。**

