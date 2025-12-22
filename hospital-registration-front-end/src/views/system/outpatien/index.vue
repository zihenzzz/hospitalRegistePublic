<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="患者姓名" prop="patientName">
        <el-input
          v-model="queryParams.patientName"
          placeholder="请输入患者姓名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="门诊日期" prop="outpatienDate">
        <el-date-picker
          v-model="queryParams.outpatienDate"
          type="date"
          placeholder="选择日期"
          value-format="yyyy-MM-dd"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:outpatien:add']"
        >新增</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="outpatienList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="门诊ID" align="center" prop="outpatienId" width="80" />
      <el-table-column label="患者姓名" align="center" prop="patientName" />
      <el-table-column label="医生" align="center" prop="doctorName" />
      <el-table-column label="科室" align="center" prop="sectionName" />
      <el-table-column label="门诊日期" align="center" prop="outpatienDate" width="100" />
      <el-table-column label="诊断结果" align="center" prop="diagnosis" :show-overflow-tooltip="true" />
      <el-table-column label="处方" align="center" prop="prescription" :show-overflow-tooltip="true" />
      <el-table-column label="费用" align="center" prop="totalFee" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleView(scope.row)"
          >查看</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:outpatien:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:outpatien:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <el-dialog :title="title" :visible.sync="open" width="700px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="患者姓名" prop="patientName">
              <el-input v-model="form.patientName" placeholder="请输入患者姓名" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="医生" prop="doctorName">
              <el-input v-model="form.doctorName" placeholder="请输入医生姓名" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="科室" prop="sectionName">
              <el-input v-model="form.sectionName" placeholder="请输入科室名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="门诊日期" prop="outpatienDate">
              <el-date-picker
                v-model="form.outpatienDate"
                type="date"
                placeholder="选择日期"
                value-format="yyyy-MM-dd"
                style="width: 100%;"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="主诉" prop="chiefComplaint">
          <el-input v-model="form.chiefComplaint" type="textarea" placeholder="请输入主诉" :rows="2" />
        </el-form-item>
        <el-form-item label="诊断结果" prop="diagnosis">
          <el-input v-model="form.diagnosis" type="textarea" placeholder="请输入诊断结果" :rows="3" />
        </el-form-item>
        <el-form-item label="处方" prop="prescription">
          <el-input v-model="form.prescription" type="textarea" placeholder="请输入处方" :rows="3" />
        </el-form-item>
        <el-row>
          <el-col :span="12">
            <el-form-item label="药品费用" prop="drugFee">
              <el-input-number v-model="form.drugFee" :min="0" :precision="2" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="检查费用" prop="checkFee">
              <el-input-number v-model="form.checkFee" :min="0" :precision="2" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listOutpatien, getOutpatien, delOutpatien, addOutpatien, updateOutpatien } from "@/api/system/outpatien";

export default {
  name: "Outpatien",
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      outpatienList: [],
      title: "",
      open: false,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        patientName: null,
        outpatienDate: null
      },
      form: {},
      rules: {
        patientName: [
          { required: true, message: "患者姓名不能为空", trigger: "blur" }
        ],
        doctorName: [
          { required: true, message: "医生姓名不能为空", trigger: "blur" }
        ],
        outpatienDate: [
          { required: true, message: "门诊日期不能为空", trigger: "change" }
        ]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    getList() {
      this.loading = true;
      listOutpatien(this.queryParams).then(response => {
        this.outpatienList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    cancel() {
      this.open = false;
      this.reset();
    },
    reset() {
      this.form = {
        outpatienId: null,
        patientName: null,
        doctorName: null,
        sectionName: null,
        outpatienDate: null,
        chiefComplaint: null,
        diagnosis: null,
        prescription: null,
        drugFee: 0,
        checkFee: 0,
        totalFee: 0,
        remark: null
      };
      this.resetForm("form");
    },
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.outpatienId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加门诊记录";
    },
    handleView(row) {
      this.reset();
      const outpatienId = row.outpatienId
      getOutpatien(outpatienId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "门诊详情";
      });
    },
    handleUpdate(row) {
      this.reset();
      const outpatienId = row.outpatienId || this.ids
      getOutpatien(outpatienId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改门诊记录";
      });
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          // 计算总费用
          this.form.totalFee = (this.form.drugFee || 0) + (this.form.checkFee || 0);
          
          if (this.form.outpatienId != null) {
            updateOutpatien(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addOutpatien(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    handleDelete(row) {
      const outpatienIds = row.outpatienId || this.ids;
      this.$modal.confirm('是否确认删除门诊编号为"' + outpatienIds + '"的数据项？').then(function() {
        return delOutpatien(outpatienIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    }
  }
};
</script>

