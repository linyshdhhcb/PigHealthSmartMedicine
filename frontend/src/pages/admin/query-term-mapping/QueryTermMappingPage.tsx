import { useEffect, useState } from "react";
import { Pencil, Plus, RefreshCw, Trash2 } from "lucide-react";
import { toast } from "sonner";

import { Button } from "@/components/ui/button";
import {
  AlertDialog,
  AlertDialogAction,
  AlertDialogCancel,
  AlertDialogContent,
  AlertDialogDescription,
  AlertDialogFooter,
  AlertDialogHeader,
  AlertDialogTitle
} from "@/components/ui/alert-dialog";
import { Card, CardContent } from "@/components/ui/card";
import { Dialog, DialogContent, DialogDescription, DialogFooter, DialogHeader, DialogTitle } from "@/components/ui/dialog";
import { Input } from "@/components/ui/input";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow
} from "@/components/ui/table";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue
} from "@/components/ui/select";
import { Badge } from "@/components/ui/badge";
import type { PageResult, QueryTermMapping } from "@/services/queryTermMappingService";
import {
  createQueryTermMapping,
  deleteQueryTermMapping,
  getQueryTermMappingsPage,
  updateQueryTermMapping
} from "@/services/queryTermMappingService";
import { getErrorMessage } from "@/utils/error";

const PAGE_SIZE = 10;

const MATCH_TYPE_OPTIONS = [
  { value: 1, label: "精确匹配" },
  { value: 2, label: "前缀匹配" },
  { value: 3, label: "正则匹配" },
  { value: 4, label: "整词匹配" }
];

const matchTypeLabel = (type: number) => {
  return MATCH_TYPE_OPTIONS.find((o) => o.value === type)?.label || `类型${type}`;
};

const emptyForm = {
  sourceTerm: "",
  targetTerm: "",
  matchType: 1,
  priority: 0,
  enabled: true,
  remark: ""
};

export function QueryTermMappingPage() {
  const [pageData, setPageData] = useState<PageResult<QueryTermMapping> | null>(null);
  const [loading, setLoading] = useState(true);
  const [deleteTarget, setDeleteTarget] = useState<QueryTermMapping | null>(null);
  const [pageNo, setPageNo] = useState(1);
  const [searchKeyword, setSearchKeyword] = useState("");
  const [keyword, setKeyword] = useState("");
  const [dialogState, setDialogState] = useState<{
    open: boolean;
    mode: "create" | "edit";
    item: QueryTermMapping | null;
  }>({ open: false, mode: "create", item: null });
  const [form, setForm] = useState(emptyForm);

  const loadData = async (current = pageNo, keywordValue = keyword) => {
    try {
      setLoading(true);
      const data = await getQueryTermMappingsPage(current, PAGE_SIZE, keywordValue || undefined);
      setPageData(data);
    } catch (error) {
      toast.error(getErrorMessage(error, "加载映射规则失败"));
      console.error(error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadData();
  }, [pageNo, keyword]);

  useEffect(() => {
    if (!dialogState.open) {
      setForm(emptyForm);
      return;
    }
    if (dialogState.mode === "edit" && dialogState.item) {
      setForm({
        sourceTerm: dialogState.item.sourceTerm || "",
        targetTerm: dialogState.item.targetTerm || "",
        matchType: dialogState.item.matchType ?? 1,
        priority: dialogState.item.priority ?? 0,
        enabled: dialogState.item.enabled ?? true,
        remark: dialogState.item.remark || ""
      });
      return;
    }
    setForm(emptyForm);
  }, [dialogState]);

  const formatDate = (dateStr?: string | null) => {
    if (!dateStr) return "-";
    return new Date(dateStr).toLocaleString("zh-CN");
  };

  const handleSearch = () => {
    setPageNo(1);
    setKeyword(searchKeyword.trim());
  };

  const handleRefresh = () => {
    setPageNo(1);
    loadData(1, keyword);
  };

  const openCreateDialog = () => {
    setDialogState({ open: true, mode: "create", item: null });
  };

  const openEditDialog = (item: QueryTermMapping) => {
    setDialogState({ open: true, mode: "edit", item });
  };

  const handleSubmit = async () => {
    const payload = {
      sourceTerm: form.sourceTerm.trim(),
      targetTerm: form.targetTerm.trim(),
      matchType: form.matchType,
      priority: form.priority,
      enabled: form.enabled,
      remark: form.remark.trim() || null
    };

    if (!payload.sourceTerm) {
      toast.error("请输入原始词");
      return;
    }
    if (!payload.targetTerm) {
      toast.error("请输入目标词");
      return;
    }

    try {
      if (dialogState.mode === "create") {
        await createQueryTermMapping(payload);
        toast.success("创建成功");
        setPageNo(1);
        await loadData(1, keyword);
      } else if (dialogState.item) {
        await updateQueryTermMapping(dialogState.item.id, payload);
        toast.success("更新成功");
        await loadData(pageNo, keyword);
      }
      setDialogState({ open: false, mode: "create", item: null });
    } catch (error) {
      toast.error(getErrorMessage(error, "保存失败"));
      console.error(error);
    }
  };

  const handleDelete = async () => {
    if (!deleteTarget) return;

    try {
      await deleteQueryTermMapping(deleteTarget.id);
      toast.success("删除成功");
      setDeleteTarget(null);
      setPageNo(1);
      await loadData(1, keyword);
    } catch (error) {
      toast.error(getErrorMessage(error, "删除失败"));
      console.error(error);
    } finally {
      setDeleteTarget(null);
    }
  };

  const records = pageData?.records || [];

  return (
    <div className="admin-page">
      <div className="admin-page-header">
        <div>
          <h1 className="admin-page-title">关键词映射管理</h1>
          <p className="admin-page-subtitle">配置查询归一化的关键词映射规则</p>
        </div>
        <div className="admin-page-actions flex flex-wrap items-center gap-3">
          <div className="relative">
            <Input
              value={searchKeyword}
              onChange={(event) => setSearchKeyword(event.target.value)}
              onKeyDown={(event) => event.key === "Enter" && handleSearch()}
              placeholder="搜索原始词/目标词"
              className="w-[240px] pl-10 h-10 bg-white border-emerald-200 focus:border-emerald-400 focus:ring-emerald-400/20 rounded-xl transition-all duration-200"
            />
            <svg className="absolute left-3 top-1/2 -translate-y-1/2 h-4 w-4 text-emerald-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
            </svg>
          </div>
          <Button variant="outline" onClick={handleSearch} className="h-10 px-4 border-emerald-200 text-emerald-700 hover:bg-emerald-50 hover:border-emerald-300 rounded-xl transition-all duration-200">
            搜索
          </Button>
          <Button variant="outline" onClick={handleRefresh} className="h-10 px-4 border-emerald-200 text-emerald-700 hover:bg-emerald-50 hover:border-emerald-300 rounded-xl transition-all duration-200">
            <RefreshCw className="w-4 h-4 mr-2" />
            刷新
          </Button>
          <Button className="h-10 px-5 bg-gradient-to-r from-emerald-500 to-emerald-600 hover:from-emerald-600 hover:to-emerald-700 text-white shadow-lg shadow-emerald-500/25 hover:shadow-emerald-600/30 rounded-xl transition-all duration-200 font-medium" onClick={openCreateDialog}>
            <Plus className="w-4 h-4 mr-2" />
            新增映射
          </Button>
        </div>
      </div>

      <Card className="border-emerald-100 shadow-sm overflow-hidden rounded-2xl">
        <CardContent className="p-0 pt-6">
          {loading ? (
            <div className="text-center py-8 text-muted-foreground">加载中...</div>
          ) : records.length === 0 ? (
            <div className="text-center py-8 text-muted-foreground">
              暂无映射规则，点击上方按钮新增
            </div>
          ) : (
            <Table className="min-w-[980px]">
              <TableHeader>
                <TableRow className="bg-gradient-to-r from-emerald-50 to-teal-50 border-b border-emerald-200">
                  <TableHead className="w-[170px] font-semibold text-emerald-700 py-4">原始词</TableHead>
                  <TableHead className="w-[170px] font-semibold text-emerald-700">目标词</TableHead>
                  <TableHead className="w-[100px] font-semibold text-emerald-700">匹配类型</TableHead>
                  <TableHead className="w-[80px] font-semibold text-emerald-700">优先级</TableHead>
                  <TableHead className="w-[80px] font-semibold text-emerald-700">状态</TableHead>
                  <TableHead className="w-[180px] font-semibold text-emerald-700">备注</TableHead>
                  <TableHead className="w-[160px] font-semibold text-emerald-700">创建时间</TableHead>
                  <TableHead className="w-[160px] font-semibold text-emerald-700">更新时间</TableHead>
                  <TableHead className="w-[150px] text-left font-semibold text-emerald-700">操作</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {records.map((item, index) => (
                  <TableRow key={item.id} className={`${index % 2 === 0 ? 'bg-white' : 'bg-gradient-to-r from-emerald-50/20 to-teal-50/20'} hover:bg-emerald-50/60 transition-colors duration-200 border-b border-emerald-50/50`}>
                    <TableCell className="font-medium max-w-[160px] truncate text-slate-800" title={item.sourceTerm}>
                      {item.sourceTerm}
                    </TableCell>
                    <TableCell className="max-w-[160px] truncate text-slate-600" title={item.targetTerm}>
                      {item.targetTerm}
                    </TableCell>
                    <TableCell>
                      <Badge variant="secondary" className="bg-gradient-to-r from-teal-50 to-emerald-50 text-teal-700 border-teal-200/50">{matchTypeLabel(item.matchType)}</Badge>
                    </TableCell>
                    <TableCell className="text-slate-600">{item.priority}</TableCell>
                    <TableCell>
                      <Badge variant={item.enabled ? "default" : "outline"} className={item.enabled ? "bg-gradient-to-r from-emerald-500 to-teal-500 text-white border-0 shadow-sm" : "bg-slate-100 text-slate-600 border-slate-200"}>
                        {item.enabled ? "启用" : "禁用"}
                      </Badge>
                    </TableCell>
                    <TableCell className="max-w-[160px] truncate text-emerald-600/70" title={item.remark || ""}>
                      {item.remark || "-"}
                    </TableCell>
                    <TableCell className="text-emerald-600/70">
                      {formatDate(item.createTime)}
                    </TableCell>
                    <TableCell className="text-emerald-600/70">
                      {formatDate(item.updateTime)}
                    </TableCell>
                    <TableCell className="text-center">
                      <div className="flex justify-center gap-2">
                        <Button variant="outline" size="sm" onClick={() => openEditDialog(item)} className="h-8 px-3 border-emerald-200 text-emerald-700 hover:bg-emerald-50 hover:border-emerald-300 rounded-lg transition-all duration-200">
                          <Pencil className="w-4 h-4 mr-0.5" />
                          编辑
                        </Button>
                        <Button
                          variant="ghost"
                          size="sm"
                          className="h-8 px-3 text-red-500 hover:text-red-600 hover:bg-red-50 transition-colors"
                          onClick={() => setDeleteTarget(item)}
                        >
                          <Trash2 className="w-4 h-4 mr-0.5" />
                          删除
                        </Button>
                      </div>
                    </TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          )}
        </CardContent>
      </Card>

      {pageData ? (
        <div className="mt-4 flex flex-wrap items-center justify-between gap-3 text-sm bg-white/60 backdrop-blur-sm rounded-xl border border-emerald-100 px-5 py-4">
          <span className="text-slate-600">共 <span className="font-semibold text-emerald-700">{pageData.total}</span> 条</span>
          <div className="flex items-center gap-2">
            <Button
              variant="outline"
              size="sm"
              onClick={() => setPageNo((prev) => Math.max(1, prev - 1))}
              disabled={pageData.current <= 1}
              className="h-8 px-4 border-emerald-200 text-emerald-700 hover:bg-emerald-50 rounded-lg transition-all duration-200"
            >
              上一页
            </Button>
            <span className="px-3 text-slate-600">
              <span className="font-semibold text-emerald-700">{pageData.current}</span> / {pageData.pages}
            </span>
            <Button
              variant="outline"
              size="sm"
              onClick={() => setPageNo((prev) => Math.min(pageData.pages || 1, prev + 1))}
              disabled={pageData.current >= pageData.pages}
              className="h-8 px-4 border-emerald-200 text-emerald-700 hover:bg-emerald-50 rounded-lg transition-all duration-200"
            >
              下一页
            </Button>
          </div>
        </div>
      ) : null}

      <AlertDialog open={!!deleteTarget} onOpenChange={() => setDeleteTarget(null)}>
        <AlertDialogContent className="rounded-2xl border-emerald-100">
          <AlertDialogHeader>
            <AlertDialogTitle className="text-emerald-800">确认删除</AlertDialogTitle>
            <AlertDialogDescription>
              删除后该映射规则将不再生效，是否继续？
            </AlertDialogDescription>
          </AlertDialogHeader>
          <AlertDialogFooter>
            <AlertDialogCancel className="rounded-xl border-emerald-200 text-emerald-700 hover:bg-emerald-50">取消</AlertDialogCancel>
            <AlertDialogAction onClick={handleDelete} className="bg-gradient-to-r from-red-500 to-rose-500 hover:from-red-600 hover:to-rose-600 text-white rounded-xl border-0 shadow-md">
              删除
            </AlertDialogAction>
          </AlertDialogFooter>
        </AlertDialogContent>
      </AlertDialog>

      <Dialog
        open={dialogState.open}
        onOpenChange={(open) =>
          setDialogState((prev) => ({ ...prev, open, item: open ? prev.item : null }))
        }
      >
        <DialogContent className="sm:max-w-[520px]">
          <DialogHeader>
            <DialogTitle>{dialogState.mode === "create" ? "新增映射规则" : "编辑映射规则"}</DialogTitle>
            <DialogDescription>配置查询归一化的关键词映射</DialogDescription>
          </DialogHeader>
          <div className="space-y-4">
            <div className="space-y-2">
              <label className="text-sm font-medium">原始词 *</label>
              <Input
                value={form.sourceTerm}
                onChange={(event) => setForm((prev) => ({ ...prev, sourceTerm: event.target.value }))}
                placeholder="用户输入的原始关键词"
              />
            </div>
            <div className="space-y-2">
              <label className="text-sm font-medium">目标词 *</label>
              <Input
                value={form.targetTerm}
                onChange={(event) => setForm((prev) => ({ ...prev, targetTerm: event.target.value }))}
                placeholder="归一化后的目标关键词"
              />
            </div>
            <div className="grid grid-cols-2 gap-4">
              <div className="space-y-2">
                <label className="text-sm font-medium">匹配类型</label>
                <Select
                  value={String(form.matchType)}
                  onValueChange={(value) => setForm((prev) => ({ ...prev, matchType: Number(value) }))}
                >
                  <SelectTrigger>
                    <SelectValue />
                  </SelectTrigger>
                  <SelectContent>
                    {MATCH_TYPE_OPTIONS.map((opt) => (
                      <SelectItem key={opt.value} value={String(opt.value)}>
                        {opt.label}
                      </SelectItem>
                    ))}
                  </SelectContent>
                </Select>
              </div>
              <div className="space-y-2">
                <label className="text-sm font-medium">优先级</label>
                <Input
                  type="number"
                  value={form.priority}
                  onChange={(event) => setForm((prev) => ({ ...prev, priority: Number(event.target.value) }))}
                  placeholder="数值越小优先级越高"
                />
              </div>
            </div>
            <div className="grid grid-cols-2 gap-4">
              <div className="space-y-2">
                <label className="text-sm font-medium">启用状态</label>
                <Select
                  value={form.enabled ? "true" : "false"}
                  onValueChange={(value) => setForm((prev) => ({ ...prev, enabled: value === "true" }))}
                >
                  <SelectTrigger>
                    <SelectValue />
                  </SelectTrigger>
                  <SelectContent>
                    <SelectItem value="true">启用</SelectItem>
                    <SelectItem value="false">禁用</SelectItem>
                  </SelectContent>
                </Select>
              </div>
            </div>
            <div className="space-y-2">
              <label className="text-sm font-medium">备注</label>
              <Input
                value={form.remark}
                onChange={(event) => setForm((prev) => ({ ...prev, remark: event.target.value }))}
                placeholder="可选备注信息"
              />
            </div>
          </div>
          <DialogFooter>
            <Button
              variant="outline"
              onClick={() => setDialogState({ open: false, mode: "create", item: null })}
            >
              取消
            </Button>
            <Button onClick={handleSubmit}>保存</Button>
          </DialogFooter>
        </DialogContent>
      </Dialog>
    </div>
  );
}
