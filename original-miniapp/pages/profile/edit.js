// 修改个人信息页
const api = require('../../utils/api.js')

Page({
  data: {
    name: '',
    gender: 0,
    phone: '',
    email: ''
  },
  onLoad() {
    const userInfo = wx.getStorageSync('userInfo')
    if (!userInfo) {
      wx.reLaunch({ url: '/pages/login/login' })
      return
    }
    this.setData({
      name: userInfo.name || '',
      gender: userInfo.gender || 0,
      phone: userInfo.phone || '',
      email: userInfo.email || ''
    })
  },
  inputName(e) { this.setData({ name: e.detail.value }) },
  inputGender(e) { this.setData({ gender: parseInt(e.detail.value) }) },
  inputPhone(e) { this.setData({ phone: e.detail.value }) },
  inputEmail(e) { this.setData({ email: e.detail.value }) },
  async submit() {
    const { name, gender, phone, email } = this.data
    if (!name.trim()) {
      wx.showToast({ title: '请填写姓名', icon: 'none' })
      return
    }
    wx.showLoading({ title: '保存中' })
    try {
      await api.updateUser({ name, gender, phone, email })
      const userInfo = wx.getStorageSync('userInfo')
      Object.assign(userInfo, { name, gender, phone, email })
      wx.setStorageSync('userInfo', userInfo)
      wx.showToast({ title: '保存成功', icon: 'success' })
      setTimeout(() => wx.navigateBack(), 500)
    } catch (e) {}
    wx.hideLoading()
  }
})
