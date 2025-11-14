import { ref } from 'vue'

export function usePaginatedList(api, pageSize = 10) {
  const list         = ref([])        // 装菜的篮子
  const pageNum      = ref(1)         // 第几次买菜
  const loading      = ref(false)     // 正在买菜中？
  const isRefreshing = ref(false)     // 要不要把篮子清空重买
  const hasMore      = ref(true)      // 老板还有没有菜
  const keyword      = ref('')        // 只要“西红柿”
  const typeId       = ref(0)         // 只要“蔬菜区”

  // 真正去买菜的函数
  const loadData = async (isRefresh = false) => {
    if (isRefresh) { pageNum.value = 1; list.value = [] }
    if (!hasMore.value && !isRefresh) return

    loading.value = true
    const res = await api({
      pageNum: pageNum.value,
      pageSize,
      ...(keyword.value && { title: keyword.value }),
      ...(typeId.value && { typeId: typeId.value })
    })
    const rows = res?.data?.data || []
    list.value = isRefresh ? rows : [...list.value, ...rows]
    hasMore.value = rows.length === pageSize
    pageNum.value++
    loading.value = false
    isRefreshing.value = false
  }

  // 下面都是按钮，按一下就行
  const onSearch   = () => loadData(true)
  const onRefresh  = () => { isRefreshing.value = true; loadData(true) }
  const loadMore   = () => loadData(false)
  const selectType = (id) => { typeId.value = id; loadData(true) }

  return { list, loading, isRefreshing, hasMore, keyword, typeId, onSearch, onRefresh, loadMore, selectType, loadData }
}