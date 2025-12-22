import config from '@/config/app.js'

/**
 * 封装的request请求
 */
const request = (options = {}) => {
  return new Promise((resolve, reject) => {
    // 获取token
    const token = uni.getStorageSync(config.cacheKeys.token)
    
    // 请求头
    const header = {
      'Content-Type': 'application/json',
      ...options.header
    }
    
    // 如果有token，添加到请求头
    if (token) {
      header['Authorization'] = 'Bearer ' + token
    }
    
    // 发起请求
    uni.request({
      url: config.baseUrl + options.url,
      method: options.method || 'GET',
      data: options.data || {},
      header: header,
      timeout: options.timeout || config.timeout,
      success: (res) => {
        // 请求成功
        if (res.statusCode === 200) {
          const data = res.data
          
          // 根据后端返回的code判断
          if (data.code === 200) {
            resolve(data)
          } else if (data.code === 401) {
            // token过期，跳转登录
            uni.showToast({
              title: '登录已过期',
              icon: 'none'
            })
            uni.removeStorageSync(config.cacheKeys.token)
            uni.removeStorageSync(config.cacheKeys.userInfo)
            setTimeout(() => {
              uni.reLaunch({
                url: '/pages/login/login'
              })
            }, 1500)
            reject(data)
          } else {
            // 其他错误
            uni.showToast({
              title: data.msg || '请求失败',
              icon: 'none'
            })
            reject(data)
          }
        } else {
          // HTTP状态码错误
          uni.showToast({
            title: '网络请求错误',
            icon: 'none'
          })
          reject(res)
        }
      },
      fail: (err) => {
        // 请求失败
        uni.showToast({
          title: '网络连接失败',
          icon: 'none'
        })
        reject(err)
      }
    })
  })
}

/**
 * GET请求
 */
export const get = (url, data = {}) => {
  return request({
    url,
    method: 'GET',
    data
  })
}

/**
 * POST请求
 */
export const post = (url, data = {}) => {
  return request({
    url,
    method: 'POST',
    data
  })
}

/**
 * PUT请求
 */
export const put = (url, data = {}) => {
  return request({
    url,
    method: 'PUT',
    data
  })
}

/**
 * DELETE请求
 */
export const del = (url, data = {}) => {
  return request({
    url,
    method: 'DELETE',
    data
  })
}

export default request

