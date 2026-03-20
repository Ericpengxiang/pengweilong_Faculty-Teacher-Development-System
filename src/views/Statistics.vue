<template>
  <div>
    <div class="page-header">
      <h2>统计分析</h2>
      <p>教师成长档案数据可视化分析</p>
    </div>

    <div v-loading="loading">
      <!-- 汇总卡片 -->
      <el-row :gutter="16" style="margin-bottom:20px">
        <el-col :span="6">
          <div class="mini-card" style="border-top:4px solid #1a73e8">
            <div class="mini-num">{{ stats.totalArchives || 0 }}</div>
            <div class="mini-label">档案总数</div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="mini-card" style="border-top:4px solid #e6a23c">
            <div class="mini-num">{{ stats.pendingCount || 0 }}</div>
            <div class="mini-label">待审核</div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="mini-card" style="border-top:4px solid #67c23a">
            <div class="mini-num">{{ stats.approvedCount || 0 }}</div>
            <div class="mini-label">已通过</div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="mini-card" style="border-top:4px solid #f56c6c">
            <div class="mini-num">{{ stats.teacherCount || 0 }}</div>
            <div class="mini-label">教师人数</div>
          </div>
        </el-col>
      </el-row>

      <el-row :gutter="16">
        <!-- 年度趋势 -->
        <el-col :span="14">
          <el-card>
            <div slot="header"><b>年度档案趋势</b></div>
            <div ref="trendChart" style="height:300px"></div>
          </el-card>
        </el-col>
        <!-- 类型分布 -->
        <el-col :span="10">
          <el-card>
            <div slot="header"><b>档案类型分布</b></div>
            <div ref="pieChart" style="height:300px"></div>
          </el-card>
        </el-col>
      </el-row>

      <!-- 教师排行 -->
      <el-card style="margin-top:16px">
        <div slot="header"><b>教师档案数量排行 TOP 10</b></div>
        <el-table :data="teacherRank" stripe size="small">
          <el-table-column type="index" label="排名" width="60">
            <template slot-scope="{$index}">
              <el-tag v-if="$index < 3" :type="['', 'warning', 'info'][$index] || 'info'" size="mini">{{ $index + 1 }}</el-tag>
              <span v-else style="color:#909399">{{ $index + 1 }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="teacherName" label="教师姓名" />
          <el-table-column prop="count" label="档案数量" width="100">
            <template slot-scope="{row}">
              <el-progress :percentage="Math.round(row.count / maxRankCount * 100)" :show-text="false" style="width:80px;display:inline-block;margin-right:8px" />
              <b>{{ row.count }}</b>
            </template>
          </el-table-column>
        </el-table>
      </el-card>
    </div>
  </div>
</template>

<script>
import { statistics } from '@/utils/api'
import * as echarts from 'echarts'

const TYPE_COLORS = ['#1a73e8','#34a853','#fbbc04','#ea4335','#9c27b0','#ff5722','#00bcd4','#607d8b','#795548']

export default {
  name: 'Statistics',
  data() {
    return {
      loading: false,
      stats: {},
      yearTrend: [],
      typeDistribution: [],
      teacherRank: [],
      trendChart: null,
      pieChart: null
    }
  },
  computed: {
    maxRankCount() {
      return Math.max(...this.teacherRank.map(r => r.count), 1)
    }
  },
  mounted() { this.loadData() },
  beforeDestroy() {
    if (this.trendChart) this.trendChart.dispose()
    if (this.pieChart) this.pieChart.dispose()
  },
  methods: {
    async loadData() {
      this.loading = true
      try {
        const res = await statistics()
        this.stats = res
        this.yearTrend = res.yearTrend || []
        this.typeDistribution = res.typeDistribution || []
        this.teacherRank = res.teacherRank || []
        this.$nextTick(() => {
          this.renderTrendChart()
          this.renderPieChart()
        })
      } catch (e) {}
      this.loading = false
    },
    renderTrendChart() {
      if (!this.$refs.trendChart) return
      this.trendChart = echarts.init(this.$refs.trendChart)
      const years = this.yearTrend.map(d => d.year + '年')
      const counts = this.yearTrend.map(d => d.count)
      this.trendChart.setOption({
        tooltip: { trigger: 'axis' },
        grid: { left: 40, right: 20, top: 20, bottom: 30 },
        xAxis: { type: 'category', data: years, axisLabel: { fontSize: 12 } },
        yAxis: { type: 'value', minInterval: 1 },
        series: [{
          name: '档案数量',
          type: 'bar',
          data: counts,
          itemStyle: { color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{ offset: 0, color: '#1a73e8' }, { offset: 1, color: '#4fc3f7' }]) },
          label: { show: true, position: 'top', fontSize: 12 },
          barMaxWidth: 50
        }]
      })
    },
    renderPieChart() {
      if (!this.$refs.pieChart) return
      this.pieChart = echarts.init(this.$refs.pieChart)
      const data = this.typeDistribution.map((d, i) => ({
        name: d.type,
        value: d.count,
        itemStyle: { color: TYPE_COLORS[i % TYPE_COLORS.length] }
      }))
      this.pieChart.setOption({
        tooltip: { trigger: 'item', formatter: '{b}: {c} ({d}%)' },
        legend: { orient: 'vertical', right: 10, top: 'center', textStyle: { fontSize: 12 } },
        series: [{
          type: 'pie',
          radius: ['40%', '70%'],
          center: ['40%', '50%'],
          data,
          label: { show: false },
          emphasis: { label: { show: true, fontSize: 14, fontWeight: 'bold' } }
        }]
      })
    }
  }
}
</script>

<style scoped>
.mini-card { background: #fff; border-radius: 8px; padding: 20px; text-align: center; box-shadow: 0 2px 8px rgba(0,0,0,0.06); margin-bottom: 16px; }
.mini-num { font-size: 32px; font-weight: 700; color: #303133; }
.mini-label { font-size: 13px; color: #909399; margin-top: 4px; }
</style>
