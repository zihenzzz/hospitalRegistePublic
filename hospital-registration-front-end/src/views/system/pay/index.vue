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
      <el-form-item label="支付状态" prop="payStatus">
        <el-select v-model="queryParams.payStatus" placeholder="支付状态" clearable>
          <el-option label="待支付" value="0" />
          <el-option label="已支付" value="1" />
          <el-option label="已退款" value="2" />
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
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:pay:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="payList">
      <el-table-column label="订单号" align="center" prop="payId" width="100" />
      <el-table-column label="用户姓名" align="center" prop="userName" />
      <el-table-column label="充值金额" align="center" prop="payAmount">
        <template slot-scope="scope">
          <span style="color: #f56c6c; font-weight: bold;">¥{{ scope.row.payAmount }}</span>
        </template>
      </el-table-column>
      <el-table-column label="支付方式" align="center" prop="payType">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.payType === '1'" type="success">微信支付</el-tag>
          <el-tag v-else-if="scope.row.payType === '2'" type="primary">支付宝</el-tag>
          <el-tag v-else-if="scope.row.payType === '3'" type="warning">现金</el-tag>
          <el-tag v-else type="info">其他</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="支付状态" align="center" prop="payStatus">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.payStatus === '0'" type="warning">待支付</el-tag>
          <el-tag v-else-if="scope.row.payStatus === '1'" type="success">已支付</el-tag>
          <el-tag v-else type="info">已退款</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="充值时间" align="center" prop="payTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.payTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="账户余额" align="center" prop="balance">
        <template slot-scope="scope">
          <span style="color: #67c23a;">¥{{ scope.row.balance }}</span>
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
            v-if="scope.row.payStatus === '1'"
            size="mini"
            type="text"
            icon="el-icon-refresh-left"
            @click="handleRefund(scope.row)"
            v-hasPermi="['system:pay:edit']"
          >退款</el-button>
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

    <el-dialog title="充值详情" :visible.sync="open" width="600px" append-to-body>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="订单号">{{ form.payId }}</el-descriptions-item>
        <el-descriptions-item label="用户姓名">{{ form.userName }}</el-descriptions-item>
        <el-descriptions-item label="充值金额">
          <span style="color: #f56c6c; font-weight: bold;">¥{{ form.payAmount }}</span>
        </el-descriptions-item>
        <el-descriptions-item label="支付方式">
          <el-tag v-if="form.payType === '1'" type="success">微信支付</el-tag>
          <el-tag v-else-if="form.payType === '2'" type="primary">支付宝</el-tag>
          <el-tag v-else-if="form.payType === '3'" type="warning">现金</el-tag>
          <el-tag v-else type="info">其他</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="支付状态">
          <el-tag v-if="form.payStatus === '0'" type="warning">待支付</el-tag>
          <el-tag v-else-if="form.payStatus === '1'" type="success">已支付</el-tag>
          <el-tag v-else type="info">已退款</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="充值时间">{{ parseTime(form.payTime) }}</el-descriptions-item>
        <el-descriptions-item label="账户余额">
          <span style="color: #67c23a; font-weight: bold;">¥{{ form.balance }}</span>
        </el-descriptions-item>
        <el-descriptions-item label="交易流水号" :span="2">{{ form.transactionId || '无' }}</el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">{{ form.remark || '无' }}</el-descriptions-item>
      </el-descriptions>
      <div slot="footer" class="dialog-footer">
        <el-button @click="open = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listPay, getPay, updatePay } from "@/api/system/pay";

export default {
  name: "Pay",
  data() {
    return {
      loading: true,
      showSearch: true,
      total: 0,
      payList: [],
      open: false,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        userName: null,
        payStatus: null
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
      listPay(this.queryParams).then(response => {
        this.payList = response.rows;
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
    handleView(row) {
      const payId = row.payId
      getPay(payId).then(response => {
        this.form = response.data;
        this.open = true;
      });
    },
    handleRefund(row) {
      this.$modal.confirm('是否确认退款该充值记录？').then(() => {
        const form = { ...row, payStatus: '2' };
        return updatePay(form);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("退款成功");
      }).catch(() => {});
    },
    handleExport() {
      this.download('system/pay/export', {
        ...this.queryParams
      }, `pay_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>

