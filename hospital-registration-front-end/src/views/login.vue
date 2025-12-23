<template>
  <div class="landing-page">
    <header class="landing-header" :class="{ 'scrolled': isScrolled }">
      <div class="header-container">
        <div class="logo"><svg viewBox="0 0 32 32" fill="none"><rect width="32" height="32" rx="8" fill="currentColor" fill-opacity="0.15"/><path d="M16 8v8m0 0l6-4m-6 4l-6-4" stroke="currentColor" stroke-width="2" stroke-linecap="round"/><circle cx="16" cy="20" r="3" fill="currentColor"/></svg><span>{{ title }}</span></div>
        <nav class="nav-links"><a @click="scrollTo('features')">功能特性</a><a @click="scrollTo('services')">服务内容</a><a @click="scrollTo('about')">关于我们</a></nav>
        <div class="header-actions"><el-button type="text" @click="showLogin = true">登录</el-button><el-button type="primary" size="small" round @click="showLogin = true">立即体验</el-button></div>
      </div>
    </header>
    <section class="hero-section">
      <div class="hero-slider">
        <div class="slide" v-for="(slide, index) in slides" :key="index" :class="{ active: currentSlide === index }">
          <div class="slide-bg" :style="{ background: slide.bg }"></div>
          <div class="slide-content"><span class="slide-tag">{{ slide.tag }}</span><h1>{{ slide.title }}</h1><p>{{ slide.desc }}</p><el-button type="primary" round size="large" @click="showLogin = true">了解更多</el-button></div>
        </div>
        <div class="slider-nav"><span v-for="(s, i) in slides" :key="i" :class="{ active: currentSlide === i }" @click="currentSlide = i"></span></div>
        <div class="slider-arrows"><button class="arrow prev" @click="prevSlide"><i class="el-icon-arrow-left"></i></button><button class="arrow next" @click="nextSlide"><i class="el-icon-arrow-right"></i></button></div>
      </div>
      <div class="scroll-hint" @click="scrollTo('features')"><span>向下滚动</span><i class="el-icon-arrow-down"></i></div>
    </section>
    <section id="features" class="cards-section">
      <div class="section-container">
        <div class="cards-grid">
          <div class="info-card" v-for="(card, index) in featureCards" :key="index" :style="{ '--accent': card.color }">
            <div class="card-icon"><i :class="card.icon"></i></div><span class="card-tag">{{ card.tag }}</span><h3>{{ card.title }}</h3><p>{{ card.desc }}</p><a class="card-link" @click="showLogin = true">了解更多 <i class="el-icon-arrow-right"></i></a>
          </div>
        </div>
      </div>
    </section>
    <section id="services" class="expertise-section">
      <div class="section-container">
        <div class="section-header"><h2>医疗服务全流程覆盖</h2><p>从预约挂号到诊后随访，一站式解决方案</p></div>
        <div class="expertise-tabs"><div class="tab-item" v-for="(item, index) in expertiseItems" :key="index" :class="{ active: activeExpertise === index }" @click="activeExpertise = index"><div class="tab-icon"><i :class="item.icon"></i></div><span>{{ item.name }}</span></div></div>
        <div class="expertise-content"><div class="expertise-detail"><p>{{ expertiseItems[activeExpertise].detail }}</p><el-button type="primary" plain round @click="showLogin = true">查看详情</el-button></div></div>
      </div>
    </section>
    <section class="stats-section">
      <div class="section-container"><div class="stats-grid"><div class="stat-box" v-for="(stat, index) in statsData" :key="index"><span class="stat-number">{{ stat.value }}</span><span class="stat-label">{{ stat.label }}</span></div></div></div>
    </section>
    <section id="about" class="about-section">
      <div class="section-container">
        <div class="about-content">
          <div class="about-text"><span class="about-tag">关于我们</span><h2>专注医疗信息化十余年</h2><p>我们致力于为医疗机构提供专业、可靠、高效的信息化解决方案。</p>
            <div class="about-features"><div class="feature"><i class="el-icon-check"></i><span>500+ 合作医院</span></div><div class="feature"><i class="el-icon-check"></i><span>1000万+ 服务患者</span></div><div class="feature"><i class="el-icon-check"></i><span>99.9% 系统稳定性</span></div><div class="feature"><i class="el-icon-check"></i><span>7x24 技术支持</span></div></div>
            <el-button type="primary" round @click="showLogin = true">立即体验</el-button>
          </div>
          <div class="about-image"><div class="image-placeholder"><i class="el-icon-office-building"></i></div></div>
        </div>
      </div>
    </section>
    <footer class="landing-footer">
      <div class="footer-main">
        <div class="footer-brand"><div class="logo"><svg viewBox="0 0 32 32" fill="none"><rect width="32" height="32" rx="8" fill="currentColor" fill-opacity="0.15"/><path d="M16 8v8m0 0l6-4m-6 4l-6-4" stroke="currentColor" stroke-width="2" stroke-linecap="round"/><circle cx="16" cy="20" r="3" fill="currentColor"/></svg><span>{{ title }}</span></div><p>专业的医疗信息化解决方案</p></div>
        <div class="footer-links"><div class="link-col"><h4>产品服务</h4><a href="#">预约挂号</a><a href="#">患者管理</a></div><div class="link-col"><h4>支持帮助</h4><a href="#">帮助中心</a><a href="#">联系我们</a></div></div>
      </div>
      <div class="footer-bottom"><span>{{ footerContent }}</span></div>
    </footer>
    <el-dialog :visible.sync="showLogin" width="400px" custom-class="login-dialog" append-to-body>
      <div class="dialog-login"><h2>欢迎登录</h2><p>登录以继续使用系统</p>
        <el-form ref="loginForm" :model="loginForm" :rules="loginRules">
          <el-form-item prop="username"><el-input v-model="loginForm.username" placeholder="账号" prefix-icon="el-icon-user" /></el-form-item>
          <el-form-item prop="password"><el-input v-model="loginForm.password" type="password" placeholder="密码" prefix-icon="el-icon-lock" show-password @keyup.enter.native="handleLogin" /></el-form-item>
          <el-form-item prop="code" v-if="captchaEnabled"><div class="code-row"><el-input v-model="loginForm.code" placeholder="验证码" prefix-icon="el-icon-key" @keyup.enter.native="handleLogin" /><img :src="codeUrl" @click="getCode" class="code-img" /></div></el-form-item>
          <el-checkbox v-model="loginForm.rememberMe" class="remember">记住密码</el-checkbox>
          <el-button type="primary" :loading="loading" class="submit-btn" @click="handleLogin">{{ loading ? '登录中...' : '登 录' }}</el-button>
        </el-form>
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
      showLogin: false, isScrolled: false, currentSlide: 0, activeExpertise: 0, codeUrl: "", loading: false, captchaEnabled: true, redirect: undefined,
      loginForm: { username: "admin", password: "admin123", rememberMe: false, code: "", uuid: "" },
      loginRules: { username: [{ required: true, trigger: "blur", message: "请输入账号" }], password: [{ required: true, trigger: "blur", message: "请输入密码" }], code: [{ required: true, trigger: "change", message: "请输入验证码" }] },
      slides: [
        { tag: "智能预约", title: "在线预约挂号系统", desc: "患者可随时随地在线预约，智能分诊导诊，减少等待时间", bg: "linear-gradient(135deg, #0891B2 0%, #065F73 100%)" },
        { tag: "数据驱动", title: "医疗数据分析平台", desc: "多维度数据统计分析，辅助医院管理决策，提升运营效率", bg: "linear-gradient(135deg, #6366F1 0%, #4338CA 100%)" },
        { tag: "安全可靠", title: "医疗信息安全保障", desc: "多重加密技术，确保患者隐私和医疗数据安全合规", bg: "linear-gradient(135deg, #10B981 0%, #047857 100%)" }
      ],
      featureCards: [
        { icon: "el-icon-date", tag: "预约管理", title: "智能预约挂号", desc: "支持多渠道预约，智能排班调度", color: "#0891B2" },
        { icon: "el-icon-user", tag: "患者服务", title: "患者档案管理", desc: "完整的电子病历和健康档案", color: "#10B981" },
        { icon: "el-icon-s-data", tag: "数据分析", title: "运营数据报表", desc: "可视化数据分析和决策支持", color: "#6366F1" },
        { icon: "el-icon-s-order", tag: "排班管理", title: "医生排班系统", desc: "灵活的排班和资源调度", color: "#F59E0B" }
      ],
      expertiseItems: [
        { icon: "el-icon-date", name: "预约挂号", detail: "支持微信、APP、网站等多渠道预约，智能分诊导诊，自动推荐合适的科室和医生。" },
        { icon: "el-icon-document", name: "电子病历", detail: "完整的电子病历系统，支持病历模板、智能填写、历史病历查询。" },
        { icon: "el-icon-goods", name: "药品管理", detail: "药品库存实时监控，智能补货提醒，处方审核，用药安全提示。" },
        { icon: "el-icon-money", name: "财务结算", detail: "支持多种支付方式，自动对账，财务报表生成，医保结算对接。" }
      ],
      statsData: [{ value: "500+", label: "合作医院" }, { value: "1000万+", label: "服务患者" }, { value: "99.9%", label: "系统稳定性" }, { value: "24/7", label: "技术支持" }]
    };
  },
  watch: { $route: { handler(route) { this.redirect = route.query && route.query.redirect; }, immediate: true } },
  created() { this.getCode(); this.getCookie(); this.startSlider(); },
  mounted() { window.addEventListener("scroll", this.handleScroll); },
  beforeDestroy() { window.removeEventListener("scroll", this.handleScroll); clearInterval(this.sliderTimer); },
  methods: {
    handleScroll() { this.isScrolled = window.scrollY > 50; },
    scrollTo(id) { document.getElementById(id)?.scrollIntoView({ behavior: "smooth" }); },
    startSlider() { this.sliderTimer = setInterval(() => { this.nextSlide(); }, 5000); },
    nextSlide() { this.currentSlide = (this.currentSlide + 1) % this.slides.length; },
    prevSlide() { this.currentSlide = (this.currentSlide - 1 + this.slides.length) % this.slides.length; },
    getCode() { getCodeImg().then(res => { this.captchaEnabled = res.captchaEnabled === undefined ? true : res.captchaEnabled; if (this.captchaEnabled) { this.codeUrl = "data:image/gif;base64," + res.img; this.loginForm.uuid = res.uuid; } }).catch(() => { this.captchaEnabled = false; }); },
    getCookie() { const username = Cookies.get("username"), password = Cookies.get("password"), rememberMe = Cookies.get("rememberMe"); this.loginForm = { username: username || this.loginForm.username, password: password ? decrypt(password) : this.loginForm.password, rememberMe: rememberMe ? Boolean(rememberMe) : false }; },
    handleLogin() { this.$refs.loginForm.validate(valid => { if (valid) { this.loading = true; if (this.loginForm.rememberMe) { Cookies.set("username", this.loginForm.username, { expires: 30 }); Cookies.set("password", encrypt(this.loginForm.password), { expires: 30 }); Cookies.set("rememberMe", this.loginForm.rememberMe, { expires: 30 }); } else { Cookies.remove("username"); Cookies.remove("password"); Cookies.remove("rememberMe"); } this.$store.dispatch("Login", this.loginForm).then(() => { this.$router.push({ path: this.redirect || "/" }).catch(() => {}); }).catch(() => { this.loading = false; if (this.captchaEnabled) this.getCode(); }); } }); }
  }
};
</script>

<style lang="scss" scoped>
.landing-page { min-height: 100vh; background: #fff; }
.landing-header { position: fixed; top: 0; left: 0; right: 0; z-index: 1000; padding: 20px 0; transition: all 0.3s; &.scrolled { background: rgba(255,255,255,0.98); box-shadow: 0 2px 20px rgba(0,0,0,0.1); padding: 12px 0; } .header-container { max-width: 1400px; margin: 0 auto; padding: 0 40px; display: flex; align-items: center; justify-content: space-between; } .logo { display: flex; align-items: center; gap: 12px; color: #0891B2; font-size: 22px; font-weight: 700; svg { width: 40px; height: 40px; } } .nav-links { display: flex; gap: 40px; a { color: #334155; font-weight: 500; cursor: pointer; &:hover { color: #0891B2; } } } .header-actions { display: flex; align-items: center; gap: 16px; } }
.hero-section { height: 100vh; position: relative; .hero-slider { height: 100%; position: relative; overflow: hidden; } .slide { position: absolute; inset: 0; opacity: 0; transition: opacity 0.8s; &.active { opacity: 1; } .slide-bg { position: absolute; inset: 0; } .slide-content { position: relative; z-index: 2; height: 100%; display: flex; flex-direction: column; justify-content: center; padding: 0 10%; color: #fff; max-width: 700px; .slide-tag { display: inline-block; background: rgba(255,255,255,0.2); padding: 8px 20px; border-radius: 30px; font-size: 14px; margin-bottom: 24px; width: fit-content; } h1 { font-size: 52px; font-weight: 700; margin: 0 0 20px; line-height: 1.2; } p { font-size: 18px; line-height: 1.7; opacity: 0.9; margin: 0 0 32px; } } } .slider-nav { position: absolute; bottom: 100px; left: 10%; display: flex; gap: 12px; z-index: 10; span { width: 40px; height: 4px; background: rgba(255,255,255,0.4); border-radius: 2px; cursor: pointer; &.active { background: #fff; width: 60px; } } } .slider-arrows { position: absolute; bottom: 100px; right: 10%; display: flex; gap: 12px; z-index: 10; .arrow { width: 50px; height: 50px; border: 2px solid rgba(255,255,255,0.5); background: transparent; border-radius: 50%; color: #fff; font-size: 18px; cursor: pointer; &:hover { background: #fff; color: #0891B2; } } } .scroll-hint { position: absolute; bottom: 30px; left: 50%; transform: translateX(-50%); color: #fff; display: flex; flex-direction: column; align-items: center; gap: 8px; font-size: 13px; cursor: pointer; animation: bounce 2s infinite; z-index: 10; } }
@keyframes bounce { 0%, 100% { transform: translateX(-50%) translateY(0); } 50% { transform: translateX(-50%) translateY(10px); } }
.cards-section { padding: 100px 0; background: #f8fafc; .section-container { max-width: 1400px; margin: 0 auto; padding: 0 40px; } .cards-grid { display: grid; grid-template-columns: repeat(4, 1fr); gap: 24px; } .info-card { background: #fff; border-radius: 16px; padding: 32px; transition: all 0.4s; border: 1px solid #e2e8f0; &:hover { transform: translateY(-8px); box-shadow: 0 20px 40px rgba(0,0,0,0.1); } .card-icon { width: 60px; height: 60px; border-radius: 14px; background: rgba(8,145,178,0.1); display: flex; align-items: center; justify-content: center; margin-bottom: 20px; i { font-size: 26px; color: var(--accent); } } .card-tag { display: block; font-size: 12px; color: var(--accent); font-weight: 600; margin-bottom: 12px; } h3 { font-size: 20px; font-weight: 600; color: #1e293b; margin: 0 0 12px; } p { font-size: 14px; color: #64748b; margin: 0 0 20px; } .card-link { font-size: 14px; font-weight: 600; color: #64748b; cursor: pointer; i { margin-left: 6px; } } } }
.expertise-section { padding: 100px 0; background: #fff; .section-container { max-width: 1400px; margin: 0 auto; padding: 0 40px; } .section-header { text-align: center; margin-bottom: 60px; h2 { font-size: 40px; font-weight: 700; color: #1e293b; margin: 0 0 16px; } p { font-size: 18px; color: #64748b; margin: 0; } } .expertise-tabs { display: flex; justify-content: center; gap: 8px; margin-bottom: 40px; flex-wrap: wrap; .tab-item { display: flex; align-items: center; gap: 10px; padding: 16px 28px; border-radius: 50px; background: #f1f5f9; cursor: pointer; .tab-icon { width: 36px; height: 36px; border-radius: 50%; background: #fff; display: flex; align-items: center; justify-content: center; i { font-size: 16px; color: #64748b; } } span { font-weight: 500; color: #475569; } &.active { background: #0891B2; .tab-icon { background: rgba(255,255,255,0.2); i { color: #fff; } } span { color: #fff; } } } } .expertise-content { max-width: 800px; margin: 0 auto; text-align: center; p { font-size: 18px; color: #475569; line-height: 1.8; margin: 0 0 32px; } } }
.stats-section { padding: 80px 0; background: linear-gradient(135deg, #0891B2 0%, #065F73 100%); .section-container { max-width: 1400px; margin: 0 auto; padding: 0 40px; } .stats-grid { display: grid; grid-template-columns: repeat(4, 1fr); gap: 40px; } .stat-box { text-align: center; color: #fff; .stat-number { display: block; font-size: 48px; font-weight: 700; margin-bottom: 8px; } .stat-label { font-size: 16px; opacity: 0.85; } } }
.about-section { padding: 100px 0; background: #f8fafc; .section-container { max-width: 1400px; margin: 0 auto; padding: 0 40px; } .about-content { display: flex; align-items: center; gap: 80px; } .about-text { flex: 1; .about-tag { display: block; font-size: 13px; color: #0891B2; font-weight: 600; margin-bottom: 16px; } h2 { font-size: 36px; font-weight: 700; color: #1e293b; margin: 0 0 20px; } > p { font-size: 16px; color: #64748b; line-height: 1.8; margin: 0 0 32px; } .about-features { display: grid; grid-template-columns: repeat(2, 1fr); gap: 16px; margin-bottom: 32px; .feature { display: flex; align-items: center; gap: 12px; i { color: #10B981; font-size: 18px; } span { font-size: 15px; color: #475569; } } } } .about-image { flex: 1; .image-placeholder { width: 100%; height: 400px; background: linear-gradient(135deg, #0891B2 0%, #065F73 100%); border-radius: 20px; display: flex; align-items: center; justify-content: center; i { font-size: 80px; color: rgba(255,255,255,0.3); } } } }
.landing-footer { background: #0f172a; color: #94a3b8; .footer-main { max-width: 1400px; margin: 0 auto; padding: 60px 40px; display: flex; justify-content: space-between; } .footer-brand { .logo { display: flex; align-items: center; gap: 12px; color: #fff; font-size: 20px; font-weight: 700; margin-bottom: 16px; svg { width: 36px; height: 36px; } } p { font-size: 14px; margin: 0; } } .footer-links { display: flex; gap: 80px; .link-col { h4 { color: #fff; font-size: 16px; margin: 0 0 20px; } a { display: block; font-size: 14px; margin-bottom: 12px; &:hover { color: #fff; } } } } .footer-bottom { border-top: 1px solid #1e293b; padding: 20px 40px; text-align: center; font-size: 14px; } }
:deep(.login-dialog) { border-radius: 20px !important; .el-dialog__header { padding: 0; } .el-dialog__body { padding: 40px; } }
.dialog-login { h2 { font-size: 24px; font-weight: 700; color: #1e293b; margin: 0 0 8px; text-align: center; } > p { font-size: 14px; color: #64748b; margin: 0 0 32px; text-align: center; } :deep(.el-input__inner) { height: 46px; border-radius: 10px; } .code-row { display: flex; gap: 12px; .code-img { height: 46px; border-radius: 10px; cursor: pointer; } } .remember { margin: 0 0 20px; } .submit-btn { width: 100%; height: 46px; border-radius: 10px; font-size: 16px; } }
@media (max-width: 1024px) { .cards-section .cards-grid { grid-template-columns: repeat(2, 1fr); } .stats-section .stats-grid { grid-template-columns: repeat(2, 1fr); } .about-section .about-content { flex-direction: column; gap: 40px; } }
@media (max-width: 768px) { .landing-header .nav-links { display: none; } .hero-section .slide .slide-content h1 { font-size: 32px; } .cards-section .cards-grid { grid-template-columns: 1fr; } .stats-section .stats-grid { grid-template-columns: 1fr; } .landing-footer .footer-main { flex-direction: column; gap: 40px; } }
</style>
