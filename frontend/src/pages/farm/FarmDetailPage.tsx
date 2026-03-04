import React, { useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { useFarmStore } from '@/stores/farmStore';
import { Button } from '@/components/ui/button';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Badge } from '@/components/ui/badge';
import { ArrowLeft, Edit, Trash2 } from 'lucide-react';
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
import { FarmScale } from '@/types/pig';

export function FarmDetailPage() {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const { currentFarm, isLoading, fetchFarmById, deleteFarm } = useFarmStore();
  const [showDeleteDialog, setShowDeleteDialog] = React.useState(false);

  useEffect(() => {
    if (id) {
      fetchFarmById(Number(id));
    }
  }, [id]);

  const handleDelete = async () => {
    if (id) {
      try {
        await deleteFarm(Number(id));
        navigate('/farm');
      } catch (error) {
        // 错误已在store中处理
      }
    }
  };

  if (isLoading) {
    return (
      <div className="container mx-auto py-6">
        <div className="flex items-center justify-center py-12">
          <div className="text-muted-foreground">加载中...</div>
        </div>
      </div>
    );
  }

  if (!currentFarm) {
    return (
      <div className="container mx-auto py-6">
        <div className="flex flex-col items-center justify-center py-12">
          <p className="text-muted-foreground">养殖场不存在</p>
          <Button onClick={() => navigate('/farm')} className="mt-4">
            返回列表
          </Button>
        </div>
      </div>
    );
  }

  return (
    <div className="container mx-auto py-6 space-y-6">
      {/* 页面标题和操作栏 */}
      <div className="flex items-center justify-between">
        <div className="flex items-center gap-4">
          <Button variant="ghost" size="sm" onClick={() => navigate('/farm')}>
            <ArrowLeft className="h-4 w-4" />
          </Button>
          <div>
            <h1 className="text-3xl font-bold">{currentFarm.name}</h1>
            <p className="text-muted-foreground mt-1">养殖场详细信息</p>
          </div>
        </div>
        <div className="flex gap-2">
          <Button variant="outline" onClick={() => navigate(`/farm/${id}/edit`)}>
            <Edit className="mr-2 h-4 w-4" />
            编辑
          </Button>
          <Button variant="destructive" onClick={() => setShowDeleteDialog(true)}>
            <Trash2 className="mr-2 h-4 w-4" />
            删除
          </Button>
        </div>
      </div>

      {/* 基本信息 */}
      <Card>
        <CardHeader>
          <CardTitle>基本信息</CardTitle>
        </CardHeader>
        <CardContent>
          <div className="grid grid-cols-2 md:grid-cols-3 gap-6">
            <div>
              <div className="text-sm text-muted-foreground mb-1">养殖场名称</div>
              <div className="font-medium">{currentFarm.name}</div>
            </div>
            <div>
              <div className="text-sm text-muted-foreground mb-1">养殖规模</div>
              <div>
                <Badge>
                  {currentFarm.scaleName || FarmScale[currentFarm.scale as keyof typeof FarmScale]}
                </Badge>
              </div>
            </div>
            <div>
              <div className="text-sm text-muted-foreground mb-1">负责人</div>
              <div className="font-medium">{currentFarm.ownerName || '-'}</div>
            </div>
            <div>
              <div className="text-sm text-muted-foreground mb-1">联系电话</div>
              <div className="font-medium">{currentFarm.contactPhone || '-'}</div>
            </div>
            <div>
              <div className="text-sm text-muted-foreground mb-1">生猪总数</div>
              <div className="font-medium">{currentFarm.totalPigs || 0} 头</div>
            </div>
            <div>
              <div className="text-sm text-muted-foreground mb-1">创建时间</div>
              <div className="font-medium">{currentFarm.createTime}</div>
            </div>
          </div>
        </CardContent>
      </Card>

      {/* 地址信息 */}
      {currentFarm.address && (
        <Card>
          <CardHeader>
            <CardTitle>地址信息</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-sm">{currentFarm.address}</div>
          </CardContent>
        </Card>
      )}

      {/* 备注 */}
      {currentFarm.remark && (
        <Card>
          <CardHeader>
            <CardTitle>备注</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-sm whitespace-pre-wrap">{currentFarm.remark}</div>
          </CardContent>
        </Card>
      )}

      {/* 快捷操作 */}
      <Card>
        <CardHeader>
          <CardTitle>快捷操作</CardTitle>
        </CardHeader>
        <CardContent>
          <div className="flex gap-4">
            <Button onClick={() => navigate(`/pig?farmId=${id}`)}>
              查看生猪列表
            </Button>
            <Button variant="outline" onClick={() => navigate('/pig/create')}>
              添加生猪
            </Button>
          </div>
        </CardContent>
      </Card>

      {/* 删除确认对话框 */}
      <AlertDialog open={showDeleteDialog} onOpenChange={setShowDeleteDialog}>
        <AlertDialogContent>
          <AlertDialogHeader>
            <AlertDialogTitle>确认删除</AlertDialogTitle>
            <AlertDialogDescription>
              此操作将永久删除该养殖场记录（{currentFarm.name}），是否继续？
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
