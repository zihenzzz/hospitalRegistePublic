import { get, post } from '@/utils/request.js'

/**
 * 用户相关API
 */

// 登录
export const login = (data) => {
  return post('/login', data)
}

// 注册
export const register = (data) => {
  return post('/register', data)
}

// 获取用户信息
export const getUserInfo = () => {
  return get('/getInfo')
}

// 获取验证码
export const getCaptcha = () => {
  return get('/captchaImage')
}

// 退出登录
export const logout = () => {
  return post('/logout')
}

// 修改用户信息
export const updateUserInfo = (data) => {
  return put('/system/user/profile', data)
}

// 修改密码
export const updatePassword = (data) => {
  return put('/system/user/profile/updatePwd', data)
}


