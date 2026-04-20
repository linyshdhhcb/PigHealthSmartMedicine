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
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow
} from "@/components/ui/table";
import { cn } from "@/lib/utils";
import { phsm } from "@/pages/admin/phsm/phsmTheme";
import { deleteFeedback, getFeedback, getFeedbacks, type Feedback, type PageResult } from "@/services/feedbackService";
import { getErrorMessage } from "@/utils/error";

const PAGE = 10;

export function FeedbackListPage() {
  const [data, setData] = useState<PageResult<Feedback> | null>(null);
  const [loading, setLoading] = useState(true);
  const [pageNo, setPageNo] = useState(1);
  const [kw, setKw] = useState("");
  const [skw, setSkw] = useState("");
  const [view, setView] = useState<Feedback | null>(null);
  const [del, setDel] = useState<Feedback | null>(null);

  const load = async (p = pageNo) => {
    try {
      setLoading(true);
      setData(await getFeedbacks(p, PAGE, skw || undefined));
    } catch (e) {
      toast.error(getErrorMessage(e, "加载失败"));
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    void load();
  }, [pageNo, skw]);

  return (
    <div className={phsm.page}>
      <div className={phsm.pageInner}>
        <div className={phsm.toolbar}>
          <div className={phsm.titleBlock}>
            <h1 className={phsm.title}>用户反馈</h1>
            <div className={phsm.titleAccent} aria-hidden />
          </div>
          <div className={cn(phsm.actions, "w-full sm:w-auto")}>
            <Input placeholder="标题/内容" className={cn("w-full sm:w-44", phsm.input)} value={kw} onChange={(e) => setKw(e.target.value)} />
            <Button size="sm" variant="secondary" className={phsm.btnSecondary} onClick={() => (setPageNo(1), setSkw(kw.trim()))}>
              搜索
            </Button>
            <Button size="sm" variant="outline" className={phsm.btnOutline} onClick={() => load()}>
              <RefreshCw className="mr-1 h-4 w-4" />
              刷新
            </Button>
          </div>
        </div>

        <Card className={phsm.card}>
          <CardContent className={cn(phsm.cardPt, "overflow-x-auto")}>
            <Table>
              <TableHeader>
                <TableRow className={phsm.tableHeadRow}>
                  <TableHead className={phsm.tableHeadCell}>姓名</TableHead>
                  <TableHead className={phsm.tableHeadCell}>邮箱</TableHead>
                  <TableHead className={phsm.tableHeadCell}>标题</TableHead>
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
                      <TableCell className="text-teal-900">{r.name}</TableCell>
                      <TableCell className="max-w-[140px] truncate text-teal-800">{r.email}</TableCell>
                      <TableCell className="font-medium text-teal-900">{r.title}</TableCell>
                      <TableCell>
                        <Button variant="ghost" size="icon" className={phsm.btnGhost} onClick={async () => setView(await getFeedback(r.id))}>
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

        <Dialog open={!!view} onOpenChange={() => setView(null)}>
          <DialogContent className={cn(phsm.dialog, "sm:max-w-lg")}>
            <DialogHeader>
              <DialogTitle className="text-teal-800">反馈详情</DialogTitle>
            </DialogHeader>
            {view ? (
              <div className="space-y-2 text-sm text-teal-900">
                <p>
                  <span className="text-teal-700/75">姓名：</span>
                  {view.name}
                </p>
                <p>
                  <span className="text-teal-700/75">邮箱：</span>
                  {view.email}
                </p>
                <p>
                  <span className="text-teal-700/75">标题：</span>
                  {view.title}
                </p>
                <p className="whitespace-pre-wrap rounded-lg border border-emerald-100 bg-emerald-50/50 p-3">
                  <span className="text-teal-700/75">内容：</span>
                  {view.content}
                </p>
              </div>
            ) : null}
          </DialogContent>
        </Dialog>

        <AlertDialog open={!!del} onOpenChange={() => setDel(null)}>
          <AlertDialogContent className={phsm.alertDialog}>
            <AlertDialogHeader>
              <AlertDialogTitle className="text-teal-800">删除该反馈？</AlertDialogTitle>
            </AlertDialogHeader>
            <AlertDialogFooter>
              <AlertDialogCancel className={phsm.btnOutline}>取消</AlertDialogCancel>
              <AlertDialogAction
                className={cn(phsm.btnPrimary, "bg-red-600 hover:bg-red-700")}
                onClick={async () => {
                  if (del) {
                    await deleteFeedback(del.id);
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
