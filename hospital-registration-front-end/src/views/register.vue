<template>
  <div class="register-page">
    <!-- 动态背景 -->
    <div class="bg-animation">
      <canvas ref="bgCanvas"></canvas>
    </div>

    <!-- 顶部导航 -->
    <header class="register-header">
      <div class="header-container">
        <div class="logo" @click="goHome">
          <div class="logo-icon">
            <svg viewBox="0 0 40 40" fill="none">
              <circle cx="20" cy="20" r="18" stroke="currentColor" stroke-width="2" fill="none"/>
              <path d="M20 8v24M12 14h16M12 26h16" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
              <circle cx="20" cy="20" r="4" fill="currentColor"/>
            </svg>
          </div>
          <span>{{ title }}</span>
        </div>
        <div class="header-actions">
          <router-link to="/login" class="login-link">
            <i class="el-icon-user"></i> 登录
          </router-link>
        </div>
      </div>
    </header>

    <!-- 注册表单区域 -->
    <div class="register-container">
      <div class="register-card">
        <div class="card-header">
          <div class="header-icon">
            <svg viewBox="0 0 40 40" fill="none">
              <circle cx="20" cy="20" r="18" stroke="currentColor" stroke-width="2" fill="none"/>
              <path d="M20 8v24M12 14h16M12 26h16" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
              <circle cx="20" cy="20" r="4" fill="currentColor"/>
            </svg>
          </div>
          <h2>创建账户</h2>
          <p>加入智慧医疗平台，开启便捷就医之旅</p>
        </div>

        <el-form ref="registerForm" :model="registerForm" :rules="registerRules" class="register-form">
          <el-form-item prop="username">
            <el-input 
              v-model="registerForm.username" 
              placeholder="请输入账号"
              prefix-icon="el-icon-user"
              size="large"
            />
          </el-form-item>

          <el-form-item prop="password">
            <el-input
              v-model="registerForm.password"
              type="password"
              placeholder="请输入密码"
              prefix-icon="el-icon-lock"
              size="large"
              show-password
            />
          </el-form-item>

          <el-form-item prop="confirmPassword">
            <el-input
              v-model="registerForm.confirmPassword"
              type="password"
              placeholder="请确认密码"
              prefix-icon="el-icon-lock"
              size="large"
              show-password
              @keyup.enter.native="handleRegister"
            />
          </el-form-item>

          <el-form-item prop="code" v-if="captchaEnabled">
            <div class="code-row">
              <el-input
                v-model="registerForm.code"
                placeholder="验证码"
                prefix-icon="el-icon-key"
                size="large"
                @keyup.enter.native="handleRegister"
              />
              <img :src="codeUrl" @click="getCode" class="code-img" alt="验证码" />
            </div>
          </el-form-item>

          <el-button 
            type="primary" 
            :loading="loading" 
            class="submit-btn" 
            size="large"
            @click="handleRegister"
          >
            {{ loading ? '注册中...' : '立即注册' }}
          </el-button>
        </el-form>

        <div class="card-footer">
          <span>已有账户？</span>
          <router-link to="/login" class="login-link">立即登录</router-link>
        </div>

        <div class="agreement">
          <el-checkbox v-model="agreeTerms">
            我已阅读并同意
            <a href="javascript:;">《用户协议》</a>
            和
            <a href="javascript:;">《隐私政策》</a>
          </el-checkbox>
        </div>
      </div>

      <!-- 特性展示 -->
      <div class="features-panel">
        <h3>为什么选择我们？</h3>
        <div class="feature-list">
          <div class="feature-item">
            <div class="feature-icon"><i class="el-icon-time"></i></div>
            <div class="feature-text">
              <h4>便捷预约</h4>
              <p>随时随地在线预约挂号</p>
            </div>
          </div>
          <div class="feature-item">
            <div class="feature-icon"><i class="el-icon-s-data"></i></div>
            <div class="feature-text">
              <h4>智能推荐</h4>
              <p>AI智能分诊，精准匹配科室</p>
            </div>
          </div>
          <div class="feature-item">
            <div class="feature-icon"><i class="el-icon-document"></i></div>
            <div class="feature-text">
              <h4>电子病历</h4>
              <p>健康档案云端存储</p>
            </div>
          </div>
          <div class="feature-item">
            <div class="feature-icon"><i class="el-icon-service"></i></div>
            <div class="feature-text">
              <h4>贴心服务</h4>
              <p>7x24小时在线客服</p>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 底部 -->
    <footer class="register-footer">
      <span>{{ footerContent }}</span>
    </footer>
  </div>
</template>

<script>
import { getCodeImg, register } from "@/api/login"
import defaultSettings from '@/settings'

export default {
  name: "Register",
  data() {
    const equalToPassword = (rule, value, callback) => {
      if (this.registerForm.password !== value) {
        callback(new Error("两次输入的密码不一致"))
      } else {
        callback()
      }
    }
    return {
      title: process.env.VUE_APP_TITLE || '智慧医疗平台',
      footerContent: defaultSettings.footerContent,
      codeUrl: "",
      agreeTerms: false,
      registerForm: {
        username: "",
        password: "",
        confirmPassword: "",
        code: "",
        uuid: ""
      },
      registerRules: {
        username: [
          { required: true, trigger: "blur", message: "请输入您的账号" },
          { min: 2, max: 20, message: '用户账号长度必须介于 2 和 20 之间', trigger: 'blur' }
        ],
        password: [
          { required: true, trigger: "blur", message: "请输入您的密码" },
          { min: 5, max: 20, message: "用户密码长度必须介于 5 和 20 之间", trigger: "blur" },
          { pattern: /^[^<>"'|\\]+$/, message: "不能包含非法字符：< > \" ' \\ |", trigger: "blur" }
        ],
        confirmPassword: [
          { required: true, trigger: "blur", message: "请再次输入您的密码" },
          { required: true, validator: equalToPassword, trigger: "blur" }
        ],
        code: [{ required: true, trigger: "change", message: "请输入验证码" }]
      },
      loading: false,
      captchaEnabled: true
    }
  },
  created() {
    this.getCode()
  },
  mounted() {
    this.initBgCanvas()
  },
  beforeDestroy() {
    if (this.animationFrame) cancelAnimationFrame(this.animationFrame)
  },
  methods: {
    goHome() {
      this.$router.push('/login')
    },
    initBgCanvas() {
      const canvas = this.$refs.bgCanvas
      if (!canvas) return
      const ctx = canvas.getContext('2d')
      let width = canvas.width = window.innerWidth
      let height = canvas.height = window.innerHeight
      
      const particles = []
      for (let i = 0; i < 60; i++) {
        particles.push({
          x: Math.random() * width,
          y: Math.random() * height,
          radius: Math.random() * 2 + 1,
          vx: (Math.random() - 0.5) * 0.3,
          vy: (Math.random() - 0.5) * 0.3,
          alpha: Math.random() * 0.4 + 0.1
        })
      }
      
      const animate = () => {
        ctx.clearRect(0, 0, width, height)
        
        particles.forEach(p => {
          p.x += p.vx
          p.y += p.vy
          if (p.x < 0 || p.x > width) p.vx *= -1
          if (p.y < 0 || p.y > height) p.vy *= -1
          
          ctx.beginPath()
          ctx.arc(p.x, p.y, p.radius, 0, Math.PI * 2)
          ctx.fillStyle = `rgba(8, 145, 178, ${p.alpha})`
          ctx.fill()
        })
        
        particles.forEach((p1, i) => {
          particles.slice(i + 1).forEach(p2 => {
            const dx = p1.x - p2.x
            const dy = p1.y - p2.y
            const dist = Math.sqrt(dx * dx + dy * dy)
            if (dist < 120) {
              ctx.beginPath()
              ctx.moveTo(p1.x, p1.y)
              ctx.lineTo(p2.x, p2.y)
              ctx.strokeStyle = `rgba(8, 145, 178, ${0.08 * (1 - dist / 120)})`
              ctx.stroke()
            }
          })
        })
        
        this.animationFrame = requestAnimationFrame(animate)
      }
      animate()
      
      window.addEventListener('resize', () => {
        width = canvas.width = window.innerWidth
        height = canvas.height = window.innerHeight
      })
    },
    getCode() {
      getCodeImg().then(res => {
        this.captchaEnabled = res.captchaEnabled === undefined ? true : res.captchaEnabled
        if (this.captchaEnabled) {
          this.codeUrl = "data:image/gif;base64," + res.img
          this.registerForm.uuid = res.uuid
        }
      }).catch(() => {
        this.captchaEnabled = false
      })
    },
    handleRegister() {
      this.$refs.registerForm.validate(valid => {
        if (valid) {
          if (!this.agreeTerms) {
            this.$message.warning('请先阅读并同意用户协议和隐私政策')
            return
          }
          this.loading = true
          register(this.registerForm).then(res => {
            const username = this.registerForm.username
            this.$alert("恭喜您，账号 " + username + " 注册成功！", '注册成功', {
              type: 'success',
              confirmButtonText: '去登录'
            }).then(() => {
              this.$router.push("/login")
            }).catch(() => {})
          }).catch(() => {
            this.loading = false
            if (this.captchaEnabled) {
              this.getCode()
            }
          })
        }
      })
    }
  }
}
</script>


<style lang="scss" scoped>
$primary: #0891B2;
$primary-dark: #0E7490;
$dark: #0F172A;
$gray: #64748B;

.register-page {
  min-height: 100vh;
  background: linear-gradient(135deg, #f0f9ff 0%, #e0f2fe 50%, #f0fdfa 100%);
  position: relative;
  overflow: hidden;
}

.bg-animation {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 0;
  pointer-events: none;
  canvas { width: 100%; height: 100%; }
}

.register-header {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 100;
  padding: 16px 0;
  background: rgba(255, 255, 255, 0.9);
  backdrop-filter: blur(20px);
  box-shadow: 0 2px 20px rgba(0, 0, 0, 0.05);
  
  .header-container {
    max-width: 1200px;
    margin: 0 auto;
    padding: 0 40px;
    display: flex;
    align-items: center;
    justify-content: space-between;
  }
  
  .logo {
    display: flex;
    align-items: center;
    gap: 12px;
    color: $primary;
    font-size: 20px;
    font-weight: 700;
    cursor: pointer;
    transition: opacity 0.3s;
    &:hover { opacity: 0.8; }
    .logo-icon svg { width: 36px; height: 36px; }
  }
  
  .header-actions {
    .login-link {
      color: $primary;
      font-weight: 500;
      text-decoration: none;
      padding: 10px 24px;
      border: 2px solid $primary;
      border-radius: 24px;
      transition: all 0.3s;
      &:hover {
        background: $primary;
        color: #fff;
      }
    }
  }
}

.register-container {
  position: relative;
  z-index: 1;
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 100px 40px 60px;
  gap: 60px;
}

.register-card {
  background: #fff;
  border-radius: 24px;
  padding: 48px;
  width: 100%;
  max-width: 440px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.08);
  
  .card-header {
    text-align: center;
    margin-bottom: 36px;
    
    .header-icon {
      width: 64px;
      height: 64px;
      margin: 0 auto 20px;
      color: $primary;
      svg { width: 100%; height: 100%; }
    }
    
    h2 {
      font-size: 28px;
      font-weight: 700;
      color: $dark;
      margin: 0 0 12px;
    }
    
    p {
      font-size: 15px;
      color: $gray;
      margin: 0;
    }
  }
  
  .register-form {
    .el-form-item {
      margin-bottom: 20px;
    }
    
    ::v-deep .el-input__inner {
      height: 50px;
      border-radius: 12px;
      border: 2px solid #e2e8f0;
      font-size: 15px;
      transition: all 0.3s;
      &:focus {
        border-color: $primary;
        box-shadow: 0 0 0 4px rgba(8, 145, 178, 0.1);
      }
    }
    
    ::v-deep .el-input__prefix {
      left: 16px;
      color: $gray;
    }
    
    ::v-deep .el-input--prefix .el-input__inner {
      padding-left: 46px;
    }
    
    .code-row {
      display: flex;
      gap: 12px;
      
      .el-input { flex: 1; }
      
      .code-img {
        height: 50px;
        border-radius: 12px;
        cursor: pointer;
        border: 2px solid #e2e8f0;
        transition: border-color 0.3s;
        &:hover { border-color: $primary; }
      }
    }
    
    .submit-btn {
      width: 100%;
      height: 52px;
      border-radius: 12px;
      font-size: 16px;
      font-weight: 600;
      background: linear-gradient(135deg, $primary, #6366F1);
      border: none;
      margin-top: 8px;
      transition: all 0.3s;
      &:hover {
        transform: translateY(-2px);
        box-shadow: 0 8px 24px rgba(8, 145, 178, 0.3);
      }
    }
  }
  
  .card-footer {
    text-align: center;
    margin-top: 24px;
    font-size: 14px;
    color: $gray;
    
    .login-link {
      color: $primary;
      font-weight: 600;
      margin-left: 4px;
      text-decoration: none;
      &:hover { text-decoration: underline; }
    }
  }
  
  .agreement {
    margin-top: 20px;
    text-align: center;
    
    ::v-deep .el-checkbox__label {
      font-size: 13px;
      color: $gray;
    }
    
    a {
      color: $primary;
      text-decoration: none;
      &:hover { text-decoration: underline; }
    }
  }
}

.features-panel {
  max-width: 400px;
  
  h3 {
    font-size: 24px;
    font-weight: 700;
    color: $dark;
    margin: 0 0 32px;
  }
  
  .feature-list {
    display: flex;
    flex-direction: column;
    gap: 24px;
  }
  
  .feature-item {
    display: flex;
    align-items: flex-start;
    gap: 16px;
    padding: 20px;
    background: rgba(255, 255, 255, 0.8);
    border-radius: 16px;
    backdrop-filter: blur(10px);
    transition: all 0.3s;
    
    &:hover {
      transform: translateX(8px);
      background: #fff;
      box-shadow: 0 10px 30px rgba(0, 0, 0, 0.08);
    }
    
    .feature-icon {
      width: 48px;
      height: 48px;
      border-radius: 12px;
      background: linear-gradient(135deg, $primary, #6366F1);
      display: flex;
      align-items: center;
      justify-content: center;
      flex-shrink: 0;
      
      i {
        font-size: 22px;
        color: #fff;
      }
    }
    
    .feature-text {
      h4 {
        font-size: 16px;
        font-weight: 600;
        color: $dark;
        margin: 0 0 6px;
      }
      
      p {
        font-size: 14px;
        color: $gray;
        margin: 0;
      }
    }
  }
}

.register-footer {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 16px;
  text-align: center;
  font-size: 13px;
  color: $gray;
  background: rgba(255, 255, 255, 0.8);
  backdrop-filter: blur(10px);
}

@media (max-width: 900px) {
  .register-container {
    flex-direction: column;
    padding: 100px 20px 80px;
  }
  
  .features-panel {
    display: none;
  }
  
  .register-card {
    padding: 32px 24px;
  }
}
</style>
