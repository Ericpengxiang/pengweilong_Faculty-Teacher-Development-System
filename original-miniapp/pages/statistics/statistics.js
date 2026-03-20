// 统计分析页
const api = require('../../utils/api.js')

Page({
  data: {
    yearTrend: [],
    typeDistribution: [],
    teacherRank: [],
    loading: true
  },
  onShow() {
    const userInfo = wx.getStorageSync('userInfo')
    if (!userInfo) {
      wx.reLaunch({ url: '/pages/login/login' })
      return
    }
    this.loadData()
  },
  async loadData() {
    this.setData({ loading: true })
    try {
      const res = await api.statistics()
      this.setData({
        yearTrend: res.yearTrend || [],
        typeDistribution: res.typeDistribution || [],
        teacherRank: res.teacherRank || [],
        loading: false
      })
    } catch (e) {
      this.setData({ loading: false })
    }
  },
  onPullDownRefresh() {
    this.loadData().then(() => wx.stopPullDownRefresh())
  }
})
