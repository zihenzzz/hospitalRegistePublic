<template>
  <div class="landing-page">
    <!-- 动态DNA背景 -->
    <div class="dna-background">
      <canvas ref="dnaCanvas"></canvas>
    </div>

    <!-- 顶部导航 -->
    <header class="landing-header" :class="{ scrolled: isScrolled }">
      <div class="header-container">
        <div class="logo">
          <div class="logo-icon">
            <svg viewBox="0 0 40 40" fill="none">
              <circle cx="20" cy="20" r="18" stroke="currentColor" stroke-width="2" fill="none"/>
              <path d="M20 8v24M12 14h16M12 26h16" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
              <circle cx="20" cy="20" r="4" fill="currentColor"/>
            </svg>
          </div>
          <span>{{ title }}</span>
        </div>
        <nav class="nav-links">
          <a @click="scrollTo('hero')">首页</a>
          <a @click="scrollTo('features')">功能特性</a>
          <a @click="scrollTo('services')">服务内容</a>
          <a @click="scrollTo('cases')">成功案例</a>
          <a @click="scrollTo('about')">关于我们</a>
        </nav>
        <div class="header-actions">
          <el-button type="text" class="login-btn-text" @click="showLogin = true">
            <i class="el-icon-user"></i> 登录
          </el-button>
          <el-button type="primary" round @click="showLogin = true">
            免费试用
          </el-button>
        </div>
        <div class="mobile-menu" @click="mobileMenuOpen = !mobileMenuOpen">
          <i :class="mobileMenuOpen ? 'el-icon-close' : 'el-icon-s-fold'"></i>
        </div>
      </div>
      <!-- 移动端菜单 -->
      <transition name="slide-down">
        <div class="mobile-nav" v-if="mobileMenuOpen">
          <a @click="scrollTo('features'); mobileMenuOpen = false">功能特性</a>
          <a @click="scrollTo('services'); mobileMenuOpen = false">服务内容</a>
          <a @click="scrollTo('cases'); mobileMenuOpen = false">成功案例</a>
          <a @click="scrollTo('about'); mobileMenuOpen = false">关于我们</a>
          <el-button type="primary" round @click="showLogin = true; mobileMenuOpen = false">登录系统</el-button>
        </div>
      </transition>
    </header>

    <!-- Hero 区域 -->
    <section id="hero" class="hero-section">
      <div class="hero-particles"></div>
      <div class="hero-content">
        <div class="hero-badge">
          <i class="el-icon-star-on"></i> 医疗信息化领导品牌
        </div>
        <h1 class="hero-title">
          <span class="gradient-text">智慧医疗</span>
          <br>数字化管理平台
        </h1>
        <p class="hero-desc">
          融合AI人工智能、大数据分析、云计算技术，为医疗机构提供全方位数字化转型解决方案，
          助力医院提升运营效率，优化患者就医体验
        </p>
        <div class="hero-buttons">
          <el-button type="primary" size="large" round @click="showLogin = true">
            <i class="el-icon-video-play"></i> 立即体验
          </el-button>
          <el-button size="large" round plain @click="scrollTo('features')">
            了解更多 <i class="el-icon-arrow-down"></i>
          </el-button>
        </div>
        <div class="hero-stats">
          <div class="stat-item" v-for="(stat, i) in heroStats" :key="i">
            <span class="stat-value">
              <span class="counter" :data-target="stat.value">{{ stat.display }}</span>
              <span class="stat-suffix">{{ stat.suffix }}</span>
            </span>
            <span class="stat-label">{{ stat.label }}</span>
          </div>
        </div>
      </div>
      <div class="hero-visual">
        <div class="floating-cards">
          <div class="float-card card-1">
            <i class="el-icon-user"></i>
            <span>患者管理</span>
          </div>
          <div class="float-card card-2">
            <i class="el-icon-date"></i>
            <span>智能预约</span>
          </div>
          <div class="float-card card-3">
            <i class="el-icon-s-data"></i>
            <span>数据分析</span>
          </div>
          <div class="float-card card-4">
            <i class="el-icon-first-aid-kit"></i>
            <span>电子病历</span>
          </div>
        </div>
      </div>
      <div class="scroll-indicator" @click="scrollTo('features')">
        <div class="mouse">
          <div class="wheel"></div>
        </div>
        <span>向下滚动探索更多</span>
      </div>
    </section>

    <!-- 功能特性 -->
    <section id="features" class="features-section">
      <div class="section-container">
        <div class="section-header">
          <span class="section-tag">核心功能</span>
          <h2>全方位医疗信息化解决方案</h2>
          <p>覆盖医院运营管理全流程，助力医疗机构数字化转型</p>
        </div>
        <div class="features-grid">
          <div class="feature-card" v-for="(feature, index) in features" :key="index" 
               :class="{ 'featured': feature.featured }" @mouseenter="activeFeature = index">
            <div class="feature-icon" :style="{ background: feature.gradient }">
              <i :class="feature.icon"></i>
            </div>
            <div class="feature-content">
              <h3>{{ feature.title }}</h3>
              <p>{{ feature.desc }}</p>
              <ul class="feature-list">
                <li v-for="(item, i) in feature.items" :key="i">
                  <i class="el-icon-check"></i> {{ item }}
                </li>
              </ul>
            </div>
            <div class="feature-hover-effect"></div>
          </div>
        </div>
      </div>
    </section>

    <!-- 服务流程 -->
    <section id="services" class="services-section">
      <div class="section-container">
        <div class="section-header light">
          <span class="section-tag">服务流程</span>
          <h2>一站式医疗服务闭环</h2>
          <p>从预约挂号到诊后随访，打造完整的医疗服务体验</p>
        </div>
        <div class="service-flow">
          <div class="flow-line"></div>
          <div class="flow-item" v-for="(service, index) in services" :key="index">
            <div class="flow-number">{{ String(index + 1).padStart(2, '0') }}</div>
            <div class="flow-icon"><i :class="service.icon"></i></div>
            <h4>{{ service.title }}</h4>
            <p>{{ service.desc }}</p>
          </div>
        </div>
      </div>
    </section>

    <!-- 技术优势 -->
    <section class="tech-section">
      <div class="section-container">
        <div class="tech-content">
          <div class="tech-text">
            <span class="section-tag">技术优势</span>
            <h2>领先的技术架构</h2>
            <p>采用微服务架构，支持高并发、高可用，确保系统稳定运行</p>
            <div class="tech-list">
              <div class="tech-item" v-for="(tech, i) in techItems" :key="i">
                <div class="tech-icon"><i :class="tech.icon"></i></div>
                <div class="tech-info">
                  <h5>{{ tech.title }}</h5>
                  <span>{{ tech.desc }}</span>
                </div>
              </div>
            </div>
          </div>
          <div class="tech-visual">
            <div class="tech-circle">
              <div class="circle-ring ring-1"></div>
              <div class="circle-ring ring-2"></div>
              <div class="circle-ring ring-3"></div>
              <div class="circle-center">
                <i class="el-icon-cpu"></i>
                <span>AI 核心</span>
              </div>
              <div class="orbit-item" v-for="(item, i) in orbitItems" :key="i" :style="{ '--delay': i * 0.5 + 's' }">
                <i :class="item.icon"></i>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- 成功案例 -->
    <section id="cases" class="cases-section">
      <div class="section-container">
        <div class="section-header">
          <span class="section-tag">成功案例</span>
          <h2>他们都在使用</h2>
          <p>已服务全国500+医疗机构，获得广泛好评</p>
        </div>
        <div class="cases-slider">
          <div class="case-card" v-for="(caseItem, index) in cases" :key="index">
            <div class="case-logo">{{ caseItem.name.charAt(0) }}</div>
            <div class="case-info">
              <h4>{{ caseItem.name }}</h4>
              <span class="case-type">{{ caseItem.type }}</span>
              <p>"{{ caseItem.quote }}"</p>
              <div class="case-stats">
                <div class="case-stat">
                  <span class="value">{{ caseItem.efficiency }}</span>
                  <span class="label">效率提升</span>
                </div>
                <div class="case-stat">
                  <span class="value">{{ caseItem.satisfaction }}</span>
                  <span class="label">满意度</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- 关于我们 -->
    <section id="about" class="about-section">
      <div class="section-container">
        <div class="about-grid">
          <div class="about-main">
            <span class="section-tag">关于我们</span>
            <h2>专注医疗信息化<br>十余年深耕</h2>
            <p>我们是一家专注于医疗信息化领域的高新技术企业，致力于为医疗机构提供专业、可靠、高效的数字化解决方案。</p>
            <div class="about-numbers">
              <div class="number-item" v-for="(num, i) in aboutNumbers" :key="i">
                <span class="num">{{ num.value }}</span>
                <span class="label">{{ num.label }}</span>
              </div>
            </div>
            <el-button type="primary" round size="large" @click="showLogin = true">
              开始使用 <i class="el-icon-arrow-right"></i>
            </el-button>
          </div>
          <div class="about-cards">
            <div class="about-card" v-for="(card, i) in aboutCards" :key="i">
              <i :class="card.icon"></i>
              <h4>{{ card.title }}</h4>
              <p>{{ card.desc }}</p>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- CTA -->
    <section class="cta-section">
      <div class="cta-bg"></div>
      <div class="section-container">
        <div class="cta-content">
          <h2>准备好开启数字化医疗之旅了吗？</h2>
          <p>立即注册，免费试用30天，体验智慧医疗带来的改变</p>
          <div class="cta-buttons">
            <el-button type="primary" size="large" round @click="showLogin = true">
              免费试用
            </el-button>
            <el-button size="large" round plain>
              <i class="el-icon-phone"></i> 联系我们
            </el-button>
          </div>
        </div>
      </div>
    </section>

    <!-- 底部 -->
    <footer class="landing-footer">
      <div class="footer-main">
        <div class="footer-brand">
          <div class="logo">
            <svg viewBox="0 0 40 40" fill="none">
              <circle cx="20" cy="20" r="18" stroke="currentColor" stroke-width="2" fill="none"/>
              <path d="M20 8v24M12 14h16M12 26h16" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
              <circle cx="20" cy="20" r="4" fill="currentColor"/>
            </svg>
            <span>{{ title }}</span>
          </div>
          <p>专业的医疗信息化解决方案提供商，助力医疗机构数字化转型</p>
          <div class="social-links">
            <a href="#"><i class="el-icon-chat-dot-round"></i></a>
            <a href="#"><i class="el-icon-phone-outline"></i></a>
            <a href="#"><i class="el-icon-message"></i></a>
          </div>
        </div>
        <div class="footer-links">
          <div class="link-group">
            <h4>产品服务</h4>
            <a href="#">预约挂号系统</a>
            <a href="#">电子病历系统</a>
            <a href="#">药品管理系统</a>
            <a href="#">财务管理系统</a>
          </div>
          <div class="link-group">
            <h4>解决方案</h4>
            <a href="#">三甲医院方案</a>
            <a href="#">社区医院方案</a>
            <a href="#">专科医院方案</a>
            <a href="#">医联体方案</a>
          </div>
          <div class="link-group">
            <h4>支持服务</h4>
            <a href="#">帮助中心</a>
            <a href="#">API文档</a>
            <a href="#">培训服务</a>
            <a href="#">联系我们</a>
          </div>
        </div>
      </div>
      <div class="footer-bottom">
        <span>{{ footerContent }}</span>
        <div class="footer-badges">
          <span><i class="el-icon-lock"></i> 安全认证</span>
          <span><i class="el-icon-check"></i> ISO认证</span>
        </div>
      </div>
    </footer>

    <!-- 登录弹窗 -->
    <el-dialog :visible.sync="showLogin" width="440px" custom-class="login-dialog" append-to-body :show-close="false">
      <div class="dialog-close" @click="showLogin = false"><i class="el-icon-close"></i></div>
      <div class="dialog-login">
        <div class="login-header">
          <div class="login-logo">
            <svg viewBox="0 0 40 40" fill="none">
              <circle cx="20" cy="20" r="18" stroke="currentColor" stroke-width="2" fill="none"/>
              <path d="M20 8v24M12 14h16M12 26h16" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
              <circle cx="20" cy="20" r="4" fill="currentColor"/>
            </svg>
          </div>
          <h2>欢迎回来</h2>
          <p>登录您的账户以继续</p>
        </div>
        <el-form ref="loginForm" :model="loginForm" :rules="loginRules">
          <el-form-item prop="username">
            <el-input v-model="loginForm.username" placeholder="请输入账号" prefix-icon="el-icon-user" size="large" />
          </el-form-item>
          <el-form-item prop="password">
            <el-input v-model="loginForm.password" type="password" placeholder="请输入密码" prefix-icon="el-icon-lock" size="large" show-password @keyup.enter.native="handleLogin" />
          </el-form-item>
          <el-form-item prop="code" v-if="captchaEnabled">
            <div class="code-row">
              <el-input v-model="loginForm.code" placeholder="验证码" prefix-icon="el-icon-key" size="large" @keyup.enter.native="handleLogin" />
              <img :src="codeUrl" @click="getCode" class="code-img" alt="验证码" />
            </div>
          </el-form-item>
          <div class="login-options">
            <el-checkbox v-model="loginForm.rememberMe">记住密码</el-checkbox>
            <a href="javascript:;">忘记密码？</a>
          </div>
          <el-button type="primary" :loading="loading" class="submit-btn" size="large" @click="handleLogin">
            {{ loading ? '登录中...' : '登 录' }}
          </el-button>
        </el-form>
        <div class="login-footer">
          <span>还没有账户？</span>
          <a @click="goToRegister" class="register-link">立即注册</a>
        </div>
      </div>
    </el-dialog>
  </div>
</template>


<script>
import { getCodeImg } from "@/api/login";
import Cookies from "js-cookie";
import { encrypt, decrypt } from "@/utils/jsencrypt";
import defaultSettings from "@/settings";

export default {
  name: "Login",
  data() {
    return {
      title: process.env.VUE_APP_TITLE || "智慧医疗平台",
      footerContent: defaultSettings.footerContent,
      showLogin: false,
      isScrolled: false,
      mobileMenuOpen: false,
      activeFeature: 0,
      codeUrl: "",
      loading: false,
      captchaEnabled: true,
      redirect: undefined,
      loginForm: { username: "admin", password: "admin123", rememberMe: false, code: "", uuid: "" },
      loginRules: {
        username: [{ required: true, trigger: "blur", message: "请输入账号" }],
        password: [{ required: true, trigger: "blur", message: "请输入密码" }],
        code: [{ required: true, trigger: "change", message: "请输入验证码" }]
      },
      heroStats: [
        { value: 500, display: "500", suffix: "+", label: "合作医院" },
        { value: 1000, display: "1000", suffix: "万+", label: "服务患者" },
        { value: 99.9, display: "99.9", suffix: "%", label: "系统稳定性" },
        { value: 24, display: "24", suffix: "/7", label: "技术支持" }
      ],
      features: [
        { icon: "el-icon-date", title: "智能预约挂号", desc: "多渠道预约，智能分诊导诊", gradient: "linear-gradient(135deg, #0891B2, #06B6D4)", featured: true,
          items: ["微信/APP/网站多端预约", "AI智能分诊推荐", "实时号源管理", "预约提醒通知"] },
        { icon: "el-icon-document", title: "电子病历系统", desc: "结构化病历，智能辅助填写", gradient: "linear-gradient(135deg, #6366F1, #8B5CF6)",
          items: ["结构化病历模板", "历史病历查询", "智能辅助诊断", "病历质控管理"] },
        { icon: "el-icon-user", title: "患者档案管理", desc: "完整健康档案，全生命周期管理", gradient: "linear-gradient(135deg, #10B981, #34D399)",
          items: ["电子健康档案", "就诊历史追溯", "健康数据分析", "慢病管理跟踪"] },
        { icon: "el-icon-s-data", title: "数据分析平台", desc: "多维度报表，辅助决策支持", gradient: "linear-gradient(135deg, #F59E0B, #FBBF24)",
          items: ["运营数据大屏", "自定义报表", "趋势预测分析", "绩效考核统计"] },
        { icon: "el-icon-goods", title: "药品库存管理", desc: "智能库存预警，用药安全保障", gradient: "linear-gradient(135deg, #EC4899, #F472B6)",
          items: ["库存实时监控", "智能补货提醒", "效期预警管理", "用药安全审核"] },
        { icon: "el-icon-money", title: "财务结算系统", desc: "多种支付方式，自动对账结算", gradient: "linear-gradient(135deg, #EF4444, #F87171)",
          items: ["多支付渠道", "自动对账核销", "医保结算对接", "财务报表生成"] }
      ],
      services: [
        { icon: "el-icon-mobile-phone", title: "在线预约", desc: "患者通过手机随时预约挂号" },
        { icon: "el-icon-location", title: "到院签到", desc: "扫码签到，智能排队叫号" },
        { icon: "el-icon-first-aid-kit", title: "就诊问诊", desc: "医生接诊，电子病历记录" },
        { icon: "el-icon-shopping-cart-2", title: "缴费取药", desc: "在线支付，药房自动配药" },
        { icon: "el-icon-document-checked", title: "报告查询", desc: "检验检查报告在线查看" },
        { icon: "el-icon-phone-outline", title: "诊后随访", desc: "智能随访提醒，健康管理" }
      ],
      techItems: [
        { icon: "el-icon-cloudy", title: "云原生架构", desc: "弹性扩展，按需付费" },
        { icon: "el-icon-lock", title: "安全加密", desc: "多重加密，数据安全" },
        { icon: "el-icon-cpu", title: "AI智能", desc: "智能辅助，提升效率" },
        { icon: "el-icon-connection", title: "互联互通", desc: "标准接口，无缝对接" }
      ],
      orbitItems: [
        { icon: "el-icon-data-analysis" },
        { icon: "el-icon-monitor" },
        { icon: "el-icon-mobile" },
        { icon: "el-icon-cloudy" }
      ],
      cases: [
        { name: "北京协和医院", type: "三甲综合医院", quote: "系统稳定可靠，大大提升了我们的工作效率", efficiency: "+45%", satisfaction: "98%" },
        { name: "上海瑞金医院", type: "三甲综合医院", quote: "患者满意度显著提升，预约等待时间大幅缩短", efficiency: "+52%", satisfaction: "97%" },
        { name: "广州中山医院", type: "三甲专科医院", quote: "数据分析功能强大，为医院管理提供了有力支持", efficiency: "+38%", satisfaction: "96%" }
      ],
      aboutNumbers: [
        { value: "10+", label: "年行业经验" },
        { value: "500+", label: "合作医院" },
        { value: "200+", label: "专业团队" },
        { value: "50+", label: "专利技术" }
      ],
      aboutCards: [
        { icon: "el-icon-trophy", title: "行业领先", desc: "连续5年医疗信息化领域领导品牌" },
        { icon: "el-icon-service", title: "专业服务", desc: "7x24小时技术支持，快速响应" },
        { icon: "el-icon-refresh", title: "持续创新", desc: "每月产品迭代，紧跟行业趋势" },
        { icon: "el-icon-s-check", title: "安全合规", desc: "通过等保三级，数据安全可靠" }
      ]
    };
  },
  watch: {
    $route: { handler(route) { this.redirect = route.query && route.query.redirect; }, immediate: true }
  },
  created() {
    this.getCode();
    this.getCookie();
  },
  mounted() {
    window.addEventListener("scroll", this.handleScroll);
    this.initDNACanvas();
  },
  beforeDestroy() {
    window.removeEventListener("scroll", this.handleScroll);
    if (this.animationFrame) cancelAnimationFrame(this.animationFrame);
  },
  methods: {
    handleScroll() {
      this.isScrolled = window.scrollY > 50;
    },
    scrollTo(id) {
      const el = document.getElementById(id);
      if (el) el.scrollIntoView({ behavior: "smooth" });
      this.mobileMenuOpen = false;
    },
    initDNACanvas() {
      const canvas = this.$refs.dnaCanvas;
      if (!canvas) return;
      const ctx = canvas.getContext("2d");
      let width = canvas.width = window.innerWidth;
      let height = canvas.height = window.innerHeight;
      
      const particles = [];
      const particleCount = 100;
      
      for (let i = 0; i < particleCount; i++) {
        particles.push({
          x: Math.random() * width,
          y: Math.random() * height,
          radius: Math.random() * 2 + 1,
          vx: (Math.random() - 0.5) * 0.5,
          vy: (Math.random() - 0.5) * 0.5,
          alpha: Math.random() * 0.5 + 0.2
        });
      }
      
      const animate = () => {
        ctx.clearRect(0, 0, width, height);
        
        // Draw DNA helix
        const time = Date.now() * 0.001;
        ctx.strokeStyle = "rgba(8, 145, 178, 0.1)";
        ctx.lineWidth = 2;
        
        for (let i = 0; i < 2; i++) {
          ctx.beginPath();
          for (let x = 0; x < width; x += 5) {
            const y = height / 2 + Math.sin(x * 0.01 + time + i * Math.PI) * 150;
            if (x === 0) ctx.moveTo(x, y);
            else ctx.lineTo(x, y);
          }
          ctx.stroke();
        }
        
        // Draw connections
        ctx.strokeStyle = "rgba(8, 145, 178, 0.05)";
        for (let x = 0; x < width; x += 30) {
          const y1 = height / 2 + Math.sin(x * 0.01 + time) * 150;
          const y2 = height / 2 + Math.sin(x * 0.01 + time + Math.PI) * 150;
          ctx.beginPath();
          ctx.moveTo(x, y1);
          ctx.lineTo(x, y2);
          ctx.stroke();
        }
        
        // Draw particles
        particles.forEach(p => {
          p.x += p.vx;
          p.y += p.vy;
          if (p.x < 0 || p.x > width) p.vx *= -1;
          if (p.y < 0 || p.y > height) p.vy *= -1;
          
          ctx.beginPath();
          ctx.arc(p.x, p.y, p.radius, 0, Math.PI * 2);
          ctx.fillStyle = `rgba(8, 145, 178, ${p.alpha})`;
          ctx.fill();
        });
        
        // Connect nearby particles
        particles.forEach((p1, i) => {
          particles.slice(i + 1).forEach(p2 => {
            const dx = p1.x - p2.x;
            const dy = p1.y - p2.y;
            const dist = Math.sqrt(dx * dx + dy * dy);
            if (dist < 100) {
              ctx.beginPath();
              ctx.moveTo(p1.x, p1.y);
              ctx.lineTo(p2.x, p2.y);
              ctx.strokeStyle = `rgba(8, 145, 178, ${0.1 * (1 - dist / 100)})`;
              ctx.stroke();
            }
          });
        });
        
        this.animationFrame = requestAnimationFrame(animate);
      };
      
      animate();
      
      window.addEventListener("resize", () => {
        width = canvas.width = window.innerWidth;
        height = canvas.height = window.innerHeight;
      });
    },
    getCode() {
      getCodeImg().then(res => {
        this.captchaEnabled = res.captchaEnabled === undefined ? true : res.captchaEnabled;
        if (this.captchaEnabled) {
          this.codeUrl = "data:image/gif;base64," + res.img;
          this.loginForm.uuid = res.uuid;
        }
      }).catch(() => { this.captchaEnabled = false; });
    },
    getCookie() {
      const username = Cookies.get("username");
      const password = Cookies.get("password");
      const rememberMe = Cookies.get("rememberMe");
      this.loginForm = {
        username: username || this.loginForm.username,
        password: password ? decrypt(password) : this.loginForm.password,
        rememberMe: rememberMe ? Boolean(rememberMe) : false
      };
    },
    handleLogin() {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          this.loading = true;
          if (this.loginForm.rememberMe) {
            Cookies.set("username", this.loginForm.username, { expires: 30 });
            Cookies.set("password", encrypt(this.loginForm.password), { expires: 30 });
            Cookies.set("rememberMe", this.loginForm.rememberMe, { expires: 30 });
          } else {
            Cookies.remove("username");
            Cookies.remove("password");
            Cookies.remove("rememberMe");
          }
          this.$store.dispatch("Login", this.loginForm).then(() => {
            this.$router.push({ path: this.redirect || "/" }).catch(() => {});
          }).catch(() => {
            this.loading = false;
            if (this.captchaEnabled) this.getCode();
          });
        }
      });
    },
    goToRegister() {
      this.showLogin = false;
      this.$router.push('/register');
    }
  }
};
</script>


<style lang="scss" scoped>
$primary: #0891B2;
$primary-dark: #0E7490;
$dark: #0F172A;
$gray: #64748B;

.landing-page {
  min-height: 100vh;
  background: #fff;
  overflow-x: hidden;
  position: relative;
}

/* DNA 背景 */
.dna-background {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 0;
  pointer-events: none;
  canvas { width: 100%; height: 100%; }
}

/* 顶部导航 */
.landing-header {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 1000;
  padding: 16px 0;
  transition: all 0.3s;
  
  &.scrolled {
    background: rgba(255,255,255,0.95);
    backdrop-filter: blur(20px);
    box-shadow: 0 4px 30px rgba(0,0,0,0.1);
    padding: 10px 0;
  }
  
  .header-container {
    max-width: 1400px;
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
    font-size: 22px;
    font-weight: 700;
    .logo-icon svg { width: 40px; height: 40px; }
  }
  
  .nav-links {
    display: flex;
    gap: 36px;
    a {
      color: #334155;
      font-weight: 500;
      font-size: 15px;
      cursor: pointer;
      position: relative;
      transition: color 0.3s;
      &:hover { color: $primary; }
      &::after {
        content: '';
        position: absolute;
        bottom: -4px;
        left: 0;
        width: 0;
        height: 2px;
        background: $primary;
        transition: width 0.3s;
      }
      &:hover::after { width: 100%; }
    }
  }
  
  .header-actions {
    display: flex;
    align-items: center;
    gap: 16px;
    .login-btn-text { color: #334155; font-weight: 500; }
  }
  
  .mobile-menu {
    display: none;
    font-size: 24px;
    cursor: pointer;
    color: #334155;
  }
}

.mobile-nav {
  position: absolute;
  top: 100%;
  left: 0;
  right: 0;
  background: #fff;
  padding: 20px;
  box-shadow: 0 10px 40px rgba(0,0,0,0.1);
  display: flex;
  flex-direction: column;
  gap: 16px;
  a { color: #334155; font-weight: 500; padding: 12px 0; }
}

.slide-down-enter-active, .slide-down-leave-active { transition: all 0.3s; }
.slide-down-enter, .slide-down-leave-to { opacity: 0; transform: translateY(-10px); }

/* Hero 区域 */
.hero-section {
  min-height: 100vh;
  display: flex;
  align-items: center;
  padding: 120px 40px 80px;
  position: relative;
  max-width: 1400px;
  margin: 0 auto;
  
  .hero-content {
    flex: 1;
    position: relative;
    z-index: 2;
  }
  
  .hero-badge {
    display: inline-flex;
    align-items: center;
    gap: 8px;
    background: rgba($primary, 0.1);
    color: $primary;
    padding: 10px 20px;
    border-radius: 30px;
    font-size: 14px;
    font-weight: 600;
    margin-bottom: 24px;
    i { color: #F59E0B; }
  }
  
  .hero-title {
    font-size: 60px;
    font-weight: 800;
    line-height: 1.1;
    color: $dark;
    margin: 0 0 24px;
    .gradient-text {
      background: linear-gradient(135deg, $primary, #6366F1);
      -webkit-background-clip: text;
      -webkit-text-fill-color: transparent;
    }
  }
  
  .hero-desc {
    font-size: 18px;
    color: $gray;
    line-height: 1.8;
    max-width: 560px;
    margin: 0 0 40px;
  }
  
  .hero-buttons {
    display: flex;
    gap: 16px;
    margin-bottom: 60px;
    .el-button { padding: 16px 32px; font-size: 16px; font-weight: 600; }
  }
  
  .hero-stats {
    display: flex;
    gap: 48px;
    .stat-item {
      .stat-value {
        display: flex;
        align-items: baseline;
        font-size: 40px;
        font-weight: 700;
        color: $dark;
        .stat-suffix { font-size: 20px; color: $primary; margin-left: 2px; }
      }
      .stat-label { display: block; font-size: 14px; color: $gray; margin-top: 4px; }
    }
  }
  
  .hero-visual {
    flex: 1;
    position: relative;
    height: 500px;
  }
  
  .floating-cards {
    position: relative;
    width: 100%;
    height: 100%;
    
    .float-card {
      position: absolute;
      background: #fff;
      padding: 20px 28px;
      border-radius: 16px;
      box-shadow: 0 20px 60px rgba(0,0,0,0.1);
      display: flex;
      align-items: center;
      gap: 12px;
      animation: float 6s ease-in-out infinite;
      
      i { font-size: 24px; color: $primary; }
      span { font-weight: 600; color: $dark; white-space: nowrap; }
      
      &.card-1 { top: 10%; right: 20%; animation-delay: 0s; }
      &.card-2 { top: 35%; right: 5%; animation-delay: 1s; }
      &.card-3 { top: 60%; right: 25%; animation-delay: 2s; }
      &.card-4 { top: 45%; right: 45%; animation-delay: 3s; }
    }
  }
  
  .scroll-indicator {
    position: absolute;
    bottom: 40px;
    left: 50%;
    transform: translateX(-50%);
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 12px;
    cursor: pointer;
    
    .mouse {
      width: 26px;
      height: 40px;
      border: 2px solid $gray;
      border-radius: 13px;
      position: relative;
      
      .wheel {
        width: 4px;
        height: 8px;
        background: $primary;
        border-radius: 2px;
        position: absolute;
        top: 8px;
        left: 50%;
        transform: translateX(-50%);
        animation: scroll 2s infinite;
      }
    }
    
    span { font-size: 13px; color: $gray; }
  }
}

@keyframes float {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(-20px); }
}

@keyframes scroll {
  0% { opacity: 1; transform: translateX(-50%) translateY(0); }
  100% { opacity: 0; transform: translateX(-50%) translateY(12px); }
}

/* 通用 Section */
.section-container {
  max-width: 1400px;
  margin: 0 auto;
  padding: 0 40px;
}

.section-header {
  text-align: center;
  margin-bottom: 60px;
  
  .section-tag {
    display: inline-block;
    background: rgba($primary, 0.1);
    color: $primary;
    padding: 8px 20px;
    border-radius: 20px;
    font-size: 14px;
    font-weight: 600;
    margin-bottom: 16px;
  }
  
  h2 {
    font-size: 42px;
    font-weight: 700;
    color: $dark;
    margin: 0 0 16px;
  }
  
  p {
    font-size: 18px;
    color: $gray;
    margin: 0;
  }
  
  &.light {
    .section-tag { background: rgba(255,255,255,0.2); color: #fff; }
    h2 { color: #fff; }
    p { color: rgba(255,255,255,0.8); }
  }
}

/* 功能特性 */
.features-section {
  padding: 120px 0;
  background: #F8FAFC;
  position: relative;
  z-index: 1;
}

.features-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 24px;
}

.feature-card {
  background: #fff;
  border-radius: 20px;
  padding: 32px;
  position: relative;
  overflow: hidden;
  transition: all 0.4s;
  border: 1px solid #E2E8F0;
  
  &:hover {
    transform: translateY(-8px);
    box-shadow: 0 30px 60px rgba(0,0,0,0.12);
    border-color: transparent;
    
    .feature-hover-effect { opacity: 1; }
  }
  
  .feature-icon {
    width: 64px;
    height: 64px;
    border-radius: 16px;
    display: flex;
    align-items: center;
    justify-content: center;
    margin-bottom: 24px;
    i { font-size: 28px; color: #fff; }
  }
  
  .feature-content {
    h3 { font-size: 22px; font-weight: 700; color: $dark; margin: 0 0 12px; }
    > p { font-size: 15px; color: $gray; margin: 0 0 20px; }
  }
  
  .feature-list {
    list-style: none;
    padding: 0;
    margin: 0;
    li {
      display: flex;
      align-items: center;
      gap: 10px;
      font-size: 14px;
      color: #475569;
      padding: 8px 0;
      i { color: #10B981; font-size: 14px; }
    }
  }
  
  .feature-hover-effect {
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    height: 4px;
    background: linear-gradient(90deg, $primary, #6366F1);
    opacity: 0;
    transition: opacity 0.3s;
  }
}

/* 服务流程 */
.services-section {
  padding: 120px 0;
  background: linear-gradient(135deg, $primary 0%, $primary-dark 100%);
  position: relative;
  z-index: 1;
}

.service-flow {
  display: flex;
  justify-content: space-between;
  position: relative;
  
  .flow-line {
    position: absolute;
    top: 50px;
    left: 10%;
    right: 10%;
    height: 2px;
    background: rgba(255,255,255,0.2);
  }
  
  .flow-item {
    text-align: center;
    position: relative;
    flex: 1;
    
    .flow-number {
      font-size: 12px;
      color: rgba(255,255,255,0.5);
      margin-bottom: 12px;
    }
    
    .flow-icon {
      width: 80px;
      height: 80px;
      background: rgba(255,255,255,0.15);
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      margin: 0 auto 20px;
      transition: all 0.3s;
      i { font-size: 32px; color: #fff; }
      
      &:hover {
        background: #fff;
        transform: scale(1.1);
        i { color: $primary; }
      }
    }
    
    h4 { font-size: 18px; font-weight: 600; color: #fff; margin: 0 0 8px; }
    p { font-size: 14px; color: rgba(255,255,255,0.7); margin: 0; }
  }
}

/* 技术优势 */
.tech-section {
  padding: 120px 0;
  background: #fff;
  position: relative;
  z-index: 1;
}

.tech-content {
  display: flex;
  align-items: center;
  gap: 80px;
}

.tech-text {
  flex: 1;
  
  h2 { font-size: 36px; font-weight: 700; color: $dark; margin: 0 0 16px; }
  > p { font-size: 16px; color: $gray; margin: 0 0 40px; }
}

.tech-list {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 24px;
}

.tech-item {
  display: flex;
  gap: 16px;
  
  .tech-icon {
    width: 48px;
    height: 48px;
    background: rgba($primary, 0.1);
    border-radius: 12px;
    display: flex;
    align-items: center;
    justify-content: center;
    flex-shrink: 0;
    i { font-size: 22px; color: $primary; }
  }
  
  .tech-info {
    h5 { font-size: 16px; font-weight: 600; color: $dark; margin: 0 0 4px; }
    span { font-size: 14px; color: $gray; }
  }
}

.tech-visual {
  flex: 1;
  display: flex;
  justify-content: center;
}

.tech-circle {
  width: 400px;
  height: 400px;
  position: relative;
  
  .circle-ring {
    position: absolute;
    border: 2px solid rgba($primary, 0.1);
    border-radius: 50%;
    
    &.ring-1 { inset: 0; animation: rotate 20s linear infinite; }
    &.ring-2 { inset: 40px; animation: rotate 15s linear infinite reverse; }
    &.ring-3 { inset: 80px; animation: rotate 10s linear infinite; }
  }
  
  .circle-center {
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    width: 120px;
    height: 120px;
    background: linear-gradient(135deg, $primary, #6366F1);
    border-radius: 50%;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    color: #fff;
    i { font-size: 36px; margin-bottom: 4px; }
    span { font-size: 14px; font-weight: 600; }
  }
  
  .orbit-item {
    position: absolute;
    width: 50px;
    height: 50px;
    background: #fff;
    border-radius: 50%;
    box-shadow: 0 10px 30px rgba(0,0,0,0.1);
    display: flex;
    align-items: center;
    justify-content: center;
    animation: orbit 8s linear infinite;
    animation-delay: var(--delay);
    
    i { font-size: 22px; color: $primary; }
    
    &:nth-child(5) { top: 0; left: 50%; transform: translateX(-50%); }
    &:nth-child(6) { top: 50%; right: 0; transform: translateY(-50%); }
    &:nth-child(7) { bottom: 0; left: 50%; transform: translateX(-50%); }
    &:nth-child(8) { top: 50%; left: 0; transform: translateY(-50%); }
  }
}

@keyframes rotate { from { transform: rotate(0deg); } to { transform: rotate(360deg); } }
@keyframes orbit { from { transform: rotate(0deg) translateX(180px) rotate(0deg); } to { transform: rotate(360deg) translateX(180px) rotate(-360deg); } }

/* 成功案例 */
.cases-section {
  padding: 120px 0;
  background: #F8FAFC;
  position: relative;
  z-index: 1;
}

.cases-slider {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 24px;
}

.case-card {
  background: #fff;
  border-radius: 20px;
  padding: 32px;
  transition: all 0.3s;
  
  &:hover {
    transform: translateY(-8px);
    box-shadow: 0 20px 50px rgba(0,0,0,0.1);
  }
  
  .case-logo {
    width: 60px;
    height: 60px;
    background: linear-gradient(135deg, $primary, #6366F1);
    border-radius: 16px;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 24px;
    font-weight: 700;
    color: #fff;
    margin-bottom: 20px;
  }
  
  .case-info {
    h4 { font-size: 20px; font-weight: 700; color: $dark; margin: 0 0 8px; }
    .case-type { font-size: 14px; color: $primary; font-weight: 500; }
    p { font-size: 15px; color: $gray; line-height: 1.7; margin: 16px 0; font-style: italic; }
  }
  
  .case-stats {
    display: flex;
    gap: 32px;
    padding-top: 20px;
    border-top: 1px solid #E2E8F0;
    
    .case-stat {
      .value { display: block; font-size: 24px; font-weight: 700; color: #10B981; }
      .label { font-size: 13px; color: $gray; }
    }
  }
}

/* 关于我们 */
.about-section {
  padding: 120px 0;
  background: #fff;
  position: relative;
  z-index: 1;
}

.about-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 80px;
  align-items: center;
}

.about-main {
  h2 { font-size: 40px; font-weight: 700; color: $dark; margin: 0 0 20px; line-height: 1.3; }
  > p { font-size: 16px; color: $gray; line-height: 1.8; margin: 0 0 40px; }
}

.about-numbers {
  display: flex;
  gap: 32px;
  margin-bottom: 40px;
  
  .number-item {
    .num { display: block; font-size: 36px; font-weight: 700; color: $primary; }
    .label { font-size: 14px; color: $gray; }
  }
}

.about-cards {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20px;
}

.about-card {
  background: #F8FAFC;
  border-radius: 16px;
  padding: 28px;
  transition: all 0.3s;
  
  &:hover {
    background: #fff;
    box-shadow: 0 20px 40px rgba(0,0,0,0.08);
    transform: translateY(-4px);
  }
  
  i { font-size: 32px; color: $primary; margin-bottom: 16px; display: block; }
  h4 { font-size: 18px; font-weight: 600; color: $dark; margin: 0 0 8px; }
  p { font-size: 14px; color: $gray; margin: 0; }
}

/* CTA */
.cta-section {
  padding: 100px 0;
  position: relative;
  overflow: hidden;
  z-index: 1;
  
  .cta-bg {
    position: absolute;
    inset: 0;
    background: linear-gradient(135deg, $dark 0%, #1E293B 100%);
  }
  
  .cta-content {
    position: relative;
    text-align: center;
    color: #fff;
    
    h2 { font-size: 36px; font-weight: 700; margin: 0 0 16px; }
    p { font-size: 18px; opacity: 0.8; margin: 0 0 40px; }
  }
  
  .cta-buttons {
    display: flex;
    justify-content: center;
    gap: 16px;
    .el-button { padding: 16px 36px; font-size: 16px; }
  }
}

/* 底部 */
.landing-footer {
  background: $dark;
  color: #94A3B8;
  position: relative;
  z-index: 1;
  
  .footer-main {
    max-width: 1400px;
    margin: 0 auto;
    padding: 80px 40px;
    display: flex;
    justify-content: space-between;
  }
  
  .footer-brand {
    max-width: 320px;
    
    .logo {
      display: flex;
      align-items: center;
      gap: 12px;
      color: #fff;
      font-size: 22px;
      font-weight: 700;
      margin-bottom: 20px;
      svg { width: 40px; height: 40px; }
    }
    
    p { font-size: 15px; line-height: 1.7; margin: 0 0 24px; }
    
    .social-links {
      display: flex;
      gap: 12px;
      a {
        width: 44px;
        height: 44px;
        background: #1E293B;
        border-radius: 12px;
        display: flex;
        align-items: center;
        justify-content: center;
        color: #94A3B8;
        font-size: 20px;
        transition: all 0.3s;
        &:hover { background: $primary; color: #fff; }
      }
    }
  }
  
  .footer-links {
    display: flex;
    gap: 80px;
    
    .link-group {
      h4 { color: #fff; font-size: 16px; font-weight: 600; margin: 0 0 24px; }
      a { display: block; font-size: 15px; margin-bottom: 14px; transition: color 0.2s; &:hover { color: #fff; } }
    }
  }
  
  .footer-bottom {
    border-top: 1px solid #1E293B;
    padding: 24px 40px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    max-width: 1400px;
    margin: 0 auto;
    font-size: 14px;
    
    .footer-badges {
      display: flex;
      gap: 24px;
      span { display: flex; align-items: center; gap: 6px; i { color: #10B981; } }
    }
  }
}

/* 登录弹窗 */
:deep(.login-dialog) {
  border-radius: 24px !important;
  overflow: hidden;
  .el-dialog__header { display: none; }
  .el-dialog__body { padding: 0; }
}

.dialog-close {
  position: absolute;
  top: 20px;
  right: 20px;
  width: 36px;
  height: 36px;
  background: #F1F5F9;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  z-index: 10;
  transition: all 0.2s;
  i { font-size: 16px; color: $gray; }
  &:hover { background: #E2E8F0; }
}

.dialog-login {
  padding: 48px 40px;
  
  .login-header {
    text-align: center;
    margin-bottom: 36px;
    
    .login-logo {
      width: 64px;
      height: 64px;
      margin: 0 auto 20px;
      color: $primary;
      svg { width: 100%; height: 100%; }
    }
    
    h2 { font-size: 26px; font-weight: 700; color: $dark; margin: 0 0 8px; }
    p { font-size: 15px; color: $gray; margin: 0; }
  }
  
  :deep(.el-input__inner) {
    height: 50px;
    border-radius: 12px;
    font-size: 15px;
  }
  
  .code-row {
    display: flex;
    gap: 12px;
    .code-img { height: 50px; border-radius: 12px; cursor: pointer; }
  }
  
  .login-options {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 24px;
    a { color: $primary; font-size: 14px; }
  }
  
  .submit-btn {
    width: 100%;
    height: 50px;
    border-radius: 12px;
    font-size: 16px;
    font-weight: 600;
  }
  
  .login-footer {
    text-align: center;
    margin-top: 24px;
    font-size: 14px;
    color: $gray;
    a { color: $primary; font-weight: 600; margin-left: 4px; }
  }
}

/* 响应式 */
@media (max-width: 1200px) {
  .features-grid { grid-template-columns: repeat(2, 1fr); }
  .cases-slider { grid-template-columns: repeat(2, 1fr); }
}

@media (max-width: 1024px) {
  .hero-section { flex-direction: column; text-align: center; padding-top: 140px;
    .hero-content { max-width: 100%; }
    .hero-desc { max-width: 100%; margin-left: auto; margin-right: auto; }
    .hero-buttons { justify-content: center; }
    .hero-stats { justify-content: center; }
    .hero-visual { display: none; }
  }
  .tech-content { flex-direction: column; }
  .tech-visual { display: none; }
  .about-grid { grid-template-columns: 1fr; gap: 40px; }
  .service-flow { flex-wrap: wrap; gap: 40px; .flow-line { display: none; } .flow-item { flex: 0 0 calc(33.33% - 27px); } }
}

@media (max-width: 768px) {
  .landing-header {
    .nav-links { display: none; }
    .header-actions { display: none; }
    .mobile-menu { display: block; }
  }
  .hero-section .hero-title { font-size: 36px; }
  .hero-section .hero-stats { flex-wrap: wrap; gap: 24px; }
  .features-grid { grid-template-columns: 1fr; }
  .cases-slider { grid-template-columns: 1fr; }
  .about-cards { grid-template-columns: 1fr; }
  .service-flow .flow-item { flex: 0 0 100%; }
  .footer-main { flex-direction: column; gap: 40px; }
  .footer-links { flex-wrap: wrap; gap: 40px; }
  .footer-bottom { flex-direction: column; gap: 16px; text-align: center; }
}
</style>
