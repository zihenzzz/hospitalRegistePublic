<template>
  <div class="dashboard-container">
    <!-- 欢迎区域 -->
    <div class="welcome-section">
      <div class="welcome-content">
        <div class="welcome-text">
          <h1>欢迎回来，{{ nickName }}</h1>
          <p>{{ currentDate }} · 祝您工作顺利</p>
        </div>
        <div class="welcome-stats">
          <div class="stat-item">
            <span class="stat-value">{{ todayPatients }}</span>
            <span class="stat-label">今日就诊</span>
          </div>
          <div class="stat-item">
            <span class="stat-value">{{ pendingTasks }}</span>
            <span class="stat-label">待处理</span>
          </div>
          <div class="stat-item">
            <span class="stat-value">{{ completedTasks }}</span>
            <span class="stat-label">已完成</span>
          </div>
        </div>
      </div>
    </div>

    <!-- 统计卡片 -->
    <el-row :gutter="20" class="stat-cards">
      <el-col :xs="24" :sm="12" :lg="6">
        <div class="stat-card">
          <div class="stat-icon primary"><i class="el-icon-user"></i></div>
          <div class="stat-info">
            <span class="stat-number">{{ stats.totalPatients }}</span>
            <span class="stat-title">患者总数</span>
            <span class="stat-trend up"><i class="el-icon-top"></i> 12.5%</span>
          </div>
        </div>
      </el-col>
      <el-col :xs="24" :sm="12" :lg="6">
        <div class="stat-card">
          <div class="stat-icon success"><i class="el-icon-s-order"></i></div>
          <div class="stat-info">
            <span class="stat-number">{{ stats.todayAppointments }}</span>
            <span class="stat-title">今日预约</span>
            <span class="stat-trend up"><i class="el-icon-top"></i> 8.2%</span>
          </div>
        </div>
      </el-col>
      <el-col :xs="24" :sm="12" :lg="6">
        <div class="stat-card">
          <div class="stat-icon warning"><i class="el-icon-time"></i></div>
          <div class="stat-info">
            <span class="stat-number">{{ stats.waitingCount }}</span>
            <span class="stat-title">等待就诊</span>
            <span class="stat-trend down"><i class="el-icon-bottom"></i> 3.1%</span>
          </div>
        </div>
      </el-col>
      <el-col :xs="24" :sm="12" :lg="6">
        <div class="stat-card">
          <div class="stat-icon info"><i class="el-icon-s-data"></i></div>
          <div class="stat-info">
            <span class="stat-number">{{ stats.completedToday }}</span>
            <span class="stat-title">今日完成</span>
            <span class="stat-trend up"><i class="el-icon-top"></i> 15.3%</span>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- 主要内容区域 -->
    <el-row :gutter="20" class="main-content">
      <!-- 快捷操作 -->
      <el-col :xs="24" :lg="8">
        <div class="card-box">
          <div class="card-header"><h3>快捷操作</h3></div>
          <div class="quick-actions">
            <div class="action-item" @click="handleAction('register')">
              <div class="action-icon primary"><i class="el-icon-plus"></i></div>
              <span>新增挂号</span>
            </div>
            <div class="action-item" @click="handleAction('appointment')">
              <div class="action-icon success"><i class="el-icon-date"></i></div>
              <span>预约管理</span>
            </div>
            <div class="action-item" @click="handleAction('patient')">
              <div class="action-icon warning"><i class="el-icon-user"></i></div>
              <span>患者查询</span>
            </div>
            <div class="action-item" @click="handleAction('report')">
              <div class="action-icon info"><i class="el-icon-document"></i></div>
              <span>报表统计</span>
            </div>
          </div>
        </div>
      </el-col>

      <!-- 今日预约 -->
      <el-col :xs="24" :lg="16">
        <div class="card-box">
          <div class="card-header">
            <h3>今日预约</h3>
            <el-button type="text" @click="viewAllAppointments">查看全部 <i class="el-icon-arrow-right"></i></el-button>
          </div>
          <el-table :data="appointments" style="width: 100%" size="medium">
            <el-table-column prop="time" label="时间" width="100"></el-table-column>
            <el-table-column prop="patientName" label="患者姓名" width="120"></el-table-column>
            <el-table-column prop="department" label="科室"></el-table-column>
            <el-table-column prop="doctor" label="医生"></el-table-column>
            <el-table-column prop="status" label="状态" width="100">
              <template slot-scope="scope">
                <el-tag :type="getStatusType(scope.row.status)" size="small">{{ scope.row.status }}</el-tag>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </el-col>
    </el-row>

    <!-- 通知公告 -->
    <el-row :gutter="20" class="notice-section">
      <el-col :xs="24" :lg="12">
        <div class="card-box">
          <div class="card-header"><h3>系统公告</h3></div>
          <div class="notice-list">
            <div class="notice-item" v-for="(item, index) in notices" :key="index">
              <div class="notice-icon"><i class="el-icon-bell"></i></div>
              <div class="notice-content">
                <h4>{{ item.title }}</h4>
                <p>{{ item.time }}</p>
              </div>
            </div>
          </div>
        </div>
      </el-col>
      <el-col :xs="24" :lg="12">
        <div class="card-box">
          <div class="card-header"><h3>待办事项</h3></div>
          <div class="todo-list">
            <div class="todo-item" v-for="(item, index) in todos" :key="index">
              <el-checkbox v-model="item.done">{{ item.title }}</el-checkbox>
              <el-tag :type="item.priority === 'high' ? 'danger' : item.priority === 'medium' ? 'warning' : 'info'" size="mini">
                {{ item.priority === 'high' ? '紧急' : item.priority === 'medium' ? '一般' : '低' }}
              </el-tag>
            </div>
          </div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'

export default {
  name: 'Index',
  data() {
    return {
      todayPatients: 128,
      pendingTasks: 15,
      completedTasks: 86,
      stats: { totalPatients: '12,580', todayAppointments: '256', waitingCount: '32', completedToday: '198' },
      appointments: [
        { time: '09:00', patientName: '张三', department: '内科', doctor: '李医生', status: '已就诊' },
        { time: '09:30', patientName: '李四', department: '外科', doctor: '王医生', status: '等待中' },
        { time: '10:00', patientName: '王五', department: '儿科', doctor: '赵医生', status: '等待中' },
        { time: '10:30', patientName: '赵六', department: '妇科', doctor: '钱医生', status: '未到' },
        { time: '11:00', patientName: '孙七', department: '骨科', doctor: '周医生', status: '等待中' }
      ],
      notices: [
        { title: '系统将于今晚22:00进行维护升级', time: '2024-01-15 10:00' },
        { title: '新版本功能更新说明', time: '2024-01-14 15:30' },
        { title: '关于加强信息安全管理的通知', time: '2024-01-13 09:00' }
      ],
      todos: [
        { title: '审核今日新增患者信息', done: false, priority: 'high' },
        { title: '处理预约变更申请', done: false, priority: 'medium' },
        { title: '更新科室排班表', done: true, priority: 'low' },
        { title: '回复患者咨询', done: false, priority: 'medium' }
      ]
    }
  },
  computed: {
    ...mapGetters(['nickName']),
    currentDate() {
      const now = new Date()
      return now.toLocaleDateString('zh-CN', { year: 'numeric', month: 'long', day: 'numeric', weekday: 'long' })
    }
  },
  methods: {
    getStatusType(status) {
      return { '已就诊': 'success', '等待中': 'warning', '未到': 'info', '取消': 'danger' }[status] || 'info'
    },
    handleAction(type) { this.$message.info(`点击了${type}操作`) },
    viewAllAppointments() { this.$message.info('查看全部预约') }
  }
}
</script>

<style lang="scss" scoped>
.dashboard-container { padding: 24px; background: #F8FAFC; min-height: calc(100vh - 104px); }

.welcome-section {
  background: linear-gradient(135deg, #0891B2 0%, #0E7490 50%, #065F73 100%);
  border-radius: 16px; padding: 32px; margin-bottom: 24px; color: #FFFFFF;
  .welcome-content { display: flex; justify-content: space-between; align-items: center; flex-wrap: wrap; gap: 24px; }
  .welcome-text { h1 { font-size: 28px; font-weight: 700; margin: 0 0 8px 0; } p { font-size: 15px; opacity: 0.9; margin: 0; } }
  .welcome-stats { display: flex; gap: 48px;
    .stat-item { text-align: center;
      .stat-value { display: block; font-size: 32px; font-weight: 700; }
      .stat-label { display: block; font-size: 14px; opacity: 0.85; margin-top: 4px; }
    }
  }
}

.stat-cards { margin-bottom: 24px;
  .el-col { margin-bottom: 16px; }
  .stat-card {
    background: #FFFFFF; border-radius: 16px; padding: 24px; display: flex; align-items: center; gap: 20px;
    box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05); transition: all 0.3s;
    &:hover { transform: translateY(-4px); box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1); }
    .stat-icon {
      width: 64px; height: 64px; border-radius: 16px; display: flex; align-items: center; justify-content: center; font-size: 28px;
      &.primary { background: rgba(8, 145, 178, 0.1); color: #0891B2; }
      &.success { background: rgba(16, 185, 129, 0.1); color: #10B981; }
      &.warning { background: rgba(245, 158, 11, 0.1); color: #F59E0B; }
      &.info { background: rgba(99, 102, 241, 0.1); color: #6366F1; }
    }
    .stat-info { flex: 1;
      .stat-number { display: block; font-size: 28px; font-weight: 700; color: #1E293B; }
      .stat-title { display: block; font-size: 14px; color: #64748B; margin: 4px 0; }
      .stat-trend { font-size: 13px; font-weight: 500; &.up { color: #10B981; } &.down { color: #EF4444; } }
    }
  }
}

.card-box {
  background: #FFFFFF; border-radius: 16px; padding: 24px; box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05); height: 100%;
  .card-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px;
    h3 { font-size: 18px; font-weight: 600; color: #1E293B; margin: 0; }
  }
}

.main-content { margin-bottom: 24px; .el-col { margin-bottom: 20px; } }

.quick-actions {
  display: grid; grid-template-columns: repeat(2, 1fr); gap: 16px;
  .action-item {
    display: flex; flex-direction: column; align-items: center; padding: 20px; border-radius: 12px;
    background: #F8FAFC; cursor: pointer; transition: all 0.3s;
    &:hover { background: #F1F5F9; transform: translateY(-2px); }
    .action-icon {
      width: 48px; height: 48px; border-radius: 12px; display: flex; align-items: center; justify-content: center;
      font-size: 22px; margin-bottom: 12px;
      &.primary { background: rgba(8, 145, 178, 0.15); color: #0891B2; }
      &.success { background: rgba(16, 185, 129, 0.15); color: #10B981; }
      &.warning { background: rgba(245, 158, 11, 0.15); color: #F59E0B; }
      &.info { background: rgba(99, 102, 241, 0.15); color: #6366F1; }
    }
    span { font-size: 14px; font-weight: 500; color: #475569; }
  }
}

.notice-section { .el-col { margin-bottom: 20px; } }

.notice-list {
  .notice-item {
    display: flex; align-items: flex-start; gap: 16px; padding: 16px 0; border-bottom: 1px solid #F1F5F9;
    &:last-child { border-bottom: none; }
    .notice-icon { width: 40px; height: 40px; border-radius: 10px; background: rgba(8, 145, 178, 0.1); color: #0891B2; display: flex; align-items: center; justify-content: center; font-size: 18px; flex-shrink: 0; }
    .notice-content { flex: 1; h4 { font-size: 14px; font-weight: 500; color: #1E293B; margin: 0 0 4px 0; } p { font-size: 13px; color: #94A3B8; margin: 0; } }
  }
}

.todo-list {
  .todo-item {
    display: flex; justify-content: space-between; align-items: center; padding: 12px 0; border-bottom: 1px solid #F1F5F9;
    &:last-child { border-bottom: none; }
    :deep(.el-checkbox__label) { color: #475569; font-size: 14px; }
    :deep(.el-checkbox__input.is-checked + .el-checkbox__label) { color: #94A3B8; text-decoration: line-through; }
  }
}

@media screen and (max-width: 768px) {
  .dashboard-container { padding: 16px; }
  .welcome-section { padding: 24px; .welcome-text h1 { font-size: 22px; } .welcome-stats { gap: 24px; .stat-item .stat-value { font-size: 24px; } } }
  .stat-card { padding: 16px; .stat-icon { width: 48px; height: 48px; font-size: 22px; } .stat-info .stat-number { font-size: 22px; } }
}
</style>
