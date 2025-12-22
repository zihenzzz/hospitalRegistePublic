import request from '@/utils/request'

// 查询充值记录列表
export function listPay(query) {
  return request({
    url: '/system/pay/list',
    method: 'get',
    params: query
  })
}

// 查询充值记录详细
export function getPay(payId) {
  return request({
    url: '/system/pay/' + payId,
    method: 'get'
  })
}

// 新增充值记录
export function addPay(data) {
  return request({
    url: '/system/pay',
    method: 'post',
    data: data
  })
}

// 修改充值记录
export function updatePay(data) {
  return request({
    url: '/system/pay',
    method: 'put',
    data: data
  })
}

// 删除充值记录
export function delPay(payId) {
  return request({
    url: '/system/pay/' + payId,
    method: 'delete'
  })
}

