// 首页
const api = require("../../utils/api.js")
Page({
  data: {
    userInfo: null,
    archiveCount: 0,
    pendingCount: 0
  },
  onShow() {
    const userInfo = wx.getStorageSync("userInfo")
    if (!userInfo) {
      wx.reLaunch({ url: "/pages/login/login" })
      return
    }
    this.setData({ userInfo })
    this.loadStats()
  },
  async loadStats() {
    try {
      const res = await api.archiveList({ page: 1, size: 1 })
      this.setData({ archiveCount: res.total || 0 })
      // 统计待审核
      const pending = await api.archiveList({ page: 1, size: 100 })
      const pendingCount = (pending.records || []).filter(a => a.status === 0).length
      this.setData({ pendingCount })
    } catch (e) {}
  },
  goArchive() { wx.switchTab({ url: "/pages/archive/list" }) },
  goAdd() { wx.navigateTo({ url: "/pages/archive/add" }) },
  goStatistics() { wx.switchTab({ url: "/pages/statistics/statistics" }) },
  goProfile() { wx.switchTab({ url: "/pages/profile/profile" }) }
})
