import { useEffect, useState } from "react";
import { Eye, Maximize2, Minimize2, Pencil, Plus, RefreshCw, Trash2 } from "lucide-react";
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
import { Dialog, DialogContent, DialogFooter, DialogHeader, DialogTitle } from "@/components/ui/dialog";
import { HtmlEditor } from "@/components/ui/html-editor";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue
} from "@/components/ui/select";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow
} from "@/components/ui/table";
import { cn } from "@/lib/utils";
import { phsm } from "@/pages/admin/phsm/phsmTheme";
import {
  createArticle,
  deleteArticle,
  getAllArticleTypes,
  getArticle,
  getArticles,
  updateArticle,
  type Article,
  type PageResult
} from "@/services/articleService";
import { getErrorMessage } from "@/utils/error";

const PAGE_SIZE = 10;

function summarizeHtml(s: string, n = 80) {
  const t = s.replace(/<[^>]+>/g, "").replace(/\s+/g, " ").trim();
  return t.length > n ? t.slice(0, n) + "…" : t;
}

export function ArticleListPage() {
  const [pageData, setPageData] = useState<PageResult<Article> | null>(null);
  const [loading, setLoading] = useState(true);
  const [types, setTypes] = useState<{ id: string; typeName: string }[]>([]);
  const [pageNo, setPageNo] = useState(1);
  const [kw, setKw] = useState("");
  const [searchKw, setSearchKw] = useState("");
  const [typeFilter, setTypeFilter] = useState<string | undefined>();
  const [deleteTarget, setDeleteTarget] = useState<Article | null>(null);
  const [dialogOpen, setDialogOpen] = useState(false);
  const [editId, setEditId] = useState<string | undefined>();
  const [form, setForm] = useState({
    title: "",
    content: "",
    author: "",
    typeId: ""
  });
  const [submitting, setSubmitting] = useState(false);
  const [dialogFullscreen, setDialogFullscreen] = useState(false);
  const [detail, setDetail] = useState<Article | null>(null);

  const loadTypes = async () => {
    try {
      const list = await getAllArticleTypes();
      setTypes(list.map((t) => ({ id: t.id, typeName: t.typeName })));
    } catch {
      setTypes([]);
    }
  };

  const load = async (current = pageNo) => {
    try {
      setLoading(true);
      const data = await getArticles(current, PAGE_SIZE, typeFilter, searchKw || undefined);
      setPageData(data);
    } catch (e) {
      toast.error(getErrorMessage(e, "加载失败"));
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    void loadTypes();
  }, []);

  useEffect(() => {
    void load();
  }, [pageNo, typeFilter, searchKw]);

  const openCreate = () => {
    setEditId(undefined);
    setForm({ title: "", content: "", author: "", typeId: types[0]?.id || "" });
    setDialogOpen(true);
  };

  const openEdit = async (row: Article) => {
    try {
      const d = await getArticle(row.id);
      setEditId(d.id);
      setForm({
        title: d.title || "",
        content: d.content || "",
        author: d.author || "",
        typeId: d.typeId || ""
      });
      setDialogOpen(true);
    } catch (e) {
      toast.error(getErrorMessage(e, "加载详情失败"));
    }
  };

  const submit = async () => {
    if (!form.title.trim()) {
      toast.error("请输入标题");
      return;
    }
    if (!form.content.trim()) {
      toast.error("请输入内容");
      return;
    }
    if (!form.typeId) {
      toast.error("请选择文章类型");
      return;
    }
    try {
      setSubmitting(true);
      const payload = {
        title: form.title.trim(),
        content: form.content,
        author: form.author.trim() || undefined,
        typeId: form.typeId
      };
      if (editId) await updateArticle(editId, payload);
      else await createArticle(payload);
      toast.success(editId ? "已更新" : "已创建");
      setDialogOpen(false);
      setPageNo(1);
      await load(1);
    } catch (e) {
      toast.error(getErrorMessage(e, "保存失败"));
    } finally {
      setSubmitting(false);
    }
  };

  const handleDelete = async () => {
    if (!deleteTarget) return;
    try {
      await deleteArticle(deleteTarget.id);
      toast.success("已删除");
      setDeleteTarget(null);
      await load();
    } catch (e) {
      toast.error(getErrorMessage(e, "删除失败"));
    }
  };

  return (
    <div className={phsm.page}>
      <div className={phsm.pageInner}>
        <div className={phsm.toolbar}>
          <div className={phsm.titleBlock}>
            <h1 className={phsm.title}>文章管理</h1>
            <div className={phsm.titleAccent} aria-hidden />
          </div>
          <div className={cn(phsm.actions, "w-full sm:w-auto")}>
            <Select
              value={typeFilter || "__all__"}
              onValueChange={(v) => (setPageNo(1), setTypeFilter(v === "__all__" ? undefined : v))}
            >
              <SelectTrigger className={cn("w-full sm:w-[160px]", phsm.input)}>
                <SelectValue placeholder="类型筛选" />
              </SelectTrigger>
              <SelectContent className="border-emerald-100 bg-white">
                <SelectItem value="__all__">全部类型</SelectItem>
                {types.map((t) => (
                  <SelectItem key={t.id} value={t.id}>
                    {t.typeName}
                  </SelectItem>
                ))}
              </SelectContent>
            </Select>
            <Input
              placeholder="标题/内容/作者"
              className={cn("w-full sm:w-48", phsm.input)}
              value={kw}
              onChange={(e) => setKw(e.target.value)}
              onKeyDown={(e) => e.key === "Enter" && (setPageNo(1), setSearchKw(kw.trim()))}
            />
            <Button
              variant="secondary"
              size="sm"
              className={phsm.btnSecondary}
              onClick={() => (setPageNo(1), setSearchKw(kw.trim()))}
            >
              搜索
            </Button>
            <Button variant="outline" size="sm" className={phsm.btnOutline} onClick={() => load()}>
              <RefreshCw className="mr-1 h-4 w-4" />
              刷新
            </Button>
            <Button size="sm" className={phsm.btnPrimary} onClick={openCreate}>
              <Plus className="mr-1 h-4 w-4" />
              新增
            </Button>
          </div>
        </div>

        <Card className={phsm.card}>
          <CardContent className={cn(phsm.cardPt, "overflow-x-auto")}>
            <Table>
              <TableHeader>
                <TableRow className={phsm.tableHeadRow}>
                  <TableHead className={phsm.tableHeadCell}>标题</TableHead>
                  <TableHead className={phsm.tableHeadCell}>类型</TableHead>
                  <TableHead className={phsm.tableHeadCell}>摘要</TableHead>
                  <TableHead className={cn("w-[100px]", phsm.tableHeadCell)}>操作</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {loading ? (
                  <TableRow>
                    <TableCell colSpan={4} className={phsm.tableMuted}>
                      加载中…
                    </TableCell>
                  </TableRow>
                ) : pageData?.records?.length ? (
                  pageData.records.map((row) => (
                    <TableRow key={row.id} className={phsm.tableRow}>
                      <TableCell className="max-w-[200px] font-medium text-teal-900">{row.title}</TableCell>
                      <TableCell className="text-teal-800">{row.typeName || "-"}</TableCell>
                      <TableCell className={cn("max-w-[320px]", phsm.tableMuted)}>{summarizeHtml(row.content)}</TableCell>
                      <TableCell>
                        <div className="flex gap-1">
                          <Button
                            variant="ghost"
                            size="icon"
                            className={phsm.btnGhost}
                            onClick={async () => setDetail(await getArticle(row.id))}
                          >
                            <Eye className="h-4 w-4" />
                          </Button>
                          <Button
                            variant="ghost"
                            size="icon"
                            className={phsm.btnGhost}
                            onClick={() => openEdit(row)}
                          >
                            <Pencil className="h-4 w-4" />
                          </Button>
                          <Button
                            variant="ghost"
                            size="icon"
                            className={cn(phsm.btnGhost, phsm.btnGhostDanger)}
                            onClick={() => setDeleteTarget(row)}
                          >
                            <Trash2 className="h-4 w-4" />
                          </Button>
                        </div>
                      </TableCell>
                    </TableRow>
                  ))
                ) : (
                  <TableRow>
                    <TableCell colSpan={4} className={phsm.tableMuted}>
                      暂无数据
                    </TableCell>
                  </TableRow>
                )}
              </TableBody>
            </Table>
            {pageData && pageData.pages > 1 ? (
              <div className={cn("mt-4 flex flex-col gap-3 sm:flex-row sm:justify-between", phsm.pagination)}>
                <span>
                  共 {pageData.total} 条 · 第 {pageData.current}/{pageData.pages} 页
                </span>
                <div className="flex flex-wrap gap-2">
                  <Button
                    size="sm"
                    variant="outline"
                    className={phsm.btnOutline}
                    disabled={pageNo <= 1}
                    onClick={() => setPageNo((p) => p - 1)}
                  >
                    上一页
                  </Button>
                  <Button
                    size="sm"
                    variant="outline"
                    className={phsm.btnOutline}
                    disabled={pageNo >= (pageData.pages || 1)}
                    onClick={() => setPageNo((p) => p + 1)}
                  >
                    下一页
                  </Button>
                </div>
              </div>
            ) : null}
          </CardContent>
        </Card>

        <Dialog open={dialogOpen} onOpenChange={setDialogOpen}>
          <DialogContent
            className={cn(
              phsm.dialog,
              "overflow-y-auto",
              dialogFullscreen ? "h-[95vh] w-[96vw] max-w-[96vw]" : "max-h-[90vh] sm:max-w-3xl"
            )}
          >
            <DialogHeader>
              <div className="flex items-center justify-between gap-2">
                <DialogTitle className="text-teal-800">{editId ? "编辑文章" : "新增文章"}</DialogTitle>
                <Button
                  type="button"
                  variant="ghost"
                  size="icon"
                  className={phsm.btnGhost}
                  onClick={() => setDialogFullscreen((v) => !v)}
                >
                  {dialogFullscreen ? <Minimize2 className="h-4 w-4" /> : <Maximize2 className="h-4 w-4" />}
                </Button>
              </div>
            </DialogHeader>
            <div className="space-y-3 py-2">
              <div>
                <Label className={phsm.label}>标题</Label>
                <Input
                  value={form.title}
                  onChange={(e) => setForm((f) => ({ ...f, title: e.target.value }))}
                  className={phsm.input}
                />
              </div>
              <div>
                <Label className={phsm.label}>类型</Label>
                <Select value={form.typeId} onValueChange={(v) => setForm((f) => ({ ...f, typeId: v }))}>
                  <SelectTrigger className={phsm.input}>
                    <SelectValue placeholder="选择类型" />
                  </SelectTrigger>
                  <SelectContent className="border-emerald-100 bg-white">
                    {types.map((t) => (
                      <SelectItem key={t.id} value={t.id}>
                        {t.typeName}
                      </SelectItem>
                    ))}
                  </SelectContent>
                </Select>
              </div>
              <div>
                <Label className={phsm.label}>作者（可选）</Label>
                <Input
                  value={form.author}
                  onChange={(e) => setForm((f) => ({ ...f, author: e.target.value }))}
                  className={phsm.input}
                />
              </div>
              <div>
                <Label className={phsm.label}>内容（支持 HTML）</Label>
                <HtmlEditor
                  value={form.content}
                  onChange={(value) => setForm((f) => ({ ...f, content: value }))}
                  className="border-emerald-100 bg-white"
                  minHeightClassName={dialogFullscreen ? "min-h-[52vh]" : "min-h-[260px]"}
                  placeholder="请输入文章内容（支持富文本）"
                />
              </div>
            </div>
            <DialogFooter className="gap-2 sm:gap-0">
              <Button variant="outline" className={phsm.btnOutline} onClick={() => setDialogOpen(false)}>
                取消
              </Button>
              <Button className={phsm.btnPrimary} onClick={submit} disabled={submitting}>
                保存
              </Button>
            </DialogFooter>
          </DialogContent>
        </Dialog>

        <Dialog open={!!detail} onOpenChange={() => setDetail(null)}>
          <DialogContent className={cn(phsm.dialog, "max-h-[90vh] overflow-y-auto sm:max-w-3xl")}>
            <DialogHeader>
              <DialogTitle className="text-teal-800">文章详情</DialogTitle>
            </DialogHeader>
            {detail ? (
              <div className="space-y-3 text-sm text-teal-900">
                <p><span className="text-teal-700/75">标题：</span>{detail.title}</p>
                <p><span className="text-teal-700/75">类型：</span>{detail.typeName || detail.typeId || "-"}</p>
                <p><span className="text-teal-700/75">作者：</span>{detail.author || "-"}</p>
                <div className="rounded-lg border border-emerald-100 bg-white p-3">
                  <div className="mb-2 text-teal-700/75">内容：</div>
                  <div className="prose prose-sm max-w-none break-words text-teal-900" dangerouslySetInnerHTML={{ __html: detail.content || "-" }} />
                </div>
              </div>
            ) : null}
          </DialogContent>
        </Dialog>

        <AlertDialog open={!!deleteTarget} onOpenChange={() => setDeleteTarget(null)}>
          <AlertDialogContent className={phsm.alertDialog}>
            <AlertDialogHeader>
              <AlertDialogTitle className="text-teal-800">删除文章？</AlertDialogTitle>
              <AlertDialogDescription className="text-teal-700/80">不可恢复。</AlertDialogDescription>
            </AlertDialogHeader>
            <AlertDialogFooter>
              <AlertDialogCancel className={phsm.btnOutline}>取消</AlertDialogCancel>
              <AlertDialogAction
                className={cn(phsm.btnPrimary, "bg-red-600 hover:bg-red-700")}
                onClick={handleDelete}
              >
                删除
              </AlertDialogAction>
            </AlertDialogFooter>
          </AlertDialogContent>
        </AlertDialog>
      </div>
    </div>
  );
}
