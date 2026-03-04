import React, { useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { usePigStore } from '@/stores/pigStore';
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
import { HealthStatus, FeedStatus } from '@/types/pig';

export function PigDetailPage() {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const { currentPig, isLoading, fetchPigById, deletePig } = usePigStore();
  const [showDeleteDialog, setShowDeleteDialog] = React.useState(false);

  useEffect(() => {
    if (id) {
      fetchPigById(Number(id));
    }
  }, [id]);

  const handleDelete = async () => {
    if (id) {
      try {
        await deletePig(Number(id));
        navigate('/pig');
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

  if (!currentPig) {
    return (
      <div className="container mx-auto py-6">
        <div className="flex flex-col items-center justify-center py-12">
          <p className="text-muted-foreground">生猪不存在</p>
          <Button onClick={() => navigate('/pig')} className="mt-4">
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
          <Button variant="ghost" size="sm" onClick={() => navigate('/pig')}>
            <ArrowLeft className="h-4 w-4" />
          </Button>
          <div>
            <h1 className="text-3xl font-bold">生猪详情</h1>
            <p className="text-muted-foreground mt-1">耳标号：{currentPig.earTagNumber}</p>
          </div>
        </div>
        <div className="flex gap-2">
          <Button variant="outline" onClick={() => navigate(`/pig/${id}/edit`)}>
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
              <div className="text-sm text-muted-foreground mb-1">耳标号</div>
              <div className="font-medium">{currentPig.earTagNumber}</div>
            </div>
            <div>
              <div className="text-sm text-muted-foreground mb-1">品种</div>
              <div className="font-medium">{currentPig.breed}</div>
            </div>
            <div>
              <div className="text-sm text-muted-foreground mb-1">性别</div>
              <div className="font-medium">{currentPig.gender === 0 ? '母猪' : '公猪'}</div>
            </div>
            <div>
              <div className="text-sm text-muted-foreground mb-1">出生日期</div>
              <div className="font-medium">{currentPig.birthDate}</div>
            </div>
            <div>
              <div className="text-sm text-muted-foreground mb-1">体重</div>
              <div className="font-medium">{currentPig.weight ? `${currentPig.weight} kg` : '-'}</div>
            </div>
            <div>
              <div className="text-sm text-muted-foreground mb-1">健康状态</div>
              <div>
                <Badge>
                  {currentPig.healthStatusName || HealthStatus[currentPig.healthStatus as keyof typeof HealthStatus]}
                </Badge>
              </div>
            </div>
            <div>
              <div className="text-sm text-muted-foreground mb-1">猪栏编号</div>
              <div className="font-medium">{currentPig.penNumber || '-'}</div>
            </div>
            <div>
              <div className="text-sm text-muted-foreground mb-1">饲养状态</div>
              <div className="font-medium">
                {currentPig.feedStatusName || FeedStatus[currentPig.feedStatus as keyof typeof FeedStatus] || '-'}
              </div>
            </div>
          </div>
        </CardContent>
      </Card>

      {/* 免疫信息 */}
      {currentPig.immunizationStatus && (
        <Card>
          <CardHeader>
            <CardTitle>免疫信息</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-sm whitespace-pre-wrap">{currentPig.immunizationStatus}</div>
          </CardContent>
        </Card>
      )}

      {/* 遗传信息 */}
      {currentPig.geneticInfo && (
        <Card>
          <CardHeader>
            <CardTitle>遗传信息</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-sm whitespace-pre-wrap">{currentPig.geneticInfo}</div>
          </CardContent>
        </Card>
      )}

      {/* 备注 */}
      {currentPig.remark && (
        <Card>
          <CardHeader>
            <CardTitle>备注</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-sm whitespace-pre-wrap">{currentPig.remark}</div>
          </CardContent>
        </Card>
      )}

      {/* 时间信息 */}
      <Card>
        <CardHeader>
          <CardTitle>时间信息</CardTitle>
        </CardHeader>
        <CardContent>
          <div className="grid grid-cols-2 gap-6">
            <div>
              <div className="text-sm text-muted-foreground mb-1">创建时间</div>
              <div className="font-medium">{currentPig.createTime}</div>
            </div>
            <div>
              <div className="text-sm text-muted-foreground mb-1">更新时间</div>
              <div className="font-medium">{currentPig.updateTime}</div>
            </div>
          </div>
        </CardContent>
      </Card>

      {/* 快捷操作 */}
      <Card>
        <CardHeader>
          <CardTitle>快捷操作</CardTitle>
        </CardHeader>
        <CardContent>
          <div className="flex gap-4">
            <Button onClick={() => navigate(`/case/create?pigId=${id}`)}>
              创建病例
            </Button>
            <Button variant="outline" onClick={() => navigate(`/health-monitor/create?pigId=${id}`)}>
              添加健康监测
            </Button>
            <Button variant="outline" onClick={() => navigate(`/case?pigId=${id}`)}>
              查看病例历史
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
              此操作将永久删除该生猪记录（耳标号：{currentPig.earTagNumber}），是否继续？
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
