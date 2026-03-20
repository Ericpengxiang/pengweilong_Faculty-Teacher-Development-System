const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  publicPath: '/',
  devServer: {
    port: 8094,
    proxy: {
      '/api': {
        target: 'http://localhost:8092',
        changeOrigin: true
      }
    }
  }
})
