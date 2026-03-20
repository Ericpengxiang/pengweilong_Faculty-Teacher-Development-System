<template>
  <div class="login-page">
    <div class="login-bg"></div>
    <div class="login-box">
      <div class="login-logo">
        <span class="logo-icon">🎓</span>
        <h1>高校教师成长档案管理系统</h1>
        <p>Faculty Teacher Development System</p>
      </div>
      <el-form ref="form" :model="form" :rules="rules" label-width="0" class="login-form">
        <el-form-item prop="username">
          <el-input v-model="form.username" placeholder="请输入用户名" prefix-icon="el-icon-user" size="large" @keyup.enter.native="handleLogin" />
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="form.password" type="password" placeholder="请输入密码" prefix-icon="el-icon-lock" size="large" show-password @keyup.enter.native="handleLogin" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" class="login-btn" :loading="loading" @click="handleLogin">登 录</el-button>
        </el-form-item>
        <div class="login-footer">
          还没有账号？<router-link to="/register">立即注册</router-link>
        </div>
      </el-form>
      <div class="demo-accounts">
        <p class="demo-title">演示账号</p>
        <div class="demo-list">
          <el-tag size="small" type="danger" @click="fillDemo('admin','123456')" style="cursor:pointer;margin-right:8px">管理员: admin / 123456</el-tag>
          <el-tag size="small" type="primary" @click="fillDemo('teacher001','123456')" style="cursor:pointer">教师: teacher001 / 123456</el-tag>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { login } from '@/utils/api'

export default {
  name: 'Login',
  data() {
    return {
      form: { username: '', password: '' },
      rules: {
        username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
        password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
      },
      loading: false
    }
  },
  methods: {
    fillDemo(username, password) {
      this.form.username = username
      this.form.password = password
    },
    handleLogin() {
      this.$refs.form.validate(async valid => {
        if (!valid) return
        this.loading = true
        try {
          const res = await login(this.form)
          localStorage.setItem('token', res.token)
          localStorage.setItem('userInfo', JSON.stringify(res))
          this.$message.success('登录成功，欢迎 ' + res.name)
          this.$router.push('/home/index')
        } catch (e) {
          // 错误已在拦截器处理
        } finally {
          this.loading = false
        }
      })
    }
  }
}
</script>

<style scoped>
.login-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  background: linear-gradient(135deg, #1a73e8 0%, #0d47a1 50%, #1565c0 100%);
}
.login-bg {
  position: absolute;
  inset: 0;
  background: url("data:image/svg+xml,%3Csvg width='60' height='60' viewBox='0 0 60 60' xmlns='http://www.w3.org/2000/svg'%3E%3Cg fill='none' fill-rule='evenodd'%3E%3Cg fill='%23ffffff' fill-opacity='0.05'%3E%3Cpath d='M36 34v-4h-2v4h-4v2h4v4h2v-4h4v-2h-4zm0-30V0h-2v4h-4v2h4v4h2V6h4V4h-4zM6 34v-4H4v4H0v2h4v4h2v-4h4v-2H6zM6 4V0H4v4H0v2h4v4h2V6h4V4H6z'/%3E%3C/g%3E%3C/g%3E%3C/svg%3E");
}
.login-box {
  position: relative;
  width: 420px;
  background: #fff;
  border-radius: 16px;
  padding: 40px;
  box-shadow: 0 20px 60px rgba(0,0,0,0.3);
}
.login-logo { text-align: center; margin-bottom: 32px; }
.logo-icon { font-size: 48px; display: block; margin-bottom: 12px; }
.login-logo h1 { font-size: 20px; color: #1a73e8; font-weight: 700; margin-bottom: 4px; }
.login-logo p { font-size: 12px; color: #909399; }
.login-btn { width: 100%; height: 44px; font-size: 16px; border-radius: 8px; }
.login-footer { text-align: center; color: #909399; font-size: 14px; }
.login-footer a { color: #1a73e8; text-decoration: none; }
.demo-accounts { margin-top: 20px; padding-top: 20px; border-top: 1px solid #f0f0f0; }
.demo-title { font-size: 12px; color: #909399; margin-bottom: 8px; text-align: center; }
.demo-list { display: flex; justify-content: center; flex-wrap: wrap; gap: 6px; }
</style>
