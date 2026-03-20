<template>
  <div class="login-page">
    <div class="login-box">
      <div class="login-logo">
        <span class="logo-icon">🎓</span>
        <h1>注册账号</h1>
        <p>高校教师成长档案管理系统</p>
      </div>
      <el-form ref="form" :model="form" :rules="rules" label-width="0" class="login-form">
        <el-form-item prop="username">
          <el-input v-model="form.username" placeholder="用户名（登录账号）" prefix-icon="el-icon-user" />
        </el-form-item>
        <el-form-item prop="name">
          <el-input v-model="form.name" placeholder="真实姓名" prefix-icon="el-icon-postcard" />
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="form.password" type="password" placeholder="密码（至少6位）" prefix-icon="el-icon-lock" show-password />
        </el-form-item>
        <el-form-item>
          <el-input v-model="form.phone" placeholder="手机号（选填）" prefix-icon="el-icon-mobile-phone" />
        </el-form-item>
        <el-form-item>
          <el-input v-model="form.email" placeholder="邮箱（选填）" prefix-icon="el-icon-message" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" class="login-btn" :loading="loading" @click="handleRegister">注 册</el-button>
        </el-form-item>
        <div class="login-footer">
          已有账号？<router-link to="/login">立即登录</router-link>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script>
import { register } from '@/utils/api'

export default {
  name: 'Register',
  data() {
    return {
      form: { username: '', name: '', password: '', phone: '', email: '' },
      rules: {
        username: [{ required: true, message: '请输入用户名', trigger: 'blur' }, { min: 3, message: '用户名至少3位', trigger: 'blur' }],
        name: [{ required: true, message: '请输入真实姓名', trigger: 'blur' }],
        password: [{ required: true, message: '请输入密码', trigger: 'blur' }, { min: 6, message: '密码至少6位', trigger: 'blur' }]
      },
      loading: false
    }
  },
  methods: {
    handleRegister() {
      this.$refs.form.validate(async valid => {
        if (!valid) return
        this.loading = true
        try {
          const res = await register(this.form)
          localStorage.setItem('token', res.token)
          localStorage.setItem('userInfo', JSON.stringify(res))
          this.$message.success('注册成功！')
          this.$router.push('/home/index')
        } catch (e) {}
        finally { this.loading = false }
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
  background: linear-gradient(135deg, #1a73e8 0%, #0d47a1 50%, #1565c0 100%);
}
.login-box {
  width: 420px;
  background: #fff;
  border-radius: 16px;
  padding: 40px;
  box-shadow: 0 20px 60px rgba(0,0,0,0.3);
}
.login-logo { text-align: center; margin-bottom: 28px; }
.logo-icon { font-size: 40px; display: block; margin-bottom: 10px; }
.login-logo h1 { font-size: 22px; color: #1a73e8; font-weight: 700; }
.login-logo p { font-size: 12px; color: #909399; margin-top: 4px; }
.login-btn { width: 100%; height: 44px; font-size: 16px; border-radius: 8px; }
.login-footer { text-align: center; color: #909399; font-size: 14px; }
.login-footer a { color: #1a73e8; text-decoration: none; }
</style>
