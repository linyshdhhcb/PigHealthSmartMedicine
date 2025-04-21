// import { Message } from '@arco-design/web-vue';
import { ElMessage } from "element-plus"

export default {
    success(msg) {
        ElMessage.success(msg || '操作成功')
    },
    error(msg) {
        ElMessage.error(msg || '操作失败')
    }
}