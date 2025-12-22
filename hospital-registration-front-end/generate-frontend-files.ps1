# 医院预约挂号系统 - 前端文件生成脚本

Write-Host "开始生成前端必需文件..." -ForegroundColor Green

# 1. 创建API文件
Write-Host "创建API文件..." -ForegroundColor Yellow

# login.js
$loginApiContent = @"
import request from '@/utils/request'

// 登录方法
export function login(username, password, code, uuid) {
  const data = {
    username,
    password,
    code,
    uuid
  }
  return request({
    url: '/login',
    headers: {
      isToken: false
    },
    method: 'post',
    data: data
  })
}

// 注册方法
export function register(data) {
  return request({
    url: '/register',
    headers: {
      isToken: false
    },
    method: 'post',
    data: data
  })
}

// 获取用户详细信息
export function getInfo() {
  return request({
    url: '/getInfo',
    method: 'get'
  })
}

// 退出方法
export function logout() {
  return request({
    url: '/logout',
    method: 'post'
  })
}

// 获取验证码
export function getCodeImg() {
  return request({
    url: '/captchaImage',
    headers: {
      isToken: false
    },
    method: 'get',
    timeout: 20000
  })
}
"@
Set-Content -Path "src\api\login.js" -Value $loginApiContent -Encoding UTF8

# menu.js
$menuApiContent = @"
import request from '@/utils/request'

// 获取路由
export const getRouters = () => {
  return request({
    url: '/getRouters',
    method: 'get'
  })
}
"@
Set-Content -Path "src\api\menu.js" -Value $menuApiContent -Encoding UTF8

# system/dict/data.js
New-Item -ItemType Directory -Force -Path "src\api\system\dict" | Out-Null
$dictDataContent = @"
import request from '@/utils/request'

// 查询字典数据列表
export function listData(query) {
  return request({
    url: '/system/dict/data/list',
    method: 'get',
    params: query
  })
}

// 查询字典数据详细
export function getData(dictCode) {
  return request({
    url: '/system/dict/data/' + dictCode,
    method: 'get'
  })
}

// 根据字典类型查询字典数据信息
export function getDicts(dictType) {
  return request({
    url: '/system/dict/data/type/' + dictType,
    method: 'get'
  })
}

// 新增字典数据
export function addData(data) {
  return request({
    url: '/system/dict/data',
    method: 'post',
    data: data
  })
}

// 修改字典数据
export function updateData(data) {
  return request({
    url: '/system/dict/data',
    method: 'put',
    data: data
  })
}

// 删除字典数据
export function delData(dictCode) {
  return request({
    url: '/system/dict/data/' + dictCode,
    method: 'delete'
  })
}
"@
Set-Content -Path "src\api\system\dict\data.js" -Value $dictDataContent -Encoding UTF8

# system/config.js
$configApiContent = @"
import request from '@/utils/request'

// 根据参数键名查询参数值
export function getConfigKey(configKey) {
  return request({
    url: '/system/config/configKey/' + configKey,
    method: 'get'
  })
}
"@
Set-Content -Path "src\api\system\config.js" -Value $configApiContent -Encoding UTF8

Write-Host "API文件创建完成" -ForegroundColor Green

# 2. 创建插件文件
Write-Host "创建插件文件..." -ForegroundColor Yellow

# plugins/index.js
$pluginsIndexContent = @"
import tab from './tab'
import auth from './auth'
import cache from './cache'
import modal from './modal'
import download from './download'

export default {
  install(Vue) {
    // 页签操作
    Vue.prototype.`$tab = tab
    // 认证对象
    Vue.prototype.`$auth = auth
    // 缓存对象
    Vue.prototype.`$cache = cache
    // 模态框对象
    Vue.prototype.`$modal = modal
    // 下载文件
    Vue.prototype.`$download = download
  }
}
"@
Set-Content -Path "src\plugins\index.js" -Value $pluginsIndexContent -Encoding UTF8

# plugins/auth.js
$authPluginContent = @"
export default {
  // 验证用户是否具备某权限
  hasPermi(permission) {
    const all_permission = "*:*:*";
    const permissions = this.`$store.getters && this.`$store.getters.permissions
    if (permission && permission.length > 0) {
      return permissions.some(v => {
        return all_permission === v || v === permission
      })
    } else {
      return false
    }
  },
  // 验证用户是否含有指定权限,只需包含其中一个
  hasPermiOr(permissions) {
    const all_permission = "*:*:*";
    const permissionFlag = this.`$store.getters && this.`$store.getters.permissions
    if (permissions && permissions.length > 0) {
      return permissions.some(item => {
        return all_permission === item || permissionFlag.indexOf(item) !== -1
      })
    } else {
      return false
    }
  },
  // 验证用户是否含有指定角色
  hasRole(role) {
    const super_admin = "admin";
    const roles = this.`$store.getters && this.`$store.getters.roles
    if (role && role.length > 0) {
      return roles.some(v => {
        return super_admin === v || v === role
      })
    } else {
      return false
    }
  },
  // 验证用户是否含有指定角色,只需包含其中一个
  hasRoleOr(roles) {
    const super_admin = "admin";
    const roleFlag = this.`$store.getters && this.`$store.getters.roles
    if (roles && roles.length > 0) {
      return roles.some(item => {
        return super_admin === item || roleFlag.indexOf(item) !== -1
      })
    } else {
      return false
    }
  }
}
"@
Set-Content -Path "src\plugins\auth.js" -Value $authPluginContent -Encoding UTF8

# plugins/cache.js
$cachePluginContent = @"
const sessionCache = {
  set(key, value) {
    if (!sessionStorage) {
      return
    }
    if (key != null && value != null) {
      sessionStorage.setItem(key, value)
    }
  },
  get(key) {
    if (!sessionStorage) {
      return null
    }
    if (key == null) {
      return null
    }
    return sessionStorage.getItem(key)
  },
  setJSON(key, jsonValue) {
    if (jsonValue != null) {
      this.set(key, JSON.stringify(jsonValue))
    }
  },
  getJSON(key) {
    const value = this.get(key)
    if (value != null) {
      return JSON.parse(value)
    }
  },
  remove(key) {
    sessionStorage.removeItem(key);
  }
}
const localCache = {
  set(key, value) {
    if (!localStorage) {
      return
    }
    if (key != null && value != null) {
      localStorage.setItem(key, value)
    }
  },
  get(key) {
    if (!localStorage) {
      return null
    }
    if (key == null) {
      return null
    }
    return localStorage.getItem(key)
  },
  setJSON(key, jsonValue) {
    if (jsonValue != null) {
      this.set(key, JSON.stringify(jsonValue))
    }
  },
  getJSON(key) {
    const value = this.get(key)
    if (value != null) {
      return JSON.parse(value)
    }
  },
  remove(key) {
    localStorage.removeItem(key);
  }
}

export default {
  /**
   * 会话级缓存
   */
  session: sessionCache,
  /**
   * 本地缓存
   */
  local: localCache
}
"@
Set-Content -Path "src\plugins\cache.js" -Value $cachePluginContent -Encoding UTF8

Write-Host "插件文件创建完成" -ForegroundColor Green

Write-Host "所有必需文件生成完成！" -ForegroundColor Green
"@
</invoke>
