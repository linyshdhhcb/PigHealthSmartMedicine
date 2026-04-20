import type { ReactNode } from "react";
import { useEffect, useState } from "react";
import { toast } from "sonner";

import { Badge } from "@/components/ui/badge";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table";
import type { SystemSettings } from "@/services/settingsService";
import { getSystemSettings } from "@/services/settingsService";
import { getErrorMessage } from "@/utils/error";

const BoolBadge = ({ value }: { value: boolean }) => (
  <Badge variant={value ? "default" : "outline"} className={value ? "bg-gradient-to-r from-emerald-500 to-teal-500 text-white border-0" : "bg-slate-100 text-slate-600 border-slate-200"}>{value ? "启用" : "禁用"}</Badge>
);

function InfoItem({ label, value }: { label: string; value: ReactNode }) {
  return (
    <div className="flex flex-col gap-1.5 rounded-xl border border-emerald-100 bg-gradient-to-br from-white to-emerald-50/30 px-4 py-3.5 hover:shadow-md hover:shadow-emerald-500/5 transition-all duration-200">
      <span className="text-xs text-emerald-600 font-medium">{label}</span>
      <div className="text-sm font-semibold text-slate-800">{value}</div>
    </div>
  );
}

export function SystemSettingsPage() {
  const [settings, setSettings] = useState<SystemSettings | null>(null);
  const [loading, setLoading] = useState(true);

  const loadSettings = async () => {
    try {
      setLoading(true);
      const data = await getSystemSettings();
      setSettings(data);
    } catch (error) {
      toast.error(getErrorMessage(error, "加载系统配置失败"));
      console.error(error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadSettings();
  }, []);

  if (loading) {
    return (
      <div className="admin-page">
        <div className="text-sm text-muted-foreground">加载中...</div>
      </div>
    );
  }

  if (!settings) {
    return (
      <div className="admin-page">
        <div className="text-sm text-muted-foreground">暂无可展示的配置</div>
      </div>
    );
  }

  const { rag, ai } = settings;
  const providers = Object.entries(ai.providers || {});

  return (
    <div className="admin-page">
      <div className="admin-page-header">
        <div>
          <h1 className="admin-page-title">系统配置</h1>
          <p className="admin-page-subtitle">只读展示当前 application 配置</p>
        </div>
      </div>

      <Card className="border-emerald-100 shadow-sm overflow-hidden rounded-2xl mb-5">
        <CardHeader className="bg-gradient-to-r from-emerald-50 to-teal-50 border-b border-emerald-100 pb-4">
          <CardTitle className="text-emerald-800">RAG 默认配置</CardTitle>
          <CardDescription>向量空间与检索基础参数</CardDescription>
        </CardHeader>
        <CardContent className="grid gap-4 md:grid-cols-3 p-5">
          <InfoItem label="Collection" value={rag.default.collectionName} />
          <InfoItem label="Dimension" value={rag.default.dimension} />
          <InfoItem label="Metric Type" value={rag.default.metricType} />
        </CardContent>
      </Card>

      <Card className="border-emerald-100 shadow-sm overflow-hidden rounded-2xl mb-5">
        <CardHeader className="bg-gradient-to-r from-teal-50 to-emerald-50 border-b border-emerald-100 pb-4">
          <CardTitle className="text-emerald-800">查询改写</CardTitle>
          <CardDescription>历史上下文压缩与改写策略</CardDescription>
        </CardHeader>
        <CardContent className="grid gap-4 md:grid-cols-3 p-5">
          <InfoItem label="Enabled" value={<BoolBadge value={rag.queryRewrite.enabled} />} />
          <InfoItem label="Max History Messages" value={rag.queryRewrite.maxHistoryMessages} />
          <InfoItem label="Max History Chars" value={rag.queryRewrite.maxHistoryChars} />
        </CardContent>
      </Card>

      <Card className="border-emerald-100 shadow-sm overflow-hidden rounded-2xl mb-5">
        <CardHeader className="bg-gradient-to-r from-emerald-50 to-teal-50 border-b border-emerald-100 pb-4">
          <CardTitle className="text-emerald-800">全局限流</CardTitle>
          <CardDescription>并发与租约控制</CardDescription>
        </CardHeader>
        <CardContent className="grid gap-4 md:grid-cols-3 p-5">
          <InfoItem label="Enabled" value={<BoolBadge value={rag.rateLimit.global.enabled} />} />
          <InfoItem label="Max Concurrent" value={rag.rateLimit.global.maxConcurrent} />
          <InfoItem label="Max Wait Seconds" value={rag.rateLimit.global.maxWaitSeconds} />
          <InfoItem label="Lease Seconds" value={rag.rateLimit.global.leaseSeconds} />
          <InfoItem label="Poll Interval (ms)" value={rag.rateLimit.global.pollIntervalMs} />
        </CardContent>
      </Card>

      <Card className="border-emerald-100 shadow-sm overflow-hidden rounded-2xl mb-5">
        <CardHeader className="bg-gradient-to-r from-teal-50 to-emerald-50 border-b border-emerald-100 pb-4">
          <CardTitle className="text-emerald-800">记忆管理</CardTitle>
          <CardDescription>摘要与上下文保留策略</CardDescription>
        </CardHeader>
        <CardContent className="grid gap-4 md:grid-cols-3 p-5">
          <InfoItem label="History Keep Turns" value={rag.memory.historyKeepTurns} />
          <InfoItem label="Summary Start Turns" value={rag.memory.summaryStartTurns} />
          <InfoItem label="Summary Enabled" value={<BoolBadge value={rag.memory.summaryEnabled} />} />
          <InfoItem label="TTL Minutes" value={rag.memory.ttlMinutes} />
          <InfoItem label="Summary Max Chars" value={rag.memory.summaryMaxChars} />
          <InfoItem label="Title Max Length" value={rag.memory.titleMaxLength} />
        </CardContent>
      </Card>

      <Card className="border-emerald-100 shadow-sm overflow-hidden rounded-2xl mb-5">
        <CardHeader className="bg-gradient-to-r from-emerald-50 to-teal-50 border-b border-emerald-100 pb-4">
          <CardTitle className="text-emerald-800">模型服务提供方</CardTitle>
          <CardDescription>接入地址与端点配置</CardDescription>
        </CardHeader>
        <CardContent className="p-5">
          <Table className="min-w-[760px]">
            <TableHeader>
              <TableRow className="bg-gradient-to-r from-emerald-50 to-teal-50 border-b border-emerald-200">
                <TableHead className="w-[140px] font-semibold text-emerald-700">Provider</TableHead>
                <TableHead className="w-[240px] font-semibold text-emerald-700">URL</TableHead>
                <TableHead className="w-[200px] font-semibold text-emerald-700">API Key</TableHead>
                <TableHead className="font-semibold text-emerald-700">Endpoints</TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {providers.map(([name, provider]) => (
                <TableRow key={name} className="hover:bg-emerald-50/50 transition-colors border-b border-emerald-50">
                  <TableCell className="font-medium text-slate-800">{name}</TableCell>
                  <TableCell className="text-slate-600">{provider.url}</TableCell>
                  <TableCell className="text-slate-600">{provider.apiKey ? provider.apiKey : "-"}</TableCell>
                  <TableCell>
                    <div className="space-y-1 text-xs text-slate-500">
                      {Object.entries(provider.endpoints).map(([key, value]) => (
                        <div key={key}>
                          {key}: {value}
                        </div>
                      ))}
                    </div>
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </CardContent>
      </Card>

      <Card className="border-emerald-100 shadow-sm overflow-hidden rounded-2xl mb-5">
        <CardHeader className="bg-gradient-to-r from-teal-50 to-emerald-50 border-b border-emerald-100 pb-4">
          <CardTitle className="text-emerald-800">模型选择策略</CardTitle>
          <CardDescription>熔断与选择阈值</CardDescription>
        </CardHeader>
        <CardContent className="grid gap-4 md:grid-cols-2 p-5">
          <InfoItem label="Failure Threshold" value={ai.selection.failureThreshold} />
          <InfoItem label="Open Duration (ms)" value={ai.selection.openDurationMs} />
        </CardContent>
      </Card>

      <Card className="border-emerald-100 shadow-sm overflow-hidden rounded-2xl mb-5">
        <CardHeader className="bg-gradient-to-r from-emerald-50 to-teal-50 border-b border-emerald-100 pb-4">
          <CardTitle className="text-emerald-800">流式响应</CardTitle>
          <CardDescription>输出分片大小</CardDescription>
        </CardHeader>
        <CardContent className="grid gap-4 md:grid-cols-2 p-5">
          <InfoItem label="Message Chunk Size" value={ai.stream.messageChunkSize} />
        </CardContent>
      </Card>

      <Card className="border-emerald-100 shadow-sm overflow-hidden rounded-2xl mb-5">
        <CardHeader className="bg-gradient-to-r from-teal-50 to-emerald-50 border-b border-emerald-100 pb-4">
          <CardTitle className="text-emerald-800">Chat 模型配置</CardTitle>
          <CardDescription>默认模型与候选列表</CardDescription>
        </CardHeader>
        <CardContent className="space-y-4 p-5">
          <div className="grid gap-4 md:grid-cols-2">
            <InfoItem label="Default Model" value={ai.chat.defaultModel} />
            <InfoItem label="Deep Thinking Model" value={ai.chat.deepThinkingModel} />
          </div>
          <Table className="min-w-[720px] border border-emerald-100 rounded-xl overflow-hidden">
            <TableHeader>
              <TableRow className="bg-gradient-to-r from-emerald-50 to-teal-50 border-b border-emerald-200">
                <TableHead className="w-[220px] font-semibold text-emerald-700">ID</TableHead>
                <TableHead className="w-[120px] font-semibold text-emerald-700">Provider</TableHead>
                <TableHead className="w-[200px] font-semibold text-emerald-700">Model</TableHead>
                <TableHead className="w-[100px] font-semibold text-emerald-700">Thinking</TableHead>
                <TableHead className="w-[90px] font-semibold text-emerald-700">Priority</TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {ai.chat.candidates.map((item) => (
                <TableRow key={item.id} className="hover:bg-emerald-50/50 transition-colors border-b border-emerald-50">
                  <TableCell className="font-medium text-slate-800">{item.id}</TableCell>
                  <TableCell className="text-slate-600">{item.provider}</TableCell>
                  <TableCell className="text-slate-600">{item.model}</TableCell>
                  <TableCell className="text-slate-600">{item.supportsThinking ? "支持" : "-"}</TableCell>
                  <TableCell className="text-slate-600">{item.priority}</TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </CardContent>
      </Card>

      <Card className="border-emerald-100 shadow-sm overflow-hidden rounded-2xl mb-5">
        <CardHeader className="bg-gradient-to-r from-emerald-50 to-teal-50 border-b border-emerald-100 pb-4">
          <CardTitle className="text-emerald-800">Embedding 模型配置</CardTitle>
          <CardDescription>向量化模型列表</CardDescription>
        </CardHeader>
        <CardContent className="space-y-4 p-5">
          <div className="grid gap-4 md:grid-cols-2">
            <InfoItem label="Default Model" value={ai.embedding.defaultModel} />
          </div>
          <Table className="min-w-[720px] border border-emerald-100 rounded-xl overflow-hidden">
            <TableHeader>
              <TableRow className="bg-gradient-to-r from-emerald-50 to-teal-50 border-b border-emerald-200">
                <TableHead className="w-[220px] font-semibold text-emerald-700">ID</TableHead>
                <TableHead className="w-[120px] font-semibold text-emerald-700">Provider</TableHead>
                <TableHead className="w-[200px] font-semibold text-emerald-700">Model</TableHead>
                <TableHead className="w-[110px] font-semibold text-emerald-700">Dimension</TableHead>
                <TableHead className="w-[90px] font-semibold text-emerald-700">Priority</TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {ai.embedding.candidates.map((item) => (
                <TableRow key={item.id} className="hover:bg-emerald-50/50 transition-colors border-b border-emerald-50">
                  <TableCell className="font-medium text-slate-800">{item.id}</TableCell>
                  <TableCell className="text-slate-600">{item.provider}</TableCell>
                  <TableCell className="text-slate-600">{item.model}</TableCell>
                  <TableCell className="text-slate-600">{item.dimension}</TableCell>
                  <TableCell className="text-slate-600">{item.priority}</TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </CardContent>
      </Card>

      <Card className="border-emerald-100 shadow-sm overflow-hidden rounded-2xl">
        <CardHeader className="bg-gradient-to-r from-teal-50 to-emerald-50 border-b border-emerald-100 pb-4">
          <CardTitle className="text-emerald-800">Rerank 模型配置</CardTitle>
          <CardDescription>重排模型列表</CardDescription>
        </CardHeader>
        <CardContent className="space-y-4 p-5">
          <div className="grid gap-4 md:grid-cols-2">
            <InfoItem label="Default Model" value={ai.rerank.defaultModel} />
          </div>
          <Table className="min-w-[640px] border border-emerald-100 rounded-xl overflow-hidden">
            <TableHeader>
              <TableRow className="bg-gradient-to-r from-teal-50 to-emerald-50 border-b border-emerald-200">
                <TableHead className="w-[220px] font-semibold text-emerald-700">ID</TableHead>
                <TableHead className="w-[120px] font-semibold text-emerald-700">Provider</TableHead>
                <TableHead className="w-[200px] font-semibold text-emerald-700">Model</TableHead>
                <TableHead className="w-[90px] font-semibold text-emerald-700">Priority</TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {ai.rerank.candidates.map((item) => (
                <TableRow key={item.id} className="hover:bg-emerald-50/50 transition-colors border-b border-emerald-50">
                  <TableCell className="font-medium text-slate-800">{item.id}</TableCell>
                  <TableCell className="text-slate-600">{item.provider}</TableCell>
                  <TableCell className="text-slate-600">{item.model}</TableCell>
                  <TableCell className="text-slate-600">{item.priority}</TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </CardContent>
      </Card>
    </div>
  );
}
