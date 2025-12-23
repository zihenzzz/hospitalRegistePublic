export default {
  // 后端API地址（开发环境）
  baseUrl: 'http://localhost:8080',
  
  // 生产环境API地址
  // baseUrl: 'https://your-domain.com/api',
  
  // 小程序AppID（需要在微信公众平台申请）
  appId: '',
  
  // 请求超时时间（毫秒）
  timeout: 30000,
  
  // 应用名称
  appName: '医院预约挂号',
  
  // 版本号
  version: '1.0.0',
  
  // 默认头像
  defaultAvatar: '/static/img/avatar.png',
  
  // 分页配置
  pageSize: 10,
  
  // 图片上传配置
  uploadUrl: '/common/upload',
  uploadMaxSize: 10 * 1024 * 1024, // 10MB
  
  // 缓存key配置
  cacheKeys: {
    token: 'TOKEN',
    userInfo: 'USER_INFO',
    visitors: 'VISITORS_LIST'
  }
}


