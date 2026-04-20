import { useEffect, useState } from "react";
import { Eye, Link2, Plus, RefreshCw, Trash2 } from "lucide-react";
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
import { Textarea } from "@/components/ui/textarea";
import { cn } from "@/lib/utils";
import { phsm } from "@/pages/admin/phsm/phsmTheme";
import {
  batchCreateIllnessMedicine,
  createIllnessMedicine,
  deleteIllnessMedicine,
  getIllnessMedicine,
  getIllnessMedicines,
  type IllnessMedicine,
  type PageResult
} from "@/services/illnessMedicineService";
import { getIllnesses } from "@/services/illnessService";
import type { Illness } from "@/services/illnessService";
import { getMedicines } from "@/services/medicineService";
import type { Medicine } from "@/services/medicineService";
import { getErrorMessage } from "@/utils/error";

const PAGE_SIZE = 10;

export function IllnessMedicineListPage() {
  const [pageData, setPageData] = useState<PageResult<IllnessMedicine> | null>(null);
  const [loading, setLoading] = useState(true);
  const [pageNo, setPageNo] = useState(1);
  const [iF, setIF] = useState<string | undefined>();
  const [mF, setMF] = useState<string | undefined>();
  const [allI, setAllI] = useState<Illness[]>([]);
  const [allM, setAllM] = useState<Medicine[]>([]);
  const [open, setOpen] = useState(false);
  const [illId, setIllId] = useState("");
  const [medId, setMedId] = useState("");
  const [batchOpen, setBatchOpen] = useState(false);
  const [batchIll, setBatchIll] = useState("");
  const [batchLines, setBatchLines] = useState("");
  const [del, setDel] = useState<IllnessMedicine | null>(null);
  const [detail, setDetail] = useState<IllnessMedicine | null>(null);

  const loadRefs = async () => {
    try {
      const [ir, mr] = await Promise.all([
        getIllnesses(1, 500),
        getMedicines(1, 500)
      ]);
      setAllI(ir.records || []);
      setAllM(mr.records || []);
    } catch {
      setAllI([]);
      setAllM([]);
    }
  };

  const load = async (p = pageNo) => {
    try {
      setLoading(true);
      setPageData(await getIllnessMedicines(p, PAGE_SIZE, iF, mF));
    } catch (e) {
      toast.error(getErrorMessage(e, "加载失败"));
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    void loadRefs();
  }, []);

  useEffect(() => {
    void load();
  }, [pageNo, iF, mF]);

  const submitSingle = async () => {
    if (!illId || !medId) {
      toast.error("请选择疾病与药品");
      return;
    }
    try {
      await createIllnessMedicine({ illnessId: illId, medicineId: medId });
      toast.success("已关联");
      setOpen(false);
      await load();
    } catch (e) {
      toast.error(getErrorMessage(e, "保存失败"));
    }
  };

  const submitBatch = async () => {
    if (!batchIll) {
      toast.error("请选择疾病");
      return;
    }
    const ids = batchLines
      .split(/[\s,，\n]+/)
      .map((x) => x.trim())
      .filter(Boolean);
    if (!ids.length) {
      toast.error("请输入药品 ID");
      return;
    }
    try {
      await batchCreateIllnessMedicine({ illnessId: batchIll, medicineIds: ids });
      toast.success("批量关联完成（已自动跳过重复）");
      setBatchOpen(false);
      setBatchLines("");
      await load();
    } catch (e) {
      toast.error(getErrorMessage(e, "批量失败"));
    }
  };

  return (
    <div className={phsm.page}>
      <div className={phsm.pageInner}>
        <div className={phsm.toolbar}>
          <div className={phsm.titleBlock}>
            <h1 className={phsm.title}>疾病-药品关联</h1>
            <div className={phsm.titleAccent} aria-hidden />
          </div>
          <div className={cn(phsm.actions, "w-full sm:w-auto")}>
            <Select value={iF || "__all__"} onValueChange={(v) => (setPageNo(1), setIF(v === "__all__" ? undefined : v))}>
              <SelectTrigger className={cn("w-full sm:w-[180px]", phsm.input)}>
                <SelectValue placeholder="疾病" />
              </SelectTrigger>
              <SelectContent className="border-emerald-100 bg-white">
                <SelectItem value="__all__">全部疾病</SelectItem>
                {allI.map((x) => (
                  <SelectItem key={x.id} value={x.id}>
                    {x.illnessName || x.id}
                  </SelectItem>
                ))}
              </SelectContent>
            </Select>
            <Select value={mF || "__all__"} onValueChange={(v) => (setPageNo(1), setMF(v === "__all__" ? undefined : v))}>
              <SelectTrigger className={cn("w-full sm:w-[180px]", phsm.input)}>
                <SelectValue placeholder="药品" />
              </SelectTrigger>
              <SelectContent className="border-emerald-100 bg-white">
                <SelectItem value="__all__">全部药品</SelectItem>
                {allM.map((x) => (
                  <SelectItem key={x.id} value={x.id}>
                    {x.medicineName || x.id}
                  </SelectItem>
                ))}
              </SelectContent>
            </Select>
            <Button size="sm" variant="outline" className={phsm.btnOutline} onClick={() => load()}>
              <RefreshCw className="mr-1 h-4 w-4" />
              刷新
            </Button>
            <Button
              size="sm"
              className={phsm.btnPrimary}
              onClick={() => {
                setIllId(allI[0]?.id || "");
                setMedId(allM[0]?.id || "");
                setOpen(true);
              }}
            >
              <Plus className="mr-1 h-4 w-4" />
              新增
            </Button>
            <Button size="sm" variant="secondary" className={phsm.btnSecondary} onClick={() => setBatchOpen(true)}>
              <Link2 className="mr-1 h-4 w-4" />
              批量关联
            </Button>
          </div>
        </div>

        <Card className={phsm.card}>
          <CardContent className={cn(phsm.cardPt, "overflow-x-auto")}>
            <Table>
              <TableHeader>
                <TableRow className={phsm.tableHeadRow}>
                  <TableHead className={phsm.tableHeadCell}>疾病</TableHead>
                  <TableHead className={phsm.tableHeadCell}>药品</TableHead>
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
                      <TableCell className="font-medium text-teal-900">{r.illnessName || r.illnessId}</TableCell>
                      <TableCell className="text-teal-800">{r.medicineName || r.medicineId}</TableCell>
                      <TableCell>
                        <Button variant="ghost" size="icon" className={phsm.btnGhost} onClick={async () => setDetail(await getIllnessMedicine(r.id))}>
                          <Eye className="h-4 w-4" />
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
                  <Button size="sm" variant="outline" className={phsm.btnOutline} disabled={pageNo <= 1} onClick={() => setPageNo((p) => p - 1)}>
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
              <DialogTitle className="text-teal-800">新增关联</DialogTitle>
            </DialogHeader>
            <div className="space-y-3">
              <div>
                <Label className={phsm.label}>疾病</Label>
                <Select value={illId} onValueChange={setIllId}>
                  <SelectTrigger className={phsm.input}>
                    <SelectValue />
                  </SelectTrigger>
                  <SelectContent className="border-emerald-100 bg-white">
                    {allI.map((x) => (
                      <SelectItem key={x.id} value={x.id}>
                        {x.illnessName}
                      </SelectItem>
                    ))}
                  </SelectContent>
                </Select>
              </div>
              <div>
                <Label className={phsm.label}>药品</Label>
                <Select value={medId} onValueChange={setMedId}>
                  <SelectTrigger className={phsm.input}>
                    <SelectValue />
                  </SelectTrigger>
                  <SelectContent className="border-emerald-100 bg-white">
                    {allM.map((x) => (
                      <SelectItem key={x.id} value={x.id}>
                        {x.medicineName}
                      </SelectItem>
                    ))}
                  </SelectContent>
                </Select>
              </div>
            </div>
            <DialogFooter className="gap-2 sm:gap-0">
              <Button variant="outline" className={phsm.btnOutline} onClick={() => setOpen(false)}>
                取消
              </Button>
              <Button className={phsm.btnPrimary} onClick={submitSingle}>
                保存
              </Button>
            </DialogFooter>
          </DialogContent>
        </Dialog>

        <Dialog open={!!detail} onOpenChange={() => setDetail(null)}>
          <DialogContent className={cn(phsm.dialog, "sm:max-w-lg")}>
            <DialogHeader>
              <DialogTitle className="text-teal-800">关联详情</DialogTitle>
            </DialogHeader>
            {detail ? (
              <div className="space-y-2 text-sm text-teal-900">
                <p><span className="text-teal-700/75">疾病：</span>{detail.illnessName || detail.illnessId || "-"}</p>
                <p><span className="text-teal-700/75">药品：</span>{detail.medicineName || detail.medicineId || "-"}</p>
                <p><span className="text-teal-700/75">创建时间：</span>{detail.createTime ? new Date(detail.createTime).toLocaleString("zh-CN") : "-"}</p>
              </div>
            ) : null}
          </DialogContent>
        </Dialog>

        <Dialog open={batchOpen} onOpenChange={setBatchOpen}>
          <DialogContent className={phsm.dialog}>
            <DialogHeader>
              <DialogTitle className="text-teal-800">批量关联</DialogTitle>
            </DialogHeader>
            <Label className={phsm.label}>疾病</Label>
            <Select value={batchIll} onValueChange={setBatchIll}>
              <SelectTrigger className={phsm.input}>
                <SelectValue placeholder="选择疾病" />
              </SelectTrigger>
              <SelectContent className="border-emerald-100 bg-white">
                {allI.map((x) => (
                  <SelectItem key={x.id} value={x.id}>
                    {x.illnessName}
                  </SelectItem>
                ))}
              </SelectContent>
            </Select>
            <Label className={cn(phsm.label, "mt-2 block")}>药品 ID（每行一个或用逗号分隔）</Label>
            <Textarea
              className={cn("font-mono text-xs", phsm.input)}
              rows={6}
              value={batchLines}
              onChange={(e) => setBatchLines(e.target.value)}
            />
            <DialogFooter className="gap-2 sm:gap-0">
              <Button variant="outline" className={phsm.btnOutline} onClick={() => setBatchOpen(false)}>
                取消
              </Button>
              <Button className={phsm.btnPrimary} onClick={submitBatch}>
                提交
              </Button>
            </DialogFooter>
          </DialogContent>
        </Dialog>

        <AlertDialog open={!!del} onOpenChange={() => setDel(null)}>
          <AlertDialogContent className={phsm.alertDialog}>
            <AlertDialogHeader>
              <AlertDialogTitle className="text-teal-800">取消关联？</AlertDialogTitle>
            </AlertDialogHeader>
            <AlertDialogFooter>
              <AlertDialogCancel className={phsm.btnOutline}>取消</AlertDialogCancel>
              <AlertDialogAction
                className={cn(phsm.btnPrimary, "bg-red-600 hover:bg-red-700")}
                onClick={async () => {
                  if (del) {
                    await deleteIllnessMedicine(del.id);
                    toast.success("已删除");
                    setDel(null);
                    await load();
                  }
                }}
              >
                确认
              </AlertDialogAction>
            </AlertDialogFooter>
          </AlertDialogContent>
        </AlertDialog>
      </div>
    </div>
  );
}
