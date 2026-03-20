/**
 * 网络请求封装 - 统一处理 Token、错误
 */
const app = getApp()

function request(options) {
  return new Promise((resolve, reject) => {
    const token = wx.getStorageSync('token') || app.globalData.token
    wx.request({
      url: app.globalData.baseUrl + options.url,
      method: options.method || 'GET',
      data: options.data || {},
      header: {
        'Content-Type': 'application/json',
        'Authorization': token ? 'Bearer ' + token : '',
        ...options.header
      },
      success(res) {
        if (res.statusCode === 401) {
          wx.removeStorageSync('token')
          wx.removeStorageSync('userInfo')
          wx.reLaunch({ url: '/pages/login/login' })
          reject(new Error('登录已过期'))
          return
        }
        const data = res.data
        if (data.code === 200) {
          resolve(data.data)
        } else {
          wx.showToast({ title: data.message || '请求失败', icon: 'none' })
          reject(data)
        }
      },
      fail(err) {
        wx.showToast({ title: '网络错误', icon: 'none' })
        reject(err)
      }
    })
  })
}

module.exports = {
  get: (url, data) => request({ url, method: 'GET', data }),
  post: (url, data) => request({ url, method: 'POST', data }),
  put: (url, data) => request({ url, method: 'PUT', data }),
  delete: (url) => request({ url, method: 'DELETE' })
}
