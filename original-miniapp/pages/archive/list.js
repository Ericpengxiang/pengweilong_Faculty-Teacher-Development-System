// 档案列表页
const api = require('../../utils/api.js')
const helper = require('../../utils/helper.js')

const TYPES = ['全部', ...helper.ARCHIVE_TYPES]
const YEARS = ['全部', '2020', '2021', '2022', '2023', '2024', '2025', '2026']

Page({
  data: {
    list: [],
    year: null,
    yearIndex: 0,
    years: YEARS,
    type: '',
    typeIndex: 0,
    types: TYPES,
    page: 1,
    size: 10,
    total: 0,
    loading: false,
    userInfo: null
  },
  onLoad() {
    const userInfo = wx.getStorageSync('userInfo')
    if (!userInfo) {
      wx.reLaunch({ url: '/pages/login/login' })
      return
    }
    this.setData({ userInfo })
    this.loadList()
  },
  onPullDownRefresh() {
    this.setData({ page: 1 })
    this.loadList().then(() => wx.stopPullDownRefresh())
  },
  async loadList() {
    if (this.data.loading) return
    this.setData({ loading: true })
    try {
      const { page, size, year, type } = this.data
      const res = await api.archiveList({ page, size, year: year || undefined, type: type || undefined })
      const list = (res.records || []).map(item => ({
        ...item,
        reviewDept: helper.getReviewDept(item.type)
      }))
      this.setData({
        list,
        total: res.total || 0,
        loading: false
      })
    } catch (e) {
      this.setData({ loading: false })
    }
  },
  filterYear(e) {
    const idx = parseInt(e.detail.value)
    const year = idx === 0 ? null : parseInt(this.data.years[idx])
    this.setData({ yearIndex: idx, year, page: 1 })
    this.loadList()
  },
  filterType(e) {
    const idx = parseInt(e.detail.value)
    const type = idx === 0 ? '' : this.data.types[idx]
    this.setData({ typeIndex: idx, type, page: 1 })
    this.loadList()
  },
  goAdd() {
    wx.navigateTo({ url: '/pages/archive/add' })
  },
  goDetail(e) {
    wx.navigateTo({ url: '/pages/archive/detail?id=' + e.currentTarget.dataset.id })
  },
  goEdit(e) {
    wx.navigateTo({ url: '/pages/archive/add?id=' + e.currentTarget.dataset.id })
  }
})
