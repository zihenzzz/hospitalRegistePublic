<template>
  <div class="data-screen">
    <!-- 顶部标题 -->
    <header class="screen-header">
      <div class="header-left">
        <span class="time">{{ currentTime }}</span>
      </div>
      <h1 class="title">智慧医疗数据大屏</h1>
      <div class="header-right">
        <el-button type="text" @click="goBack">
          <i class="el-icon-back"></i> 返回首页
        </el-button>
      </div>
    </header>

    <!-- 主体内容 -->
    <main class="screen-main">
      <!-- 左侧 -->
      <div class="screen-left">
        <div class="panel">
          <div class="panel-header">
            <span class="panel-title">今日挂号统计</span>
          </div>
          <div class="panel-body">
            <div class="stat-cards">
              <div class="stat-card">
                <div class="stat-icon blue"><i class="el-icon-tickets"></i></div>
                <div class="stat-info">
                  <span class="stat-value">{{ dashData.todayRegistrations || 0 }}</span>
                  <span class="stat-label">今日挂号</span>
                </div>
              </div>
              <div class="stat-card">
                <div class="stat-icon green"><i class="el-icon-check"></i></div>
                <div class="stat-info">
                  <span class="stat-value">{{ dashData.todayCompleted || 0 }}</span>
                  <span class="stat-label">已完成</span>
                </div>
              </div>
              <div class="stat-card">
                <div class="stat-icon orange"><i class="el-icon-time"></i></div>
                <div class="stat-info">
                  <span class="stat-value">{{ dashData.todayPending || 0 }}</span>
                  <span class="stat-label">待就诊</span>
                </div>
              </div>
            </div>
          </div>
        </div>

        <div class="panel">
          <div class="panel-header">
            <span class="panel-title">科室挂号排行</span>
          </div>
          <div class="panel-body">
            <div class="rank-list">
              <div class="rank-item" v-for="(item, index) in dashData.sectionRank || []" :key="index">
                <span class="rank-no" :class="'rank-' + (index + 1)">{{ index + 1 }}</span>
                <span class="rank-name">{{ item.name }}</span>
                <div class="rank-bar">
                  <div class="bar-fill" :style="{ width: getBarWidth(item.count) }"></div>
                </div>
                <span class="rank-count">{{ item.count }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 中间 -->
      <div class="screen-center">
        <div class="center-top">
          <div class="big-number">
            <div class="number-item">
              <span class="number">{{ dashData.totalDoctors || 0 }}</span>
              <span class="label">医生总数</span>
            </div>
            <div class="number-item">
              <span class="number">{{ dashData.totalSections || 0 }}</span>
              <span class="label">科室数量</span>
            </div>
            <div class="number-item">
              <span class="number">{{ dashData.totalRegistrations || 0 }}</span>
              <span class="label">累计挂号</span>
            </div>
            <div class="number-item">
              <span class="number">{{ dashData.totalPatients || 0 }}</span>
              <span class="label">服务患者</span>
            </div>
          </div>
        </div>

        <div class="panel center-chart">
          <div class="panel-header">
            <span class="panel-title">近7日挂号趋势</span>
          </div>
          <div class="panel-body">
            <div ref="trendChart" class="chart-container"></div>
          </div>
        </div>
      </div>

      <!-- 右侧 -->
      <div class="screen-right">
        <div class="panel">
          <div class="panel-header">
            <span class="panel-title">医生接诊排行</span>
          </div>
          <div class="panel-body">
            <div class="doctor-rank">
              <div class="doctor-item" v-for="(doc, index) in dashData.doctorRank || []" :key="index">
                <span class="rank-badge" :class="'top-' + (index + 1)">{{ index + 1 }}</span>
                <el-avatar :size="36">{{ doc.name ? doc.name.charAt(0) : '' }}</el-avatar>
                <div class="doctor-info">
                  <span class="name">{{ doc.name }}</span>
                  <span class="section">{{ doc.section }}</span>
                </div>
                <span class="count">{{ doc.count }}人</span>
              </div>
            </div>
          </div>
        </div>

        <div class="panel">
          <div class="panel-header">
            <span class="panel-title">挂号时段分布</span>
          </div>
          <div class="panel-body">
            <div ref="timeChart" class="chart-container"></div>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { getDashboardData } from '@/api/portal'

export default {
  name: 'DataScreen',
  data() {
    return {
      currentTime: '',
      dashData: {},
      trendChart: null,
      timeChart: null
    }
  },
  created() {
    this.updateTime()
    setInterval(this.updateTime, 1000)
    this.loadData()
  },
  mounted() {
    this.$nextTick(() => {
      this.initCharts()
    })
    window.addEventListener('resize', this.handleResize)
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.handleResize)
    if (this.trendChart) this.trendChart.dispose()
    if (this.timeChart) this.timeChart.dispose()
  },
  methods: {
    updateTime() {
      const now = new Date()
      this.currentTime = now.toLocaleString('zh-CN', {
        year: 'numeric', month: '2-digit', day: '2-digit',
        hour: '2-digit', minute: '2-digit', second: '2-digit'
      })
    },
    
    async loadData() {
      try {
        const res = await getDashboardData()
        this.dashData = res.data || this.getMockData()
      } catch (error) {
        console.log('使用模拟数据')
        this.dashData = this.getMockData()
      }
      this.$nextTick(() => {
        this.updateCharts()
      })
    },
    
    getMockData() {
      return {
        todayRegistrations: 156,
        todayCompleted: 89,
        todayPending: 67,
        totalDoctors: 52,
        totalSections: 18,
        totalRegistrations: 12580,
        totalPatients: 8960,
        sectionRank: [
          { name: '内科', count: 45 },
          { name: '外科', count: 38 },
          { name: '儿科', count: 32 },
          { name: '妇科', count: 28 },
          { name: '骨科', count: 25 }
        ],
        doctorRank: [
          { name: '张医生', section: '内科', count: 28 },
          { name: '李医生', section: '外科', count: 25 },
          { name: '王医生', section: '儿科', count: 22 },
          { name: '赵医生', section: '妇科', count: 20 },
          { name: '刘医生', section: '骨科', count: 18 }
        ],
        trendData: {
          dates: ['12-18', '12-19', '12-20', '12-21', '12-22', '12-23', '12-24'],
          values: [120, 145, 132, 168, 155, 142, 156]
        },
        timeDistribution: [
          { name: '上午', value: 45 },
          { name: '下午', value: 35 },
          { name: '晚上', value: 20 }
        ]
      }
    },
    
    getBarWidth(count) {
      const max = Math.max(...(this.dashData.sectionRank || []).map(i => i.count), 1)
      return (count / max * 100) + '%'
    },
    
    initCharts() {
      // 趋势图
      this.trendChart = echarts.init(this.$refs.trendChart)
      // 时段分布图
      this.timeChart = echarts.init(this.$refs.timeChart)
    },
    
    updateCharts() {
      if (!this.trendChart || !this.timeChart) return
      
      // 趋势图配置
      const trendOption = {
        grid: { top: 20, right: 20, bottom: 30, left: 50 },
        xAxis: {
          type: 'category',
          data: this.dashData.trendData && this.dashData.trendData.dates ? this.dashData.trendData.dates : [],
          axisLine: { lineStyle: { color: 'rgba(255,255,255,0.2)' } },
          axisLabel: { color: 'rgba(255,255,255,0.7)' }
        },
        yAxis: {
          type: 'value',
          axisLine: { show: false },
          splitLine: { lineStyle: { color: 'rgba(255,255,255,0.1)' } },
          axisLabel: { color: 'rgba(255,255,255,0.7)' }
        },
        series: [{
          type: 'line',
          data: this.dashData.trendData && this.dashData.trendData.values ? this.dashData.trendData.values : [],
          smooth: true,
          symbol: 'circle',
          symbolSize: 8,
          lineStyle: { color: '#10b981', width: 3 },
          itemStyle: { color: '#10b981' },
          areaStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              { offset: 0, color: 'rgba(16, 185, 129, 0.4)' },
              { offset: 1, color: 'rgba(16, 185, 129, 0.05)' }
            ])
          }
        }]
      }
      this.trendChart.setOption(trendOption)
      
      // 时段分布图配置
      const timeOption = {
        series: [{
          type: 'pie',
          radius: ['50%', '70%'],
          center: ['50%', '50%'],
          data: (this.dashData.timeDistribution || []).map((item, i) => ({
            ...item,
            itemStyle: { color: ['#10b981', '#6366f1', '#f59e0b'][i] }
          })),
          label: {
            color: 'rgba(255,255,255,0.8)',
            formatter: '{b}\n{d}%'
          }
        }]
      }
      this.timeChart.setOption(timeOption)
    },
    
    handleResize() {
      if (this.trendChart) this.trendChart.resize()
      if (this.timeChart) this.timeChart.resize()
    },
    
    goBack() {
      this.$router.push('/')
    }
  }
}
</script>

<style lang="scss" scoped>
.data-screen {
  width: 100vw;
  height: 100vh;
  background: linear-gradient(135deg, #0f172a 0%, #1e293b 100%);
  overflow: hidden;
  display: flex;
  flex-direction: column;
}

.screen-header {
  height: 80px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 40px;
  background: linear-gradient(180deg, rgba(16, 185, 129, 0.3) 0%, transparent 100%);
  border-bottom: 1px solid rgba(16, 185, 129, 0.3);
  
  .header-left, .header-right { width: 200px; }
  .header-right { text-align: right; }
  
  .time {
    color: rgba(255, 255, 255, 0.7);
    font-size: 14px;
  }
  
  .title {
    font-size: 28px;
    font-weight: 700;
    background: linear-gradient(90deg, #10b981, #34d399);
    -webkit-background-clip: text;
    background-clip: text;
    -webkit-text-fill-color: transparent;
    margin: 0;
    letter-spacing: 4px;
  }
  
  .el-button { color: rgba(255, 255, 255, 0.7); }
}

.screen-main {
  flex: 1;
  display: flex;
  padding: 20px;
  gap: 20px;
  overflow: hidden;
}

.screen-left, .screen-right {
  width: 320px;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.screen-center {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.panel {
  background: rgba(255, 255, 255, 0.05);
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 12px;
  overflow: hidden;
  
  .panel-header {
    padding: 16px 20px;
    border-bottom: 1px solid rgba(255, 255, 255, 0.1);
    
    .panel-title {
      font-size: 16px;
      font-weight: 600;
      color: #fff;
      position: relative;
      padding-left: 12px;
      
      &::before {
        content: '';
        position: absolute;
        left: 0;
        top: 50%;
        transform: translateY(-50%);
        width: 4px;
        height: 16px;
        background: linear-gradient(180deg, #10b981, #34d399);
        border-radius: 2px;
      }
    }
  }
  
  .panel-body { padding: 20px; }
}

.stat-cards {
  display: flex;
  flex-direction: column;
  gap: 16px;
  
  .stat-card {
    display: flex;
    align-items: center;
    gap: 16px;
    padding: 16px;
    background: rgba(255, 255, 255, 0.05);
    border-radius: 10px;
    
    .stat-icon {
      width: 48px;
      height: 48px;
      border-radius: 12px;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 24px;
      
      &.blue { background: rgba(16, 185, 129, 0.2); color: #10b981; }
      &.green { background: rgba(16, 185, 129, 0.2); color: #10b981; }
      &.orange { background: rgba(245, 158, 11, 0.2); color: #f59e0b; }
    }
    
    .stat-info {
      .stat-value {
        display: block;
        font-size: 28px;
        font-weight: 700;
        color: #fff;
      }
      .stat-label {
        font-size: 13px;
        color: rgba(255, 255, 255, 0.6);
      }
    }
  }
}

.rank-list {
  .rank-item {
    display: flex;
    align-items: center;
    gap: 12px;
    padding: 12px 0;
    border-bottom: 1px solid rgba(255, 255, 255, 0.05);
    
    &:last-child { border-bottom: none; }
    
    .rank-no {
      width: 24px;
      height: 24px;
      border-radius: 6px;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 12px;
      font-weight: 600;
      background: rgba(255, 255, 255, 0.1);
      color: rgba(255, 255, 255, 0.6);
      
      &.rank-1 { background: linear-gradient(135deg, #f59e0b, #fbbf24); color: #fff; }
      &.rank-2 { background: linear-gradient(135deg, #94a3b8, #cbd5e1); color: #fff; }
      &.rank-3 { background: linear-gradient(135deg, #b45309, #d97706); color: #fff; }
    }
    
    .rank-name {
      width: 60px;
      font-size: 14px;
      color: rgba(255, 255, 255, 0.9);
    }
    
    .rank-bar {
      flex: 1;
      height: 8px;
      background: rgba(255, 255, 255, 0.1);
      border-radius: 4px;
      overflow: hidden;
      
      .bar-fill {
        height: 100%;
        background: linear-gradient(90deg, #10b981, #34d399);
        border-radius: 4px;
        transition: width 0.5s;
      }
    }
    
    .rank-count {
      width: 40px;
      text-align: right;
      font-size: 14px;
      font-weight: 600;
      color: #10b981;
    }
  }
}

.center-top {
  .big-number {
    display: flex;
    justify-content: space-around;
    padding: 30px;
    background: rgba(255, 255, 255, 0.05);
    border: 1px solid rgba(255, 255, 255, 0.1);
    border-radius: 12px;
    
    .number-item {
      text-align: center;
      
      .number {
        display: block;
        font-size: 42px;
        font-weight: 700;
        background: linear-gradient(180deg, #fff, rgba(255, 255, 255, 0.7));
        -webkit-background-clip: text;
        background-clip: text;
        -webkit-text-fill-color: transparent;
      }
      
      .label {
        font-size: 14px;
        color: rgba(255, 255, 255, 0.6);
      }
    }
  }
}

.center-chart {
  flex: 1;
  display: flex;
  flex-direction: column;
  
  .panel-body {
    flex: 1;
    padding: 10px 20px 20px;
  }
  
  .chart-container {
    width: 100%;
    height: 100%;
    min-height: 200px;
  }
}

.doctor-rank {
  .doctor-item {
    display: flex;
    align-items: center;
    gap: 12px;
    padding: 12px 0;
    border-bottom: 1px solid rgba(255, 255, 255, 0.05);
    
    &:last-child { border-bottom: none; }
    
    .rank-badge {
      width: 20px;
      height: 20px;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 11px;
      font-weight: 600;
      background: rgba(255, 255, 255, 0.1);
      color: rgba(255, 255, 255, 0.6);
      
      &.top-1 { background: #f59e0b; color: #fff; }
      &.top-2 { background: #94a3b8; color: #fff; }
      &.top-3 { background: #b45309; color: #fff; }
    }
    
    .doctor-info {
      flex: 1;
      
      .name {
        display: block;
        font-size: 14px;
        color: #fff;
        font-weight: 500;
      }
      .section {
        font-size: 12px;
        color: rgba(255, 255, 255, 0.5);
      }
    }
    
    .count {
      font-size: 14px;
      font-weight: 600;
      color: #10b981;
    }
  }
}

.screen-right .panel:last-child {
  flex: 1;
  display: flex;
  flex-direction: column;
  
  .panel-body {
    flex: 1;
    display: flex;
    align-items: center;
    justify-content: center;
  }
  
  .chart-container {
    width: 100%;
    height: 200px;
  }
}
</style>
