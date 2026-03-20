<template>
  <div>
    <div class="page-header" style="display:flex;align-items:center;gap:12px">
      <el-button icon="el-icon-arrow-left" circle size="small" @click="$router.back()" />
      <div>
        <h2>档案详情</h2>
        <p>查看档案完整信息</p>
      </div>
    </div>

    <el-card v-loading="loading">
      <div v-if="detail">
        <!-- 标题区 -->
        <div class="detail-header">
          <div class="detail-title-row">
            <h3 class="detail-title">{{ detail.title }}</h3>
            <el-tag :type="statusMap[detail.status].type" size="medium" style="margin-left:12px">
              {{ statusMap[detail.status].label }}
            </el-tag>
          </div>
          <div class="detail-meta">
            <span><i class="el-icon-collection-tag"></i> {{ detail.type }}</span>
            <span><i class="el-icon-date"></i> {{ detail.year }} 年</span>
            <span v-if="detail.teacherName"><i class="el-icon-user"></i> {{ detail.teacherName }}</span>
            <span><i class="el-icon-office-building"></i> {{ reviewDept }}</span>
          </div>
        </div>

        <el-divider />

        <!-- 档案描述 -->
        <div class="detail-section">
          <div class="section-title">档案描述</div>
          <div class="section-content">{{ detail.description || '（无描述）' }}</div>
        </div>

        <!-- 附件 -->
        <div class="detail-section" v-if="detail.fileUrl">
          <div class="section-title">附件</div>
          <el-link :href="detail.fileUrl" target="_blank" type="primary" icon="el-icon-paperclip">查看附件</el-link>
        </div>

        <!-- 审核部门提示 -->
        <el-alert
          :title="'本档案由「' + reviewDept + '」负责审核'"
          type="info"
          :closable="false"
          show-icon
          style="margin-top:16px"
        />

        <!-- 操作按钮 -->
        <div class="detail-actions">
          <!-- 管理员审核操作 -->
          <template v-if="isAdmin && detail.status === 0">
            <el-button type="success" icon="el-icon-check" @click="handleApprove(1)">通过审核</el-button>
            <el-button type="danger" icon="el-icon-close" @click="handleApprove(2)">拒绝审核</el-button>
          </template>
          <!-- 编辑操作 -->
          <el-button
            v-if="canEdit"
            type="primary"
            icon="el-icon-edit"
            @click="$router.push('/home/archive/edit/' + detail.archiveId)"
          >编辑</el-button>
          <!-- 删除操作 -->
          <el-button
            v-if="canDelete"
            type="danger"
            plain
            icon="el-icon-delete"
            @click="handleDelete"
          >删除</el-button>
        </div>
      </div>
      <el-empty v-else-if="!loading" description="档案不存在或已删除" />
    </el-card>
  </div>
</template>

<script>
import { archiveDetail, archiveDelete, archiveApprove, getReviewDept } from '@/utils/api'

export default {
  name: 'ArchiveDetail',
  data() {
    return {
      detail: null,
      loading: false,
      statusMap: { 0: { label: '待审核', type: 'warning' }, 1: { label: '已通过', type: 'success' }, 2: { label: '已拒绝', type: 'danger' } }
    }
  },
  computed: {
    userInfo() {
      try { return JSON.parse(localStorage.getItem('userInfo') || '{}') } catch { return {} }
    },
    isAdmin() { return this.userInfo.role === 'admin' },
    reviewDept() { return this.detail ? getReviewDept(this.detail.type) : '' },
    canEdit() {
      if (!this.detail) return false
      return this.isAdmin || (this.detail.teacherId === this.userInfo.userId && this.detail.status !== 2)
    },
    canDelete() {
      if (!this.detail) return false
      return this.isAdmin || this.detail.teacherId === this.userInfo.userId
    }
  },
  mounted() { this.loadDetail() },
  methods: {
    async loadDetail() {
      this.loading = true
      try {
        this.detail = await archiveDetail(this.$route.params.id)
      } catch (e) {}
      this.loading = false
    },
    async handleApprove(status) {
      const label = status === 1 ? '通过' : '拒绝'
      this.$confirm(`确定要${label}这份档案吗？`, '确认操作', { type: 'warning' }).then(async () => {
        try {
          await archiveApprove(this.detail.archiveId, status)
          this.$message.success(`已${label}`)
          this.loadDetail()
        } catch (e) {}
      }).catch(() => {})
    },
    handleDelete() {
      this.$confirm('确定要删除这份档案吗？', '确认删除', { type: 'warning' }).then(async () => {
        try {
          await archiveDelete(this.detail.archiveId)
          this.$message.success('删除成功')
          this.$router.push('/home/archives')
        } catch (e) {}
      }).catch(() => {})
    }
  }
}
</script>

<style scoped>
.detail-header { padding: 8px 0 16px; }
.detail-title-row { display: flex; align-items: center; margin-bottom: 12px; }
.detail-title { font-size: 22px; font-weight: 600; color: #303133; }
.detail-meta { display: flex; gap: 20px; color: #909399; font-size: 14px; flex-wrap: wrap; }
.detail-meta span { display: flex; align-items: center; gap: 4px; }
.detail-section { margin-bottom: 20px; }
.section-title { font-size: 15px; font-weight: 600; color: #303133; margin-bottom: 10px; padding-left: 10px; border-left: 3px solid #1a73e8; }
.section-content { color: #606266; line-height: 1.8; white-space: pre-wrap; background: #f9fafb; padding: 16px; border-radius: 6px; }
.detail-actions { margin-top: 24px; display: flex; gap: 12px; flex-wrap: wrap; }
</style>
