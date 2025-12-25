<template>
  <div class="dashboard-page">
    <!-- 动态背景 -->
    <div class="dashboard-bg">
      <div class="bg-gradient"></div>
      <div class="floating-shapes">
        <div class="shape shape-1"></div>
        <div class="shape shape-2"></div>
        <div class="shape shape-3"></div>
      </div>
    </div>

    <!-- 欢迎横幅 -->
    <section class="welcome-banner">
      <div class="welcome-content">
        <div class="welcome-text">
          <span class="greeting">{{ greeting }}，</span>
          <h1>{{ nickName || '管理员' }}</h1>
          <p>{{ currentDate }} · 祝您工作顺利，一切顺心</p>
        </div>
        <div class="welcome-actions">
          <el-button type="primary" round @click="handleQuickAction('appointment')">
            <i class="el-icon-plus"></i> 新增预约
          </el-button>
          <el-button round plain @click="handleQuickAction('patient')">
            <i class="el-icon-search"></i> 查询患者
          </el-button>
        </div>
      </div>
      <div class="welcome-illustration">
        <div class="pulse-circle"></div>
        <div class="medical-icon">
          <i class="el-icon-first-aid-kit"></i>
        </div>
      </div>
    </section>

    <!-- 实时数据统计 -->
    <section class="stats-section">
      <div class="stat-card" v-for="(stat, index) in realTimeStats" :key="index" :class="stat.type">
        <div class="stat-icon">
          <i :class="stat.icon"></i>
        </div>
        <div class="stat-info">
          <span class="stat-value">
            <span class="counter">{{ stat.value }}</span>
            <span class="stat-unit" v-if="stat.unit">{{ stat.unit }}</span>
          </span>
          <span class="stat-label">{{ stat.label }}</span>
          <div class="stat-trend" :class="stat.trend > 0 ? 'up' : 'down'">
            <i :class="stat.trend > 0 ? 'el-icon-top' : 'el-icon-bottom'"></i>
            <span>{{ Math.abs(stat.trend) }}%</span>
            <span class="trend-text">较昨日</span>
          </div>
        </div>
        <div class="stat-chart">
          <svg viewBox="0 0 100 40" preserveAspectRatio="none">
            <path :d="stat.chartPath" fill="none" stroke="currentColor" stroke-width="2" opacity="0.3"/>
            <path :d="stat.chartPath" fill="url(#gradient)" opacity="0.1"/>
          </svg>
        </div>
      </div>
    </section>

    <!-- 主要内容区 -->
    <section class="main-content">
      <!-- 左侧 -->
      <div class="content-left">
        <!-- 今日预约 -->
        <div class="card appointments-card">
          <div class="card-header">
            <h3><i class="el-icon-date"></i> 今日预约</h3>
            <el-button type="text" @click="viewAllAppointments">查看全部 <i class="el-icon-arrow-right"></i></el-button>
          </div>
          <div class="appointments-timeline">
            <div class="timeline-item" v-for="(apt, index) in todayAppointments" :key="index" :class="apt.status">
              <div class="timeline-time">{{ apt.time }}</div>
              <div class="timeline-content">
                <div class="patient-info">
                  <div class="patient-avatar">{{ apt.name.charAt(0) }}</div>
                  <div class="patient-detail">
                    <span class="patient-name">{{ apt.name }}</span>
                    <span class="patient-dept">{{ apt.department }} · {{ apt.doctor }}</span>
                  </div>
                </div>
                <el-tag :type="getStatusType(apt.status)" size="small" effect="plain">
                  {{ getStatusText(apt.status) }}
                </el-tag>
              </div>
            </div>
          </div>
        </div>

        <!-- 科室就诊统计 -->
        <div class="card chart-card">
          <div class="card-header">
            <h3><i class="el-icon-s-data"></i> 科室就诊统计</h3>
            <el-radio-group v-model="chartPeriod" size="small">
              <el-radio-button label="week">本周</el-radio-button>
              <el-radio-button label="month">本月</el-radio-button>
            </el-radio-group>
          </div>
          <div class="chart-container">
            <div class="bar-chart">
              <div class="bar-item" v-for="(dept, index) in departmentStats" :key="index">
                <div class="bar-label">{{ dept.name }}</div>
                <div class="bar-track">
                  <div class="bar-fill" :style="{ width: dept.percent + '%', background: dept.color }">
                    <span class="bar-value">{{ dept.count }}</span>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 右侧 -->
      <div class="content-right">
        <!-- 快捷操作 -->
        <div class="card quick-actions-card">
          <div class="card-header">
            <h3><i class="el-icon-s-grid"></i> 快捷操作</h3>
          </div>
          <div class="quick-grid">
            <div class="quick-item" v-for="(action, index) in quickActions" :key="index" @click="handleQuickAction(action.key)">
              <div class="quick-icon" :style="{ background: action.gradient }">
                <i :class="action.icon"></i>
              </div>
              <span>{{ action.label }}</span>
            </div>
          </div>
        </div>

        <!-- 待办事项 -->
        <div class="card todo-card">
          <div class="card-header">
            <h3><i class="el-icon-s-claim"></i> 待办事项</h3>
            <el-badge :value="pendingCount" :max="99" class="todo-badge">
              <el-button type="text" size="small">全部</el-button>
            </el-badge>
          </div>
          <div class="todo-list">
            <div class="todo-item" v-for="(todo, index) in todoList" :key="index" :class="{ done: todo.done }">
              <el-checkbox v-model="todo.done" @change="handleTodoChange(todo)"></el-checkbox>
              <div class="todo-content">
                <span class="todo-title">{{ todo.title }}</span>
                <span class="todo-time">{{ todo.time }}</span>
              </div>
              <el-tag :type="getPriorityType(todo.priority)" size="mini" effect="dark">
                {{ getPriorityText(todo.priority) }}
              </el-tag>
            </div>
          </div>
          <div class="todo-add">
            <el-input v-model="newTodo" placeholder="添加新待办..." size="small" @keyup.enter.native="addTodo">
              <el-button slot="append" icon="el-icon-plus" @click="addTodo"></el-button>
            </el-input>
          </div>
        </div>

        <!-- 系统公告 -->
        <div class="card notice-card">
          <div class="card-header">
            <h3><i class="el-icon-bell"></i> 系统公告</h3>
          </div>
          <div class="notice-list">
            <div class="notice-item" v-for="(notice, index) in notices" :key="index" @click="viewNotice(notice)">
              <div class="notice-icon" :class="notice.type">
                <i :class="getNoticeIcon(notice.type)"></i>
              </div>
              <div class="notice-content">
                <span class="notice-title">{{ notice.title }}</span>
                <span class="notice-time">{{ notice.time }}</span>
              </div>
              <i class="el-icon-arrow-right"></i>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- 底部信息 -->
    <section class="bottom-section">
      <!-- 最近患者 -->
      <div class="card patients-card">
        <div class="card-header">
          <h3><i class="el-icon-user"></i> 最近就诊患者</h3>
          <el-button type="text">更多 <i class="el-icon-arrow-right"></i></el-button>
        </div>
        <el-table :data="recentPatients" style="width: 100%" size="medium" :show-header="true">
          <el-table-column prop="name" label="姓名" width="100">
            <template slot-scope="scope">
              <div class="patient-cell">
                <span class="avatar">{{ scope.row.name.charAt(0) }}</span>
                <span>{{ scope.row.name }}</span>
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="gender" label="性别" width="80"></el-table-column>
          <el-table-column prop="age" label="年龄" width="80"></el-table-column>
          <el-table-column prop="department" label="就诊科室"></el-table-column>
          <el-table-column prop="doctor" label="主治医生"></el-table-column>
          <el-table-column prop="visitTime" label="就诊时间" width="160"></el-table-column>
          <el-table-column prop="status" label="状态" width="100">
            <template slot-scope="scope">
              <el-tag :type="scope.row.status === '已完成' ? 'success' : 'warning'" size="small">
                {{ scope.row.status }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="120">
            <template slot-scope="scope">
              <el-button type="text" size="small" @click="viewPatient(scope.row)">查看</el-button>
              <el-button type="text" size="small" @click="editPatient(scope.row)">编辑</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </section>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'

export default {
  name: 'Index',
  data() {
    return {
      chartPeriod: 'week',
      newTodo: '',
      realTimeStats: [
        { icon: 'el-icon-user', label: '今日就诊', value: 156, unit: '人', trend: 12.5, type: 'primary', chartPath: 'M0,30 Q25,20 50,25 T100,15' },
        { icon: 'el-icon-date', label: '预约挂号', value: 89, unit: '人', trend: 8.3, type: 'success', chartPath: 'M0,25 Q25,30 50,20 T100,10' },
        { icon: 'el-icon-time', label: '等待就诊', value: 23, unit: '人', trend: -5.2, type: 'warning', chartPath: 'M0,20 Q25,25 50,15 T100,20' },
        { icon: 'el-icon-s-data', label: '今日收入', value: 12.8, unit: '万', trend: 15.6, type: 'info', chartPath: 'M0,35 Q25,15 50,20 T100,5' }
      ],
      todayAppointments: [
        { time: '09:00', name: '张三', department: '内科', doctor: '李医生', status: 'completed' },
        { time: '09:30', name: '李四', department: '外科', doctor: '王医生', status: 'in-progress' },
        { time: '10:00', name: '王五', department: '儿科', doctor: '赵医生', status: 'waiting' },
        { time: '10:30', name: '赵六', department: '妇科', doctor: '钱医生', status: 'waiting' },
        { time: '11:00', name: '孙七', department: '骨科', doctor: '周医生', status: 'cancelled' }
      ],
      departmentStats: [
        { name: '内科', count: 45, percent: 90, color: 'linear-gradient(90deg, #0891B2, #06B6D4)' },
        { name: '外科', count: 38, percent: 76, color: 'linear-gradient(90deg, #6366F1, #818CF8)' },
        { name: '儿科', count: 32, percent: 64, color: 'linear-gradient(90deg, #10B981, #34D399)' },
        { name: '妇科', count: 28, percent: 56, color: 'linear-gradient(90deg, #F59E0B, #FBBF24)' },
        { name: '骨科', count: 22, percent: 44, color: 'linear-gradient(90deg, #EC4899, #F472B6)' }
      ],
      quickActions: [
        { key: 'appointment', icon: 'el-icon-plus', label: '新增预约', gradient: 'linear-gradient(135deg, #0891B2, #06B6D4)' },
        { key: 'patient', icon: 'el-icon-user', label: '患者管理', gradient: 'linear-gradient(135deg, #6366F1, #818CF8)' },
        { key: 'schedule', icon: 'el-icon-date', label: '排班管理', gradient: 'linear-gradient(135deg, #10B981, #34D399)' },
        { key: 'report', icon: 'el-icon-document', label: '报表统计', gradient: 'linear-gradient(135deg, #F59E0B, #FBBF24)' },
        { key: 'medicine', icon: 'el-icon-first-aid-kit', label: '药品管理', gradient: 'linear-gradient(135deg, #EC4899, #F472B6)' },
        { key: 'finance', icon: 'el-icon-money', label: '财务管理', gradient: 'linear-gradient(135deg, #EF4444, #F87171)' }
      ],
      todoList: [
        { id: 1, title: '审核今日新增患者信息', time: '10:00', priority: 'high', done: false },
        { id: 2, title: '处理3条预约变更申请', time: '11:30', priority: 'medium', done: false },
        { id: 3, title: '更新本周科室排班表', time: '14:00', priority: 'low', done: true },
        { id: 4, title: '回复患者在线咨询', time: '15:30', priority: 'medium', done: false }
      ],
      notices: [
        { id: 1, title: '系统将于今晚22:00进行维护升级', time: '10分钟前', type: 'warning' },
        { id: 2, title: '新版本功能更新说明已发布', time: '1小时前', type: 'info' },
        { id: 3, title: '关于加强信息安全管理的通知', time: '2小时前', type: 'success' }
      ],
      recentPatients: [
        { id: 1, name: '张三', gender: '男', age: 35, department: '内科', doctor: '李医生', visitTime: '2024-01-15 09:30', status: '已完成' },
        { id: 2, name: '李四', gender: '女', age: 28, department: '外科', doctor: '王医生', visitTime: '2024-01-15 10:00', status: '就诊中' },
        { id: 3, name: '王五', gender: '男', age: 45, department: '儿科', doctor: '赵医生', visitTime: '2024-01-15 10:30', status: '已完成' },
        { id: 4, name: '赵六', gender: '女', age: 32, department: '妇科', doctor: '钱医生', visitTime: '2024-01-15 11:00', status: '已完成' }
      ]
    }
  },
  computed: {
    ...mapGetters(['nickName']),
    greeting() {
      const hour = new Date().getHours()
      if (hour < 12) return '早上好'
      if (hour < 18) return '下午好'
      return '晚上好'
    },
    currentDate() {
      return new Date().toLocaleDateString('zh-CN', { year: 'numeric', month: 'long', day: 'numeric', weekday: 'long' })
    },
    pendingCount() {
      return this.todoList.filter(t => !t.done).length
    }
  },
  methods: {
    getStatusType(status) {
      const types = { completed: 'success', 'in-progress': 'primary', waiting: 'warning', cancelled: 'info' }
      return types[status] || 'info'
    },
    getStatusText(status) {
      const texts = { completed: '已完成', 'in-progress': '就诊中', waiting: '等待中', cancelled: '已取消' }
      return texts[status] || status
    },
    getPriorityType(priority) {
      const types = { high: 'danger', medium: 'warning', low: 'info' }
      return types[priority] || 'info'
    },
    getPriorityText(priority) {
      const texts = { high: '紧急', medium: '一般', low: '低' }
      return texts[priority] || priority
    },
    getNoticeIcon(type) {
      const icons = { warning: 'el-icon-warning', info: 'el-icon-info', success: 'el-icon-success' }
      return icons[type] || 'el-icon-bell'
    },
    handleQuickAction(key) {
      this.$message.info(`点击了 ${key} 操作`)
    },
    viewAllAppointments() {
      this.$message.info('查看全部预约')
    },
    handleTodoChange(todo) {
      this.$message.success(todo.done ? '已完成' : '已恢复')
    },
    addTodo() {
      if (this.newTodo.trim()) {
        this.todoList.unshift({ id: Date.now(), title: this.newTodo, time: '刚刚', priority: 'medium', done: false })
        this.newTodo = ''
        this.$message.success('添加成功')
      }
    },
    viewNotice(notice) {
      this.$message.info(`查看公告: ${notice.title}`)
    },
    viewPatient(row) {
      this.$message.info(`查看患者: ${row.name}`)
    },
    editPatient(row) {
      this.$message.info(`编辑患者: ${row.name}`)
    }
  }
}
</script>


<style lang="scss" scoped>
$primary: #0891B2;
$success: #10B981;
$warning: #F59E0B;
$danger: #EF4444;
$info: #6366F1;
$dark: #1E293B;
$gray: #64748B;

.dashboard-page {
  min-height: calc(100vh - 104px);
  padding: 24px;
  position: relative;
  overflow: hidden;
}

/* 动态背景 */
.dashboard-bg {
  position: fixed;
  top: 0;
  left: 240px;
  right: 0;
  bottom: 0;
  pointer-events: none;
  z-index: 0;
  
  .bg-gradient {
    position: absolute;
    inset: 0;
    background: linear-gradient(135deg, rgba($primary, 0.02) 0%, rgba($info, 0.02) 100%);
  }
  
  .floating-shapes {
    position: absolute;
    inset: 0;
    overflow: hidden;
    
    .shape {
      position: absolute;
      border-radius: 50%;
      opacity: 0.5;
      animation: float 20s ease-in-out infinite;
      
      &.shape-1 {
        width: 400px;
        height: 400px;
        background: radial-gradient(circle, rgba($primary, 0.08) 0%, transparent 70%);
        top: -100px;
        right: -100px;
        animation-delay: 0s;
      }
      
      &.shape-2 {
        width: 300px;
        height: 300px;
        background: radial-gradient(circle, rgba($success, 0.08) 0%, transparent 70%);
        bottom: 10%;
        left: 10%;
        animation-delay: -5s;
      }
      
      &.shape-3 {
        width: 200px;
        height: 200px;
        background: radial-gradient(circle, rgba($info, 0.08) 0%, transparent 70%);
        top: 40%;
        right: 20%;
        animation-delay: -10s;
      }
    }
  }
}

@keyframes float {
  0%, 100% { transform: translate(0, 0) scale(1); }
  25% { transform: translate(20px, -20px) scale(1.05); }
  50% { transform: translate(-10px, 20px) scale(0.95); }
  75% { transform: translate(-20px, -10px) scale(1.02); }
}

/* 欢迎横幅 */
.welcome-banner {
  position: relative;
  z-index: 1;
  background: linear-gradient(135deg, $primary 0%, #0E7490 50%, #065F73 100%);
  border-radius: 24px;
  padding: 40px;
  margin-bottom: 24px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  overflow: hidden;
  
  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: url("data:image/svg+xml,%3Csvg width='60' height='60' viewBox='0 0 60 60' xmlns='http://www.w3.org/2000/svg'%3E%3Cg fill='none' fill-rule='evenodd'%3E%3Cg fill='%23ffffff' fill-opacity='0.05'%3E%3Cpath d='M36 34v-4h-2v4h-4v2h4v4h2v-4h4v-2h-4zm0-30V0h-2v4h-4v2h4v4h2V6h4V4h-4zM6 34v-4H4v4H0v2h4v4h2v-4h4v-2H6zM6 4V0H4v4H0v2h4v4h2V6h4V4H6z'/%3E%3C/g%3E%3C/g%3E%3C/svg%3E");
    opacity: 0.5;
  }
  
  .welcome-content {
    position: relative;
    z-index: 1;
    color: #fff;
    
    .greeting {
      font-size: 16px;
      opacity: 0.9;
    }
    
    h1 {
      font-size: 32px;
      font-weight: 700;
      margin: 8px 0 12px;
    }
    
    p {
      font-size: 14px;
      opacity: 0.85;
      margin: 0 0 24px;
    }
    
    .welcome-actions {
      display: flex;
      gap: 12px;
      
      .el-button {
        border-color: rgba(255,255,255,0.3);
        &.el-button--primary { background: #fff; color: $primary; border-color: #fff; }
        &.is-plain { color: #fff; &:hover { background: rgba(255,255,255,0.1); } }
      }
    }
  }
  
  .welcome-illustration {
    position: relative;
    width: 120px;
    height: 120px;
    
    .pulse-circle {
      position: absolute;
      inset: 0;
      border: 2px solid rgba(255,255,255,0.3);
      border-radius: 50%;
      animation: pulse 2s ease-out infinite;
    }
    
    .medical-icon {
      position: absolute;
      inset: 20px;
      background: rgba(255,255,255,0.15);
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      
      i { font-size: 40px; color: #fff; }
    }
  }
}

@keyframes pulse {
  0% { transform: scale(1); opacity: 1; }
  100% { transform: scale(1.5); opacity: 0; }
}

/* 统计卡片 */
.stats-section {
  position: relative;
  z-index: 1;
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
  margin-bottom: 24px;
}

.stat-card {
  background: #fff;
  border-radius: 20px;
  padding: 24px;
  display: flex;
  align-items: flex-start;
  gap: 16px;
  position: relative;
  overflow: hidden;
  box-shadow: 0 4px 20px rgba(0,0,0,0.04);
  transition: all 0.3s;
  
  &:hover {
    transform: translateY(-4px);
    box-shadow: 0 12px 30px rgba(0,0,0,0.08);
  }
  
  .stat-icon {
    width: 56px;
    height: 56px;
    border-radius: 16px;
    display: flex;
    align-items: center;
    justify-content: center;
    flex-shrink: 0;
    
    i { font-size: 26px; color: #fff; }
  }
  
  &.primary .stat-icon { background: linear-gradient(135deg, $primary, #06B6D4); }
  &.success .stat-icon { background: linear-gradient(135deg, $success, #34D399); }
  &.warning .stat-icon { background: linear-gradient(135deg, $warning, #FBBF24); }
  &.info .stat-icon { background: linear-gradient(135deg, $info, #818CF8); }
  
  .stat-info {
    flex: 1;
    min-width: 0;
    
    .stat-value {
      display: flex;
      align-items: baseline;
      gap: 4px;
      
      .counter { font-size: 32px; font-weight: 700; color: $dark; }
      .stat-unit { font-size: 14px; color: $gray; }
    }
    
    .stat-label {
      display: block;
      font-size: 14px;
      color: $gray;
      margin: 4px 0 8px;
    }
    
    .stat-trend {
      display: flex;
      align-items: center;
      gap: 4px;
      font-size: 13px;
      
      &.up { color: $success; }
      &.down { color: $danger; }
      
      .trend-text { color: $gray; margin-left: 4px; }
    }
  }
  
  .stat-chart {
    position: absolute;
    bottom: 0;
    right: 0;
    width: 100px;
    height: 50px;
    opacity: 0.5;
    
    svg { width: 100%; height: 100%; }
  }
  
  &.primary .stat-chart { color: $primary; }
  &.success .stat-chart { color: $success; }
  &.warning .stat-chart { color: $warning; }
  &.info .stat-chart { color: $info; }
}

/* 主要内容区 */
.main-content {
  position: relative;
  z-index: 1;
  display: grid;
  grid-template-columns: 1fr 380px;
  gap: 24px;
  margin-bottom: 24px;
}

.content-left {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.content-right {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

/* 通用卡片 */
.card {
  background: #fff;
  border-radius: 20px;
  padding: 24px;
  box-shadow: 0 4px 20px rgba(0,0,0,0.04);
  
  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
    
    h3 {
      font-size: 18px;
      font-weight: 600;
      color: $dark;
      margin: 0;
      display: flex;
      align-items: center;
      gap: 8px;
      
      i { color: $primary; }
    }
  }
}

/* 预约时间线 */
.appointments-timeline {
  .timeline-item {
    display: flex;
    gap: 16px;
    padding: 16px 0;
    border-bottom: 1px solid #F1F5F9;
    transition: all 0.2s;
    
    &:last-child { border-bottom: none; }
    &:hover { background: #F8FAFC; margin: 0 -24px; padding: 16px 24px; }
    
    .timeline-time {
      width: 50px;
      font-size: 14px;
      font-weight: 600;
      color: $dark;
      flex-shrink: 0;
    }
    
    .timeline-content {
      flex: 1;
      display: flex;
      justify-content: space-between;
      align-items: center;
    }
    
    .patient-info {
      display: flex;
      align-items: center;
      gap: 12px;
      
      .patient-avatar {
        width: 40px;
        height: 40px;
        background: linear-gradient(135deg, $primary, $info);
        border-radius: 12px;
        display: flex;
        align-items: center;
        justify-content: center;
        color: #fff;
        font-weight: 600;
      }
      
      .patient-detail {
        display: flex;
        flex-direction: column;
        
        .patient-name { font-size: 15px; font-weight: 500; color: $dark; }
        .patient-dept { font-size: 13px; color: $gray; }
      }
    }
  }
}

/* 柱状图 */
.bar-chart {
  .bar-item {
    margin-bottom: 16px;
    
    &:last-child { margin-bottom: 0; }
    
    .bar-label {
      font-size: 14px;
      color: $dark;
      margin-bottom: 8px;
    }
    
    .bar-track {
      height: 28px;
      background: #F1F5F9;
      border-radius: 8px;
      overflow: hidden;
    }
    
    .bar-fill {
      height: 100%;
      border-radius: 8px;
      display: flex;
      align-items: center;
      justify-content: flex-end;
      padding-right: 12px;
      transition: width 0.8s ease;
      
      .bar-value {
        font-size: 13px;
        font-weight: 600;
        color: #fff;
      }
    }
  }
}

/* 快捷操作 */
.quick-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 12px;
}

.quick-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 10px;
  padding: 20px 12px;
  border-radius: 16px;
  background: #F8FAFC;
  cursor: pointer;
  transition: all 0.3s;
  
  &:hover {
    background: #fff;
    box-shadow: 0 8px 24px rgba(0,0,0,0.08);
    transform: translateY(-4px);
  }
  
  .quick-icon {
    width: 48px;
    height: 48px;
    border-radius: 14px;
    display: flex;
    align-items: center;
    justify-content: center;
    
    i { font-size: 22px; color: #fff; }
  }
  
  span { font-size: 13px; color: $dark; font-weight: 500; }
}

/* 待办事项 */
.todo-list {
  max-height: 280px;
  overflow-y: auto;
  
  .todo-item {
    display: flex;
    align-items: center;
    gap: 12px;
    padding: 14px 0;
    border-bottom: 1px solid #F1F5F9;
    
    &:last-child { border-bottom: none; }
    
    &.done {
      .todo-title { text-decoration: line-through; color: $gray; }
    }
    
    .todo-content {
      flex: 1;
      min-width: 0;
      
      .todo-title {
        display: block;
        font-size: 14px;
        color: $dark;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
      }
      
      .todo-time { font-size: 12px; color: $gray; }
    }
  }
}

.todo-add {
  margin-top: 16px;
  padding-top: 16px;
  border-top: 1px solid #F1F5F9;
}

/* 公告列表 */
.notice-list {
  .notice-item {
    display: flex;
    align-items: center;
    gap: 14px;
    padding: 14px 0;
    border-bottom: 1px solid #F1F5F9;
    cursor: pointer;
    transition: all 0.2s;
    
    &:last-child { border-bottom: none; }
    &:hover { background: #F8FAFC; margin: 0 -24px; padding: 14px 24px; }
    
    .notice-icon {
      width: 40px;
      height: 40px;
      border-radius: 12px;
      display: flex;
      align-items: center;
      justify-content: center;
      flex-shrink: 0;
      
      i { font-size: 18px; }
      
      &.warning { background: rgba($warning, 0.1); i { color: $warning; } }
      &.info { background: rgba($info, 0.1); i { color: $info; } }
      &.success { background: rgba($success, 0.1); i { color: $success; } }
    }
    
    .notice-content {
      flex: 1;
      min-width: 0;
      
      .notice-title {
        display: block;
        font-size: 14px;
        color: $dark;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
      }
      
      .notice-time { font-size: 12px; color: $gray; }
    }
    
    > i { color: #CBD5E1; }
  }
}

/* 底部区域 */
.bottom-section {
  position: relative;
  z-index: 1;
}

.patients-card {
  .patient-cell {
    display: flex;
    align-items: center;
    gap: 10px;
    
    .avatar {
      width: 32px;
      height: 32px;
      background: linear-gradient(135deg, $primary, $info);
      border-radius: 8px;
      display: flex;
      align-items: center;
      justify-content: center;
      color: #fff;
      font-size: 13px;
      font-weight: 600;
    }
  }
}

/* 响应式 */
@media (max-width: 1400px) {
  .stats-section { grid-template-columns: repeat(2, 1fr); }
}

@media (max-width: 1200px) {
  .main-content { grid-template-columns: 1fr; }
  .content-right { display: grid; grid-template-columns: repeat(2, 1fr); gap: 24px; }
}

@media (max-width: 768px) {
  .dashboard-page { padding: 16px; }
  .welcome-banner { flex-direction: column; text-align: center; padding: 24px;
    .welcome-illustration { display: none; }
    .welcome-actions { justify-content: center; }
  }
  .stats-section { grid-template-columns: 1fr; }
  .content-right { grid-template-columns: 1fr; }
  .quick-grid { grid-template-columns: repeat(2, 1fr); }
}
</style>
