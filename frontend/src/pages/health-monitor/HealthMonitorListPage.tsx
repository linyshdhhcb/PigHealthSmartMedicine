import React, { useEffect } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';
import { useHealthMonitorStore } from '@/stores/healthMonitorStore';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from '@/components/ui/table';
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '@/components/ui/select';
import {
  AlertDialog,
  AlertDialogAction,
  AlertDialogCancel,
  AlertDialogContent,
  AlertDialogDescription,
  AlertDialogFooter,
  AlertDialogHeader,
  AlertDialogTitle,
} from '@/components/ui/alert-dialog';
import { Plus, Eye, Trash2 } from 'lucide-react';

export function HealthMonitorListPage() {
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();
  const pigIdFromQuery = searchParams.get('pigId');

  const {
    healthMonitors,
    total,
    pageNum,
    pageSize,
    isLoading,
    filters,
    fetchHealthMonitors,
    setFilters,
    setPageNum,
    setPageSize,
    deleteHealthMonitor,
  } = useHealthMonitorStore();

  const [deleteId, setDeleteId] = React.useState<number | null>(null);

  useEffect(() => {
    if (pigIdFromQuery) {
      setFilters({ pigId: Number(pigIdFromQuery) });
    }
    fetchHealthMonitors();
  }, [pageNum, pageSize, filters]);

  const handleDelete = async () => {
    if (deleteId) {
      try {
        await deleteHealthMonitor(deleteId);
        setDeleteId(null);
      } catch (error) {
        // 错误已在store中处理
      }
    }
  };

  const totalPages = Math.ceil(total / pageSize);

  return (
    <div className="container mx-auto py-6 space-y-6">
      {/* 页面标题 */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-3xl font-bold">健康监测</h1>
          <p className="text-muted-foreground mt-1">查看和管理生猪健康监测记录</p>
        </div>
        <Button onClick={() => navigate('/health-monitor/create')}>
          <Plus className="mr-2 h-4 w-4" />
          添加监测记录
        </Button>
      </div>

      {/* 筛选 */}
      <Card>
        <CardHeader>
          <CardTitle>筛选条件</CardTitle>
        </CardHeader>
        <CardContent>
          <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
            <div>
              <label className="text-sm font-medium mb-2 block">生猪ID</label>
              <Input
                type="number"
                placeholder="输入生猪ID"
                value={filters.pigId || ''}
                onChange={(e) =>
                  setFilters({ pigId: e.target.value ? Number(e.target.value) : undefined })
                }
              />
            </div>
            <div className="flex items-end">
              <Button
                variant="outline"
                onClick={() => {
                  setFilters({});
                  fetchHealthMonitors();
                }}
              >
                重置筛选
              </Button>
            </div>
          </div>
        </CardContent>
      </Card>

      {/* 健康监测列表 */}
      <Card>
        <CardHeader>
          <CardTitle>监测记录列表（共 {total} 条）</CardTitle>
        </CardHeader>
        <CardContent>
          {isLoading ? (
            <div className="flex items-center justify-center py-8">
              <div className="text-muted-foreground">加载中...</div>
            </div>
          ) : healthMonitors.length === 0 ? (
            <div className="flex flex-col items-center justify-center py-8">
              <p className="text-muted-foreground">暂无监测记录</p>
              <Button onClick={() => navigate('/health-monitor/create')} className="mt-4">
                添加第一条监测记录
              </Button>
            </div>
          ) : (
            <>
              <Table>
                <TableHeader>
                  <TableRow>
                    <TableHead>记录ID</TableHead>
                    <TableHead>生猪耳标号</TableHead>
                    <TableHead>监测日期</TableHead>
                    <TableHead>体温(℃)</TableHead>
                    <TableHead>体重(kg)</TableHead>
                    <TableHead>食欲</TableHead>
                    <TableHead>精神状态</TableHead>
                    <TableHead>操作</TableHead>
                  </TableRow>
                </TableHeader>
                <TableBody>
                  {healthMonitors.map((monitor) => (
                    <TableRow key={monitor.id}>
                      <TableCell>{monitor.id}</TableCell>
                      <TableCell>{monitor.pigEarTagNumber || '-'}</TableCell>
                      <TableCell>{monitor.monitorDate}</TableCell>
                      <TableCell>{monitor.temperature || '-'}</TableCell>
                      <TableCell>{monitor.weight || '-'}</TableCell>
                      <TableCell>{monitor.appetite || '-'}</TableCell>
                      <TableCell>{monitor.mentalState || '-'}</TableCell>
                      <TableCell>
                        <div className="flex gap-2">
                          <Button
                            variant="ghost"
                            size="sm"
                            onClick={() => navigate(`/pig/${monitor.pigId}`)}
                          >
                            <Eye className="h-4 w-4" />
                          </Button>
                          <Button
                            variant="ghost"
                            size="sm"
                            onClick={() => setDeleteId(monitor.id)}
                          >
                            <Trash2 className="h-4 w-4" />
                          </Button>
                        </div>
                      </TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>

              {/* 分页 */}
              <div className="flex items-center justify-between mt-4">
                <div className="text-sm text-muted-foreground">
                  显示第 {(pageNum - 1) * pageSize + 1} 到{' '}
                  {Math.min(pageNum * pageSize, total)} 条，共 {total} 条
                </div>
                <div className="flex items-center gap-2">
                  <Button
                    variant="outline"
                    size="sm"
                    onClick={() => setPageNum(pageNum - 1)}
                    disabled={pageNum === 1}
                  >
                    上一页
                  </Button>
                  <div className="text-sm">
                    第 {pageNum} / {totalPages} 页
                  </div>
                  <Button
                    variant="outline"
                    size="sm"
                    onClick={() => setPageNum(pageNum + 1)}
                    disabled={pageNum >= totalPages}
                  >
                    下一页
                  </Button>
                  <Select
                    value={String(pageSize)}
                    onValueChange={(value) => setPageSize(Number(value))}
                  >
                    <SelectTrigger className="w-24">
                      <SelectValue />
                    </SelectTrigger>
                    <SelectContent>
                      <SelectItem value="10">10条/页</SelectItem>
                      <SelectItem value="20">20条/页</SelectItem>
                      <SelectItem value="50">50条/页</SelectItem>
                    </SelectContent>
                  </Select>
                </div>
              </div>
            </>
          )}
        </CardContent>
      </Card>

      {/* 删除确认对话框 */}
      <AlertDialog open={!!deleteId} onOpenChange={() => setDeleteId(null)}>
        <AlertDialogContent>
          <AlertDialogHeader>
            <AlertDialogTitle>确认删除</AlertDialogTitle>
            <AlertDialogDescription>
              此操作将永久删除该监测记录，是否继续？
            </AlertDialogDescription>
          </AlertDialogHeader>
          <AlertDialogFooter>
            <AlertDialogCancel>取消</AlertDialogCancel>
            <AlertDialogAction onClick={handleDelete}>确认删除</AlertDialogAction>
          </AlertDialogFooter>
        </AlertDialogContent>
      </AlertDialog>
    </div>
  );
}
