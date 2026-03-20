<template>
  <div>
    <div class="page-header" style="display:flex;align-items:center;gap:12px">
      <el-button icon="el-icon-arrow-left" circle size="small" @click="$router.back()" />
      <div>
        <h2>{{ isEdit ? '编辑档案' : '新增档案' }}</h2>
        <p>{{ isEdit ? '修改档案信息' : '填写教师成长档案信息' }}</p>
      </div>
    </div>

    <el-card>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px" v-loading="loading">
        <el-row :gutter="20">
          <el-col :span="16">
            <el-form-item label="档案标题" prop="title">
              <el-input v-model="form.title" placeholder="请输入档案标题" maxlength="200" show-word-limit />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="年份" prop="year">
              <el-select v-model="form.year" placeholder="选择年份" style="width:100%">
                <el-option v-for="y in years" :key="y" :label="y + '年'" :value="y" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="档案类型" prop="type">
              <el-select v-model="form.type" placeholder="选择类型" style="width:100%" @change="onTypeChange">
                <el-option v-for="t in archiveTypes" :key="t" :label="t" :value="t" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="审核部门">
              <el-input :value="reviewDept" disabled />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="档案描述" prop="description">
          <el-input v-model="form.description" type="textarea" :rows="5" placeholder="请详细描述档案内容..." maxlength="2000" show-word-limit />
        </el-form-item>

        <el-form-item label="附件">
          <el-upload
            class="upload-area"
            action="/api/file/upload"
            :headers="uploadHeaders"
            :on-success="handleUploadSuccess"
            :on-error="handleUploadError"
            :file-list="fileList"
            :limit="3"
            accept=".pdf,.doc,.docx,.jpg,.jpeg,.png"
          >
            <el-button size="small" icon="el-icon-upload2">点击上传</el-button>
            <div slot="tip" class="el-upload__tip">支持 PDF、Word、图片，单文件不超过 10MB</div>
          </el-upload>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" :loading="submitting" @click="handleSubmit">
            {{ isEdit ? '保存修改' : '提交档案' }}
          </el-button>
          <el-button @click="$router.back()">取消</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import { archiveDetail, archiveAdd, archiveUpdate, ARCHIVE_TYPES, getReviewDept } from '@/utils/api'

export default {
  name: 'ArchiveForm',
  data() {
    return {
      form: { title: '', type: '', year: new Date().getFullYear(), description: '', fileUrl: '' },
      rules: {
        title: [{ required: true, message: '请输入档案标题', trigger: 'blur' }],
        type: [{ required: true, message: '请选择档案类型', trigger: 'change' }],
        year: [{ required: true, message: '请选择年份', trigger: 'change' }],
        description: [{ required: true, message: '请填写档案描述', trigger: 'blur' }]
      },
      archiveTypes: ARCHIVE_TYPES,
      years: Array.from({ length: 10 }, (_, i) => new Date().getFullYear() - i),
      loading: false,
      submitting: false,
      fileList: []
    }
  },
  computed: {
    isEdit() { return !!this.$route.params.id },
    reviewDept() { return getReviewDept(this.form.type) },
    uploadHeaders() {
      const token = localStorage.getItem('token')
      return { Authorization: 'Bearer ' + token }
    }
  },
  mounted() {
    if (this.isEdit) this.loadDetail()
  },
  methods: {
    async loadDetail() {
      this.loading = true
      try {
        const res = await archiveDetail(this.$route.params.id)
        this.form = {
          archiveId: res.archiveId,
          title: res.title,
          type: res.type,
          year: res.year,
          description: res.description || '',
          fileUrl: res.fileUrl || ''
        }
        if (res.fileUrl) {
          this.fileList = [{ name: '已上传附件', url: res.fileUrl }]
        }
      } catch (e) {}
      this.loading = false
    },
    onTypeChange() {},
    handleUploadSuccess(res) {
      if (res.code === 200) {
        this.form.fileUrl = res.data
        this.$message.success('上传成功')
      }
    },
    handleUploadError() {
      this.$message.error('上传失败')
    },
    handleSubmit() {
      this.$refs.form.validate(async valid => {
        if (!valid) return
        this.submitting = true
        try {
          if (this.isEdit) {
            await archiveUpdate(this.form)
            this.$message.success('修改成功')
          } else {
            await archiveAdd(this.form)
            this.$message.success('档案提交成功，等待审核')
          }
          this.$router.push('/home/archives')
        } catch (e) {}
        this.submitting = false
      })
    }
  }
}
</script>

<style scoped>
.upload-area { display: inline-block; }
</style>
