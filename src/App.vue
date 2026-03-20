<template>
  <div id="app">
    <router-view />
  </div>
</template>

<script>
export default {
  name: 'App'
}
</script>

<style>
* { box-sizing: border-box; margin: 0; padding: 0; }
body { font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', 'PingFang SC', 'Hiragino Sans GB', 'Microsoft YaHei', sans-serif; background: #f5f7fa; color: #303133; }
#app { min-height: 100vh; }
.page-header { margin-bottom: 20px; }
.page-header h2 { font-size: 20px; color: #303133; font-weight: 600; }
.page-header p { color: #909399; font-size: 14px; margin-top: 4px; }
</style>
