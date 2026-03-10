<template>
  <div class="my-records">
    <el-tabs v-model="activeTab" @tab-click="handleTabChange">
      <el-tab-pane label="待就诊" name="pending"></el-tab-pane>
      <el-tab-pane label="已完成" name="completed"></el-tab-pane>
      <el-tab-pane label="已取消" name="cancelled"></el-tab-pane>
    </el-tabs>

    <div class="records-list" v-loading="loading">
      <div class="record-card" v-for="record in records" :key="record.registrationId">
        <div class="record-header">
          <span class="record-no">挂号单号：{{ record.registrationId }}</span>
          <el-tag :type="getStatusType(record.status)" size="small">{{ getStatusText(record.status) }}</el-tag>
        </div>
        <div class="record-body">
          <div class="info-row">
            <span class="label">就诊科室：</span>
            <span class="value">{{ record.sectionName }}</span>
          </div>
          <div class="info-row">
            <span class="label">就诊医生：</span>
            <span class="value">{{ record.doctorName }}</span>
          </div>
          <div class="info-row">
            <span class="label">就诊时间：</span>
            <span class="value">{{ record.schedulingDate }} {{ getTimeSlot(record.schedulingTime) }}</span>
          </div>
          <div class="info-row">
            <span class="label">就诊人：</span>
            <span class="value">{{ record.patientName }}</span>
          </div>
          <div class="info-row">
            <span class="label">挂号费用：</span>
            <span class="value fee">¥{{ record.registrationFee }}</span>
          </div>
        </div>
        <div class="record-footer" v-if="record.status === '0'">
          <el-button type="danger" size="small" plain @click="handleCancel(record)">取消预约</el-button>
        </div>
      </div>
      
      <el-empty v-if="!loading && records.length === 0" description="暂无挂号记录" />
    </div>

    <el-pagination
      v-if="total > 0"
      :current-page="queryParams.pageNum"
      :page-size="queryParams.pageSize"
      :total="total"
      layout="total, prev, pager, next"
      @current-change="handlePageChange"
    />
  </div>
</template>

<script>
import { getMyRegistrations, cancelRegistration } from '@/api/portal'

export default {
  name: 'MyRecords',
  data() {
    return {
      activeTab: 'pending',
      loading: false,
      records: [],
      total: 0,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        status: '0'
      }
    }
  },
  created() {
    this.loadRecords()
  },
  methods: {
    async loadRecords() {
      this.loading = true
      try {
        const res = await getMyRegistrations(this.queryParams)
        this.records = res.rows || []
        this.total = res.total || 0
      } catch (error) {
        console.error('加载挂号记录失败', error)
      } finally {
        this.loading = false
      }
    },
    
    handleTabChange(tab) {
      const statusMap = { pending: '0', completed: '1', cancelled: '2' }
      this.queryParams.status = statusMap[tab.name]
      this.queryParams.pageNum = 1
      this.loadRecords()
    },
    
    handlePageChange(page) {
      this.queryParams.pageNum = page
      this.loadRecords()
    },
    
    getStatusType(status) {
      const types = { '0': 'warning', '1': 'success', '2': 'info' }
      return types[status] || 'info'
    },
    
    getStatusText(status) {
      const texts = { '0': '待就诊', '1': '已完成', '2': '已取消' }
      return texts[status] || '未知'
    },
    
    getTimeSlot(time) {
      const slots = { '1': '上午 08:00-12:00', '2': '下午 14:00-17:00', '3': '晚上 18:00-21:00' }
      return slots[time] || ''
    },
    
    handleCancel(record) {
      this.$confirm('确定要取消该预约吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          await cancelRegistration(record.registrationId)
          this.$message.success('取消成功')
          this.loadRecords()
        } catch (error) {
          this.$message.error('取消失败')
        }
      }).catch(() => {})
    }
  }
}
</script>

<style lang="scss" scoped>
.my-records {
  .records-list {
    min-height: 300px;
    margin: 20px 0;
  }
  
  .record-card {
    background: #f8fafc;
    border-radius: 12px;
    padding: 20px;
    margin-bottom: 16px;
    
    .record-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 16px;
      padding-bottom: 12px;
      border-bottom: 1px solid #e2e8f0;
      
      .record-no {
        font-size: 14px;
        color: #64748b;
      }
    }
    
    .record-body {
      .info-row {
        display: flex;
        margin-bottom: 8px;
        
        .label {
          width: 80px;
          color: #64748b;
          font-size: 14px;
        }
        
        .value {
          flex: 1;
          color: #0f172a;
          font-size: 14px;
          
          &.fee {
            color: #f59e0b;
            font-weight: 600;
          }
        }
      }
    }
    
    .record-footer {
      margin-top: 16px;
      padding-top: 12px;
      border-top: 1px solid #e2e8f0;
      text-align: right;
    }
  }
}
</style>
