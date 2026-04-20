import { useEffect, useState } from "react";
import { Eye, Pencil, Plus, RefreshCw, Trash2, Upload } from "lucide-react";
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
  MEDICINE_TYPE_OPTIONS,
  createMedicine,
  deleteMedicine,
  getMedicine,
  getMedicines,
  updateMedicine,
  uploadMedicineImage,
  type Medicine,
  type PageResult
} from "@/services/medicineService";
import { getErrorMessage } from "@/utils/error";

const PAGE_SIZE = 10;

export function MedicineListPage() {
  const [pageData, setPageData] = useState<PageResult<Medicine> | null>(null);
  const [loading, setLoading] = useState(true);
  const [pageNo, setPageNo] = useState(1);
  const [kw, setKw] = useState("");
  const [skw, setSkw] = useState("");
  const [typeF, setTypeF] = useState<number | undefined>();
  const [del, setDel] = useState<Medicine | null>(null);
  const [open, setOpen] = useState(false);
  const [editId, setEditId] = useState<string>();
  const [form, setForm] = useState({
    medicineName: "",
    keyword: "",
    medicineBrand: "",
    medicineType: 0,
    medicinePrice: 0 as number | undefined,
    medicineEffect: "",
    interaction: "",
    taboo: "",
    usAge: "",
    imgPath: ""
  });
  const [busy, setBusy] = useState(false);
  const [detail, setDetail] = useState<Medicine | null>(null);

  const load = async (p = pageNo) => {
    try {
      setLoading(true);
      setPageData(await getMedicines(p, PAGE_SIZE, typeF, skw || undefined));
    } catch (e) {
      toast.error(getErrorMessage(e, "加载失败"));
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    void load();
  }, [pageNo, typeF, skw]);

  const onUpload = async (file: File) => {
    try {
      const url = await uploadMedicineImage(file);
      setForm((f) => ({ ...f, imgPath: url }));
      toast.success("图片已上传");
    } catch (e) {
      toast.error(getErrorMessage(e, "上传失败"));
    }
  };

  const save = async () => {
    if (!form.medicineName.trim()) {
      toast.error("请输入药品名称");
      return;
    }
    try {
      setBusy(true);
      const body = {
        medicineName: form.medicineName.trim(),
        keyword: form.keyword.trim() || undefined,
        medicineBrand: form.medicineBrand.trim() || undefined,
        medicineType: form.medicineType,
        medicinePrice: form.medicinePrice ?? 0,
        medicineEffect: form.medicineEffect.trim() || undefined,
        interaction: form.interaction.trim() || undefined,
        taboo: form.taboo.trim() || undefined,
        usAge: form.usAge.trim() || undefined,
        imgPath: form.imgPath.trim() || undefined
      };
      if (editId) await updateMedicine(editId, body);
      else await createMedicine(body);
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

  const priceLabel = (p?: number | null) =>
    p != null ? Number(p).toFixed(2) : "-";

  return (
    <div className={phsm.page}>
      <div className={phsm.pageInner}>
        <div className={phsm.toolbar}>
          <div className={phsm.titleBlock}>
            <h1 className={phsm.title}>药品管理</h1>
            <div className={phsm.titleAccent} aria-hidden />
          </div>
          <div className={cn(phsm.actions, "w-full sm:w-auto")}>
            <Select
              value={typeF === undefined ? "__all__" : String(typeF)}
              onValueChange={(v) => (setPageNo(1), setTypeF(v === "__all__" ? undefined : Number(v)))}
            >
              <SelectTrigger className={cn("w-full sm:w-[120px]", phsm.input)}>
                <SelectValue placeholder="类型" />
              </SelectTrigger>
              <SelectContent className="border-emerald-100 bg-white">
                <SelectItem value="__all__">全部类型</SelectItem>
                {MEDICINE_TYPE_OPTIONS.map((o) => (
                  <SelectItem key={o.value} value={String(o.value)}>
                    {o.label}
                  </SelectItem>
                ))}
              </SelectContent>
            </Select>
            <Input
              className={cn("w-full sm:w-40", phsm.input)}
              placeholder="名称/关键字"
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
                  medicineName: "",
                  keyword: "",
                  medicineBrand: "",
                  medicineType: 0,
                  medicinePrice: 0,
                  medicineEffect: "",
                  interaction: "",
                  taboo: "",
                  usAge: "",
                  imgPath: ""
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
                  <TableHead className={phsm.tableHeadCell}>类型</TableHead>
                  <TableHead className={phsm.tableHeadCell}>价格</TableHead>
                  <TableHead className={phsm.tableHeadCell}>图</TableHead>
                  <TableHead className={cn("w-[160px]", phsm.tableHeadCell)}>操作</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {loading ? (
                  <TableRow>
                    <TableCell colSpan={5} className={phsm.tableMuted}>
                      加载中…
                    </TableCell>
                  </TableRow>
                ) : pageData?.records?.length ? (
                  pageData.records.map((r) => (
                    <TableRow key={r.id} className={phsm.tableRow}>
                      <TableCell className="font-medium text-teal-900">{r.medicineName}</TableCell>
                      <TableCell className="text-teal-800">
                        {MEDICINE_TYPE_OPTIONS.find((x) => x.value === r.medicineType)?.label || "-"}
                      </TableCell>
                      <TableCell className="text-teal-800">{priceLabel(r.medicinePrice)}</TableCell>
                      <TableCell>
                        {r.imgPath ? (
                          <img src={r.imgPath} alt="" className="h-10 w-10 rounded-lg border border-emerald-100 object-cover shadow-sm" />
                        ) : (
                          <span className={phsm.tableMuted}>-</span>
                        )}
                      </TableCell>
                      <TableCell>
                        <Button
                          variant="ghost"
                          size="icon"
                          className={phsm.btnGhost}
                          onClick={async () => setDetail(await getMedicine(r.id))}
                        >
                          <Eye className="h-4 w-4" />
                        </Button>
                        <Button
                          variant="ghost"
                          size="icon"
                          className={phsm.btnGhost}
                          onClick={async () => {
                            const d = await getMedicine(r.id);
                            setEditId(d.id);
                            setForm({
                              medicineName: d.medicineName || "",
                              keyword: d.keyword || "",
                              medicineBrand: d.medicineBrand || "",
                              medicineType: d.medicineType ?? 0,
                              medicinePrice: d.medicinePrice != null ? Number(d.medicinePrice) : 0,
                              medicineEffect: d.medicineEffect || "",
                              interaction: d.interaction || "",
                              taboo: d.taboo || "",
                              usAge: d.usAge || "",
                              imgPath: d.imgPath || ""
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
                    <TableCell colSpan={5} className={phsm.tableMuted}>
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
          <DialogContent className={cn(phsm.dialog, "max-h-[92vh] overflow-y-auto sm:max-w-xl")}>
            <DialogHeader>
              <DialogTitle className="text-teal-800">{editId ? "编辑药品" : "新增药品"}</DialogTitle>
            </DialogHeader>
            <div className="grid gap-2 sm:grid-cols-2">
              <div className="sm:col-span-2">
                <Label className={phsm.label}>名称</Label>
                <Input
                  className={phsm.input}
                  value={form.medicineName}
                  onChange={(e) => setForm({ ...form, medicineName: e.target.value })}
                />
              </div>
              <div>
                <Label className={phsm.label}>类型</Label>
                <Select value={String(form.medicineType)} onValueChange={(v) => setForm({ ...form, medicineType: Number(v) })}>
                  <SelectTrigger className={phsm.input}>
                    <SelectValue />
                  </SelectTrigger>
                  <SelectContent className="border-emerald-100 bg-white">
                    {MEDICINE_TYPE_OPTIONS.map((o) => (
                      <SelectItem key={o.value} value={String(o.value)}>
                        {o.label}
                      </SelectItem>
                    ))}
                  </SelectContent>
                </Select>
              </div>
              <div>
                <Label className={phsm.label}>价格</Label>
                <Input
                  className={phsm.input}
                  type="number"
                  step="0.01"
                  min={0}
                  value={form.medicinePrice ?? ""}
                  onChange={(e) =>
                    setForm({ ...form, medicinePrice: e.target.value === "" ? undefined : Number(e.target.value) })
                  }
                />
              </div>
              <div>
                <Label className={phsm.label}>关键字</Label>
                <Input className={phsm.input} value={form.keyword} onChange={(e) => setForm({ ...form, keyword: e.target.value })} />
              </div>
              <div>
                <Label className={phsm.label}>品牌</Label>
                <Input className={phsm.input} value={form.medicineBrand} onChange={(e) => setForm({ ...form, medicineBrand: e.target.value })} />
              </div>
              <div className="sm:col-span-2">
                <Label className={phsm.label}>图片</Label>
                <div className="flex flex-wrap items-center gap-2">
                  <label className="inline-flex cursor-pointer items-center gap-2 rounded-lg border border-emerald-100 bg-emerald-50/60 px-3 py-2 text-sm text-teal-700 transition-all duration-200 hover:border-emerald-300 hover:bg-emerald-100">
                    <Upload className="h-4 w-4 text-emerald-600" />
                    <span>上传</span>
                    <input
                      type="file"
                      accept="image/jpeg,image/png"
                      className="hidden"
                      onChange={(e) => {
                        const f = e.target.files?.[0];
                        if (f) void onUpload(f);
                        e.target.value = "";
                      }}
                    />
                  </label>
                  {form.imgPath ? (
                    <span className="truncate text-xs text-teal-700/70">{form.imgPath}</span>
                  ) : null}
                </div>
              </div>
              <div className="sm:col-span-2">
                <Label className={phsm.label}>功效</Label>
                <Textarea className={phsm.input} value={form.medicineEffect} onChange={(e) => setForm({ ...form, medicineEffect: e.target.value })} />
              </div>
              <div className="sm:col-span-2">
                <Label className={phsm.label}>相互作用</Label>
                <Textarea className={phsm.input} value={form.interaction} onChange={(e) => setForm({ ...form, interaction: e.target.value })} />
              </div>
              <div className="sm:col-span-2">
                <Label className={phsm.label}>禁忌</Label>
                <Textarea className={phsm.input} value={form.taboo} onChange={(e) => setForm({ ...form, taboo: e.target.value })} />
              </div>
              <div className="sm:col-span-2">
                <Label className={phsm.label}>用法用量</Label>
                <Textarea className={phsm.input} value={form.usAge} onChange={(e) => setForm({ ...form, usAge: e.target.value })} />
              </div>
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
              <DialogTitle className="text-teal-800">药品详情</DialogTitle>
            </DialogHeader>
            {detail ? (
              <div className="grid gap-2 text-sm text-teal-900 sm:grid-cols-2">
                <p><span className="text-teal-700/75">名称：</span>{detail.medicineName || "-"}</p>
                <p><span className="text-teal-700/75">类型：</span>{MEDICINE_TYPE_OPTIONS.find((x) => x.value === detail.medicineType)?.label || "-"}</p>
                <p><span className="text-teal-700/75">价格：</span>{priceLabel(detail.medicinePrice)}</p>
                <p><span className="text-teal-700/75">品牌：</span>{detail.medicineBrand || "-"}</p>
                <p className="sm:col-span-2"><span className="text-teal-700/75">关键字：</span>{detail.keyword || "-"}</p>
                {detail.imgPath ? (
                  <div className="sm:col-span-2">
                    <div className="mb-1 text-teal-700/75">图片：</div>
                    <img src={detail.imgPath} alt="" className="h-24 w-24 rounded-lg border border-emerald-100 object-cover" />
                  </div>
                ) : null}
                <p className="sm:col-span-2 whitespace-pre-wrap rounded-lg border border-emerald-100 bg-emerald-50/50 p-3">
                  <span className="text-teal-700/75">功效：</span>{detail.medicineEffect || "-"}
                </p>
                <p className="sm:col-span-2 whitespace-pre-wrap rounded-lg border border-emerald-100 bg-emerald-50/50 p-3">
                  <span className="text-teal-700/75">相互作用：</span>{detail.interaction || "-"}
                </p>
                <p className="sm:col-span-2 whitespace-pre-wrap rounded-lg border border-emerald-100 bg-emerald-50/50 p-3">
                  <span className="text-teal-700/75">禁忌：</span>{detail.taboo || "-"}
                </p>
                <p className="sm:col-span-2 whitespace-pre-wrap rounded-lg border border-emerald-100 bg-emerald-50/50 p-3">
                  <span className="text-teal-700/75">用法用量：</span>{detail.usAge || "-"}
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
                    await deleteMedicine(del.id);
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
