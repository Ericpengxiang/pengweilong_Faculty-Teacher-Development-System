// 档案详情页
const api = require('../../utils/api.js')
const helper = require('../../utils/helper.js')

Page({
  data: {
    detail: null,
    userInfo: null,
    reviewDept: ''
  },
  onLoad(opts) {
    const userInfo = wx.getStorageSync('userInfo')
    if (!userInfo) {
      wx.reLaunch({ url: '/pages/login/login' })
      return
    }
    this.setData({ userInfo })
    if (opts.id) this.loadDetail(opts.id)
  },
  async loadDetail(id) {
    wx.showLoading({ title: '加载中' })
    try {
      const res = await api.archiveDetail(id)
      this.setData({
        detail: res,
        reviewDept: helper.getReviewDept(res.type)
      })
      wx.setNavigationBarTitle({ title: res.title })
    } catch (e) {}
    wx.hideLoading()
  },
  goEdit() {
    wx.navigateTo({ url: '/pages/archive/add?id=' + this.data.detail.archiveId })
  },
  async deleteArchive() {
    wx.showModal({
      title: '确认删除',
      content: '确定要删除这条档案吗？',
      success: async (res) => {
        if (res.confirm) {
          try {
            await api.archiveDelete(this.data.detail.archiveId)
            wx.showToast({ title: '已删除', icon: 'success' })
            setTimeout(() => wx.navigateBack(), 500)
          } catch (e) {}
        }
      }
    })
  },
  async approve(e) {
    if (this.data.userInfo.role !== 'admin') return
    const status = parseInt(e.currentTarget.dataset.status)
    try {
      await api.archiveApprove(this.data.detail.archiveId, status)
      wx.showToast({ title: status === 1 ? '已通过' : '已拒绝', icon: 'success' })
      this.loadDetail(this.data.detail.archiveId)
    } catch (e) {}
  }
})
