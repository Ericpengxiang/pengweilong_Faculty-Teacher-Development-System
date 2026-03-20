<template>
  <div class="layout">
    <!-- 顶部导航 -->
    <div class="header">
      <div class="header-left">
        <span class="logo">🎓</span>
        <span class="title">高校教师成长档案管理系统</span>
      </div>
      <div class="header-right">
        <el-dropdown @command="handleCommand">
          <div class="user-info">
            <el-avatar :size="32" :style="{ background: userInfo.role === 'admin' ? '#f56c6c' : '#1a73e8' }">
              {{ userInfo.name ? userInfo.name[0] : 'U' }}
            </el-avatar>
            <span class="user-name">{{ userInfo.name || userInfo.username }}</span>
            <el-tag size="mini" :type="userInfo.role === 'admin' ? 'danger' : 'primary'" style="margin-left:6px">
              {{ userInfo.role === 'admin' ? '管理员' : '教师' }}
            </el-tag>
            <i class="el-icon-arrow-down" style="margin-left:6px;color:#909399"></i>
          </div>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item command="profile" icon="el-icon-user">个人中心</el-dropdown-item>
            <el-dropdown-item command="logout" icon="el-icon-switch-button" divided>退出登录</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </div>
    </div>

    <div class="main-container">
      <!-- 侧边栏 -->
      <div class="sidebar">
        <el-menu
          :default-active="$route.path"
          router
          background-color="#001529"
          text-color="#ffffffa6"
          active-text-color="#fff"
          class="sidebar-menu"
        >
          <el-menu-item index="/home/index">
            <i class="el-icon-s-home"></i>
            <span>首页概览</span>
          </el-menu-item>
          <el-menu-item index="/home/archives">
            <i class="el-icon-folder-opened"></i>
            <span>档案管理</span>
          </el-menu-item>
          <el-menu-item index="/home/statistics">
            <i class="el-icon-data-analysis"></i>
            <span>统计分析</span>
          </el-menu-item>
          <el-menu-item index="/home/profile">
            <i class="el-icon-user"></i>
            <span>个人中心</span>
          </el-menu-item>
        </el-menu>
      </div>

      <!-- 内容区 -->
      <div class="content">
        <router-view />
      </div>
    </div>
  </div>
</template>

<script>
import { getUserInfo } from '@/utils/api'

export default {
  name: 'Layout',
  data() {
    return {
      freshUserInfo: null
    }
  },
  computed: {
    userInfo() {
      if (this.freshUserInfo) return this.freshUserInfo
      try {
        return JSON.parse(localStorage.getItem('userInfo') || '{}')
      } catch { return {} }
    }
  },
  mounted() {
    // 每次进入布局时从 API 刷新用户信息，避免 localStorage 中的旧数据乱码
    this.refreshUserInfo()
  },
  methods: {
    async refreshUserInfo() {
      try {
        const res = await getUserInfo()
        if (res && res.userId) {
          this.freshUserInfo = res
          localStorage.setItem('userInfo', JSON.stringify(res))
        }
      } catch (e) {}
    },
    handleCommand(cmd) {
      if (cmd === 'logout') {
        this.$confirm('确定要退出登录吗？', '提示', { type: 'warning' }).then(() => {
          localStorage.removeItem('token')
          localStorage.removeItem('userInfo')
          this.$router.push('/login')
          this.$message.success('已退出登录')
        }).catch(() => {})
      } else if (cmd === 'profile') {
        this.$router.push('/home/profile')
      }
    }
  }
}
</script>

<style scoped>
.layout { display: flex; flex-direction: column; min-height: 100vh; }
.header {
  height: 60px;
  background: #001529;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 24px;
  position: fixed;
  top: 0; left: 0; right: 0;
  z-index: 1000;
  box-shadow: 0 2px 8px rgba(0,0,0,0.3);
}
.header-left { display: flex; align-items: center; gap: 10px; }
.logo { font-size: 28px; }
.title { color: #fff; font-size: 18px; font-weight: 600; }
.header-right { display: flex; align-items: center; }
.user-info { display: flex; align-items: center; cursor: pointer; color: #ffffffa6; }
.user-name { margin-left: 8px; font-size: 14px; color: #ffffffd9; }
.main-container { display: flex; margin-top: 60px; min-height: calc(100vh - 60px); }
.sidebar { width: 220px; background: #001529; min-height: calc(100vh - 60px); position: fixed; top: 60px; left: 0; bottom: 0; overflow-y: auto; }
.sidebar-menu { border-right: none; height: 100%; }
.content { margin-left: 220px; flex: 1; padding: 24px; background: #f5f7fa; min-height: calc(100vh - 60px); }
</style>
