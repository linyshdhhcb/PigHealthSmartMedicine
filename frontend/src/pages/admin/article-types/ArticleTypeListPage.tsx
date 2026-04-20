import { useEffect, useState } from "react";
import { Eye, Pencil, Plus, RefreshCw, Trash2 } from "lucide-react";
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
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow
} from "@/components/ui/table";
import { phsm } from "@/pages/admin/phsm/phsmTheme";
import type { ArticleType, PageResult } from "@/services/articleTypeService";
import {
  createArticleType,
  deleteArticleType,
  getArticleType,
  getArticleTypes,
  updateArticleType
} from "@/services/articleTypeService";
import { cn } from "@/lib/utils";
import { getErrorMessage } from "@/utils/error";

const PAGE_SIZE = 10;

export function ArticleTypeListPage() {
  const [pageData, setPageData] = useState<PageResult<ArticleType> | null>(null);
  const [loading, setLoading] = useState(true);
  const [deleteTarget, setDeleteTarget] = useState<ArticleType | null>(null);
  const [pageNo, setPageNo] = useState(1);
  const [searchName, setSearchName] = useState("");
  const [filterName, setFilterName] = useState("");
  const [dialogOpen, setDialogOpen] = useState(false);
  const [editId, setEditId] = useState<string | undefined>();
  const [typeName, setTypeName] = useState("");
  const [submitting, setSubmitting] = useState(false);
  const [detail, setDetail] = useState<ArticleType | null>(null);

  const load = async (current = pageNo, name = filterName) => {
    try {
      setLoading(true);
      const data = await getArticleTypes(current, PAGE_SIZE, name || undefined);
      setPageData(data);
    } catch (e) {
      toast.error(getErrorMessage(e, "加载失败"));
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    load();
  }, [pageNo, filterName]);

  const openCreate = () => {
    setEditId(undefined);
    setTypeName("");
    setDialogOpen(true);
  };

  const openEdit = async (row: ArticleType) => {
    try {
      const detail = await getArticleType(row.id);
      setEditId(detail.id);
      setTypeName(detail.typeName || "");
      setDialogOpen(true);
    } catch (e) {
      toast.error(getErrorMessage(e, "加载详情失败"));
    }
  };

  const handleSubmit = async () => {
    const name = typeName.trim();
    if (!name) {
      toast.error("请输入类型名称");
      return;
    }
    if (name.length > 20) {
      toast.error("最多20字符");
      return;
    }
    try {
      setSubmitting(true);
      if (editId) {
        await updateArticleType(editId, { typeName: name });
        toast.success("已更新");
      } else {
        await createArticleType({ typeName: name });
        toast.success("已创建");
      }
      setDialogOpen(false);
      await load(1);
      setPageNo(1);
    } catch (e) {
      toast.error(getErrorMessage(e, "保存失败"));
    } finally {
      setSubmitting(false);
    }
  };

  const handleDelete = async () => {
    if (!deleteTarget) return;
    try {
      await deleteArticleType(deleteTarget.id);
      toast.success("已删除");
      setDeleteTarget(null);
      await load();
    } catch (e) {
      toast.error(getErrorMessage(e, "删除失败"));
    }
  };

  const formatDate = (s?: string | null) => (s ? new Date(s).toLocaleString("zh-CN") : "-");

  return (
    <div className={phsm.page}>
      <div className={phsm.pageInner}>
        <div className={phsm.toolbar}>
          <div className={phsm.titleBlock}>
            <h1 className={phsm.title}>文章类型</h1>
            <div className={phsm.titleAccent} aria-hidden />
          </div>
          <div className={cn(phsm.actions, "w-full sm:w-auto")}>
            <Input
              placeholder="按名称搜索"
              value={searchName}
              onChange={(e) => setSearchName(e.target.value)}
              className={cn("w-full sm:w-48", phsm.input)}
              onKeyDown={(e) => e.key === "Enter" && (setPageNo(1), setFilterName(searchName.trim()))}
            />
            <Button
              variant="secondary"
              size="sm"
              className={phsm.btnSecondary}
              onClick={() => (setPageNo(1), setFilterName(searchName.trim()))}
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
                  <TableHead className={phsm.tableHeadCell}>类型名称</TableHead>
                  <TableHead className={phsm.tableHeadCell}>创建人</TableHead>
                  <TableHead className={phsm.tableHeadCell}>更新时间</TableHead>
                  <TableHead className={cn("w-[120px]", phsm.tableHeadCell)}>操作</TableHead>
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
                      <TableCell className="font-medium text-teal-900">{row.typeName}</TableCell>
                      <TableCell className={phsm.tableMuted}>{row.createdBy || "-"}</TableCell>
                      <TableCell className={phsm.tableMuted}>{formatDate(row.updateTime)}</TableCell>
                      <TableCell>
                        <div className="flex gap-1">
                          <Button
                            variant="ghost"
                            size="icon"
                            className={phsm.btnGhost}
                            onClick={async () => setDetail(await getArticleType(row.id))}
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
              <div className={cn("mt-4 flex flex-col gap-3 sm:flex-row sm:items-center sm:justify-between", phsm.pagination)}>
                <span>
                  共 {pageData.total} 条 / 第 {pageData.current} / {pageData.pages} 页
                </span>
                <div className="flex flex-wrap gap-2">
                  <Button
                    size="sm"
                    variant="outline"
                    className={phsm.btnOutline}
                    disabled={pageNo <= 1}
                    onClick={() => setPageNo((p) => Math.max(1, p - 1))}
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
          <DialogContent className={phsm.dialog}>
            <DialogHeader>
              <DialogTitle className="text-teal-800">{editId ? "编辑文章类型" : "新增文章类型"}</DialogTitle>
            </DialogHeader>
            <div className="space-y-2 py-2">
              <Label htmlFor="typeName" className={phsm.label}>
                类型名称（必填，≤20字）
              </Label>
              <Input
                id="typeName"
                value={typeName}
                onChange={(e) => setTypeName(e.target.value)}
                maxLength={20}
                className={phsm.input}
              />
            </div>
            <DialogFooter className="gap-2 sm:gap-0">
              <Button variant="outline" className={phsm.btnOutline} onClick={() => setDialogOpen(false)}>
                取消
              </Button>
              <Button className={phsm.btnPrimary} onClick={handleSubmit} disabled={submitting}>
                保存
              </Button>
            </DialogFooter>
          </DialogContent>
        </Dialog>

        <Dialog open={!!detail} onOpenChange={() => setDetail(null)}>
          <DialogContent className={cn(phsm.dialog, "sm:max-w-lg")}>
            <DialogHeader>
              <DialogTitle className="text-teal-800">类型详情</DialogTitle>
            </DialogHeader>
            {detail ? (
              <div className="space-y-2 text-sm text-teal-900">
                <p><span className="text-teal-700/75">名称：</span>{detail.typeName || "-"}</p>
                <p><span className="text-teal-700/75">创建人：</span>{detail.createdBy || "-"}</p>
                <p><span className="text-teal-700/75">创建时间：</span>{formatDate(detail.createTime)}</p>
                <p><span className="text-teal-700/75">更新时间：</span>{formatDate(detail.updateTime)}</p>
              </div>
            ) : null}
          </DialogContent>
        </Dialog>

        <AlertDialog open={!!deleteTarget} onOpenChange={() => setDeleteTarget(null)}>
          <AlertDialogContent className={phsm.alertDialog}>
            <AlertDialogHeader>
              <AlertDialogTitle className="text-teal-800">确认删除？</AlertDialogTitle>
              <AlertDialogDescription className="text-teal-700/80">删除后不可恢复。</AlertDialogDescription>
            </AlertDialogHeader>
            <AlertDialogFooter>
              <AlertDialogCancel className={phsm.btnOutline}>取消</AlertDialogCancel>
              <AlertDialogAction className={cn(phsm.btnPrimary, "bg-red-600 hover:bg-red-700")} onClick={handleDelete}>
                删除
              </AlertDialogAction>
            </AlertDialogFooter>
          </AlertDialogContent>
        </AlertDialog>
      </div>
    </div>
  );
}
