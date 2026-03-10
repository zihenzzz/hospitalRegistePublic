<template>
  <div class="portal-page" ref="portalPage">
    <!-- 固定导航 -->
    <header class="nav-header" :class="{ scrolled: scrollY > 50 }">
      <div class="nav-container">
        <div class="logo" @click="scrollToSection('hero')">
          <div class="logo-icon">
            <svg viewBox="0 0 40 40" fill="none">
              <circle cx="20" cy="20" r="18" stroke="currentColor" stroke-width="2"/>
              <path d="M20 10v20M12 16h16M12 24h16" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
            </svg>
          </div>
          <span>智慧医疗</span>
        </div>
        <nav class="nav-menu">
          <a @click="scrollToSection('hero')" :class="{ active: currentSection === 'hero' }">首页</a>
          <a @click="scrollToSection('search')" :class="{ active: currentSection === 'search' }">快速挂号</a>
          <a @click="scrollToSection('doctors')" :class="{ active: currentSection === 'doctors' }">找医生</a>
          <a @click="scrollToSection('hospitals')" :class="{ active: currentSection === 'hospitals' }">找医院</a>
          <a @click="scrollToSection('departments')" :class="{ active: currentSection === 'departments' }">按科室</a>
          <a @click="goDataScreen">数据大屏</a>
        </nav>
        <div class="nav-actions">
          <el-button type="text" class="ai-btn" @click="openAiAssistant">
            <i class="el-icon-chat-dot-round"></i> AI问诊
          </el-button>
          <template v-if="!isLoggedIn">
            <el-button type="text" @click="goLogin">登录</el-button>
            <el-button type="primary" round size="small" @click="goRegister">注册</el-button>
          </template>
          <el-dropdown v-else @command="handleCommand">
            <div class="user-avatar">
              <el-avatar :size="32">{{ userName ? userName.charAt(0) : 'U' }}</el-avatar>
            </div>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item command="records">我的挂号</el-dropdown-item>
              <el-dropdown-item command="profile">个人中心</el-dropdown-item>
              <el-dropdown-item v-if="isAdmin" command="admin" divided>管理后台</el-dropdown-item>
              <el-dropdown-item command="logout" divided>退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>
      </div>
    </header>

    <!-- Hero 区域 - 全屏视差 -->
    <section id="hero" class="hero-section" ref="heroSection">
      <div class="parallax-bg" :style="{ transform: `translateY(${scrollY * 0.5}px)` }">
        <div class="bg-gradient"></div>
        <div class="bg-grid"></div>
        <div class="floating-orbs">
          <div class="orb orb-1"></div>
          <div class="orb orb-2"></div>
          <div class="orb orb-3"></div>
          <div class="orb orb-4"></div>
        </div>
      </div>
      <div class="hero-content" :style="{ transform: `translateY(${scrollY * 0.2}px)`, opacity: Math.max(0, 1 - scrollY / 500) }">
        <div class="hero-badge">
          <span class="pulse-dot"></span>
          <span>全国优质医疗资源平台</span>
        </div>
        <h1 class="hero-title">
          <span class="line line-1">让看病</span>
          <span class="line line-2">更简单</span>
        </h1>
        <p class="hero-desc">汇聚全国三甲医院专家资源，提供在线预约挂号、智能分诊、健康咨询一站式服务</p>
        <div class="hero-actions">
          <el-button type="primary" size="large" round @click="scrollToSection('search')">
            <i class="el-icon-date"></i> 立即预约
          </el-button>
          <el-button size="large" round plain @click="openAiAssistant">
            <i class="el-icon-chat-dot-round"></i> AI智能问诊
          </el-button>
        </div>
        <div class="hero-stats">
          <div class="stat-item">
            <span class="stat-num">{{ animatedStats.doctors }}+</span>
            <span class="stat-label">认证医生</span>
          </div>
          <div class="stat-item">
            <span class="stat-num">{{ animatedStats.hospitals }}+</span>
            <span class="stat-label">合作医院</span>
          </div>
          <div class="stat-item">
            <span class="stat-num">{{ animatedStats.patients }}万+</span>
            <span class="stat-label">服务患者</span>
          </div>
        </div>
      </div>
      <div class="scroll-indicator" @click="scrollToSection('search')">
        <span>向下滚动</span>
        <div class="mouse">
          <div class="wheel"></div>
        </div>
      </div>
    </section>

    <!-- 快速搜索区域 -->
    <section id="search" class="search-section" ref="searchSection">
      <div class="section-inner">
        <div class="search-card" :class="{ visible: visibleSections.search }">
          <div class="card-glow"></div>
          <h2 class="section-title">快速预约挂号</h2>
          <p class="section-subtitle">输入症状、医生或医院名称，快速找到适合您的医疗服务</p>
          <div class="search-box">
            <div class="search-tabs">
              <span :class="{ active: searchType === 'symptom' }" @click="searchType = 'symptom'">按症状</span>
              <span :class="{ active: searchType === 'doctor' }" @click="searchType = 'doctor'">按医生</span>
              <span :class="{ active: searchType === 'hospital' }" @click="searchType = 'hospital'">按医院</span>
            </div>
            <div class="search-input-wrap">
              <i class="el-icon-search"></i>
              <input v-model="searchKeyword" :placeholder="searchPlaceholder" @keyup.enter="handleSearch" />
              <el-button type="primary" @click="handleSearch">搜索</el-button>
            </div>
            <div class="hot-searches">
              <span class="label">热门搜索：</span>
              <span class="tag" v-for="tag in hotSearches" :key="tag" @click="quickSearch(tag)">{{ tag }}</span>
            </div>
          </div>
        </div>

        <!-- 功能卡片 - Orano风格大卡片 -->
        <div class="feature-cards" :class="{ visible: visibleSections.search }">
          <div class="feature-card" v-for="(entry, index) in quickEntries" :key="index" 
               :style="{ transitionDelay: index * 0.1 + 's' }" @click="handleEntryClick(entry)">
            <div class="card-bg" :style="{ background: entry.gradient }"></div>
            <div class="card-content">
              <div class="card-icon">
                <i :class="entry.icon"></i>
              </div>
              <h3>{{ entry.title }}</h3>
              <p>{{ entry.desc }}</p>
              <div class="card-arrow">
                <i class="el-icon-arrow-right"></i>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- 推荐医生区域 -->
    <section id="doctors" class="doctors-section" ref="doctorsSection">
      <div class="section-inner">
        <div class="section-header" :class="{ visible: visibleSections.doctors }">
          <div class="header-left">
            <span class="section-tag">名医推荐</span>
            <h2>找到适合您的专家</h2>
            <p>汇聚各科室资深专家，为您提供专业诊疗服务</p>
          </div>
          <el-button type="text" class="more-btn" @click="viewAllDoctors">
            查看全部 <i class="el-icon-arrow-right"></i>
          </el-button>
        </div>
        <div class="doctors-grid">
          <div class="doctor-card" v-for="(doctor, index) in featuredDoctors" :key="doctor.userId"
               :class="{ visible: visibleSections.doctors }" :style="{ transitionDelay: index * 0.1 + 's' }">
            <div class="card-shine"></div>
            <div class="card-top">
              <el-avatar :size="80" :src="doctor.avatar">{{ doctor.nickName ? doctor.nickName.charAt(0) : 'D' }}</el-avatar>
              <div class="doctor-basic">
                <h4>{{ doctor.nickName }}</h4>
                <span class="title">{{ doctor.rank1 || '主治医师' }}</span>
                <span class="dept">{{ doctor.sectionName || '综合科' }}</span>
              </div>
            </div>
            <div class="card-tags">
              <el-tag size="mini" type="success">可预约</el-tag>
              <el-tag size="mini" v-if="doctor.consultCount">{{ doctor.consultCount }}次问诊</el-tag>
            </div>
            <p class="doctor-intro">{{ doctor.remark || '擅长常见病、多发病的诊治，具有丰富的临床经验' }}</p>
            <div class="card-actions">
              <el-button size="small" plain @click="viewDoctorDetail(doctor)">查看详情</el-button>
              <el-button size="small" type="primary" @click="bookDoctor(doctor)">预约挂号</el-button>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- 科室导航区域 - 视差背景 -->
    <section id="departments" class="departments-section" ref="departmentsSection">
      <div class="parallax-bg dept-bg" :style="{ transform: `translateY(${(scrollY - 1800) * 0.3}px)` }"></div>
      <div class="section-inner">
        <div class="section-header light" :class="{ visible: visibleSections.departments }">
          <span class="section-tag">科室导航</span>
          <h2>按科室找医生</h2>
          <p>覆盖各类专科，满足您的就医需求</p>
        </div>
        <div class="dept-grid">
          <div class="dept-card" v-for="(dept, index) in allDepartments" :key="dept.sectionId"
               :class="{ visible: visibleSections.departments }" :style="{ transitionDelay: index * 0.05 + 's' }"
               @click="selectDepartment(dept)">
            <span class="dept-icon">{{ getDeptIcon(dept.sectionName) }}</span>
            <span class="dept-name">{{ dept.sectionName }}</span>
            <span class="dept-count">{{ dept.doctorCount || Math.floor(Math.random() * 20 + 5) }}位医生</span>
          </div>
        </div>
      </div>
    </section>

    <!-- 医院推荐区域 -->
    <section id="hospitals" class="hospitals-section" ref="hospitalsSection">
      <div class="section-inner">
        <div class="section-header" :class="{ visible: visibleSections.hospitals }">
          <div class="header-left">
            <span class="section-tag">合作医院</span>
            <h2>优质医疗机构</h2>
            <p>与全国知名医院建立合作，为您提供便捷的就医通道</p>
          </div>
        </div>
        <div class="hospitals-grid" :class="{ visible: visibleSections.hospitals }">
          <div class="hospital-card" v-for="(hospital, index) in hospitals" :key="index"
               :style="{ transitionDelay: index * 0.1 + 's' }">
            <div class="hospital-logo">{{ hospital.name.substring(0, 2) }}</div>
            <div class="hospital-info">
              <h4>{{ hospital.name }}</h4>
              <div class="hospital-tags">
                <el-tag size="mini" type="danger" v-if="hospital.level">{{ hospital.level }}</el-tag>
                <el-tag size="mini">{{ hospital.type }}</el-tag>
              </div>
              <p class="hospital-address"><i class="el-icon-location"></i>{{ hospital.address }}</p>
            </div>
            <el-button size="small" type="primary" plain>查看详情</el-button>
          </div>
        </div>
      </div>
    </section>

    <!-- 服务流程 -->
    <section class="process-section" ref="processSection">
      <div class="section-inner">
        <div class="section-header center" :class="{ visible: visibleSections.process }">
          <span class="section-tag">就诊流程</span>
          <h2>简单四步，轻松就医</h2>
        </div>
        <div class="process-steps">
          <div class="step-item" v-for="(step, index) in processSteps" :key="index"
               :class="{ visible: visibleSections.process }" :style="{ transitionDelay: index * 0.15 + 's' }">
            <div class="step-number">{{ String(index + 1).padStart(2, '0') }}</div>
            <div class="step-icon"><i :class="step.icon"></i></div>
            <h4>{{ step.title }}</h4>
            <p>{{ step.desc }}</p>
          </div>
          <div class="step-line"></div>
        </div>
      </div>
    </section>

    <!-- 底部 -->
    <footer class="portal-footer">
      <div class="footer-main">
        <div class="footer-brand">
          <div class="logo">
            <svg viewBox="0 0 40 40" fill="none">
              <circle cx="20" cy="20" r="18" stroke="currentColor" stroke-width="2"/>
              <path d="M20 10v20M12 16h16M12 24h16" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
            </svg>
            <span>智慧医疗平台</span>
          </div>
          <p>专业的医疗预约服务平台，让看病更简单</p>
        </div>
        <div class="footer-links">
          <div class="link-group">
            <h4>快速入口</h4>
            <a @click="scrollToSection('search')">预约挂号</a>
            <a @click="scrollToSection('doctors')">找医生</a>
            <a @click="scrollToSection('hospitals')">找医院</a>
            <a @click="scrollToSection('departments')">按科室</a>
          </div>
          <div class="link-group">
            <h4>帮助中心</h4>
            <a href="javascript:;">就诊指南</a>
            <a href="javascript:;">常见问题</a>
            <a href="javascript:;">联系我们</a>
          </div>
        </div>
      </div>
      <div class="footer-bottom">
        <span>© 2024 智慧医疗平台 All Rights Reserved</span>
      </div>
    </footer>

    <!-- AI助手 -->
    <ai-assistant ref="aiAssistant" />

    <!-- 预约弹窗 -->
    <el-dialog :visible.sync="showBookingDialog" title="预约挂号" width="800px" custom-class="booking-dialog">
      <booking-form v-if="showBookingDialog" :department="selectedDepartment" :doctor="selectedDoctor"
        @success="onBookingSuccess" @cancel="showBookingDialog = false" />
    </el-dialog>

    <!-- 我的挂号记录弹窗 -->
    <el-dialog :visible.sync="showRecordsDialog" title="我的挂号记录" width="900px">
      <my-records v-if="showRecordsDialog" />
    </el-dialog>
  </div>
</template>

<script>
import AiAssistant from '@/components/AiAssistant'
import BookingForm from './components/BookingForm'
import MyRecords from './components/MyRecords'
import { getSectionList, getDoctorList } from '@/api/portal'
import { getToken } from '@/utils/auth'
import { mapGetters } from 'vuex'

export default {
  name: 'Portal',
  components: { AiAssistant, BookingForm, MyRecords },
  data() {
    return {
      scrollY: 0,
      currentSection: 'hero',
      searchType: 'symptom',
      searchKeyword: '',
      showBookingDialog: false,
      showRecordsDialog: false,
      selectedDepartment: null,
      selectedDoctor: null,
      visibleSections: { search: false, doctors: false, departments: false, hospitals: false, process: false },
      animatedStats: { doctors: 0, hospitals: 0, patients: 0 },
      hotSearches: ['感冒发烧', '皮肤过敏', '胃痛', '头痛', '失眠'],
      quickEntries: [
        { title: '预约挂号', desc: '在线预约专家号源，免排队', icon: 'el-icon-date', gradient: 'linear-gradient(135deg, #10B981, #059669)', action: 'booking' },
        { title: 'AI智能问诊', desc: '智能分诊，精准推荐科室', icon: 'el-icon-chat-dot-round', gradient: 'linear-gradient(135deg, #6366F1, #4F46E5)', action: 'ai' },
        { title: '找医生', desc: '按专长找专家，查看排班', icon: 'el-icon-user', gradient: 'linear-gradient(135deg, #F59E0B, #D97706)', action: 'doctors' },
        { title: '找医院', desc: '查看合作医院，就近就医', icon: 'el-icon-office-building', gradient: 'linear-gradient(135deg, #EC4899, #DB2777)', action: 'hospitals' }
      ],
      processSteps: [
        { icon: 'el-icon-search', title: '选择科室', desc: '根据症状选择对应科室或直接搜索' },
        { icon: 'el-icon-user', title: '选择医生', desc: '查看医生排班，选择合适的时间' },
        { icon: 'el-icon-edit-outline', title: '填写信息', desc: '填写就诊人信息，确认预约' },
        { icon: 'el-icon-circle-check', title: '到院就诊', desc: '按预约时间到院，出示预约信息' }
      ],
      hospitals: [
        { name: '北京协和医院', level: '三甲', type: '综合医院', address: '北京市东城区帅府园1号' },
        { name: '上海瑞金医院', level: '三甲', type: '综合医院', address: '上海市黄浦区瑞金二路197号' },
        { name: '广州中山医院', level: '三甲', type: '综合医院', address: '广州市越秀区中山二路58号' },
        { name: '四川华西医院', level: '三甲', type: '综合医院', address: '成都市武侯区国学巷37号' }
      ],
      allDepartments: [],
      featuredDoctors: []
    }
  },
  computed: {
    ...mapGetters(['roles', 'name', 'avatar']),
    isLoggedIn() { return !!getToken() },
    isAdmin() { return this.roles && this.roles.includes('admin') },
    userName() { return this.name || '用户' },
    searchPlaceholder() {
      const placeholders = { symptom: '输入症状，如：头痛、发烧、咳嗽', doctor: '输入医生姓名', hospital: '输入医院名称' }
      return placeholders[this.searchType]
    }
  },
  created() { this.loadData() },
  mounted() {
    window.addEventListener('scroll', this.handleScroll)
    this.setupIntersectionObserver()
    this.animateStats()
  },
  beforeDestroy() { window.removeEventListener('scroll', this.handleScroll) },
  methods: {
    async loadData() {
      try {
        const [deptRes, doctorRes] = await Promise.all([
          getSectionList({ pageNum: 1, pageSize: 20, status: '0' }),
          getDoctorList({ pageNum: 1, pageSize: 8 })
        ])
        this.allDepartments = deptRes.rows || []
        this.featuredDoctors = doctorRes.rows || []
      } catch (e) { console.error('加载数据失败', e) }
    },
    handleScroll() {
      this.scrollY = window.scrollY
      const sections = ['hero', 'search', 'doctors', 'departments', 'hospitals']
      for (const id of sections) {
        const el = document.getElementById(id)
        if (el) {
          const rect = el.getBoundingClientRect()
          if (rect.top <= 200 && rect.bottom > 200) { this.currentSection = id; break }
        }
      }
    },
    setupIntersectionObserver() {
      const observer = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
          if (entry.isIntersecting) {
            const id = entry.target.id || entry.target.className.split('-')[0]
            if (id === 'search' || entry.target.classList.contains('search-section')) this.visibleSections.search = true
            if (id === 'doctors' || entry.target.classList.contains('doctors-section')) this.visibleSections.doctors = true
            if (id === 'departments' || entry.target.classList.contains('departments-section')) this.visibleSections.departments = true
            if (id === 'hospitals' || entry.target.classList.contains('hospitals-section')) this.visibleSections.hospitals = true
            if (entry.target.classList.contains('process-section')) this.visibleSections.process = true
          }
        })
      }, { threshold: 0.15 })
      this.$nextTick(() => {
        document.querySelectorAll('section').forEach(section => observer.observe(section))
      })
    },
    animateStats() {
      const targets = { doctors: 5000, hospitals: 200, patients: 100 }
      const duration = 2000
      const start = Date.now()
      const animate = () => {
        const elapsed = Date.now() - start
        const progress = Math.min(elapsed / duration, 1)
        const eased = 1 - Math.pow(1 - progress, 3)
        this.animatedStats.doctors = Math.floor(targets.doctors * eased)
        this.animatedStats.hospitals = Math.floor(targets.hospitals * eased)
        this.animatedStats.patients = Math.floor(targets.patients * eased)
        if (progress < 1) requestAnimationFrame(animate)
      }
      setTimeout(animate, 500)
    },
    scrollToSection(id) {
      const el = document.getElementById(id)
      if (el) el.scrollIntoView({ behavior: 'smooth' })
    },
    getDeptIcon(name) {
      const icons = { '内科': '🫀', '外科': '🔪', '儿科': '👶', '妇科': '👩', '眼科': '👁️', '口腔': '🦷', '皮肤': '🧴', '骨科': '🦴', '神经': '🧠', '心血管': '❤️' }
      for (const key in icons) { if (name && name.includes(key)) return icons[key] }
      return '🏥'
    },
    handleSearch() {
      if (this.searchKeyword) { this.openAiAssistant(); this.$refs.aiAssistant.sendMessage(this.searchKeyword) }
    },
    quickSearch(tag) { this.searchKeyword = tag; this.handleSearch() },
    handleEntryClick(entry) {
      if (entry.action === 'booking') this.scrollToSection('departments')
      else if (entry.action === 'ai') this.openAiAssistant()
      else if (entry.action === 'doctors') this.scrollToSection('doctors')
      else if (entry.action === 'hospitals') this.scrollToSection('hospitals')
    },
    selectDepartment(dept) {
      if (!this.isLoggedIn) { this.promptLogin(); return }
      this.selectedDepartment = dept; this.selectedDoctor = null; this.showBookingDialog = true
    },
    bookDoctor(doctor) {
      if (!this.isLoggedIn) { this.promptLogin(); return }
      this.selectedDoctor = doctor; this.selectedDepartment = null; this.showBookingDialog = true
    },
    viewDoctorDetail(doctor) { this.bookDoctor(doctor) },
    viewAllDoctors() { this.scrollToSection('departments') },
    promptLogin() {
      this.$confirm('请先登录后再进行操作', '提示', { confirmButtonText: '去登录', cancelButtonText: '取消', type: 'info' })
        .then(() => this.goLogin()).catch(() => {})
    },
    onBookingSuccess() { this.showBookingDialog = false; this.$message.success('预约成功！') },
    openAiAssistant() { this.$refs.aiAssistant.open() },
    goLogin() { this.$router.push('/login') },
    goRegister() { this.$router.push('/register') },
    goDataScreen() { this.$router.push('/dashboard') },
    handleCommand(cmd) {
      if (cmd === 'records') this.showRecordsDialog = true
      else if (cmd === 'profile') this.$router.push('/user/profile')
      else if (cmd === 'admin') this.$router.push('/admin')
      else if (cmd === 'logout') this.$store.dispatch('LogOut').then(() => location.reload())
    }
  }
}
</script>

<style lang="scss">
@import './portal.scss';
</style>
