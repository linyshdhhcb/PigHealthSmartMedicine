import React, { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useFarmStore } from '@/stores/farmStore';
import { Button } from '@/components/ui/button';
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
import { Plus, Eye, Edit, Trash2 } from 'lucide-react';
import { FarmScale } from '@/types/pig';

export function FarmListPage() {
  const navigate = useNavigate();

  const {
    farms,
    total,
    pageNum,
    pageSize,
    isLoading,
    filters,
    fetchFarms,
    setFilters,
    setPageNum,
    setPageSize,
    deleteFarm,
  } = useFarmStore();

  const [deleteId, setDeleteId] = React.useState<number | null>(null);

  useEffect(() => {
    fetchFarms();
  }, [pageNum, pageSize, filters]);

  const handleDelete = async () => {
    if (deleteId) {
      try {
        await deleteFarm(deleteId);
        setDeleteId(null);
      } catch (error) {
        // 错误已在store中处理
      }
    }
  };

  const getFarmScaleBadge = (scale: string) => {
    const variants: Record<string, 'default' | 'secondary' | 'destructive' | 'outline'> = {
      SMALL: 'outline',
      MEDIUM: 'secondary',
      LARGE: 'default',
    };
    return variants[scale] || 'default';
  };

  const totalPages = Math.ceil(total / pageSize);

  return (
    <div className="container mx-auto py-6 space-y-6">
      {/* 页面标题 */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-3xl font-bold">养殖场管理</h1>
          <p className="text-muted-foreground mt-1">管理养殖场信息</p>
        </div>
        <Button onClick={() => navigate('/farm/create')}>
          <Plus className="mr-2 h-4 w-4" />
          新增养殖场
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
              <label className="text-sm font-medium mb-2 block">养殖规模</label>
              <Select
                value={filters.scale || 'all'}
                onValueChange={(value) =>
                  setFilters({ scale: value === 'all' ? undefined : value })
                }
              >
                <SelectTrigger>
                  <SelectValue placeholder="选择规模" />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="all">全部</SelectItem>
                  <SelectItem value="SMALL">小型（500头以下）</SelectItem>
                  <SelectItem value="MEDIUM">中型（500-5000头）</SelectItem>
                  <SelectItem value="LARGE">大型（5000头以上）</SelectItem>
                </SelectContent>
              </Select>
            </div>
            <div className="flex items-end">
              <Button
                variant="outline"
                onClick={() => {
                  setFilters({});
                  fetchFarms();
                }}
              >
                重置筛选
              </Button>
            </div>
          </div>
        </CardContent>
      </Card>

      {/* 养殖场列表 */}
      <Card>
        <CardHeader>
          <CardTitle>养殖场列表（共 {total} 条）</CardTitle>
        </CardHeader>
        <CardContent>
          {isLoading ? (
            <div className="flex items-center justify-center py-8">
              <div className="text-muted-foreground">加载中...</div>
            </div>
          ) : farms.length === 0 ? (
            <div className="flex flex-col items-center justify-center py-8">
              <p className="text-muted-foreground">暂无养殖场记录</p>
              <Button onClick={() => navigate('/farm/create')} className="mt-4">
                创建第一个养殖场
              </Button>
            </div>
          ) : (
            <>
              <Table>
                <TableHeader>
                  <TableRow>
                    <TableHead>养殖场名称</TableHead>
                    <TableHead>规模</TableHead>
                    <TableHead>地址</TableHead>
                    <TableHead>联系电话</TableHead>
                    <TableHead>生猪总数</TableHead>
                    <TableHead>负责人</TableHead>
                    <TableHead>操作</TableHead>
                  </TableRow>
                </TableHeader>
                <TableBody>
                  {farms.map((farm) => (
                    <TableRow key={farm.id}>
                      <TableCell className="font-medium">{farm.name}</TableCell>
                      <TableCell>
                        <Badge variant={getFarmScaleBadge(farm.scale)}>
                          {farm.scaleName || FarmScale[farm.scale as keyof typeof FarmScale]}
                        </Badge>
                      </TableCell>
                      <TableCell className="max-w-xs truncate">{farm.address || '-'}</TableCell>
                      <TableCell>{farm.contactPhone || '-'}</TableCell>
                      <TableCell>{farm.totalPigs || 0}</TableCell>
                      <TableCell>{farm.ownerName || '-'}</TableCell>
                      <TableCell>
                        <div className="flex gap-2">
                          <Button
                            variant="ghost"
                            size="sm"
                            onClick={() => navigate(`/farm/${farm.id}`)}
                          >
                            <Eye className="h-4 w-4" />
                          </Button>
                          <Button
                            variant="ghost"
                            size="sm"
                            onClick={() => navigate(`/farm/${farm.id}/edit`)}
                          >
                            <Edit className="h-4 w-4" />
                          </Button>
                          <Button
                            variant="ghost"
                            size="sm"
                            onClick={() => setDeleteId(farm.id)}
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
              此操作将永久删除该养殖场记录，是否继续？
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
