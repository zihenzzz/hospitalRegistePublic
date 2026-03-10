import request from '@/utils/request'

// 获取科室列表（使用现有接口）
export function getSectionList(query) {
  return request({
    url: '/system/section/list',
    method: 'get',
    params: query
  })
}

// 获取医生列表（使用用户列表接口，筛选医生）
export function getDoctorList(query) {
  return request({
    url: '/system/user/list',
    method: 'get',
    params: { ...query, userType: '02' }
  })
}

// 获取排班列表
export function getSchedulingList(query) {
  return request({
    url: '/system/scheduling/list',
    method: 'get',
    params: query
  })
}

// 创建挂号记录
export function createRegistration(data) {
  return request({
    url: '/system/registration',
    method: 'post',
    data: data
  })
}

// 获取我的挂号记录
export function getMyRegistrations(query) {
  return request({
    url: '/system/registration/list',
    method: 'get',
    params: query
  })
}

// 取消挂号
export function cancelRegistration(id) {
  return request({
    url: '/system/registration/' + id,
    method: 'delete'
  })
}

// 获取统计数据
export function getStatistics() {
  return request({
    url: '/system/statistics/overview',
    method: 'get'
  })
}

// 获取大屏统计数据
export function getDashboardData() {
  return request({
    url: '/system/statistics/dashboard',
    method: 'get'
  })
}
