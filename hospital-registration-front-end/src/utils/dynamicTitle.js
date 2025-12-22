import defaultSettings from '@/settings'

/**
 * 动态修改标题
 */
export function useDynamicTitle() {
  if (defaultSettings.dynamicTitle) {
    const title = defaultSettings.title || '医院预约挂号管理系统'
    const docTitle = document.title.split('-')
    document.title = docTitle[docTitle.length - 1] + ' - ' + title
  }
}
