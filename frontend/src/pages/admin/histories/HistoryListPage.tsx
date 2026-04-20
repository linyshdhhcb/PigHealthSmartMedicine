import { useEffect, useState } from "react";
import { Eye, RefreshCw, Trash2 } from "lucide-react";
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
import { Dialog, DialogContent, DialogHeader, DialogTitle } from "@/components/ui/dialog";
import { Input } from "@/components/ui/input";
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
  OPERATE_TYPE_OPTIONS,
  clearHistories,
  deleteHistory,
  getHistory,
  getHistories,
  type History,
  type PageResult
} from "@/services/historyService";
import { getErrorMessage } from "@/utils/error";

const PAGE = 10;

export function HistoryListPage() {
  const [data, setData] = useState<PageResult<History> | null>(null);
  const [loading, setLoading] = useState(true);
  const [pageNo, setPageNo] = useState(1);
  const [kw, setKw] = useState("");
  const [skw, setSkw] = useState("");
  const [op, setOp] = useState<number | undefined>();
  const [confirmClear, setConfirmClear] = useState(false);
  const [detail, setDetail] = useState<History | null>(null);

  const load = async (p = pageNo) => {
    try {
      setLoading(true);
      setData(await getHistories(p, PAGE, op, skw || undefined));
    } catch (e) {
      toast.error(getErrorMessage(e, "加载失败"));
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    void load();
  }, [pageNo, skw, op]);

  return (
    <div className={phsm.page}>
      <div className={phsm.pageInner}>
        <div className={phsm.toolbar}>
          <div className={phsm.titleBlock}>
            <h1 className={phsm.title}>操作记录</h1>
            <div className={phsm.titleAccent} aria-hidden />
          </div>
          <div className={cn(phsm.actions, "w-full sm:w-auto")}>
            <Select
              value={op === undefined ? "__all__" : String(op)}
              onValueChange={(v) => (setPageNo(1), setOp(v === "__all__" ? undefined : Number(v)))}
            >
              <SelectTrigger className={cn("w-full sm:w-[120px]", phsm.input)}>
                <SelectValue placeholder="类型" />
              </SelectTrigger>
              <SelectContent className="border-emerald-100 bg-white">
                <SelectItem value="__all__">全部</SelectItem>
                {OPERATE_TYPE_OPTIONS.map((o) => (
                  <SelectItem key={o.value} value={String(o.value)}>
                    {o.label}
                  </SelectItem>
                ))}
              </SelectContent>
            </Select>
            <Input className={cn("w-full sm:w-40", phsm.input)} placeholder="关键字" value={kw} onChange={(e) => setKw(e.target.value)} />
            <Button size="sm" variant="secondary" className={phsm.btnSecondary} onClick={() => (setPageNo(1), setSkw(kw.trim()))}>
              搜索
            </Button>
            <Button size="sm" variant="outline" className={phsm.btnOutline} onClick={() => load()}>
              <RefreshCw className="mr-1 h-4 w-4" />
              刷新
            </Button>
            <Button
              size="sm"
              variant="destructive"
              className="border-red-200 bg-red-600 text-white shadow-sm transition-all duration-200 hover:-translate-y-0.5 hover:bg-red-700"
              onClick={() => setConfirmClear(true)}
            >
              清空
            </Button>
          </div>
        </div>

        <Card className={phsm.card}>
          <CardContent className={cn(phsm.cardPt, "overflow-x-auto")}>
            <Table>
              <TableHeader>
                <TableRow className={phsm.tableHeadRow}>
                  <TableHead className={phsm.tableHeadCell}>用户</TableHead>
                  <TableHead className={phsm.tableHeadCell}>关键字</TableHead>
                  <TableHead className={phsm.tableHeadCell}>类型</TableHead>
                  <TableHead className={phsm.tableHeadCell}>时间</TableHead>
                  <TableHead className={cn("w-[60px]", phsm.tableHeadCell)} />
                </TableRow>
              </TableHeader>
              <TableBody>
                {loading ? (
                  <TableRow>
                    <TableCell colSpan={5} className={phsm.tableMuted}>
                      加载中…
                    </TableCell>
                  </TableRow>
                ) : data?.records?.length ? (
                  data.records.map((r) => (
                    <TableRow key={r.id} className={phsm.tableRow}>
                      <TableCell className="font-mono text-xs text-teal-900">{r.userId}</TableCell>
                      <TableCell className="text-teal-800">{r.keyword}</TableCell>
                      <TableCell className="text-teal-800">
                        {OPERATE_TYPE_OPTIONS.find((x) => x.value === r.operateType)?.label || r.operateType}
                      </TableCell>
                      <TableCell className="text-xs text-teal-700/75">
                        {r.createTime ? new Date(r.createTime).toLocaleString("zh-CN") : "-"}
                      </TableCell>
                      <TableCell>
                        <Button
                          variant="ghost"
                          size="icon"
                          className={phsm.btnGhost}
                          onClick={async () => setDetail(await getHistory(r.id))}
                        >
                          <Eye className="h-4 w-4" />
                        </Button>
                        <Button
                          variant="ghost"
                          size="icon"
                          className={cn(phsm.btnGhost, phsm.btnGhostDanger)}
                          onClick={async () => {
                            try {
                              await deleteHistory(r.id);
                              toast.success("已删除");
                              await load();
                            } catch (e) {
                              toast.error(getErrorMessage(e, "删除失败"));
                            }
                          }}
                        >
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
            {data && data.pages > 1 ? (
              <div className={cn("mt-4 flex flex-col gap-3 sm:flex-row sm:justify-between", phsm.pagination)}>
                <span>
                  {data.total} 条 · {data.current}/{data.pages}
                </span>
                <div className="flex flex-wrap gap-2">
                  <Button size="sm" variant="outline" className={phsm.btnOutline} disabled={pageNo <= 1} onClick={() => setPageNo((p) => p - 1)}>
                    上一页
                  </Button>
                  <Button size="sm" variant="outline" className={phsm.btnOutline} disabled={pageNo >= data.pages} onClick={() => setPageNo((p) => p + 1)}>
                    下一页
                  </Button>
                </div>
              </div>
            ) : null}
          </CardContent>
        </Card>

        <Dialog open={!!detail} onOpenChange={() => setDetail(null)}>
          <DialogContent className={cn(phsm.dialog, "sm:max-w-lg")}>
            <DialogHeader>
              <DialogTitle className="text-teal-800">操作记录详情</DialogTitle>
            </DialogHeader>
            {detail ? (
              <div className="space-y-2 text-sm text-teal-900">
                <p><span className="text-teal-700/75">用户：</span>{detail.userId || "-"}</p>
                <p><span className="text-teal-700/75">关键字：</span>{detail.keyword || "-"}</p>
                <p><span className="text-teal-700/75">类型：</span>{OPERATE_TYPE_OPTIONS.find((x) => x.value === detail.operateType)?.label || detail.operateType || "-"}</p>
                <p><span className="text-teal-700/75">时间：</span>{detail.createTime ? new Date(detail.createTime).toLocaleString("zh-CN") : "-"}</p>
              </div>
            ) : null}
          </DialogContent>
        </Dialog>

        <AlertDialog open={confirmClear} onOpenChange={setConfirmClear}>
          <AlertDialogContent className={phsm.alertDialog}>
            <AlertDialogHeader>
              <AlertDialogTitle className="text-left text-teal-800">
                清空历史记录？管理员将清空全部，普通用户仅清空本人记录。
              </AlertDialogTitle>
            </AlertDialogHeader>
            <AlertDialogFooter>
              <AlertDialogCancel className={phsm.btnOutline}>取消</AlertDialogCancel>
              <AlertDialogAction
                className={cn(phsm.btnPrimary, "bg-red-600 hover:bg-red-700")}
                onClick={async () => {
                  try {
                    await clearHistories();
                    toast.success("已清空");
                    setConfirmClear(false);
                    await load();
                  } catch (e) {
                    toast.error(getErrorMessage(e, "清空失败"));
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
