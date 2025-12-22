<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="用户姓名" prop="userName">
        <el-input
          v-model="queryParams.userName"
          placeholder="请输入用户姓名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="医生" prop="doctorName">
        <el-input
          v-model="queryParams.doctorName"
          placeholder="请输入医生姓名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="评分" prop="rating">
        <el-select v-model="queryParams.rating" placeholder="评分" clearable>
          <el-option label="5星" value="5" />
          <el-option label="4星" value="4" />
          <el-option label="3星" value="3" />
          <el-option label="2星" value="2" />
          <el-option label="1星" value="1" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:evaluate:remove']"
        >删除</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="evaluateList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="评价ID" align="center" prop="evaluateId" width="80" />
      <el-table-column label="用户姓名" align="center" prop="userName" />
      <el-table-column label="医生" align="center" prop="doctorName" />
      <el-table-column label="科室" align="center" prop="sectionName" />
      <el-table-column label="评分" align="center" prop="rating" width="120">
        <template slot-scope="scope">
          <el-rate
            v-model="scope.row.rating"
            disabled
            show-score
            text-color="#ff9900"
          />
        </template>
      </el-table-column>
      <el-table-column label="评价内容" align="center" prop="evaluateContent" :show-overflow-tooltip="true" width="250" />
      <el-table-column label="评价时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="150" class-name="small-padding fixed-width">
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
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:evaluate:remove']"
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

    <el-dialog title="评价详情" :visible.sync="open" width="600px" append-to-body>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="评价ID">{{ form.evaluateId }}</el-descriptions-item>
        <el-descriptions-item label="用户姓名">{{ form.userName }}</el-descriptions-item>
        <el-descriptions-item label="医生">{{ form.doctorName }}</el-descriptions-item>
        <el-descriptions-item label="科室">{{ form.sectionName }}</el-descriptions-item>
        <el-descriptions-item label="评分" :span="2">
          <el-rate
            v-model="form.rating"
            disabled
            show-score
            text-color="#ff9900"
          />
        </el-descriptions-item>
        <el-descriptions-item label="评价内容" :span="2">
          <div style="padding: 10px; background: #f5f5f5; border-radius: 4px;">
            {{ form.evaluateContent }}
          </div>
        </el-descriptions-item>
        <el-descriptions-item label="评价时间" :span="2">
          {{ parseTime(form.createTime) }}
        </el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">{{ form.remark || '无' }}</el-descriptions-item>
      </el-descriptions>
      <div slot="footer" class="dialog-footer">
        <el-button @click="open = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listEvaluate, getEvaluate, delEvaluate } from "@/api/system/evaluate";

export default {
  name: "Evaluate",
  data() {
    return {
      loading: true,
      ids: [],
      multiple: true,
      showSearch: true,
      total: 0,
      evaluateList: [],
      open: false,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        userName: null,
        doctorName: null,
        rating: null
      },
      form: {}
    };
  },
  created() {
    this.getList();
  },
  methods: {
    getList() {
      this.loading = true;
      listEvaluate(this.queryParams).then(response => {
        this.evaluateList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
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
      this.ids = selection.map(item => item.evaluateId)
      this.multiple = !selection.length
    },
    handleView(row) {
      const evaluateId = row.evaluateId
      getEvaluate(evaluateId).then(response => {
        this.form = response.data;
        this.open = true;
      });
    },
    handleDelete(row) {
      const evaluateIds = row.evaluateId || this.ids;
      this.$modal.confirm('是否确认删除评价编号为"' + evaluateIds + '"的数据项？').then(function() {
        return delEvaluate(evaluateIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    }
  }
};
</script>

