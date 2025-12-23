<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="医生姓名" prop="doctorName">
        <el-input
          v-model="queryParams.doctorName"
          placeholder="请输入医生姓名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="科室" prop="sectionId">
        <el-select v-model="queryParams.sectionId" placeholder="请选择科室" clearable>
          <el-option label="内科" value="1" />
          <el-option label="外科" value="2" />
        </el-select>
      </el-form-item>
      <el-form-item label="排班日期" prop="schedulingDate">
        <el-date-picker
          v-model="queryParams.schedulingDate"
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
          v-hasPermi="['system:scheduling:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:scheduling:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:scheduling:remove']"
        >删除</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="schedulingList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="排班ID" align="center" prop="schedulingId" />
      <el-table-column label="医生姓名" align="center" prop="doctorName" />
      <el-table-column label="科室" align="center" prop="sectionName" />
      <el-table-column label="排班日期" align="center" prop="schedulingDate" width="100" />
      <el-table-column label="时段" align="center" prop="schedulingTime">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.schedulingTime === '1'" type="success">上午</el-tag>
          <el-tag v-else-if="scope.row.schedulingTime === '2'" type="warning">下午</el-tag>
          <el-tag v-else type="info">晚上</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="可预约数" align="center" prop="availableNum" />
      <el-table-column label="已预约数" align="center" prop="registeredNum" />
      <el-table-column label="状态" align="center" prop="status">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_normal_disable" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:scheduling:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:scheduling:remove']"
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

    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="医生姓名" prop="doctorName">
          <el-input v-model="form.doctorName" placeholder="请输入医生姓名" />
        </el-form-item>
        <el-form-item label="科室" prop="sectionId">
          <el-select v-model="form.sectionId" placeholder="请选择科室">
            <el-option label="内科" value="1" />
            <el-option label="外科" value="2" />
          </el-select>
        </el-form-item>
        <el-form-item label="排班日期" prop="schedulingDate">
          <el-date-picker
            v-model="form.schedulingDate"
            type="date"
            placeholder="选择日期"
            value-format="yyyy-MM-dd"
            style="width: 100%;"
          />
        </el-form-item>
        <el-form-item label="时段" prop="schedulingTime">
          <el-radio-group v-model="form.schedulingTime">
            <el-radio label="1">上午</el-radio>
            <el-radio label="2">下午</el-radio>
            <el-radio label="3">晚上</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="可预约数" prop="availableNum">
          <el-input-number v-model="form.availableNum" :min="1" :max="100" />
        </el-form-item>
        <el-form-item label="挂号费用" prop="registrationFee">
          <el-input-number v-model="form.registrationFee" :min="0" :precision="2" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio
              v-for="dict in dict.type.sys_normal_disable"
              :key="dict.value"
              :label="dict.value"
            >{{dict.label}}</el-radio>
          </el-radio-group>
        </el-form-item>
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
import { listScheduling, getScheduling, delScheduling, addScheduling, updateScheduling } from "@/api/system/scheduling";

export default {
  name: "Scheduling",
  dicts: ['sys_normal_disable'],
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      schedulingList: [],
      title: "",
      open: false,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        doctorName: null,
        sectionId: null,
        schedulingDate: null
      },
      form: {},
      rules: {
        doctorName: [
          { required: true, message: "医生姓名不能为空", trigger: "blur" }
        ],
        sectionId: [
          { required: true, message: "科室不能为空", trigger: "change" }
        ],
        schedulingDate: [
          { required: true, message: "排班日期不能为空", trigger: "change" }
        ],
        schedulingTime: [
          { required: true, message: "时段不能为空", trigger: "change" }
        ],
        availableNum: [
          { required: true, message: "可预约数不能为空", trigger: "blur" }
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
      listScheduling(this.queryParams).then(response => {
        this.schedulingList = response.rows;
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
        schedulingId: null,
        doctorName: null,
        sectionId: null,
        schedulingDate: null,
        schedulingTime: "1",
        availableNum: 20,
        registeredNum: 0,
        registrationFee: 10.00,
        status: "0",
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
      this.ids = selection.map(item => item.schedulingId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加排班";
    },
    handleUpdate(row) {
      this.reset();
      const schedulingId = row.schedulingId || this.ids
      getScheduling(schedulingId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改排班";
      });
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.schedulingId != null) {
            updateScheduling(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addScheduling(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    handleDelete(row) {
      const schedulingIds = row.schedulingId || this.ids;
      this.$modal.confirm('是否确认删除排班编号为"' + schedulingIds + '"的数据项？').then(function() {
        return delScheduling(schedulingIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    }
  }
};
</script>


