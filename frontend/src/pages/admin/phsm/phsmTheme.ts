/**
 * 医疗内容管理页统一视觉：Emerald + Teal（仅样式类名，供 Tailwind 使用）
 */
export const phsm = {
  /** 页面最外层：渐变背景 + 内边距 */
  page: "min-h-full bg-gradient-to-br from-emerald-50 via-white to-teal-100 p-4 sm:p-6 lg:p-8",
  /** 内容区最大宽度与纵向间距 */
  pageInner: "mx-auto max-w-[1600px] space-y-5",
  /** 顶栏：标题 + 操作区 */
  toolbar: "flex flex-col gap-4 sm:flex-row sm:flex-wrap sm:items-end sm:justify-between",
  titleBlock: "space-y-2",
  /** 主标题 */
  title: "text-xl font-semibold tracking-tight text-teal-800 sm:text-2xl",
  /** 标题下装饰条 */
  titleAccent: "h-1 w-14 rounded-full bg-gradient-to-r from-emerald-500 via-emerald-600 to-teal-700 shadow-sm shadow-emerald-400/20",
  actions: "flex flex-wrap items-center gap-2",
  /** 数据卡片 */
  card: "rounded-xl border border-emerald-100 bg-white/95 shadow-md shadow-emerald-400/20 backdrop-blur-sm transition-all duration-300 hover:-translate-y-0.5 hover:shadow-lg hover:shadow-teal-300/20",
  cardPt: "pt-6",
  /** 输入框（Input / 与选择器触发器组合使用） */
  input:
    "border-emerald-100 bg-emerald-50/60 text-teal-900 placeholder:text-teal-600/45 transition-all duration-200 focus-visible:border-emerald-500 focus-visible:ring-2 focus-visible:ring-emerald-400/20",
  /** 表格头行背景 */
  tableHeadRow: "border-emerald-100 bg-teal-300/20 hover:bg-teal-300/20",
  tableHeadCell: "text-teal-800 font-medium",
  /** 表格行 */
  tableRow: "border-emerald-100 transition-colors duration-200 hover:bg-emerald-50/90",
  tableMuted: "text-teal-700/75",
  /** 主按钮 */
  btnPrimary:
    "bg-emerald-600 text-white shadow-sm shadow-emerald-400/30 transition-all duration-200 hover:-translate-y-0.5 hover:bg-emerald-700 hover:shadow-md",
  /** 次要（原 secondary） */
  btnSecondary:
    "border border-emerald-200 bg-emerald-50 text-emerald-700 transition-all duration-200 hover:-translate-y-0.5 hover:bg-emerald-100 hover:border-emerald-300",
  /** 线框 */
  btnOutline:
    "border border-emerald-200 bg-white/90 text-teal-800 transition-all duration-200 hover:-translate-y-0.5 hover:border-emerald-400 hover:bg-emerald-50",
  /** 幽灵按钮（表格内图标） */
  btnGhost: "text-teal-700 transition-all duration-200 hover:bg-emerald-400/20 hover:text-emerald-800",
  btnGhostDanger: "text-red-600 hover:bg-red-50 hover:text-red-700",
  /** 分页区文字 */
  pagination: "text-sm text-teal-700/80",
  /** 弹窗内容区 */
  dialog:
    "border border-emerald-100 bg-gradient-to-b from-white to-emerald-50/40 shadow-xl shadow-emerald-400/15 sm:rounded-xl",
  alertDialog: "border border-emerald-100 bg-white shadow-xl shadow-emerald-400/15 sm:rounded-xl",
  /** Label */
  label: "text-teal-800",
  /** 统计卡片（浏览量页） */
  statCard:
    "rounded-xl border border-emerald-100 bg-gradient-to-br from-white to-teal-100/40 shadow-md shadow-emerald-400/20 transition-all duration-300 hover:-translate-y-0.5 hover:shadow-lg hover:shadow-teal-300/25",
  statLabel: "text-sm font-medium text-teal-700",
  statValue: "text-3xl font-semibold bg-gradient-to-r from-emerald-600 to-teal-700 bg-clip-text text-transparent"
} as const;
