import React, { useEffect } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';
import { useCaseStore } from '@/stores/caseStore';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Badge } from '@/components/ui/badge';
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
import { CaseStatus } from '@/types/pig';

export function CaseListPage() {
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();
  const pigIdFromQuery = searchParams.get('pigId');

  const {
    cases,
    total,
    pageNum,
    pageSize,
    isLoading,
    filters,
    fetchCases,
    setFilters,
    setPageNum,
    setPageSize,
    deleteCase,
  } = useCaseStore();

  const [deleteId, setDeleteId] = React.useState<number | null>(null);

  useEffect(() => {
    if (pigIdFromQuery) {
      setFilters({ pigId: Number(pigIdFromQuery) });
    }
    fetchCases();
  }, [pageNum, pageSize, filters]);

  const handleDelete = async () => {
    if (deleteId) {
      try {
        await deleteCase(deleteId);
        setDeleteId(null);
      } catch (error) {
        // 错误已在store中处理
      }
    }
  };

  const getCaseStatusBadge = (status: string) => {
    const variants: Record<string, 'default' | 'secondary' | 'destructive' | 'outline'> = {
      PENDING: 'outline',
      IN_PROGRESS: 'secondary',
      COMPLETED: 'default',
      CLOSED: 'destructive',
    };
    return variants[status] || 'default';
  };

  const totalPages = Math.ceil(total / pageSize);

  return (
    <div className="container mx-auto py-6 space-y-6">
      {/* 页面标题 */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-3xl font-bold">病例管理</h1>
          <p className="text-muted-foreground mt-1">查看和管理生猪病例记录</p>
        </div>
        <Button onClick={() => navigate('/case/create')}>
          <Plus className="mr-2 h-4 w-4" />
          创建病例
        </Button>
      </div>

      {/* 筛选和搜索 */}
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
            <div>
              <label className="text-sm font-medium mb-2 block">病例状态</label>
              <Select
                value={filters.status || 'all'}
                onValueChange={(value) =>
                  setFilters({ status: value === 'all' ? undefined : value })
                }
              >
                <SelectTrigger>
                  <SelectValue placeholder="选择状态" />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="all">全部</SelectItem>
                  <SelectItem value="PENDING">待处理</SelectItem>
                  <SelectItem value="IN_PROGRESS">处理中</SelectItem>
                  <SelectItem value="COMPLETED">已完成</SelectItem>
                  <SelectItem value="CLOSED">已关闭</SelectItem>
                </SelectContent>
              </Select>
            </div>
            <div className="flex items-end">
              <Button
                variant="outline"
                onClick={() => {
                  setFilters({});
                  fetchCases();
                }}
              >
                重置筛选
              </Button>
            </div>
          </div>
        </CardContent>
      </Card>

      {/* 病例列表 */}
      <Card>
        <CardHeader>
          <CardTitle>病例列表（共 {total} 条）</CardTitle>
        </CardHeader>
        <CardContent>
          {isLoading ? (
            <div className="flex items-center justify-center py-8">
              <div className="text-muted-foreground">加载中...</div>
            </div>
          ) : cases.length === 0 ? (
            <div className="flex flex-col items-center justify-center py-8">
              <p className="text-muted-foreground">暂无病例记录</p>
              <Button onClick={() => navigate('/case/create')} className="mt-4">
                创建第一个病例
              </Button>
            </div>
          ) : (
            <>
              <Table>
                <TableHeader>
                  <TableRow>
                    <TableHead>病例ID</TableHead>
                    <TableHead>生猪耳标号</TableHead>
                    <TableHead>症状描述</TableHead>
                    <TableHead>诊断结果</TableHead>
                    <TableHead>状态</TableHead>
                    <TableHead>创建时间</TableHead>
                    <TableHead>操作</TableHead>
                  </TableRow>
                </TableHeader>
                <TableBody>
                  {cases.map((caseItem) => (
                    <TableRow key={caseItem.id}>
                      <TableCell>{caseItem.id}</TableCell>
                      <TableCell>{caseItem.pigEarTagNumber || '-'}</TableCell>
                      <TableCell className="max-w-xs truncate">
                        {caseItem.symptomDescription}
                      </TableCell>
                      <TableCell className="max-w-xs truncate">
                        {caseItem.diagnosisResult || '-'}
                      </TableCell>
                      <TableCell>
                        <Badge variant={getCaseStatusBadge(caseItem.status)}>
                          {caseItem.statusName || CaseStatus[caseItem.status as keyof typeof CaseStatus]}
                        </Badge>
                      </TableCell>
                      <TableCell>{caseItem.createTime}</TableCell>
                      <TableCell>
                        <div className="flex gap-2">
                          <Button
                            variant="ghost"
                            size="sm"
                            onClick={() => navigate(`/case/${caseItem.id}`)}
                          >
                            <Eye className="h-4 w-4" />
                          </Button>
                          <Button
                            variant="ghost"
                            size="sm"
                            onClick={() => setDeleteId(caseItem.id)}
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
              此操作将永久删除该病例记录，是否继续？
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
