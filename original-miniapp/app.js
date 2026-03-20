// 高校教师成长档案管理系统 - 小程序入口
App({
  globalData: {
    userInfo: null,
    token: null,
    // ============================================================
    // 【重要】运行前必须修改 baseUrl！
    // 1. 查看你电脑的本机 IP 地址：
    //    Windows: 打开 cmd，输入 ipconfig，找到 IPv4 地址
    //    Mac/Linux: 打开终端，输入 ifconfig | grep "inet "
    // 2. 将下面的 192.168.x.x 替换为你的实际 IP 地址
    // 3. 确保手机/模拟器和电脑在同一 WiFi 网络下
    // 4. 在微信开发者工具中勾选"不校验合法域名"（详见教程）
    // ============================================================
    baseUrl: 'http://192.168.1.100:8080/api'
    // 示例：
    // baseUrl: 'http://192.168.0.105:8080/api'  // 家庭网络
    // baseUrl: 'http://10.0.0.5:8080/api'       // 公司网络
    // baseUrl: 'http://localhost:8080/api'       // 仅用于模拟器（不适用真机）
  },
  onLaunch() {
    const token = wx.getStorageSync('token')
    const userInfo = wx.getStorageSync('userInfo')
    if (token && userInfo) {
      this.globalData.token = token
      this.globalData.userInfo = userInfo
    }
  }
})
