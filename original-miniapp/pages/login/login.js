// 登录页
const api = require('../../utils/api.js')

Page({
  data: {
    username: '',
    password: '',
    isRegister: false,
    name: '',
    gender: 0,
    phone: '',
    email: ''
  },
  onLoad() {
    const token = wx.getStorageSync('token')
    if (token) {
      wx.switchTab({ url: '/pages/index/index' })
    }
  },
  switchMode() {
    this.setData({ isRegister: !this.data.isRegister })
  },
  inputUsername(e) { this.setData({ username: e.detail.value }) },
  inputPassword(e) { this.setData({ password: e.detail.value }) },
  inputName(e) { this.setData({ name: e.detail.value }) },
  inputPhone(e) { this.setData({ phone: e.detail.value }) },
  inputEmail(e) { this.setData({ email: e.detail.value }) },
  async submit() {
    const { username, password, isRegister, name } = this.data
    if (!username || !password) {
      wx.showToast({ title: '请填写用户名和密码', icon: 'none' })
      return
    }
    if (isRegister && !name) {
      wx.showToast({ title: '请填写姓名', icon: 'none' })
      return
    }
    wx.showLoading({ title: '处理中' })
    try {
      const app = getApp()
      const res = isRegister
        ? await api.register({ username, password, name, gender: 0, phone: this.data.phone, email: this.data.email })
        : await api.login({ username, password })
      wx.setStorageSync('token', res.token)
      wx.setStorageSync('userInfo', { userId: res.userId, username: res.username, name: res.name, role: res.role })
      app.globalData.token = res.token
      app.globalData.userInfo = { userId: res.userId, username: res.username, name: res.name, role: res.role }
      wx.showToast({ title: '成功', icon: 'success' })
      setTimeout(() => wx.switchTab({ url: '/pages/index/index' }), 500)
    } catch (e) {
      // 错误已在 request 中提示
    }
    wx.hideLoading()
  }
})
