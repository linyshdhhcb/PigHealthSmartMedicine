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
import { Textarea } from "@/components/ui/textarea";
import type { PageResult, SampleQuestion } from "@/services/sampleQuestionService";
import {
  createSampleQuestion,
  deleteSampleQuestion,
  getSampleQuestionsPage,
  updateSampleQuestion
} from "@/services/sampleQuestionService";
import { getErrorMessage } from "@/utils/error";

const PAGE_SIZE = 10;

const emptyForm = {
  title: "",
  description: "",
  question: ""
};

export function SampleQuestionPage() {
  const [pageData, setPageData] = useState<PageResult<SampleQuestion> | null>(null);
  const [loading, setLoading] = useState(true);
  const [deleteTarget, setDeleteTarget] = useState<SampleQuestion | null>(null);
  const [pageNo, setPageNo] = useState(1);
  const [searchKeyword, setSearchKeyword] = useState("");
  const [keyword, setKeyword] = useState("");
  const [dialogState, setDialogState] = useState<{
    open: boolean;
    mode: "create" | "edit";
    item: SampleQuestion | null;
  }>({ open: false, mode: "create", item: null });
  const [form, setForm] = useState(emptyForm);

  const loadQuestions = async (current = pageNo, keywordValue = keyword) => {
    try {
      setLoading(true);
      const data = await getSampleQuestionsPage(current, PAGE_SIZE, keywordValue || undefined);
      setPageData(data);
    } catch (error) {
      toast.error(getErrorMessage(error, "加载示例问题失败"));
      console.error(error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadQuestions();
  }, [pageNo, keyword]);

  useEffect(() => {
    if (!dialogState.open) {
      setForm(emptyForm);
      return;
    }
    if (dialogState.mode === "edit" && dialogState.item) {
      setForm({
        title: dialogState.item.title || "",
        description: dialogState.item.description || "",
        question: dialogState.item.question || ""
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
    loadQuestions(1, keyword);
  };

  const openCreateDialog = () => {
    setDialogState({ open: true, mode: "create", item: null });
  };

  const openEditDialog = (item: SampleQuestion) => {
    setDialogState({ open: true, mode: "edit", item });
  };

  const handleSubmit = async () => {
    const payload = {
      title: form.title.trim() || null,
      description: form.description.trim() || null,
      question: form.question.trim()
    };

    if (!payload.question) {
      toast.error("请输入示例问题内容");
      return;
    }

    try {
      if (dialogState.mode === "create") {
        await createSampleQuestion(payload);
        toast.success("创建成功");
        setPageNo(1);
        await loadQuestions(1, keyword);
      } else if (dialogState.item) {
        await updateSampleQuestion(dialogState.item.id, payload);
        toast.success("更新成功");
        await loadQuestions(pageNo, keyword);
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
      await deleteSampleQuestion(deleteTarget.id);
      toast.success("删除成功");
      setDeleteTarget(null);
      setPageNo(1);
      await loadQuestions(1, keyword);
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
          <h1 className="admin-page-title">示例问题管理</h1>
          <p className="admin-page-subtitle">配置欢迎页的示例问题与推荐问法</p>
        </div>
        <div className="admin-page-actions flex flex-wrap items-center gap-3">
          <div className="relative">
            <Input
              value={searchKeyword}
              onChange={(event) => setSearchKeyword(event.target.value)}
              placeholder="搜索标题/描述/问题"
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
            新增示例
          </Button>
        </div>
      </div>

      <Card className="border-emerald-100 shadow-sm overflow-hidden rounded-2xl">
        <CardContent className="p-0 pt-6">
          {loading ? (
            <div className="text-center py-8 text-muted-foreground">加载中...</div>
          ) : records.length === 0 ? (
            <div className="text-center py-8 text-muted-foreground">
              暂无示例问题，点击上方按钮新增
            </div>
          ) : (
            <Table className="min-w-[860px]">
              <TableHeader>
                <TableRow className="bg-gradient-to-r from-emerald-50 to-teal-50 border-b border-emerald-200">
                  <TableHead className="w-[180px] font-semibold text-emerald-700 py-4">标题</TableHead>
                  <TableHead className="w-[220px] font-semibold text-emerald-700">描述</TableHead>
                  <TableHead className="font-semibold text-emerald-700">示例问题</TableHead>
                  <TableHead className="w-[170px] font-semibold text-emerald-700">更新时间</TableHead>
                  <TableHead className="w-[140px] text-left font-semibold text-emerald-700">操作</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {records.map((item, index) => (
                  <TableRow key={item.id} className={`${index % 2 === 0 ? 'bg-white' : 'bg-gradient-to-r from-emerald-50/20 to-teal-50/20'} hover:bg-emerald-50/60 transition-colors duration-200 border-b border-emerald-50/50`}>
                    <TableCell className="font-medium max-w-[160px] truncate text-slate-800" title={item.title || ""}>
                      {item.title || "-"}
                    </TableCell>
                    <TableCell className="max-w-[200px] truncate text-slate-600" title={item.description || ""}>
                      {item.description || "-"}
                    </TableCell>
                    <TableCell className="max-w-[360px] truncate text-slate-600" title={item.question}>
                      {item.question}
                    </TableCell>
                    <TableCell className="text-emerald-600/70">
                      {formatDate(item.updateTime || item.createTime)}
                    </TableCell>
                    <TableCell className="text-center">
                      <div className="flex justify-end gap-2">
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
              删除后该示例问题将不会出现在欢迎页，是否继续？
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
            <DialogTitle>{dialogState.mode === "create" ? "新增示例问题" : "编辑示例问题"}</DialogTitle>
            <DialogDescription>配置欢迎页的示例问题和推荐问法</DialogDescription>
          </DialogHeader>
          <div className="space-y-4">
            <div className="space-y-2">
              <label className="text-sm font-medium">标题</label>
              <Input
                value={form.title}
                onChange={(event) => setForm((prev) => ({ ...prev, title: event.target.value }))}
                placeholder="例如：任务拆解"
              />
            </div>
            <div className="space-y-2">
              <label className="text-sm font-medium">描述</label>
              <Input
                value={form.description}
                onChange={(event) => setForm((prev) => ({ ...prev, description: event.target.value }))}
                placeholder="例如：把目标拆成可执行步骤与优先级"
              />
            </div>
            <div className="space-y-2">
              <label className="text-sm font-medium">示例问题</label>
              <Textarea
                value={form.question}
                onChange={(event) => setForm((prev) => ({ ...prev, question: event.target.value }))}
                placeholder="请输入示例问题内容"
                className="min-h-[120px]"
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
