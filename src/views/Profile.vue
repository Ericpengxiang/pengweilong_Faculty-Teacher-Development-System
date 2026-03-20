<template>
  <div>
    <div class="page-header">
      <h2>个人中心</h2>
      <p>管理您的个人信息</p>
    </div>

    <el-row :gutter="20">
      <!-- 头像卡片 -->
      <el-col :span="8">
        <el-card class="avatar-card">
          <div class="avatar-wrap">
            <el-avatar :size="80" :style="{ background: userInfo.role === 'admin' ? '#f56c6c' : '#1a73e8', fontSize: '32px' }">
              {{ userInfo.name ? userInfo.name[0] : 'U' }}
            </el-avatar>
            <h3>{{ userInfo.name }}</h3>
            <p class="username">@{{ userInfo.username }}</p>
            <el-tag :type="userInfo.role === 'admin' ? 'danger' : 'primary'">
              {{ userInfo.role === 'admin' ? '系统管理员' : '教师' }}
            </el-tag>
          </div>
          <el-divider />
          <div class="profile-stats">
            <div class="pstat">
              <div class="pstat-num">{{ myStats.total || 0 }}</div>
              <div class="pstat-label">我的档案</div>
            </div>
            <div class="pstat">
              <div class="pstat-num">{{ myStats.approved || 0 }}</div>
              <div class="pstat-label">已通过</div>
            </div>
            <div class="pstat">
              <div class="pstat-num">{{ myStats.pending || 0 }}</div>
              <div class="pstat-label">待审核</div>
            </div>
          </div>
        </el-card>
      </el-col>

      <!-- 编辑表单 -->
      <el-col :span="16">
        <el-card>
          <div slot="header"><b>编辑个人信息</b></div>
          <el-form ref="form" :model="form" :rules="rules" label-width="80px" v-loading="loading">
            <el-form-item label="姓名" prop="name">
              <el-input v-model="form.name" placeholder="请输入真实姓名" />
            </el-form-item>
            <el-form-item label="性别">
              <el-radio-group v-model="form.gender">
                <el-radio :label="0">保密</el-radio>
                <el-radio :label="1">男</el-radio>
                <el-radio :label="2">女</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="手机号">
              <el-input v-model="form.phone" placeholder="请输入手机号" />
            </el-form-item>
            <el-form-item label="邮箱">
              <el-input v-model="form.email" placeholder="请输入邮箱" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :loading="saving" @click="handleSave">保存修改</el-button>
              <el-button @click="loadUserInfo">重置</el-button>
            </el-form-item>
          </el-form>
        </el-card>

        <!-- 账号信息 -->
        <el-card style="margin-top:16px">
          <div slot="header"><b>账号信息</b></div>
          <el-descriptions :column="2" border>
            <el-descriptions-item label="用户名">{{ userInfo.username }}</el-descriptions-item>
            <el-descriptions-item label="角色">
              <el-tag :type="userInfo.role === 'admin' ? 'danger' : 'primary'" size="small">
                {{ userInfo.role === 'admin' ? '管理员' : '教师' }}
              </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="用户ID">{{ userInfo.userId }}</el-descriptions-item>
            <el-descriptions-item label="性别">{{ ['保密', '男', '女'][form.gender] || '保密' }}</el-descriptions-item>
          </el-descriptions>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { getUserInfo, updateUser, archiveList } from '@/utils/api'

export default {
  name: 'Profile',
  data() {
    return {
      form: { name: '', gender: 0, phone: '', email: '' },
      rules: { name: [{ required: true, message: '请输入姓名', trigger: 'blur' }] },
      loading: false,
      saving: false,
      myStats: { total: 0, approved: 0, pending: 0 }
    }
  },
  computed: {
    userInfo() {
      try { return JSON.parse(localStorage.getItem('userInfo') || '{}') } catch { return {} }
    }
  },
  mounted() {
    this.loadUserInfo()
    this.loadMyStats()
  },
  methods: {
    async loadUserInfo() {
      this.loading = true
      try {
        const res = await getUserInfo()
        this.form = { name: res.name || '', gender: res.gender || 0, phone: res.phone || '', email: res.email || '' }
      } catch (e) {}
      this.loading = false
    },
    async loadMyStats() {
      try {
        const all = await archiveList({ page: 1, size: 1 })
        const approved = await archiveList({ page: 1, size: 1, status: 1 })
        const pending = await archiveList({ page: 1, size: 1, status: 0 })
        this.myStats = {
          total: all.total || 0,
          approved: approved.total || 0,
          pending: pending.total || 0
        }
      } catch (e) {}
    },
    async handleSave() {
      this.$refs.form.validate(async valid => {
        if (!valid) return
        this.saving = true
        try {
          await updateUser(this.form)
          // 更新本地存储
          const info = JSON.parse(localStorage.getItem('userInfo') || '{}')
          info.name = this.form.name
          localStorage.setItem('userInfo', JSON.stringify(info))
          this.$message.success('保存成功')
        } catch (e) {}
        this.saving = false
      })
    }
  }
}
</script>

<style scoped>
.avatar-card { text-align: center; }
.avatar-wrap { padding: 20px 0; }
.avatar-wrap h3 { margin: 12px 0 4px; font-size: 18px; }
.username { color: #909399; font-size: 13px; margin-bottom: 10px; }
.profile-stats { display: flex; justify-content: space-around; padding: 8px 0; }
.pstat { text-align: center; }
.pstat-num { font-size: 22px; font-weight: 700; color: #303133; }
.pstat-label { font-size: 12px; color: #909399; margin-top: 2px; }
</style>
