import request from '@/utils/request'

// AI对话
export function chat(data) {
  return request({
    url: '/ai/assistant/chat',
    method: 'post',
    data: data,
    timeout: 60000 // AI响应可能较慢，设置60秒超时
  })
}

// 执行确认操作
export function executeAction(data) {
  return request({
    url: '/ai/assistant/execute',
    method: 'post',
    data: data
  })
}
