// 新增/编辑档案页
const api = require("../../utils/api.js")
const helper = require("../../utils/helper.js")
const TYPES = helper.ARCHIVE_TYPES
Page({
  data: {
    isEdit: false,
    archiveId: null,
    title: "",
    type: "",
    typeIdx: 0,
    types: TYPES,
    year: new Date().getFullYear(),
    description: "",
    fileUrl: ""
  },
  onLoad(options) {
    if (options.id) {
      this.setData({ isEdit: true, archiveId: parseInt(options.id) })
      this.loadDetail(options.id)
    } else {
      this.setData({ type: TYPES[0] })
    }
  },
  async loadDetail(id) {
    try {
      const res = await api.archiveDetail(id)
      const typeIdx = TYPES.indexOf(res.type)
      this.setData({
        title: res.title,
        type: res.type,
        typeIdx: typeIdx >= 0 ? typeIdx : 0,
        year: res.year,
        description: res.description || "",
        fileUrl: res.fileUrl || ""
      })
    } catch (e) {}
  },
  inputTitle(e) { this.setData({ title: e.detail.value }) },
  inputType(e) {
    const idx = parseInt(e.detail.value)
    this.setData({ typeIdx: idx, type: TYPES[idx] })
  },
  inputYear(e) { this.setData({ year: parseInt(e.detail.value) }) },
  inputDesc(e) { this.setData({ description: e.detail.value }) },
  async chooseFile() {
    try {
      const res = await new Promise((resolve, reject) => {
        wx.chooseMessageFile({
          count: 1,
          type: "file",
          success: resolve,
          fail: reject
        })
      })
      const file = res.tempFiles[0]
      wx.showLoading({ title: "上传中..." })
      const url = await api.upload(file.path)
      this.setData({ fileUrl: url })
      wx.hideLoading()
      wx.showToast({ title: "上传成功", icon: "success" })
    } catch (e) {
      wx.hideLoading()
    }
  },
  async submit() {
    const { title, type, year, description, fileUrl, isEdit, archiveId } = this.data
    if (!title.trim()) {
      wx.showToast({ title: "请输入标题", icon: "none" })
      return
    }
    if (!type) {
      wx.showToast({ title: "请选择类型", icon: "none" })
      return
    }
    wx.showLoading({ title: isEdit ? "保存中..." : "提交中..." })
    try {
      const dto = { title: title.trim(), type, year, description, fileUrl }
      if (isEdit) {
        dto.archiveId = archiveId
        await api.archiveUpdate(dto)
        wx.showToast({ title: "保存成功", icon: "success" })
      } else {
        await api.archiveAdd(dto)
        wx.showToast({ title: "提交成功", icon: "success" })
      }
      setTimeout(() => wx.navigateBack(), 800)
    } catch (e) {}
    wx.hideLoading()
  }
})
