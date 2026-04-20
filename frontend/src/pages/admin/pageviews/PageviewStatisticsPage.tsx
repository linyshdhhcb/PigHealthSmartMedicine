import { useEffect, useState } from "react";
import { Eye, RefreshCw } from "lucide-react";
import { toast } from "sonner";
import { Bar, BarChart, CartesianGrid, ResponsiveContainer, Tooltip, XAxis, YAxis } from "recharts";

import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Dialog, DialogContent, DialogHeader, DialogTitle } from "@/components/ui/dialog";
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
import { getPageviewStatistics } from "@/services/pageviewService";
import { getErrorMessage } from "@/utils/error";

const BAR_FILL = "#10b981";
const GRID_STROKE = "rgba(16, 185, 129, 0.2)";
const AXIS_TICK = "#0f766e";

export function PageviewStatisticsPage() {
  const [loading, setLoading] = useState(true);
  const [stats, setStats] = useState<{
    totalPageviews: number;
    todayPageviews: number;
    topIllnesses: { illnessId: string; illnessName: string; pageviews: number }[];
  } | null>(null);
  const [detail, setDetail] = useState<{ illnessId: string; illnessName: string; pageviews: number } | null>(null);

  const load = async () => {
    try {
      setLoading(true);
      setStats(await getPageviewStatistics());
    } catch (e) {
      toast.error(getErrorMessage(e, "加载失败"));
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    void load();
  }, []);

  const chartData =
    stats?.topIllnesses?.map((x) => ({
      name: x.illnessName?.slice(0, 8) || x.illnessId,
      pv: Number(x.pageviews)
    })) || [];

  return (
    <div className={phsm.page}>
      <div className={phsm.pageInner}>
        <div className={phsm.toolbar}>
          <div className={phsm.titleBlock}>
            <h1 className={phsm.title}>浏览量统计</h1>
            <div className={phsm.titleAccent} aria-hidden />
          </div>
          <Button size="sm" variant="outline" className={phsm.btnOutline} onClick={() => load()} disabled={loading}>
            <RefreshCw className="mr-1 h-4 w-4" />
            刷新
          </Button>
        </div>

        {loading ? (
          <p className={cn("rounded-xl border border-emerald-100 bg-white/90 px-4 py-8 text-center", phsm.tableMuted)}>加载中…</p>
        ) : stats ? (
          <>
            <div className="grid gap-4 sm:grid-cols-2">
              <Card className={phsm.statCard}>
                <CardHeader className="pb-2">
                  <CardTitle className={phsm.statLabel}>累计浏览</CardTitle>
                </CardHeader>
                <CardContent className={phsm.statValue}>{stats.totalPageviews}</CardContent>
              </Card>
              <Card className={phsm.statCard}>
                <CardHeader className="pb-2">
                  <CardTitle className={phsm.statLabel}>今日浏览（按更新）</CardTitle>
                </CardHeader>
                <CardContent className={phsm.statValue}>{stats.todayPageviews}</CardContent>
              </Card>
            </div>

            <Card className={phsm.card}>
              <CardHeader>
                <CardTitle className="text-base font-semibold text-teal-800">热门疾病 Top10</CardTitle>
              </CardHeader>
              <CardContent className="h-[360px] min-h-[280px]">
                <ResponsiveContainer width="100%" height="100%">
                  <BarChart data={chartData} margin={{ left: 8, right: 8, bottom: 40 }}>
                    <CartesianGrid strokeDasharray="3 3" stroke={GRID_STROKE} />
                    <XAxis
                      dataKey="name"
                      angle={-25}
                      textAnchor="end"
                      interval={0}
                      height={60}
                      tick={{ fontSize: 11, fill: AXIS_TICK }}
                    />
                    <YAxis tick={{ fill: AXIS_TICK }} />
                    <Tooltip
                      contentStyle={{
                        borderRadius: "0.75rem",
                        border: "1px solid rgb(209 250 229)",
                        background: "rgba(255,255,255,0.95)",
                        boxShadow: "0 10px 15px -3px rgba(16, 185, 129, 0.15)"
                      }}
                      labelStyle={{ color: "#115e59" }}
                    />
                    <Bar dataKey="pv" fill={BAR_FILL} radius={[8, 8, 0, 0]} />
                  </BarChart>
                </ResponsiveContainer>
              </CardContent>
            </Card>

            <Card className={phsm.card}>
              <CardHeader>
                <CardTitle className="text-base font-semibold text-teal-800">明细列表</CardTitle>
              </CardHeader>
              <CardContent className="overflow-x-auto">
                <Table>
                  <TableHeader>
                    <TableRow className={phsm.tableHeadRow}>
                      <TableHead className={phsm.tableHeadCell}>疾病</TableHead>
                      <TableHead className={phsm.tableHeadCell}>浏览量</TableHead>
                      <TableHead className={phsm.tableHeadCell}>操作</TableHead>
                    </TableRow>
                  </TableHeader>
                  <TableBody>
                    {stats.topIllnesses.length ? (
                      stats.topIllnesses.map((row) => (
                        <TableRow key={row.illnessId} className={phsm.tableRow}>
                          <TableCell className="font-medium text-teal-900">{row.illnessName || row.illnessId}</TableCell>
                          <TableCell className="text-teal-800">{row.pageviews}</TableCell>
                          <TableCell>
                            <Button variant="ghost" size="icon" className={phsm.btnGhost} onClick={() => setDetail(row)}>
                              <Eye className="h-4 w-4" />
                            </Button>
                          </TableCell>
                        </TableRow>
                      ))
                    ) : (
                      <TableRow>
                        <TableCell colSpan={3} className={phsm.tableMuted}>暂无数据</TableCell>
                      </TableRow>
                    )}
                  </TableBody>
                </Table>
              </CardContent>
            </Card>
          </>
        ) : (
          <p className={cn("rounded-xl border border-emerald-100 bg-white/90 px-4 py-8 text-center", phsm.tableMuted)}>暂无数据</p>
        )}

        <Dialog open={!!detail} onOpenChange={() => setDetail(null)}>
          <DialogContent className={cn(phsm.dialog, "sm:max-w-lg")}>
            <DialogHeader>
              <DialogTitle className="text-teal-800">浏览统计详情</DialogTitle>
            </DialogHeader>
            {detail ? (
              <div className="space-y-2 text-sm text-teal-900">
                <p><span className="text-teal-700/75">疾病 ID：</span>{detail.illnessId}</p>
                <p><span className="text-teal-700/75">疾病名称：</span>{detail.illnessName || "-"}</p>
                <p><span className="text-teal-700/75">浏览量：</span>{detail.pageviews}</p>
              </div>
            ) : null}
          </DialogContent>
        </Dialog>
      </div>
    </div>
  );
}
