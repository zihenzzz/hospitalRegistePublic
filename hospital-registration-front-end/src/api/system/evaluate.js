import request from '@/utils/request'

// 查询评价列表
export function listEvaluate(query) {
  return request({
    url: '/system/evaluate/list',
    method: 'get',
    params: query
  })
}

// 查询评价详细
export function getEvaluate(evaluateId) {
  return request({
    url: '/system/evaluate/' + evaluateId,
    method: 'get'
  })
}

// 新增评价
export function addEvaluate(data) {
  return request({
    url: '/system/evaluate',
    method: 'post',
    data: data
  })
}

// 修改评价
export function updateEvaluate(data) {
  return request({
    url: '/system/evaluate',
    method: 'put',
    data: data
  })
}

// 删除评价
export function delEvaluate(evaluateId) {
  return request({
    url: '/system/evaluate/' + evaluateId,
    method: 'delete'
  })
}

