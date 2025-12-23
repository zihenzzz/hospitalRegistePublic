import request from '@/utils/request'

// 查询科室列表
export function listSection(query) {
  return request({
    url: '/system/section/list',
    method: 'get',
    params: query
  })
}

// 查询科室详细
export function getSection(sectionId) {
  return request({
    url: '/system/section/' + sectionId,
    method: 'get'
  })
}

// 新增科室
export function addSection(data) {
  return request({
    url: '/system/section',
    method: 'post',
    data: data
  })
}

// 修改科室
export function updateSection(data) {
  return request({
    url: '/system/section',
    method: 'put',
    data: data
  })
}

// 删除科室
export function delSection(sectionId) {
  return request({
    url: '/system/section/' + sectionId,
    method: 'delete'
  })
}


