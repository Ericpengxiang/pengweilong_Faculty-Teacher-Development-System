<template>
  <div>
    <div class="page-header">
      <h2>首页概览</h2>
      <p>欢迎回来，{{ freshName }}！今天是 {{ today }}</p>
    </div>

    <!-- 统计卡片 -->
    <el-row :gutter="16" class="stat-cards">
      <el-col :span="6" v-for="card in statCards" :key="card.key">
        <div class="stat-card" :style="{ borderTop: '4px solid ' + card.color }">
          <div class="stat-icon" :style="{ background: card.color + '20', color: card.color }">
            <i :class="card.icon"></i>
          </div>
          <div class="stat-info">
            <div class="stat-num">{{ stats[card.key] || 0 }}</div>
            <div class="stat-label">{{ card.label }}</div>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- 快捷操作 -->
    <el-row :gutter="16" style="margin-top:20px">
      <el-col :span="12">
        <el-card class="quick-card">
          <div slot="header"><b>快捷操作</b></div>
          <div class="quick-actions">
            <div class="quick-item" @click="$router.push('/home/archive/add')">
              <i class="el-icon-plus quick-icon" style="color:#1a73e8"></i>
              <span>新增档案</span>
            </div>
            <div class="quick-item" @click="$router.push('/home/archives')">
              <i class="el-icon-search quick-icon" style="color:#67c23a"></i>
              <span>查看档案</span>
            </div>
            <div class="quick-item" @click="$router.push('/home/statistics')">
              <i class="el-icon-data-analysis quick-icon" style="color:#e6a23c"></i>
              <span>统计分析</span>
            </div>
            <div class="quick-item" @click="$router.push('/home/profile')">
              <i class="el-icon-user quick-icon" style="color:#f56c6c"></i>
              <span>个人中心</span>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card class="quick-card">
          <div slot="header"><b>档案类型分布</b></div>
          <div v-if="typeDistribution.length">
            <div v-for="item in typeDistribution.slice(0,5)" :key="item.type" class="type-row">
              <span class="type-name">{{ item.type }}</span>
              <el-progress :percentage="Math.round(item.count / totalArchives * 100)" :color="getTypeColor(item.type)" :show-text="false" style="flex:1;margin:0 12px" />
              <span class="type-count">{{ item.count }}</span>
            </div>
          </div>
          <el-empty v-else description="暂无数据" :image-size="60" />
        </el-card>
      </el-col>
    </el-row>

    <!-- 最近档案 -->
    <el-card style="margin-top:20px">
      <div slot="header" style="display:flex;justify-content:space-between;align-items:center">
        <b>最近档案</b>
        <el-button type="text" @click="$router.push('/home/archives')">查看全部 <i class="el-icon-arrow-right"></i></el-button>
      </div>
      <el-table :data="recentArchives" stripe size="small">
        <el-table-column prop="title" label="标题" min-width="200" show-overflow-tooltip />
        <el-table-column prop="type" label="类型" width="100">
          <template slot-scope="{row}">
            <el-tag size="mini" :color="getTypeColor(row.type) + '20'" :style="{ color: getTypeColor(row.type), border: 'none' }">{{ row.type }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="year" label="年份" width="80" />
        <el-table-column prop="status" label="状态" width="90">
          <template slot-scope="{row}">
            <el-tag :type="statusMap[row.status].type" size="mini">{{ statusMap[row.status].label }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="teacherName" label="教师" width="100" v-if="userInfo.role === 'admin'" />
        <el-table-column label="操作" width="80">
          <template slot-scope="{row}">
            <el-button type="text" size="mini" @click="$router.push('/home/archive/detail/' + row.archiveId)">查看</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
import { statistics, archiveList, getReviewDept, getUserInfo } from '@/utils/api'

const TYPE_COLORS = {
  '科研成果': '#1a73e8', '教学成果': '#34a853', '教学改革': '#fbbc04',
  '获奖情况': '#ea4335', '培训记录': '#9c27b0', '进修培训': '#ff5722',
  '职称晋升': '#00bcd4', '个人学历': '#607d8b', '基础信息': '#795548'
}

export default {
  name: 'Index',
  data() {
    return {
      stats: {},
      typeDistribution: [],
      recentArchives: [],
      totalArchives: 0,
      freshName: '',
      statusMap: { 0: { label: '待审核', type: 'warning' }, 1: { label: '已通过', type: 'success' }, 2: { label: '已拒绝', type: 'danger' } },
      statCards: [
        { key: 'totalArchives', label: '档案总数', icon: 'el-icon-folder', color: '#1a73e8' },
        { key: 'pendingCount', label: '待审核', icon: 'el-icon-time', color: '#e6a23c' },
        { key: 'approvedCount', label: '已通过', icon: 'el-icon-circle-check', color: '#67c23a' },
        { key: 'rejectedCount', label: '已拒绝', icon: 'el-icon-circle-close', color: '#f56c6c' },
      ]
    }
  },
  computed: {
    userInfo() {
      try { return JSON.parse(localStorage.getItem('userInfo') || '{}') } catch { return {} }
    },
    today() {
      return new Date().toLocaleDateString('zh-CN', { year: 'numeric', month: 'long', day: 'numeric', weekday: 'long' })
    }
  },
  mounted() {
    this.loadData()
    this.refreshName()
  },
  methods: {
    async refreshName() {
      try {
        const res = await getUserInfo()
        if (res && res.name) this.freshName = res.name
        else {
          const u = JSON.parse(localStorage.getItem('userInfo') || '{}')
          this.freshName = u.name || u.username || ''
        }
      } catch (e) {
        const u = JSON.parse(localStorage.getItem('userInfo') || '{}')
        this.freshName = u.name || u.username || ''
      }
    },
    async loadData() {
      try {
        const res = await statistics()
        // 后端返回的是 yearTrend/typeDistribution/teacherRank
        this.typeDistribution = res.typeDistribution || []
        const totalFromTypes = this.typeDistribution.reduce((s, i) => s + i.count, 0)
        this.totalArchives = totalFromTypes || 1
        this.stats = {
          totalArchives: res.totalArchives || totalFromTypes || 0,
          pendingCount: res.pendingCount || 0,
          approvedCount: res.approvedCount || totalFromTypes || 0,
          rejectedCount: res.rejectedCount || 0,
        }
      } catch (e) {}
      try {
        const res = await archiveList({ page: 1, size: 5 })
        this.recentArchives = (res.records || res.list || []).map(r => ({
          ...r,
          reviewDept: getReviewDept(r.type)
        }))
      } catch (e) {}
    },
    getTypeColor(type) {
      return TYPE_COLORS[type] || '#909399'
    }
  }
}
</script>

<style scoped>
.stat-cards { margin-bottom: 4px; }
.stat-card {
  background: #fff;
  border-radius: 8px;
  padding: 20px;
  display: flex;
  align-items: center;
  gap: 16px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.06);
  margin-bottom: 16px;
}
.stat-icon { width: 52px; height: 52px; border-radius: 12px; display: flex; align-items: center; justify-content: center; font-size: 24px; flex-shrink: 0; }
.stat-num { font-size: 28px; font-weight: 700; color: #303133; line-height: 1; }
.stat-label { font-size: 13px; color: #909399; margin-top: 4px; }
.quick-card { height: 200px; }
.quick-actions { display: grid; grid-template-columns: 1fr 1fr; gap: 12px; }
.quick-item { display: flex; flex-direction: column; align-items: center; padding: 16px; border-radius: 8px; cursor: pointer; transition: background 0.2s; }
.quick-item:hover { background: #f5f7fa; }
.quick-icon { font-size: 28px; margin-bottom: 6px; }
.quick-item span { font-size: 13px; color: #606266; }
.type-row { display: flex; align-items: center; margin-bottom: 10px; }
.type-name { width: 80px; font-size: 13px; color: #606266; flex-shrink: 0; }
.type-count { width: 30px; text-align: right; font-size: 13px; color: #303133; font-weight: 600; }
</style>
