import request from '@/utils/request'

// 查询排班列表
export function listScheduling(query) {
  return request({
    url: '/system/scheduling/list',
    method: 'get',
    params: query
  })
}

// 查询排班详细
export function getScheduling(schedulingId) {
  return request({
    url: '/system/scheduling/' + schedulingId,
    method: 'get'
  })
}

// 新增排班
export function addScheduling(data) {
  return request({
    url: '/system/scheduling',
    method: 'post',
    data: data
  })
}

// 修改排班
export function updateScheduling(data) {
  return request({
    url: '/system/scheduling',
    method: 'put',
    data: data
  })
}

// 删除排班
export function delScheduling(schedulingId) {
  return request({
    url: '/system/scheduling/' + schedulingId,
    method: 'delete'
  })
}

