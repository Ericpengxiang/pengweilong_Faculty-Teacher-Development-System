import request from './request'

// 档案类型与审核部门映射
export const ARCHIVE_TYPES = [
  '科研成果', '教学成果', '教学改革', '获奖情况',
  '培训记录', '进修培训', '职称晋升', '个人学历', '基础信息'
]

const DEPT_MAP = {
  '科研成果': '科研管理中心',
  '教学成果': '教务管理中心',
  '教学改革': '教务管理中心',
  '获奖情况': '教务管理中心',
  '培训记录': '教师教学发展中心',
  '进修培训': '教师教学发展中心',
  '职称晋升': '人力资源中心',
  '个人学历': '人力资源中心',
  '基础信息': '人力资源中心',
}

export function getReviewDept(type) {
  return DEPT_MAP[type] || '综合管理部门'
}

export const STATUS_MAP = {
  0: { label: '待审核', type: 'warning' },
  1: { label: '已通过', type: 'success' },
  2: { label: '已拒绝', type: 'danger' }
}

// 认证
export const login = (data) => request.post('/login', data)
export const register = (data) => request.post('/register', data)

// 用户
export const getUserInfo = () => request.get('/user/info')
export const updateUser = (data) => request.put('/user/update', data)

// 档案
export const archiveList = (params) => request.get('/archive/list', { params })
export const archiveDetail = (id) => request.get(`/archive/detail/${id}`)
export const archiveAdd = (data) => request.post('/archive/add', data)
export const archiveUpdate = (data) => request.put('/archive/update', data)
export const archiveDelete = (id) => request.delete(`/archive/delete/${id}`)
export const archiveApprove = (id, status) => request.put(`/archive/approve/${id}?status=${status}`)

// 统计
export const statistics = () => request.get('/statistics')

// 文件上传
export const uploadFile = (formData) => request.post('/file/upload', formData, {
  headers: { 'Content-Type': 'multipart/form-data' }
})
