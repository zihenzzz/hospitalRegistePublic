import request from '@/utils/request'

// 查询门诊列表
export function listOutpatien(query) {
  return request({
    url: '/system/outpatien/list',
    method: 'get',
    params: query
  })
}

// 查询门诊详细
export function getOutpatien(outpatienId) {
  return request({
    url: '/system/outpatien/' + outpatienId,
    method: 'get'
  })
}

// 新增门诊
export function addOutpatien(data) {
  return request({
    url: '/system/outpatien',
    method: 'post',
    data: data
  })
}

// 修改门诊
export function updateOutpatien(data) {
  return request({
    url: '/system/outpatien',
    method: 'put',
    data: data
  })
}

// 删除门诊
export function delOutpatien(outpatienId) {
  return request({
    url: '/system/outpatien/' + outpatienId,
    method: 'delete'
  })
}


