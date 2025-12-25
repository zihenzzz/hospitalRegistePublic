import { get, post, put, del } from '@/utils/request.js'

/**
 * 医院业务相关API
 */

// ==================== 科室相关 ====================
// 获取科室列表
export const getSectionList = (params) => {
  return get('/system/section/list', params)
}

// 获取科室详情
export const getSectionDetail = (sectionId) => {
  return get(`/system/section/${sectionId}`)
}

// ==================== 排班相关 ====================
// 获取排班列表
export const getSchedulingList = (params) => {
  return get('/system/scheduling/list', params)
}

// 获取排班详情
export const getSchedulingDetail = (schedulingId) => {
  return get(`/system/scheduling/${schedulingId}`)
}

// ==================== 挂号相关 ====================
// 获取挂号列表
export const getRegistrationList = (params) => {
  return get('/system/registration/list', params)
}

// 获取挂号详情
export const getRegistrationDetail = (registrationId) => {
  return get(`/system/registration/${registrationId}`)
}

// 预约挂号
export const createRegistration = (data) => {
  return post('/system/registration', data)
}

// 取消挂号
export const cancelRegistration = (registrationId) => {
  return del(`/system/registration/${registrationId}`)
}

// 删除挂号（别名）
export const delRegistration = (registrationId) => {
  return del(`/system/registration/${registrationId}`)
}

// ==================== 门诊相关 ====================
// 获取门诊列表
export const getOutpatienList = (params) => {
  return get('/system/outpatien/list', params)
}

// 获取门诊详情
export const getOutpatienDetail = (outpatienId) => {
  return get(`/system/outpatien/${outpatienId}`)
}

// ==================== 充值相关 ====================
// 获取充值记录
export const getPayList = (params) => {
  return get('/system/pay/list', params)
}

// 创建充值订单
export const createPayOrder = (data) => {
  return post('/system/pay', data)
}

// ==================== 收藏相关 ====================
// 获取收藏列表
export const getCollectList = (params) => {
  return get('/system/collect/list', params)
}

// 添加收藏
export const addCollect = (data) => {
  return post('/system/collect', data)
}

// 取消收藏
export const removeCollect = (collectId) => {
  return del(`/system/collect/${collectId}`)
}

// ==================== 评价相关 ====================
// 获取评价列表
export const getEvaluateList = (params) => {
  return get('/system/evaluate/list', params)
}

// 提交评价
export const createEvaluate = (data) => {
  return post('/system/evaluate', data)
}

// 删除评价
export const deleteEvaluate = (evaluateId) => {
  return del(`/system/evaluate/${evaluateId}`)
}



// ==================== AI智能助手 ====================
// AI对话
export const aiChat = (data) => {
  return post('/ai/assistant/chat', data)
}

// 执行AI确认操作
export const aiExecuteAction = (data) => {
  return post('/ai/assistant/execute', data)
}
