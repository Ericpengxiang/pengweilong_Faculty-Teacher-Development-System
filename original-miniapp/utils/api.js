/**
 * API 接口封装
 */
const http = require('./request.js')

const api = {
  // 认证
  login: (data) => http.post('/login', data),
  register: (data) => http.post('/register', data),

  // 用户
  getUserInfo: () => http.get('/user/info'),
  updateUser: (data) => http.put('/user/update', data),

  // 档案
  archiveList: (params) => http.get('/archive/list', params),
  archiveDetail: (id) => http.get('/archive/detail/' + id),
  archiveAdd: (data) => http.post('/archive/add', data),
  archiveUpdate: (data) => http.put('/archive/update', data),
  archiveDelete: (id) => http.delete('/archive/delete/' + id),
  archiveApprove: (id, status) => http.put('/archive/approve/' + id + '?status=' + status),

  // 统计
  statistics: () => http.get('/statistics'),

  // 文件上传
  upload: (filePath) => {
    return new Promise((resolve, reject) => {
      const token = wx.getStorageSync('token')
      wx.uploadFile({
        url: getApp().globalData.baseUrl + '/file/upload',
        filePath: filePath,
        name: 'file',
        header: { 'Authorization': 'Bearer ' + token },
        success(res) {
          const data = JSON.parse(res.data)
          if (data.code === 200) resolve(data.data)
          else reject(data)
        },
        fail: reject
      })
    })
  }
}

module.exports = api
