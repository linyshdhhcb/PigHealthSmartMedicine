import { useCallback, useEffect, useRef, useState } from "react";
import { Database, FileBarChart, FolderOpen, Layers, Pencil, Plus, RefreshCw, Trash2 } from "lucide-react";
import { toast } from "sonner";
import { useNavigate, useSearchParams } from "react-router-dom";

import { Button } from "@/components/ui/button";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import {
  AlertDialog,
  AlertDialogAction,
  AlertDialogCancel,
  AlertDialogContent,
  AlertDialogDescription,
  AlertDialogFooter,
  AlertDialogHeader,
  AlertDialogTitle,
} from "@/components/ui/alert-dialog";
import { Badge } from "@/components/ui/badge";
import { Card, CardContent } from "@/components/ui/card";
import { Dialog, DialogContent, DialogDescription, DialogFooter, DialogHeader, DialogTitle } from "@/components/ui/dialog";
import { Input } from "@/components/ui/input";

import type { KnowledgeBase, PageResult } from "@/services/knowledgeService";
import { deleteKnowledgeBase, getKnowledgeBasesPage, renameKnowledgeBase } from "@/services/knowledgeService";
import { CreateKnowledgeBaseDialog } from "@/components/admin/CreateKnowledgeBaseDialog";
import { getErrorMessage } from "@/utils/error";
import { cn } from "@/lib/utils";

const PAGE_SIZE = 10;
const STATS_PAGE_SIZE = 200;

export function KnowledgeListPage() {
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();
  const nameFromQuery = searchParams.get("name") || "";
  const [pageData, setPageData] = useState<PageResult<KnowledgeBase> | null>(null);
  const [loading, setLoading] = useState(true);
  const [deleteTarget, setDeleteTarget] = useState<KnowledgeBase | null>(null);
  const [createDialogOpen, setCreateDialogOpen] = useState(false);
  const [searchName, setSearchName] = useState(nameFromQuery);
  const [keyword, setKeyword] = useState(nameFromQuery);
  const [pageNo, setPageNo] = useState(1);
  const [renameDialog, setRenameDialog] = useState<{ open: boolean; kb: KnowledgeBase | null }>({
    open: false,
    kb: null
  });
  const [renameValue, setRenameValue] = useState("");
  const [stats, setStats] = useState({
    totalCount: 0,
    documentCount: 0,
    activeCount: 0,
    creatorCount: 0
  });
  const [statsLoading, setStatsLoading] = useState(true);
  const statsRequestId = useRef(0);

  const knowledgeBases = pageData?.records || [];

  const loadKnowledgeBases = async (current = pageNo, name = keyword) => {
    try {
      setLoading(true);
      const data = await getKnowledgeBasesPage(current, PAGE_SIZE, name || undefined);
      setPageData(data);
    } catch (error) {
      toast.error(getErrorMessage(error, "加载知识库列表失败"));
      console.error(error);
    } finally {
      setLoading(false);
    }
  };

  const loadStats = useCallback(async (name = keyword) => {
    const requestId = ++statsRequestId.current;
    const normalized = name.trim();
    setStatsLoading(true);
    try {
      const firstPage = await getKnowledgeBasesPage(1, STATS_PAGE_SIZE, normalized || undefined);
      if (statsRequestId.current !== requestId) return;

      let documentTotal = 0;
      let activeTotal = 0;
      const creatorNames = new Set<string>();
      const addRecords = (records: KnowledgeBase[] = []) => {
        records.forEach((kb) => {
          const docCount = kb.documentCount ?? 0;
          documentTotal += docCount;
          if (docCount > 0) {
            activeTotal += 1;
          }
          if (kb.createdBy) {
            creatorNames.add(kb.createdBy);
          }
        });
      };

      addRecords(firstPage.records || []);

      const totalCount = firstPage.total ?? (firstPage.records?.length || 0);
      const totalPages =
        firstPage.pages || Math.max(1, Math.ceil(totalCount / STATS_PAGE_SIZE));

      for (let page = 2; page <= totalPages; page += 1) {
        const nextPage = await getKnowledgeBasesPage(page, STATS_PAGE_SIZE, normalized || undefined);
        if (statsRequestId.current !== requestId) return;
        addRecords(nextPage.records || []);
      }

      if (statsRequestId.current !== requestId) return;
      setStats({
        totalCount,
        documentCount: documentTotal,
        activeCount: activeTotal,
        creatorCount: creatorNames.size
      });
    } catch (error) {
      if (statsRequestId.current !== requestId) return;
      console.error(error);
      setStats({
        totalCount: 0,
        documentCount: 0,
        activeCount: 0,
        creatorCount: 0
      });
    } finally {
      if (statsRequestId.current === requestId) {
        setStatsLoading(false);
      }
    }
  }, [keyword]);

  useEffect(() => {
    loadKnowledgeBases();
  }, [pageNo, keyword]);

  useEffect(() => {
    loadStats(keyword);
  }, [keyword, loadStats]);

  useEffect(() => {
    const trimmed = nameFromQuery.trim();
    if (trimmed !== keyword) {
      setSearchName(trimmed);
      setKeyword(trimmed);
      setPageNo(1);
    }
  }, [nameFromQuery, keyword]);

  useEffect(() => {
    if (renameDialog.open) {
      setRenameValue(renameDialog.kb?.name || "");
    }
  }, [renameDialog]);

  const handleSearch = () => {
    setPageNo(1);
    setKeyword(searchName.trim());
  };

  const handleRefresh = () => {
    setPageNo(1);
    loadKnowledgeBases(1, keyword);
    loadStats(keyword);
  };

  const handleDelete = async () => {
    if (!deleteTarget) return;

    try {
      await deleteKnowledgeBase(deleteTarget.id);
      toast.success("删除成功");
      setDeleteTarget(null);
      setPageNo(1);
      await loadKnowledgeBases(1, keyword);
      await loadStats(keyword);
    } catch (error) {
      toast.error(getErrorMessage(error, "删除失败"));
      console.error(error);
    } finally {
      setDeleteTarget(null);
    }
  };

  const formatDate = (dateStr?: string) => {
    if (!dateStr) return "-";
    return new Date(dateStr).toLocaleString("zh-CN");
  };

  const formatStatValue = (value: number) => {
    if (statsLoading) return "--";
    return value.toLocaleString("zh-CN");
  };

  const renderEmbeddingModel = (model?: string) => {
    if (!model) return "-";
    const parts = model.split("-");
    if (parts.length < 2) {
      return <span className="text-sm text-slate-700">{model}</span>;
    }
    const head = parts.slice(0, -1).join("-");
    const tail = parts[parts.length - 1];
    return (
      <div className="flex flex-col text-xs text-slate-500">
        <span className="font-medium text-slate-700">{head}</span>
        <span>{tail}</span>
      </div>
    );
  };

  const getCollectionBadgeClass = (name?: string) => {
    const value = (name || "").toLowerCase();
    if (value.includes("biz")) {
      return "border-blue-200 bg-blue-50 text-blue-700";
    }
    if (value.includes("group")) {
      return "border-purple-200 bg-purple-50 text-purple-700";
    }
    return "border-slate-200 bg-slate-100 text-slate-600";
  };

  const handleRename = async () => {
    if (!renameDialog.kb) return;
    const nextName = renameValue.trim();
    if (!nextName) {
      toast.error("请输入知识库名称");
      return;
    }
    if (nextName === renameDialog.kb.name) {
      setRenameDialog({ open: false, kb: null });
      return;
    }
    try {
      await renameKnowledgeBase(renameDialog.kb.id, nextName);
      toast.success("重命名成功");
      setRenameDialog({ open: false, kb: null });
      await loadKnowledgeBases(pageNo, keyword);
    } catch (error) {
      toast.error(getErrorMessage(error, "重命名失败"));
      console.error(error);
    }
  };

  return (
    <div className="admin-page">
      <div className="admin-page-header">
        <div>
          <h1 className="admin-page-title">知识库管理</h1>
          <p className="admin-page-subtitle">管理所有知识库及其文档</p>
        </div>
        <div className="admin-page-actions flex flex-wrap items-center gap-3">
          <div className="relative">
            <Input
              value={searchName}
              onChange={(event) => setSearchName(event.target.value)}
              placeholder="搜索知识库名称"
              className="w-[220px] pl-10 h-10 bg-white border-emerald-200 focus:border-emerald-400 focus:ring-emerald-400/20 rounded-xl transition-all duration-200"
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
          <Button className="h-10 px-5 bg-gradient-to-r from-emerald-500 to-emerald-600 hover:from-emerald-600 hover:to-emerald-700 text-white shadow-lg shadow-emerald-500/25 hover:shadow-emerald-600/30 rounded-xl transition-all duration-200 font-medium" onClick={() => setCreateDialogOpen(true)}>
            <Plus className="w-4 h-4 mr-2" />
            新建知识库
          </Button>
        </div>
      </div>

      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4 mb-6">
        {[
          { label: "知识库", value: stats.totalCount, icon: Database, scope: "全部", gradient: "from-emerald-400 to-teal-500" },
          { label: "文档数", value: stats.documentCount, icon: FileBarChart, scope: "全部", gradient: "from-teal-400 to-emerald-500" },
          { label: "含文档知识库", value: stats.activeCount, icon: FolderOpen, scope: "全部", gradient: "from-emerald-500 to-teal-600" },
          { label: "创建用户数", value: stats.creatorCount, icon: Layers, scope: "全部", gradient: "from-teal-500 to-emerald-600" }
        ].map((item) => {
          const Icon = item.icon;
          return (
            <div key={item.label} className="relative overflow-hidden bg-white rounded-2xl border border-emerald-100 shadow-sm hover:shadow-lg hover:shadow-emerald-500/10 hover:-translate-y-0.5 transition-all duration-300 group">
              <div className={`absolute inset-0 bg-gradient-to-br ${item.gradient} opacity-[0.03] group-hover:opacity-[0.06] transition-opacity duration-300`} />
              <div className="relative p-5">
                <div className="flex items-center gap-3">
                  <div className={`p-2.5 rounded-xl bg-gradient-to-br ${item.gradient} shadow-lg`}>
                    <Icon className="h-5 w-5 text-white" />
                  </div>
                  <div>
                    <div className="text-sm text-slate-500 font-medium">{item.label}</div>
                    <div className="text-2xl font-bold text-slate-800">{formatStatValue(item.value)}</div>
                  </div>
                </div>
                <div className="mt-3 pt-3 border-t border-emerald-100">
                  <span className="text-xs text-emerald-600 bg-emerald-50 px-2 py-1 rounded-lg">{item.scope}</span>
                </div>
              </div>
            </div>
          );
        })}
      </div>

      <Card className="border-emerald-100 shadow-sm overflow-hidden rounded-2xl">
        <CardContent className="p-0">
          {loading ? (
            <div className="text-center py-12 text-muted-foreground">加载中...</div>
          ) : knowledgeBases.length === 0 ? (
            <div className="text-center py-12 text-muted-foreground">
              <svg className="mx-auto h-12 w-12 text-emerald-300 mb-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={1.5} d="M12 6.253v13m0-13C10.832 5.477 9.246 5 7.5 5S4.168 5.477 3 6.253v13C4.168 18.477 5.754 18 7.5 18s3.332.477 4.5 1.253m0-13C13.168 5.477 14.754 5 16.5 5c1.747 0 3.332.477 4.5 1.253v13C19.832 18.477 18.247 18 16.5 18c-1.746 0-3.332.477-4.5 1.253" />
              </svg>
              暂无知识库，点击上方按钮创建
            </div>
          ) : (
            <Table className="min-w-[980px]">
              <TableHeader>
                <TableRow className="bg-gradient-to-r from-emerald-50 to-teal-50 border-b border-emerald-100 hover:bg-gradient-to-r hover:from-emerald-100 hover:to-teal-100 transition-colors">
                  <TableHead className="w-[200px] font-semibold text-emerald-700 py-4">名称</TableHead>
                  <TableHead className="w-[180px] font-semibold text-emerald-700">Embedding模型</TableHead>
                  <TableHead className="w-[220px] font-semibold text-emerald-700">Collection</TableHead>
                  <TableHead className="w-[90px] font-semibold text-emerald-700">文档数</TableHead>
                  <TableHead className="w-[120px] font-semibold text-emerald-700">负责人</TableHead>
                  <TableHead className="w-[160px] font-semibold text-emerald-700">创建时间</TableHead>
                  <TableHead className="w-[160px] font-semibold text-emerald-700">修改时间</TableHead>
                  <TableHead className="w-[150px] text-left font-semibold text-emerald-700">操作</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {knowledgeBases.map((kb, index) => (
                  <TableRow key={kb.id} className={`${index % 2 === 0 ? 'bg-white' : 'bg-gradient-to-r from-emerald-50/30 to-teal-50/30'} hover:bg-emerald-50/60 transition-colors duration-200 border-b border-emerald-50`}>
                    <TableCell className="font-medium py-4">
                      <button
                        type="button"
                        className="max-w-[200px] truncate text-emerald-700 hover:text-emerald-600 hover:underline transition-colors"
                        onClick={() => navigate(`/admin/knowledge/${kb.id}`)}
                      >
                        {kb.name}
                      </button>
                    </TableCell>
                    <TableCell>
                      {renderEmbeddingModel(kb.embeddingModel)}
                    </TableCell>
                    <TableCell>
                      {kb.collectionName ? (
                        <Badge
                          variant="outline"
                          className={cn("px-3 py-1.5 rounded-lg font-medium border-2 transition-all hover:scale-105", getCollectionBadgeClass(kb.collectionName))}
                        >
                          {kb.collectionName}
                        </Badge>
                      ) : (
                        "-"
                      )}
                    </TableCell>
                    <TableCell>
                      <span className="inline-flex items-center justify-center min-w-[36px] px-2.5 py-1 bg-teal-50 text-teal-700 rounded-lg text-sm font-medium">
                        {kb.documentCount ?? "-"}
                      </span>
                    </TableCell>
                    <TableCell>
                      <span className="text-slate-600">{kb.createdBy || "-"}</span>
                    </TableCell>
                    <TableCell className="text-slate-500">
                      {formatDate(kb.createTime)}
                    </TableCell>
                    <TableCell className="text-slate-500">
                      {formatDate(kb.updateTime)}
                    </TableCell>
                    <TableCell className="text-center">
                      <div className="flex justify-center gap-2">
                        <Button
                          variant="outline"
                          size="sm"
                          onClick={() => {
                            setRenameDialog({ open: true, kb });
                          }}
                          className="h-8 px-3 border-emerald-200 text-emerald-600 hover:bg-emerald-50 hover:border-emerald-300 rounded-lg transition-all duration-200"
                        >
                          <Pencil className="w-3.5 h-3.5 mr-1" />
                          编辑
                        </Button>
                        <Button
                          variant="ghost"
                          size="sm"
                          className="h-8 px-3 text-red-500 hover:text-red-600 hover:bg-red-50 rounded-lg transition-all duration-200"
                          onClick={() => setDeleteTarget(kb)}
                        >
                          <Trash2 className="w-3.5 h-3.5 mr-1" />
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

      {/* 删除确认对话框 */}
      <AlertDialog open={!!deleteTarget} onOpenChange={() => setDeleteTarget(null)}>
        <AlertDialogContent>
          <AlertDialogHeader>
            <AlertDialogTitle>确认删除</AlertDialogTitle>
            <AlertDialogDescription>
              知识库删除后当前不提供恢复入口。确定要继续吗？
            </AlertDialogDescription>
          </AlertDialogHeader>
          <AlertDialogFooter>
            <AlertDialogCancel>取消</AlertDialogCancel>
            <AlertDialogAction onClick={handleDelete} className="bg-destructive text-destructive-foreground">
              删除
            </AlertDialogAction>
          </AlertDialogFooter>
        </AlertDialogContent>
      </AlertDialog>

      <Dialog open={renameDialog.open} onOpenChange={(open) => setRenameDialog({ open, kb: open ? renameDialog.kb : null })}>
        <DialogContent className="sm:max-w-[420px]" onOpenAutoFocus={(e) => e.preventDefault()} onCloseAutoFocus={(e) => { e.preventDefault(); requestAnimationFrame(() => (document.activeElement as HTMLElement)?.blur()); }}>
          <DialogHeader>
            <DialogTitle>重命名知识库</DialogTitle>
            <DialogDescription>修改知识库名称</DialogDescription>
          </DialogHeader>
          <div className="space-y-2">
            <label className="text-sm font-medium">名称</label>
            <Input value={renameValue} onChange={(event) => setRenameValue(event.target.value)} />
          </div>
          <DialogFooter>
            <Button variant="outline" onClick={() => setRenameDialog({ open: false, kb: null })}>
              取消
            </Button>
            <Button onClick={handleRename}>保存</Button>
          </DialogFooter>
        </DialogContent>
      </Dialog>

      {pageData ? (
        <div className="mt-4 flex flex-wrap items-center justify-between gap-2 text-sm text-slate-500">
          <span>共 {pageData.total} 条</span>
          <div className="flex items-center gap-2">
            <Button
              variant="outline"
              size="sm"
              onClick={() => setPageNo((prev) => Math.max(1, prev - 1))}
              disabled={pageData.current <= 1}
            >
              上一页
            </Button>
            <span>
              {pageData.current} / {pageData.pages}
            </span>
            <Button
              variant="outline"
              size="sm"
              onClick={() => setPageNo((prev) => Math.min(pageData.pages || 1, prev + 1))}
              disabled={pageData.current >= pageData.pages}
            >
              下一页
            </Button>
          </div>
        </div>
      ) : null}

      {/* 创建知识库对话框 */}
      <CreateKnowledgeBaseDialog
        open={createDialogOpen}
        onOpenChange={setCreateDialogOpen}
        onSuccess={() => {
          setPageNo(1);
          loadKnowledgeBases(1, keyword);
          loadStats(keyword);
        }}
      />
    </div>
  );
}
