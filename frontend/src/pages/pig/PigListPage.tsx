import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { usePigStore } from '@/stores/pigStore';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
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
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Badge } from '@/components/ui/badge';
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
import { Plus, Search, Eye, Edit, Trash2 } from 'lucide-react';
import { HealthStatus } from '@/types/pig';

export function PigListPage() {
  const navigate = useNavigate();
  const {
    pigs,
    total,
    pageNum,
    pageSize,
    isLoading,
    filters,
    fetchPigs,
    setFilters,
    setPageNum,
    deletePig,
  } = usePigStore();

  const [searchKeyword, setSearchKeyword] = useState('');
  const [deleteTarget, setDeleteTarget] = useState<number | null>(null);

  // 初始化加载数据
  useEffect(() => {
    fetchPigs();
  }, [pageNum, pageSize, filters]);

  // 处理搜索
  const handleSearch = () => {
    setFilters({ earTagNumber: searchKeyword || undefined });
  };

  // 处理健康状态筛选
  const handleHealthStatusChange = (value: string) => {
    setFilters({ healthStatus: value === 'all' ? undefined : value });
  };

  // 处理删除
  const handleDelete = async () => {
    if (deleteTarget) {
      try {
        await deletePig(deleteTarget);
        setDeleteTarget(null);
      } catch (error) {
        // 错误已在store中处理
      }
    }
  };

  // 获取健康状态徽章样式
  const getHealthStatusBadge = (status: string) => {
    const variants: Record<string, 'default' | 'secondary' | 'destructive' | 'outline'> = {
      HEALTHY: 'default',
      SICK: 'destructive',
      RECOVERING: 'secondary',
      DEAD: 'outline',
    };
    return variants[status] || 'default';
  };

  return (
    <div className="container mx-auto py-6 space-y-6">
      {/* 页面标题和操作栏 */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-3xl font-bold">生猪管理</h1>
          <p className="text-muted-foreground mt-1">管理养殖场的生猪信息</p>
        </div>
        <Button onClick={() => navigate('/pig/create')}>
          <Plus className="mr-2 h-4 w-4" />
          新增生猪
        </Button>
      </div>

      {/* 筛选和搜索栏 */}
      <Card>
        <CardContent className="pt-6">
          <div className="flex gap-4">
            <div className="flex-1">
              <div className="flex gap-2">
                <Input
                  placeholder="搜索耳标号..."
                  value={searchKeyword}
                  onChange={(e) => setSearchKeyword(e.target.value)}
                  onKeyDown={(e) => e.key === 'Enter' && handleSearch()}
                  className="max-w-sm"
                />
                <Button onClick={handleSearch} variant="secondary">
                  <Search className="mr-2 h-4 w-4" />
                  搜索
                </Button>
              </div>
            </div>
            <Select
              value={filters.healthStatus || 'all'}
              onValueChange={handleHealthStatusChange}
            >
              <SelectTrigger className="w-[180px]">
                <SelectValue placeholder="健康状态" />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="all">全部状态</SelectItem>
                <SelectItem value="HEALTHY">健康</SelectItem>
                <SelectItem value="SICK">患病</SelectItem>
                <SelectItem value="RECOVERING">康复中</SelectItem>
                <SelectItem value="DEAD">死亡</SelectItem>
              </SelectContent>
            </Select>
          </div>
        </CardContent>
      </Card>

      {/* 生猪列表 */}
      <Card>
        <CardHeader>
          <CardTitle>生猪列表（共 {total} 头）</CardTitle>
        </CardHeader>
        <CardContent>
          {isLoading ? (
            <div className="flex items-center justify-center py-12">
              <div className="text-muted-foreground">加载中...</div>
            </div>
          ) : pigs.length === 0 ? (
            <div className="flex flex-col items-center justify-center py-12 text-center">
              <p className="text-muted-foreground">暂无数据</p>
              <Button
                variant="link"
                onClick={() => navigate('/pig/create')}
                className="mt-2"
              >
                立即添加生猪
              </Button>
            </div>
          ) : (
            <>
              <Table>
                <TableHeader>
                  <TableRow>
                    <TableHead>耳标号</TableHead>
                    <TableHead>品种</TableHead>
                    <TableHead>性别</TableHead>
                    <TableHead>出生日期</TableHead>
                    <TableHead>体重(kg)</TableHead>
                    <TableHead>健康状态</TableHead>
                    <TableHead>猪栏编号</TableHead>
                    <TableHead className="text-right">操作</TableHead>
                  </TableRow>
                </TableHeader>
                <TableBody>
                  {pigs.map((pig) => (
                    <TableRow key={pig.id}>
                      <TableCell className="font-medium">{pig.earTagNumber}</TableCell>
                      <TableCell>{pig.breed}</TableCell>
                      <TableCell>{pig.gender === 0 ? '母猪' : '公猪'}</TableCell>
                      <TableCell>{pig.birthDate}</TableCell>
                      <TableCell>{pig.weight || '-'}</TableCell>
                      <TableCell>
                        <Badge variant={getHealthStatusBadge(pig.healthStatus)}>
                          {pig.healthStatusName || HealthStatus[pig.healthStatus as keyof typeof HealthStatus]}
                        </Badge>
                      </TableCell>
                      <TableCell>{pig.penNumber || '-'}</TableCell>
                      <TableCell className="text-right">
                        <div className="flex justify-end gap-2">
                          <Button
                            variant="ghost"
                            size="sm"
                            onClick={() => navigate(`/pig/${pig.id}`)}
                          >
                            <Eye className="h-4 w-4" />
                          </Button>
                          <Button
                            variant="ghost"
                            size="sm"
                            onClick={() => navigate(`/pig/${pig.id}/edit`)}
                          >
                            <Edit className="h-4 w-4" />
                          </Button>
                          <Button
                            variant="ghost"
                            size="sm"
                            onClick={() => setDeleteTarget(pig.id)}
                          >
                            <Trash2 className="h-4 w-4 text-destructive" />
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
                  显示 {(pageNum - 1) * pageSize + 1} 到{' '}
                  {Math.min(pageNum * pageSize, total)} 条，共 {total} 条
                </div>
                <div className="flex gap-2">
                  <Button
                    variant="outline"
                    size="sm"
                    disabled={pageNum === 1}
                    onClick={() => setPageNum(pageNum - 1)}
                  >
                    上一页
                  </Button>
                  <Button
                    variant="outline"
                    size="sm"
                    disabled={pageNum * pageSize >= total}
                    onClick={() => setPageNum(pageNum + 1)}
                  >
                    下一页
                  </Button>
                </div>
              </div>
            </>
          )}
        </CardContent>
      </Card>

      {/* 删除确认对话框 */}
      <AlertDialog open={deleteTarget !== null} onOpenChange={() => setDeleteTarget(null)}>
        <AlertDialogContent>
          <AlertDialogHeader>
            <AlertDialogTitle>确认删除</AlertDialogTitle>
            <AlertDialogDescription>
              此操作将永久删除该生猪记录，是否继续？
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
