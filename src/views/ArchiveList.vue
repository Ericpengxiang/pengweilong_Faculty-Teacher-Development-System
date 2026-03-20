<template>
  <div>
    <div class="page-header" style="display:flex;justify-content:space-between;align-items:flex-start">
      <div>
        <h2>档案管理</h2>
        <p>管理教师成长档案记录</p>
      </div>
      <el-button type="primary" icon="el-icon-plus" @click="$router.push('/home/archive/add')">新增档案</el-button>
    </div>

    <!-- 筛选栏 -->
    <el-card class="filter-card">
      <el-form :model="filters" inline size="small">
        <el-form-item label="年份">
          <el-select v-model="filters.year" placeholder="全部年份" clearable style="width:120px" @change="loadList">
            <el-option v-for="y in years" :key="y" :label="y + '年'" :value="y" />
          </el-select>
        </el-form-item>
        <el-form-item label="类型">
          <el-select v-model="filters.type" placeholder="全部类型" clearable style="width:120px" @change="loadList">
            <el-option v-for="t in archiveTypes" :key="t" :label="t" :value="t" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="filters.status" placeholder="全部状态" clearable style="width:120px" @change="loadList">
            <el-option label="待审核" :value="0" />
            <el-option label="已通过" :value="1" />
            <el-option label="已拒绝" :value="2" />
          </el-select>
        </el-form-item>
        <el-form-item label="关键词">
          <el-input v-model="filters.keyword" placeholder="搜索标题" clearable style="width:160px" @keyup.enter.native="loadList" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="loadList">搜索</el-button>
          <el-button icon="el-icon-refresh" @click="resetFilters">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 档案列表 -->
    <el-card style="margin-top:16px">
      <el-table :data="list" v-loading="loading" stripe>
        <el-table-column type="index" label="#" width="50" />
        <el-table-column prop="title" label="档案标题" min-width="200" show-overflow-tooltip>
          <template slot-scope="{row}">
            <el-link type="primary" @click="$router.push('/home/archive/detail/' + row.archiveId)">{{ row.title }}</el-link>
          </template>
        </el-table-column>
        <el-table-column prop="type" label="类型" width="110">
          <template slot-scope="{row}">
            <el-tag size="small" :color="getTypeColor(row.type) + '15'" :style="{ color: getTypeColor(row.type), border: '1px solid ' + getTypeColor(row.type) + '40' }">
              {{ row.type }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="year" label="年份" width="80" />
        <el-table-column prop="status" label="审核状态" width="100">
          <template slot-scope="{row}">
            <el-tag :type="statusMap[row.status].type" size="small">{{ statusMap[row.status].label }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="reviewDept" label="审核部门" width="140" show-overflow-tooltip />
        <el-table-column prop="teacherName" label="教师" width="90" v-if="isAdmin" />
        <el-table-column prop="createTime" label="提交时间" width="160" show-overflow-tooltip />
        <el-table-column label="操作" width="160" fixed="right">
          <template slot-scope="{row}">
            <el-button type="text" size="mini" @click="$router.push('/home/archive/detail/' + row.archiveId)">详情</el-button>
            <el-button type="text" size="mini" @click="$router.push('/home/archive/edit/' + row.archiveId)" v-if="canEdit(row)">编辑</el-button>
            <el-button type="text" size="mini" style="color:#f56c6c" @click="handleDelete(row)" v-if="canDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div style="margin-top:16px;text-align:right">
        <el-pagination
          :current-page="page"
          :page-size="pageSize"
          :total="total"
          layout="total, prev, pager, next"
          @current-change="handlePageChange"
        />
      </div>
    </el-card>
  </div>
</template>

<script>
import { archiveList, archiveDelete, ARCHIVE_TYPES, getReviewDept } from '@/utils/api'

const TYPE_COLORS = {
  '科研成果': '#1a73e8', '教学成果': '#34a853', '教学改革': '#fbbc04',
  '获奖情况': '#ea4335', '培训记录': '#9c27b0', '进修培训': '#ff5722',
  '职称晋升': '#00bcd4', '个人学历': '#607d8b', '基础信息': '#795548'
}

export default {
  name: 'ArchiveList',
  data() {
    return {
      list: [],
      loading: false,
      page: 1,
      pageSize: 10,
      total: 0,
      filters: { year: '', type: '', status: '', keyword: '' },
      archiveTypes: ARCHIVE_TYPES,
      statusMap: { 0: { label: '待审核', type: 'warning' }, 1: { label: '已通过', type: 'success' }, 2: { label: '已拒绝', type: 'danger' } },
      years: Array.from({ length: 10 }, (_, i) => new Date().getFullYear() - i)
    }
  },
  computed: {
    userInfo() {
      try { return JSON.parse(localStorage.getItem('userInfo') || '{}') } catch { return {} }
    },
    isAdmin() { return this.userInfo.role === 'admin' }
  },
  mounted() { this.loadList() },
  methods: {
    async loadList() {
      this.loading = true
      try {
        const params = { page: this.page, size: this.pageSize }
        if (this.filters.year) params.year = this.filters.year
        if (this.filters.type) params.type = this.filters.type
        if (this.filters.status !== '') params.status = this.filters.status
        if (this.filters.keyword) params.keyword = this.filters.keyword
        const res = await archiveList(params)
        // 兼容不同分页结构
        if (res && res.records) {
          this.list = res.records.map(r => ({ ...r, reviewDept: getReviewDept(r.type) }))
          // MyBatis-Plus 分页 total 可能为 0（COUNT 查询未配置），用 records.length 兜底
          this.total = (res.total > 0) ? res.total : res.records.length
        } else if (res && res.list) {
          this.list = res.list.map(r => ({ ...r, reviewDept: getReviewDept(r.type) }))
          this.total = (res.total > 0) ? res.total : res.list.length
        } else if (Array.isArray(res)) {
          this.list = res.map(r => ({ ...r, reviewDept: getReviewDept(r.type) }))
          this.total = res.length
        }
      } catch (e) {}
      this.loading = false
    },
    resetFilters() {
      this.filters = { year: '', type: '', status: '', keyword: '' }
      this.page = 1
      this.loadList()
    },
    handlePageChange(p) { this.page = p; this.loadList() },
    canEdit(row) { return this.isAdmin || (row.teacherId === this.userInfo.userId && row.status !== 2) },
    canDelete(row) { return this.isAdmin || row.teacherId === this.userInfo.userId },
    handleDelete(row) {
      this.$confirm(`确定删除档案"${row.title}"吗？`, '确认删除', { type: 'warning' }).then(async () => {
        try {
          await archiveDelete(row.archiveId)
          this.$message.success('删除成功')
          this.loadList()
        } catch (e) {}
      }).catch(() => {})
    },
    getTypeColor(type) { return TYPE_COLORS[type] || '#909399' }
  }
}
</script>

<style scoped>
.filter-card { margin-bottom: 0; }
.filter-card .el-form-item { margin-bottom: 0; }
</style>
