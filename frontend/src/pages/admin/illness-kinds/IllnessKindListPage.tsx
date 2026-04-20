import { useEffect, useState } from "react";
import { Eye, Pencil, Plus, RefreshCw, Trash2 } from "lucide-react";
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
  createIllnessKind,
  deleteIllnessKind,
  getIllnessKind,
  getIllnessKinds,
  updateIllnessKind,
  type IllnessKind,
  type PageResult
} from "@/services/illnessKindService";
import { getErrorMessage } from "@/utils/error";

const PAGE_SIZE = 10;

export function IllnessKindListPage() {
  const [pageData, setPageData] = useState<PageResult<IllnessKind> | null>(null);
  const [loading, setLoading] = useState(true);
  const [pageNo, setPageNo] = useState(1);
  const [sw, setSw] = useState("");
  const [fw, setFw] = useState("");
  const [del, setDel] = useState<IllnessKind | null>(null);
  const [open, setOpen] = useState(false);
  const [editId, setEditId] = useState<string>();
  const [name, setName] = useState("");
  const [info, setInfo] = useState("");
  const [busy, setBusy] = useState(false);
  const [detail, setDetail] = useState<IllnessKind | null>(null);

  const load = async (p = pageNo) => {
    try {
      setLoading(true);
      setPageData(await getIllnessKinds(p, PAGE_SIZE, fw || undefined));
    } catch (e) {
      toast.error(getErrorMessage(e, "加载失败"));
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    void load();
  }, [pageNo, fw]);

  const submit = async () => {
    if (!name.trim()) {
      toast.error("请输入名称");
      return;
    }
    try {
      setBusy(true);
      if (editId) await updateIllnessKind(editId, { name: name.trim(), info: info.trim() || undefined });
      else await createIllnessKind({ name: name.trim(), info: info.trim() || undefined });
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
            <h1 className={phsm.title}>疾病种类</h1>
            <div className={phsm.titleAccent} aria-hidden />
          </div>
          <div className={cn(phsm.actions, "w-full sm:w-auto")}>
            <Input
              value={sw}
              onChange={(e) => setSw(e.target.value)}
              placeholder="名称"
              className={cn("w-full sm:w-44", phsm.input)}
            />
            <Button size="sm" variant="secondary" className={phsm.btnSecondary} onClick={() => (setPageNo(1), setFw(sw.trim()))}>
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
                setName("");
                setInfo("");
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
                  <TableHead className={phsm.tableHeadCell}>描述</TableHead>
                  <TableHead className={cn("w-[160px]", phsm.tableHeadCell)}>操作</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {loading ? (
                  <TableRow>
                    <TableCell colSpan={3} className={phsm.tableMuted}>
                      加载中…
                    </TableCell>
                  </TableRow>
                ) : pageData?.records?.length ? (
                  pageData.records.map((r) => (
                    <TableRow key={r.id} className={phsm.tableRow}>
                      <TableCell className="font-medium text-teal-900">{r.name}</TableCell>
                      <TableCell className={cn("max-w-md text-sm", phsm.tableMuted)}>{r.info || "-"}</TableCell>
                      <TableCell>
                        <Button
                          variant="ghost"
                          size="icon"
                          className={phsm.btnGhost}
                          onClick={async () => setDetail(await getIllnessKind(r.id))}
                        >
                          <Eye className="h-4 w-4" />
                        </Button>
                        <Button
                          variant="ghost"
                          size="icon"
                          className={phsm.btnGhost}
                          onClick={async () => {
                            const d = await getIllnessKind(r.id);
                            setEditId(d.id);
                            setName(d.name || "");
                            setInfo(d.info || "");
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
                    <TableCell colSpan={3} className={phsm.tableMuted}>
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
          <DialogContent className={phsm.dialog}>
            <DialogHeader>
              <DialogTitle className="text-teal-800">{editId ? "编辑" : "新增"}</DialogTitle>
            </DialogHeader>
            <Label className={phsm.label}>分类名称</Label>
            <Input value={name} onChange={(e) => setName(e.target.value)} className={phsm.input} />
            <Label className={phsm.label}>描述</Label>
            <Textarea value={info} onChange={(e) => setInfo(e.target.value)} className={phsm.input} />
            <DialogFooter className="gap-2 sm:gap-0">
              <Button variant="outline" className={phsm.btnOutline} onClick={() => setOpen(false)}>
                取消
              </Button>
              <Button className={phsm.btnPrimary} onClick={submit} disabled={busy}>
                保存
              </Button>
            </DialogFooter>
          </DialogContent>
        </Dialog>

        <Dialog open={!!detail} onOpenChange={() => setDetail(null)}>
          <DialogContent className={cn(phsm.dialog, "sm:max-w-lg")}>
            <DialogHeader>
              <DialogTitle className="text-teal-800">疾病种类详情</DialogTitle>
            </DialogHeader>
            {detail ? (
              <div className="space-y-2 text-sm text-teal-900">
                <p><span className="text-teal-700/75">名称：</span>{detail.name || "-"}</p>
                <p className="whitespace-pre-wrap rounded-lg border border-emerald-100 bg-emerald-50/50 p-3">
                  <span className="text-teal-700/75">描述：</span>{detail.info || "-"}
                </p>
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
                    await deleteIllnessKind(del.id);
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
