<template>
  <div class="booking-form">
    <!-- 步骤条 -->
    <el-steps :active="currentStep" finish-status="success" align-center>
      <el-step title="选择科室/医生"></el-step>
      <el-step title="选择时间"></el-step>
      <el-step title="填写信息"></el-step>
      <el-step title="确认预约"></el-step>
    </el-steps>

    <!-- 步骤1: 选择科室/医生 -->
    <div v-show="currentStep === 0" class="step-content">
      <div class="dept-select" v-if="!selectedDept">
        <h4>选择科室</h4>
        <div class="dept-grid">
          <div 
            class="dept-item" 
            v-for="dept in departments" 
            :key="dept.sectionId"
            @click="selectDept(dept)"
          >
            <span class="dept-icon">{{ getDeptIcon(dept.sectionName) }}</span>
            <span class="dept-name">{{ dept.sectionName }}</span>
          </div>
        </div>
      </div>
      
      <div class="doctor-select" v-else>
        <div class="selected-dept">
          <span>已选科室：{{ selectedDept.sectionName }}</span>
          <el-button type="text" @click="selectedDept = null">重新选择</el-button>
        </div>
        <h4>选择医生</h4>
        <div class="doctor-list">
          <div 
            class="doctor-item" 
            :class="{ active: selectedDoc && selectedDoc.userId === doc.userId }"
            v-for="doc in doctors" 
            :key="doc.userId"
            @click="selectDoctor(doc)"
          >
            <el-avatar :size="48">{{ doc.nickName ? doc.nickName.charAt(0) : '' }}</el-avatar>
            <div class="doctor-info">
              <span class="name">{{ doc.nickName }}</span>
              <span class="title">{{ doc.rank1 || '主治医师' }}</span>
            </div>
            <i v-if="selectedDoc && selectedDoc.userId === doc.userId" class="el-icon-check"></i>
          </div>
        </div>
      </div>
    </div>

    <!-- 步骤2: 选择时间 -->
    <div v-show="currentStep === 1" class="step-content">
      <h4>选择就诊日期</h4>
      <div class="date-picker">
        <div 
          class="date-item" 
          :class="{ active: selectedDate === date.value, disabled: !date.hasSchedule }"
          v-for="date in dateList" 
          :key="date.value"
          @click="date.hasSchedule && selectDate(date.value)"
        >
          <span class="week">{{ date.week }}</span>
          <span class="day">{{ date.day }}</span>
        </div>
      </div>
      
      <h4>选择就诊时段</h4>
      <div class="schedule-list" v-if="schedules.length > 0">
        <div 
          class="schedule-item"
          :class="{ active: selectedSchedule && selectedSchedule.schedulingId === sch.schedulingId }"
          v-for="sch in schedules" 
          :key="sch.schedulingId"
          @click="selectSchedule(sch)"
        >
          <div class="time-slot">
            <span class="slot-label">{{ getTimeSlotLabel(sch.schedulingTime) }}</span>
            <span class="slot-time">{{ getTimeSlotTime(sch.schedulingTime) }}</span>
          </div>
          <div class="slot-info">
            <span class="fee">¥{{ sch.registrationFee }}</span>
            <span class="remain" :class="{ full: sch.residualNumber <= 0 }">
              {{ sch.residualNumber > 0 ? '余' + sch.residualNumber + '号' : '已约满' }}
            </span>
          </div>
        </div>
      </div>
      <el-empty v-else description="该日期暂无排班" />
    </div>

    <!-- 步骤3: 填写信息 -->
    <div v-show="currentStep === 2" class="step-content">
      <el-form ref="patientForm" :model="patientInfo" :rules="patientRules" label-width="100px">
        <el-form-item label="就诊人姓名" prop="name">
          <el-input v-model="patientInfo.name" placeholder="请输入真实姓名"></el-input>
        </el-form-item>
        <el-form-item label="性别" prop="gender">
          <el-radio-group v-model="patientInfo.gender">
            <el-radio label="1">男</el-radio>
            <el-radio label="2">女</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="手机号码" prop="phone">
          <el-input v-model="patientInfo.phone" placeholder="请输入手机号码"></el-input>
        </el-form-item>
        <el-form-item label="身份证号" prop="idCard">
          <el-input v-model="patientInfo.idCard" placeholder="请输入身份证号"></el-input>
        </el-form-item>
        <el-form-item label="病情描述">
          <el-input type="textarea" v-model="patientInfo.description" placeholder="请简要描述您的症状（选填）" :rows="3"></el-input>
        </el-form-item>
      </el-form>
    </div>

    <!-- 步骤4: 确认预约 -->
    <div v-show="currentStep === 3" class="step-content">
      <div class="confirm-card">
        <h4>预约信息确认</h4>
        <div class="info-row">
          <span class="label">就诊科室：</span>
          <span class="value">{{ selectedDept ? selectedDept.sectionName : '' }}</span>
        </div>
        <div class="info-row">
          <span class="label">就诊医生：</span>
          <span class="value">{{ selectedDoc ? selectedDoc.nickName : '' }}</span>
        </div>
        <div class="info-row">
          <span class="label">就诊时间：</span>
          <span class="value">{{ selectedDate }} {{ selectedSchedule ? getTimeSlotLabel(selectedSchedule.schedulingTime) : '' }}</span>
        </div>
        <div class="info-row">
          <span class="label">挂号费用：</span>
          <span class="value fee">¥{{ selectedSchedule ? selectedSchedule.registrationFee : '' }}</span>
        </div>
        <el-divider />
        <div class="info-row">
          <span class="label">就诊人：</span>
          <span class="value">{{ patientInfo.name }}</span>
        </div>
        <div class="info-row">
          <span class="label">手机号码：</span>
          <span class="value">{{ patientInfo.phone }}</span>
        </div>
      </div>
    </div>

    <!-- 底部按钮 -->
    <div class="form-footer">
      <el-button v-if="currentStep > 0" @click="prevStep">上一步</el-button>
      <el-button v-if="currentStep < 3" type="primary" @click="nextStep" :disabled="!canNext">下一步</el-button>
      <el-button v-if="currentStep === 3" type="primary" @click="submitBooking" :loading="submitting">确认预约</el-button>
    </div>
  </div>
</template>

<script>
import { getSectionList, getDoctorList, getSchedulingList, createRegistration } from '@/api/portal'

export default {
  name: 'BookingForm',
  props: {
    department: Object,
    doctor: Object
  },
  data() {
    return {
      currentStep: 0,
      departments: [],
      doctors: [],
      schedules: [],
      dateList: [],
      selectedDept: null,
      selectedDoc: null,
      selectedDate: '',
      selectedSchedule: null,
      patientInfo: {
        name: '',
        gender: '1',
        phone: '',
        idCard: '',
        description: ''
      },
      patientRules: {
        name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
        gender: [{ required: true, message: '请选择性别', trigger: 'change' }],
        phone: [
          { required: true, message: '请输入手机号', trigger: 'blur' },
          { pattern: /^1[3-9]\d{9}$/, message: '手机号格式不正确', trigger: 'blur' }
        ],
        idCard: [
          { required: true, message: '请输入身份证号', trigger: 'blur' },
          { pattern: /^[1-9]\d{5}(18|19|20)\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\d|3[01])\d{3}[\dXx]$/, message: '身份证号格式不正确', trigger: 'blur' }
        ]
      },
      submitting: false
    }
  },
  computed: {
    canNext() {
      if (this.currentStep === 0) return this.selectedDoc
      if (this.currentStep === 1) return this.selectedSchedule
      if (this.currentStep === 2) return true
      return true
    }
  },
  created() {
    this.initDateList()
    this.loadDepartments()
    if (this.department) {
      this.selectedDept = this.department
      this.loadDoctors()
    }
    if (this.doctor) {
      this.selectedDoc = this.doctor
      this.selectedDept = { sectionId: this.doctor.sectionId, sectionName: this.doctor.sectionName }
    }
  },
  methods: {
    initDateList() {
      const weekDays = ['周日', '周一', '周二', '周三', '周四', '周五', '周六']
      const today = new Date()
      for (let i = 0; i < 7; i++) {
        const date = new Date(today)
        date.setDate(today.getDate() + i)
        const month = date.getMonth() + 1
        const day = date.getDate()
        this.dateList.push({
          value: `${date.getFullYear()}-${String(month).padStart(2, '0')}-${String(day).padStart(2, '0')}`,
          week: i === 0 ? '今天' : (i === 1 ? '明天' : weekDays[date.getDay()]),
          day: `${month}/${day}`,
          hasSchedule: true
        })
      }
      this.selectedDate = this.dateList[0].value
    },
    
    async loadDepartments() {
      const res = await getSectionList({ pageNum: 1, pageSize: 100, status: '0' })
      this.departments = res.rows || []
    },
    
    async loadDoctors() {
      if (!this.selectedDept) return
      const res = await getDoctorList({ sectionId: this.selectedDept.sectionId, pageNum: 1, pageSize: 50 })
      this.doctors = res.rows || []
    },
    
    async loadSchedules() {
      if (!this.selectedDoc || !this.selectedDate) return
      const res = await getSchedulingList({
        doctorId: this.selectedDoc.userId,
        schedulingDate: this.selectedDate,
        status: '0'
      })
      this.schedules = res.rows || []
    },
    
    getDeptIcon(name) {
      const icons = { '内科': '🫀', '外科': '🔪', '儿科': '👶', '妇科': '👩', '眼科': '👁️', '口腔': '🦷', '皮肤': '🧴', '骨科': '🦴' }
      for (const key in icons) {
        if (name && name.includes(key)) return icons[key]
      }
      return '🏥'
    },
    
    getTimeSlotLabel(time) {
      const labels = { '1': '上午', '2': '下午', '3': '晚上' }
      return labels[time] || ''
    },
    
    getTimeSlotTime(time) {
      const times = { '1': '08:00-12:00', '2': '14:00-17:00', '3': '18:00-21:00' }
      return times[time] || ''
    },
    
    selectDept(dept) {
      this.selectedDept = dept
      this.selectedDoc = null
      this.loadDoctors()
    },
    
    selectDoctor(doc) {
      this.selectedDoc = doc
    },
    
    selectDate(date) {
      this.selectedDate = date
      this.selectedSchedule = null
      this.loadSchedules()
    },
    
    selectSchedule(sch) {
      if (sch.residualNumber <= 0) {
        this.$message.warning('该时段已约满')
        return
      }
      this.selectedSchedule = sch
    },
    
    prevStep() {
      this.currentStep--
    },
    
    nextStep() {
      if (this.currentStep === 0 && !this.selectedDoc) {
        this.$message.warning('请选择医生')
        return
      }
      if (this.currentStep === 1 && !this.selectedSchedule) {
        this.$message.warning('请选择就诊时段')
        return
      }
      if (this.currentStep === 2) {
        this.$refs.patientForm.validate(valid => {
          if (valid) this.currentStep++
        })
        return
      }
      this.currentStep++
      if (this.currentStep === 1) {
        this.loadSchedules()
      }
    },
    
    async submitBooking() {
      this.submitting = true
      try {
        await createRegistration({
          schedulingId: this.selectedSchedule.schedulingId,
          patientName: this.patientInfo.name,
          patientGender: this.patientInfo.gender,
          patientPhone: this.patientInfo.phone,
          patientIdCard: this.patientInfo.idCard,
          description: this.patientInfo.description
        })
        this.$emit('success')
      } catch (error) {
        this.$message.error('预约失败，请重试')
      } finally {
        this.submitting = false
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.booking-form {
  .el-steps { margin-bottom: 32px; }
  
  .step-content {
    min-height: 300px;
    
    h4 {
      font-size: 16px;
      color: #0f172a;
      margin: 0 0 16px;
    }
  }
  
  .dept-grid {
    display: grid;
    grid-template-columns: repeat(4, 1fr);
    gap: 12px;
    
    .dept-item {
      display: flex;
      flex-direction: column;
      align-items: center;
      padding: 16px;
      background: #f8fafc;
      border-radius: 12px;
      cursor: pointer;
      transition: all 0.3s;
      
      &:hover {
        background: #ecfeff;
        transform: translateY(-2px);
      }
      
      .dept-icon { font-size: 28px; margin-bottom: 8px; }
      .dept-name { font-size: 14px; color: #0f172a; }
    }
  }
  
  .selected-dept {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 12px 16px;
    background: #ecfeff;
    border-radius: 8px;
    margin-bottom: 16px;
    
    span { color: #0891b2; font-weight: 500; }
  }
  
  .doctor-list {
    display: flex;
    flex-direction: column;
    gap: 12px;
    
    .doctor-item {
      display: flex;
      align-items: center;
      gap: 16px;
      padding: 16px;
      background: #f8fafc;
      border-radius: 12px;
      cursor: pointer;
      border: 2px solid transparent;
      transition: all 0.3s;
      
      &:hover { background: #f1f5f9; }
      &.active {
        border-color: #0891b2;
        background: #ecfeff;
      }
      
      .doctor-info {
        flex: 1;
        .name { display: block; font-weight: 600; color: #0f172a; }
        .title { font-size: 13px; color: #64748b; }
      }
      
      .el-icon-check { color: #0891b2; font-size: 20px; }
    }
  }
  
  .date-picker {
    display: flex;
    gap: 12px;
    margin-bottom: 24px;
    
    .date-item {
      flex: 1;
      display: flex;
      flex-direction: column;
      align-items: center;
      padding: 12px;
      background: #f8fafc;
      border-radius: 12px;
      cursor: pointer;
      border: 2px solid transparent;
      transition: all 0.3s;
      
      &:hover:not(.disabled) { background: #f1f5f9; }
      &.active {
        border-color: #0891b2;
        background: #ecfeff;
      }
      &.disabled {
        opacity: 0.5;
        cursor: not-allowed;
      }
      
      .week { font-size: 13px; color: #64748b; }
      .day { font-size: 16px; font-weight: 600; color: #0f172a; }
    }
  }
  
  .schedule-list {
    display: flex;
    flex-direction: column;
    gap: 12px;
    
    .schedule-item {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 16px 20px;
      background: #f8fafc;
      border-radius: 12px;
      cursor: pointer;
      border: 2px solid transparent;
      transition: all 0.3s;
      
      &:hover { background: #f1f5f9; }
      &.active {
        border-color: #0891b2;
        background: #ecfeff;
      }
      
      .time-slot {
        .slot-label { font-weight: 600; color: #0f172a; margin-right: 12px; }
        .slot-time { font-size: 13px; color: #64748b; }
      }
      
      .slot-info {
        .fee { font-size: 18px; font-weight: 600; color: #f59e0b; margin-right: 16px; }
        .remain {
          font-size: 13px;
          color: #10b981;
          &.full { color: #ef4444; }
        }
      }
    }
  }
  
  .confirm-card {
    background: #f8fafc;
    border-radius: 16px;
    padding: 24px;
    
    h4 {
      text-align: center;
      margin-bottom: 24px;
    }
    
    .info-row {
      display: flex;
      margin-bottom: 12px;
      
      .label {
        width: 100px;
        color: #64748b;
      }
      .value {
        flex: 1;
        color: #0f172a;
        font-weight: 500;
        
        &.fee { color: #f59e0b; font-size: 18px; }
      }
    }
  }
  
  .form-footer {
    display: flex;
    justify-content: flex-end;
    gap: 12px;
    margin-top: 24px;
    padding-top: 24px;
    border-top: 1px solid #f1f5f9;
  }
}
</style>
