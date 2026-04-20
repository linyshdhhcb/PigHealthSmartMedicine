import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardContent } from "@/components/ui/card";
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table";
import { ChevronRight, Eye } from "lucide-react";
import type { RagTraceRun } from "@/services/ragTraceService";
import {
  formatDateTime,
  formatDuration,
  statusBadgeVariant,
  statusLabel
} from "@/pages/admin/traces/traceUtils";

interface RunsTableProps {
  runs: RagTraceRun[];
  loading: boolean;
  current: number;
  pages: number;
  total: number;
  onOpenRun: (traceId: string) => void;
  onPrevPage: () => void;
  onNextPage: () => void;
}

const getStatusBadgeClass = (status?: string | null) => {
  const normalized = status?.toLowerCase();
  if (normalized === "success") return "bg-gradient-to-r from-emerald-500 to-teal-500 text-white border-0 shadow-sm";
  if (normalized === "failed") return "bg-gradient-to-r from-red-500 to-rose-500 text-white border-0 shadow-sm";
  if (normalized === "running") return "bg-gradient-to-r from-amber-500 to-orange-500 text-white border-0 shadow-sm";
  return "bg-slate-100 text-slate-600 border-slate-200";
};

export function RunsTable({
  runs,
  loading,
  current,
  pages,
  total,
  onOpenRun,
  onPrevPage,
  onNextPage
}: RunsTableProps) {
  return (
    <Card className="border-emerald-100 shadow-sm overflow-hidden rounded-2xl">
      <div className="bg-gradient-to-r from-emerald-50 to-teal-50 px-6 py-5 border-b border-emerald-100">
        <h2 className="text-lg font-semibold text-emerald-800">运行列表</h2>
        <p className="text-sm text-emerald-600 mt-0.5">按时间倒序查看运行记录，通过操作按钮进入独立详情页</p>
      </div>
      <CardContent className="p-0">
        {loading ? (
          <div className="py-12 text-center">
            <div className="inline-flex items-center gap-2 text-emerald-600">
              <svg className="animate-spin h-5 w-5" fill="none" viewBox="0 0 24 24">
                <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4" />
                <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z" />
              </svg>
              加载中...
            </div>
          </div>
        ) : runs.length === 0 ? (
          <div className="py-12 text-center text-muted-foreground">
            <svg className="mx-auto h-12 w-12 text-emerald-300 mb-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={1.5} d="M9 17v-2m3 2v-4m3 4v-6m2 10H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
            </svg>
            暂无链路数据
          </div>
        ) : (
          <div className="overflow-x-auto">
            <Table className="min-w-[960px]">
              <TableHeader>
                <TableRow className="bg-gradient-to-r from-emerald-50 to-teal-50 border-b border-emerald-100">
                  <TableHead className="trace-col-trace font-semibold text-emerald-700 py-4">Trace Name</TableHead>
                  <TableHead className="trace-col-run-id font-semibold text-emerald-700">Trace Id</TableHead>
                  <TableHead className="trace-col-meta font-semibold text-emerald-700">会话ID / TaskID</TableHead>
                  <TableHead className="trace-col-user font-semibold text-emerald-700">用户名</TableHead>
                  <TableHead className="trace-col-duration font-semibold text-emerald-700">耗时</TableHead>
                  <TableHead className="trace-col-status font-semibold text-emerald-700">状态</TableHead>
                  <TableHead className="font-semibold text-emerald-700">执行时间</TableHead>
                  <TableHead className="trace-col-action font-semibold text-emerald-700">操作</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {runs.map((run, index) => (
                  <TableRow key={run.traceId} className={`${index % 2 === 0 ? 'bg-white' : 'bg-gradient-to-r from-emerald-50/20 to-teal-50/20'} hover:bg-emerald-50/60 transition-colors duration-200 border-b border-emerald-50/50`}>
                    <TableCell className="trace-col-trace py-4">
                      <div className="trace-list-run-trace">
                        <p className="trace-list-run-name line-clamp-1 font-medium text-slate-800" title={run.traceName || "-"}>
                          {run.traceName || "-"}
                        </p>
                      </div>
                    </TableCell>
                    <TableCell className="trace-col-run-id">
                      <span className="trace-list-run-id font-mono text-xs text-emerald-700 bg-emerald-50 px-2 py-1 rounded" title={run.traceId}>
                        {run.traceId}
                      </span>
                    </TableCell>
                    <TableCell className="trace-col-meta">
                      <p className="trace-list-run-meta-line text-slate-600" title={`会话ID: ${run.conversationId || "-"}`}>
                        {run.conversationId || "-"}
                      </p>
                      <p className="trace-list-run-meta-line is-secondary text-slate-400" title={`TaskID: ${run.taskId || "-"}`}>
                        {run.taskId || "-"}
                      </p>
                    </TableCell>
                    <TableCell className="trace-col-user">
                      <span
                        className="trace-list-user-name text-slate-600"
                        title={run.userName || run.username || run.userId || "-"}
                      >
                        {run.userName || run.username || run.userId || "-"}
                      </span>
                    </TableCell>
                    <TableCell className="trace-col-duration trace-list-duration-cell">
                      <span className="text-slate-700 font-medium">{formatDuration(run.durationMs ?? undefined)}</span>
                    </TableCell>
                    <TableCell className="trace-col-status trace-list-status-cell">
                      <Badge className={getStatusBadgeClass(run.status)} variant={statusBadgeVariant(run.status)}>
                        {statusLabel(run.status)}
                      </Badge>
                    </TableCell>
                    <TableCell className="text-slate-500">{formatDateTime(run.startTime ?? undefined)}</TableCell>
                    <TableCell className="trace-col-action trace-list-action-cell">
                      <Button
                        size="sm"
                        variant="outline"
                        className="trace-list-action-btn h-8 px-3 border-emerald-200 text-emerald-700 hover:bg-emerald-50 hover:border-emerald-300 hover:text-emerald-800 rounded-lg transition-all duration-200"
                        onClick={() => onOpenRun(run.traceId)}
                      >
                        <Eye className="h-3.5 w-3.5" />
                        查看链路
                        <ChevronRight className="h-3.5 w-3.5" />
                      </Button>
                    </TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </div>
        )}
        <div className="border-t border-emerald-100 px-6 py-4 flex flex-wrap items-center justify-between gap-3 bg-gradient-to-r from-emerald-50/50 to-teal-50/50">
          <span className="text-sm text-slate-600">
            第 <span className="font-semibold text-emerald-700">{current}</span> / <span className="font-semibold">{pages}</span> 页，共 <span className="font-semibold text-emerald-700">{total.toLocaleString("zh-CN")}</span> 条
          </span>
          <div className="flex items-center gap-2">
            <Button
              className="trace-list-pagination-btn h-8 px-4 border-emerald-200 text-emerald-700 hover:bg-emerald-50 rounded-lg transition-all duration-200"
              variant="outline"
              disabled={current <= 1 || loading}
              onClick={onPrevPage}
            >
              上一页
            </Button>
            <Button
              className="trace-list-pagination-btn h-8 px-4 border-emerald-200 text-emerald-700 hover:bg-emerald-50 rounded-lg transition-all duration-200"
              variant="outline"
              disabled={current >= pages || loading}
              onClick={onNextPage}
            >
              下一页
            </Button>
          </div>
        </div>
      </CardContent>
    </Card>
  );
}
