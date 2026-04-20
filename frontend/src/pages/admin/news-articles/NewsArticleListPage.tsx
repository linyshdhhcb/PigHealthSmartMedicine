import { useEffect, useState } from "react";
import { Eye, Maximize2, Minimize2, Pencil, Plus, RefreshCw, Trash2 } from "lucide-react";
import { toast } from "sonner";

import { Button } from "@/components/ui/button";
import {
  AlertDialog,
  AlertDialogAction,
  AlertDialogCancel,
  AlertDialogContent,
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
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow
} from "@/components/ui/table";
import { Textarea } from "@/components/ui/textarea";
import { cn } from "@/lib/utils";
import { phsm } from "@/pages/admin/phsm/phsmTheme";
import {
  createNewsArticle,
  deleteNewsArticle,
  getNewsArticle,
  getNewsArticles,
  updateNewsArticle,
  type NewsArticle,
  type PageResult
} from "@/services/newsArticleService";
import { getErrorMessage } from "@/utils/error";

const PAGE = 10;

function summarize(s: string, n = 80) {
  const t = s.replace(/<[^>]+>/g, "").replace(/\s+/g, " ").trim();
  return t.length > n ? t.slice(0, n) + "…" : t;
}

export function NewsArticleListPage() {
  const [data, setData] = useState<PageResult<NewsArticle> | null>(null);
  const [loading, setLoading] = useState(true);
  const [pageNo, setPageNo] = useState(1);
  const [kw, setKw] = useState("");
  const [skw, setSkw] = useState("");
  const [src, setSrc] = useState("");
  const [ssrc, setSsrc] = useState("");
  const [del, setDel] = useState<NewsArticle | null>(null);
  const [open, setOpen] = useState(false);
  const [editId, setEditId] = useState<string>();
  const [f, setF] = useState({
    title: "",
    content: "",
    author: "",
    url: "",
    source: "",
    summary: "",
    publishLocal: ""
  });
  const [busy, setBusy] = useState(false);
  const [dialogFullscreen, setDialogFullscreen] = useState(false);
  const [detail, setDetail] = useState<NewsArticle | null>(null);

  const load = async (p = pageNo) => {
    try {
      setLoading(true);
      setData(await getNewsArticles(p, PAGE, skw || undefined, ssrc || undefined));
    } catch (e) {
      toast.error(getErrorMessage(e, "加载失败"));
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    void load();
  }, [pageNo, skw, ssrc]);

  const openCreate = () => {
    setEditId(undefined);
    const now = new Date();
    now.setMinutes(now.getMinutes() - now.getTimezoneOffset());
    setF({
      title: "",
      content: "",
      author: "",
      url: "",
      source: "",
      summary: "",
      publishLocal: now.toISOString().slice(0, 16)
    });
    setOpen(true);
  };

  const openEdit = async (row: NewsArticle) => {
    try {
      const d = await getNewsArticle(row.id);
      let local = "";
      if (d.publishTime) {
        const dt = new Date(d.publishTime);
        dt.setMinutes(dt.getMinutes() - dt.getTimezoneOffset());
        local = dt.toISOString().slice(0, 16);
      }
      setEditId(d.id);
      setF({
        title: d.title,
        content: d.content,
        author: d.author || "",
        url: d.url || "",
        source: d.source || "",
        summary: d.summary || "",
        publishLocal: local
      });
      setOpen(true);
    } catch (e) {
      toast.error(getErrorMessage(e, "加载失败"));
    }
  };

  const save = async () => {
    if (!f.title.trim() || !f.content.trim()) {
      toast.error("标题与内容必填");
      return;
    }
    try {
      setBusy(true);
      const publishTime = f.publishLocal ? new Date(f.publishLocal).toISOString() : undefined;
      const body = {
        title: f.title.trim(),
        content: f.content,
        author: f.author.trim() || undefined,
        url: f.url.trim() || undefined,
        source: f.source.trim() || undefined,
        summary: f.summary.trim() || undefined,
        publishTime
      };
      if (editId) await updateNewsArticle(editId, body);
      else await createNewsArticle(body);
      toast.success("已保存");
      setOpen(false);
      setPageNo(1);
      await load(1);
    } catch (e) {
      toast.error(getErrorMessage(e, "保存失败"));
    } finally {
      setBusy(false);
    }
  };

  return (
    <div className={phsm.page}>
      <div className={phsm.pageInner}>
        <div className={phsm.toolbar}>
          <div className={phsm.titleBlock}>
            <h1 className={phsm.title}>新闻资讯</h1>
            <div className={phsm.titleAccent} aria-hidden />
          </div>
          <div className={cn(phsm.actions, "w-full sm:w-auto")}>
            <Input
              placeholder="标题/内容"
              className={cn("w-full sm:w-40", phsm.input)}
              value={kw}
              onChange={(e) => setKw(e.target.value)}
            />
            <Input
              placeholder="来源"
              className={cn("w-full sm:w-32", phsm.input)}
              value={src}
              onChange={(e) => setSrc(e.target.value)}
            />
            <Button
              size="sm"
              variant="secondary"
              className={phsm.btnSecondary}
              onClick={() => (setPageNo(1), setSkw(kw.trim()), setSsrc(src.trim()))}
            >
              搜索
            </Button>
            <Button size="sm" variant="outline" className={phsm.btnOutline} onClick={() => load()}>
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
                  <TableHead className={phsm.tableHeadCell}>来源</TableHead>
                  <TableHead className={phsm.tableHeadCell}>摘要</TableHead>
                  <TableHead className={cn("w-[160px]", phsm.tableHeadCell)}>操作</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {loading ? (
                  <TableRow>
                    <TableCell colSpan={4} className={phsm.tableMuted}>
                      加载中…
                    </TableCell>
                  </TableRow>
                ) : data?.records?.length ? (
                  data.records.map((r) => (
                    <TableRow key={r.id} className={phsm.tableRow}>
                      <TableCell className="font-medium text-teal-900">{r.title}</TableCell>
                      <TableCell className="text-teal-800">{r.source || "-"}</TableCell>
                      <TableCell className={cn("max-w-[300px] text-sm", phsm.tableMuted)}>
                        {(r.summary || summarize(r.content)).slice(0, 120)}
                      </TableCell>
                      <TableCell>
                        <Button variant="ghost" size="icon" className={phsm.btnGhost} onClick={async () => setDetail(await getNewsArticle(r.id))}>
                          <Eye className="h-4 w-4" />
                        </Button>
                        <Button variant="ghost" size="icon" className={phsm.btnGhost} onClick={() => openEdit(r)}>
                          <Pencil className="h-4 w-4" />
                        </Button>
                        <Button variant="ghost" size="icon" className={cn(phsm.btnGhost, phsm.btnGhostDanger)} onClick={() => setDel(r)}>
                          <Trash2 className="h-4 w-4" />
                        </Button>
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
            {data && data.pages > 1 ? (
              <div className={cn("mt-4 flex flex-col gap-3 sm:flex-row sm:justify-between", phsm.pagination)}>
                <span>
                  {data.total} 条 · {data.current}/{data.pages}
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
                    disabled={pageNo >= data.pages}
                    onClick={() => setPageNo((p) => p + 1)}
                  >
                    下一页
                  </Button>
                </div>
              </div>
            ) : null}
          </CardContent>
        </Card>

        <Dialog open={open} onOpenChange={setOpen}>
          <DialogContent
            className={cn(
              phsm.dialog,
              "overflow-y-auto",
              dialogFullscreen ? "h-[95vh] w-[96vw] max-w-[96vw]" : "max-h-[90vh] sm:max-w-3xl"
            )}
          >
            <DialogHeader>
              <div className="flex items-center justify-between gap-2">
                <DialogTitle className="text-teal-800">{editId ? "编辑新闻" : "新增新闻"}</DialogTitle>
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
            <div className="space-y-2 py-2">
              <Label className={phsm.label}>标题</Label>
              <Input value={f.title} onChange={(e) => setF({ ...f, title: e.target.value })} className={phsm.input} />
              <Label className={phsm.label}>内容</Label>
              <HtmlEditor
                value={f.content}
                onChange={(value) => setF({ ...f, content: value })}
                className="border-emerald-100 bg-white"
                minHeightClassName={dialogFullscreen ? "min-h-[50vh]" : "min-h-[220px]"}
                placeholder="请输入新闻内容（支持富文本）"
              />
              <Label className={phsm.label}>作者</Label>
              <Input value={f.author} onChange={(e) => setF({ ...f, author: e.target.value })} className={phsm.input} />
              <Label className={phsm.label}>发布时间</Label>
              <Input
                type="datetime-local"
                value={f.publishLocal}
                onChange={(e) => setF({ ...f, publishLocal: e.target.value })}
                className={phsm.input}
              />
              <Label className={phsm.label}>来源</Label>
              <Input value={f.source} onChange={(e) => setF({ ...f, source: e.target.value })} className={phsm.input} />
              <Label className={phsm.label}>摘要</Label>
              <Textarea value={f.summary} onChange={(e) => setF({ ...f, summary: e.target.value })} className={phsm.input} />
              <Label className={phsm.label}>转载 URL</Label>
              <Input value={f.url} onChange={(e) => setF({ ...f, url: e.target.value })} className={phsm.input} />
            </div>
            <DialogFooter className="gap-2 sm:gap-0">
              <Button variant="outline" className={phsm.btnOutline} onClick={() => setOpen(false)}>
                取消
              </Button>
              <Button className={phsm.btnPrimary} onClick={save} disabled={busy}>
                保存
              </Button>
            </DialogFooter>
          </DialogContent>
        </Dialog>

        <Dialog open={!!detail} onOpenChange={() => setDetail(null)}>
          <DialogContent className={cn(phsm.dialog, "max-h-[90vh] overflow-y-auto sm:max-w-3xl")}>
            <DialogHeader>
              <DialogTitle className="text-teal-800">新闻详情</DialogTitle>
            </DialogHeader>
            {detail ? (
              <div className="space-y-2 text-sm text-teal-900">
                <p><span className="text-teal-700/75">标题：</span>{detail.title}</p>
                <p><span className="text-teal-700/75">来源：</span>{detail.source || "-"}</p>
                <p><span className="text-teal-700/75">作者：</span>{detail.author || "-"}</p>
                <p><span className="text-teal-700/75">发布时间：</span>{detail.publishTime ? new Date(detail.publishTime).toLocaleString("zh-CN") : "-"}</p>
                <p><span className="text-teal-700/75">转载 URL：</span>{detail.url || "-"}</p>
                <div className="rounded-lg border border-emerald-100 bg-white p-3">
                  <div className="mb-2 text-teal-700/75">内容：</div>
                  <div className="prose prose-sm max-w-none break-words text-teal-900" dangerouslySetInnerHTML={{ __html: detail.content || "-" }} />
                </div>
              </div>
            ) : null}
          </DialogContent>
        </Dialog>

        <AlertDialog open={!!del} onOpenChange={() => setDel(null)}>
          <AlertDialogContent className={phsm.alertDialog}>
            <AlertDialogHeader>
              <AlertDialogTitle className="text-teal-800">删除？</AlertDialogTitle>
            </AlertDialogHeader>
            <AlertDialogFooter>
              <AlertDialogCancel className={phsm.btnOutline}>取消</AlertDialogCancel>
              <AlertDialogAction
                className={cn(phsm.btnPrimary, "bg-red-600 hover:bg-red-700")}
                onClick={async () => {
                  if (del) {
                    await deleteNewsArticle(del.id);
                    toast.success("已删除");
                    setDel(null);
                    await load();
                  }
                }}
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
