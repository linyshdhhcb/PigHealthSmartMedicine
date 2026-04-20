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
  createIllness,
  deleteIllness,
  getAllIllnessKinds,
  getIllness,
  getIllnesses,
  updateIllness,
  type Illness,
  type PageResult
} from "@/services/illnessService";
import type { IllnessKind } from "@/services/illnessKindService";
import { getErrorMessage } from "@/utils/error";

const PAGE_SIZE = 10;

export function IllnessListPage() {
  const [pageData, setPageData] = useState<PageResult<Illness> | null>(null);
  const [kinds, setKinds] = useState<IllnessKind[]>([]);
  const [loading, setLoading] = useState(true);
  const [pageNo, setPageNo] = useState(1);
  const [kw, setKw] = useState("");
  const [skw, setSkw] = useState("");
  const [kindF, setKindF] = useState<string | undefined>();
  const [del, setDel] = useState<Illness | null>(null);
  const [open, setOpen] = useState(false);
  const [editId, setEditId] = useState<string>();
  const [form, setForm] = useState({
    illnessName: "",
    kindId: "",
    includeReason: "",
    illnessSymptom: "",
    specialSymptom: ""
  });
  const [busy, setBusy] = useState(false);
  const [dialogFullscreen, setDialogFullscreen] = useState(false);
  const [detail, setDetail] = useState<Illness | null>(null);

  useEffect(() => {
    void getAllIllnessKinds().then(setKinds).catch(() => setKinds([]));
  }, []);

  const load = async (p = pageNo) => {
    try {
      setLoading(true);
      setPageData(await getIllnesses(p, PAGE_SIZE, kindF, skw || undefined));
    } catch (e) {
      toast.error(getErrorMessage(e, "加载失败"));
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    void load();
  }, [pageNo, kindF, skw]);

  const save = async () => {
    if (!form.illnessName.trim() || !form.kindId) {
      toast.error("名称与分类必填");
      return;
    }
    try {
      setBusy(true);
      const body = {
        illnessName: form.illnessName.trim(),
        kindId: form.kindId,
        includeReason: form.includeReason.trim() || undefined,
        illnessSymptom: form.illnessSymptom.trim() || undefined,
        specialSymptom: form.specialSymptom.trim() || undefined
      };
      if (editId) await updateIllness(editId, body);
      else await createIllness(body);
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

  const summarize = (t?: string | null, n = 60) => {
    if (!t) return "-";
    const x = t.replace(/<[^>]+>/g, "").replace(/\s+/g, " ").trim();
    return x.length > n ? x.slice(0, n) + "…" : x;
  };

  return (
    <div className={phsm.page}>
      <div className={phsm.pageInner}>
        <div className={phsm.toolbar}>
          <div className={phsm.titleBlock}>
            <h1 className={phsm.title}>疾病管理</h1>
            <div className={phsm.titleAccent} aria-hidden />
          </div>
          <div className={cn(phsm.actions, "w-full sm:w-auto")}>
            <Select
              value={kindF || "__all__"}
              onValueChange={(v) => (setPageNo(1), setKindF(v === "__all__" ? undefined : v))}
            >
              <SelectTrigger className={cn("w-full sm:w-[160px]", phsm.input)}>
                <SelectValue placeholder="疾病分类" />
              </SelectTrigger>
              <SelectContent className="border-emerald-100 bg-white">
                <SelectItem value="__all__">全部分类</SelectItem>
                {kinds.map((k) => (
                  <SelectItem key={k.id} value={k.id}>
                    {k.name || k.id}
                  </SelectItem>
                ))}
              </SelectContent>
            </Select>
            <Input
              placeholder="名称/症状"
              className={cn("w-full sm:w-40", phsm.input)}
              value={kw}
              onChange={(e) => setKw(e.target.value)}
            />
            <Button size="sm" variant="secondary" className={phsm.btnSecondary} onClick={() => (setPageNo(1), setSkw(kw.trim()))}>
              搜索
            </Button>
            <Button size="sm" variant="outline" className={phsm.btnOutline} onClick={() => load()}>
              <RefreshCw className="mr-1 h-4 w-4" />
              刷新
            </Button>
            <Button
              size="sm"
              className={phsm.btnPrimary}
              onClick={() => {
                setEditId(undefined);
                setForm({
                  illnessName: "",
                  kindId: kinds[0]?.id || "",
                  includeReason: "",
                  illnessSymptom: "",
                  specialSymptom: ""
                });
                setOpen(true);
              }}
            >
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
                  <TableHead className={phsm.tableHeadCell}>名称</TableHead>
                  <TableHead className={phsm.tableHeadCell}>分类</TableHead>
                  <TableHead className={phsm.tableHeadCell}>症状摘要</TableHead>
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
                ) : pageData?.records?.length ? (
                  pageData.records.map((r) => (
                    <TableRow key={r.id} className={phsm.tableRow}>
                      <TableCell className="font-medium text-teal-900">{r.illnessName}</TableCell>
                      <TableCell className="text-teal-800">{r.kindName || "-"}</TableCell>
                      <TableCell className={cn("max-w-md text-sm", phsm.tableMuted)}>{summarize(r.illnessSymptom)}</TableCell>
                      <TableCell>
                        <Button
                          variant="ghost"
                          size="icon"
                          className={phsm.btnGhost}
                          onClick={async () => setDetail(await getIllness(r.id))}
                        >
                          <Eye className="h-4 w-4" />
                        </Button>
                        <Button
                          variant="ghost"
                          size="icon"
                          className={phsm.btnGhost}
                          onClick={async () => {
                            const d = await getIllness(r.id);
                            setEditId(d.id);
                            setForm({
                              illnessName: d.illnessName || "",
                              kindId: d.kindId || "",
                              includeReason: d.includeReason || "",
                              illnessSymptom: d.illnessSymptom || "",
                              specialSymptom: d.specialSymptom || ""
                            });
                            setOpen(true);
                          }}
                        >
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
            {pageData && pageData.pages > 1 ? (
              <div className={cn("mt-4 flex flex-col gap-3 sm:flex-row sm:justify-between", phsm.pagination)}>
                <span>
                  {pageData.total} 条 · {pageData.current}/{pageData.pages}
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
                    disabled={pageNo >= pageData.pages}
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
                <DialogTitle className="text-teal-800">{editId ? "编辑疾病" : "新增疾病"}</DialogTitle>
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
            <div className="space-y-2">
              <Label className={phsm.label}>疾病名称</Label>
              <Input
                value={form.illnessName}
                onChange={(e) => setForm({ ...form, illnessName: e.target.value })}
                className={phsm.input}
              />
              <Label className={phsm.label}>疾病分类</Label>
              <Select value={form.kindId} onValueChange={(v) => setForm({ ...form, kindId: v })}>
                <SelectTrigger className={phsm.input}>
                  <SelectValue />
                </SelectTrigger>
                <SelectContent className="border-emerald-100 bg-white">
                  {kinds.map((k) => (
                    <SelectItem key={k.id} value={k.id}>
                      {k.name || k.id}
                    </SelectItem>
                  ))}
                </SelectContent>
              </Select>
              <Label className={phsm.label}>诱发因素</Label>
              <HtmlEditor
                value={form.includeReason}
                onChange={(value) => setForm({ ...form, includeReason: value })}
                className="border-emerald-100 bg-white"
                minHeightClassName={dialogFullscreen ? "min-h-[26vh]" : "min-h-[140px]"}
                placeholder="请输入诱发因素（支持富文本）"
              />
              <Label className={phsm.label}>疾病症状（可 HTML）</Label>
              <HtmlEditor
                value={form.illnessSymptom}
                onChange={(value) => setForm({ ...form, illnessSymptom: value })}
                className="border-emerald-100 bg-white"
                minHeightClassName={dialogFullscreen ? "min-h-[26vh]" : "min-h-[140px]"}
                placeholder="请输入疾病症状（支持富文本）"
              />
              <Label className={phsm.label}>特殊症状（可 HTML）</Label>
              <HtmlEditor
                value={form.specialSymptom}
                onChange={(value) => setForm({ ...form, specialSymptom: value })}
                className="border-emerald-100 bg-white"
                minHeightClassName={dialogFullscreen ? "min-h-[26vh]" : "min-h-[140px]"}
                placeholder="请输入特殊症状（支持富文本）"
              />
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
              <DialogTitle className="text-teal-800">疾病详情</DialogTitle>
            </DialogHeader>
            {detail ? (
              <div className="space-y-3 text-sm text-teal-900">
                <p><span className="text-teal-700/75">名称：</span>{detail.illnessName || "-"}</p>
                <p><span className="text-teal-700/75">分类：</span>{detail.kindName || detail.kindId || "-"}</p>
                <div className="rounded-lg border border-emerald-100 bg-white p-3">
                  <div className="mb-2 text-teal-700/75">诱发因素：</div>
                  <div className="prose prose-sm max-w-none break-words text-teal-900" dangerouslySetInnerHTML={{ __html: detail.includeReason || "-" }} />
                </div>
                <div className="rounded-lg border border-emerald-100 bg-white p-3">
                  <div className="mb-2 text-teal-700/75">疾病症状：</div>
                  <div className="prose prose-sm max-w-none break-words text-teal-900" dangerouslySetInnerHTML={{ __html: detail.illnessSymptom || "-" }} />
                </div>
                <div className="rounded-lg border border-emerald-100 bg-white p-3">
                  <div className="mb-2 text-teal-700/75">特殊症状：</div>
                  <div className="prose prose-sm max-w-none break-words text-teal-900" dangerouslySetInnerHTML={{ __html: detail.specialSymptom || "-" }} />
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
                    await deleteIllness(del.id);
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
