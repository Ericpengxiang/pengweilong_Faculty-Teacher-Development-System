// 个人中心页
const api = require('../../utils/api.js')

Page({
  data: {
    userInfo: null
  },
  onShow() {
    const userInfo = wx.getStorageSync('userInfo')
    if (!userInfo) {
      wx.reLaunch({ url: '/pages/login/login' })
      return
    }
    this.setData({ userInfo })
    this.loadUserInfo()
  },
  async loadUserInfo() {
    try {
      const res = await api.getUserInfo()
      this.setData({ userInfo: { ...this.data.userInfo, ...res } })
      wx.setStorageSync('userInfo', this.data.userInfo)
    } catch (e) {}
  },
  logout() {
    wx.showModal({
      title: '确认退出',
      content: '确定要退出登录吗？',
      success(res) {
        if (res.confirm) {
          wx.removeStorageSync('token')
          wx.removeStorageSync('userInfo')
          getApp().globalData.token = null
          getApp().globalData.userInfo = null
          wx.reLaunch({ url: '/pages/login/login' })
        }
      }
    })
  },
  goEdit() {
    wx.navigateTo({ url: '/pages/profile/edit' })
  }
})
