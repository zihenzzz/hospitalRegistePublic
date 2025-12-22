import request from '@/utils/request'

// 查询挂号列表
export function listRegistration(query) {
  return request({
    url: '/system/registration/list',
    method: 'get',
    params: query
  })
}

// 查询挂号详细
export function getRegistration(registrationId) {
  return request({
    url: '/system/registration/' + registrationId,
    method: 'get'
  })
}

// 新增挂号
export function addRegistration(data) {
  return request({
    url: '/system/registration',
    method: 'post',
    data: data
  })
}

// 修改挂号
export function updateRegistration(data) {
  return request({
    url: '/system/registration',
    method: 'put',
    data: data
  })
}

// 删除挂号
export function delRegistration(registrationId) {
  return request({
    url: '/system/registration/' + registrationId,
    method: 'delete'
  })
}

