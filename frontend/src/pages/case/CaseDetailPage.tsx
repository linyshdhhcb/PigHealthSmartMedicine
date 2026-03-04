import React, { useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { useCaseStore } from '@/stores/caseStore';
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
import { CaseStatus } from '@/types/pig';

export function CaseDetailPage() {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const { currentCase, isLoading, fetchCaseById, deleteCase } = useCaseStore();
  const [showDeleteDialog, setShowDeleteDialog] = React.useState(false);

  useEffect(() => {
    if (id) {
      fetchCaseById(Number(id));
    }
  }, [id]);

  const handleDelete = async () => {
    if (id) {
      try {
        await deleteCase(Number(id));
        navigate('/case');
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

  if (!currentCase) {
    return (
      <div className="container mx-auto py-6">
        <div className="flex flex-col items-center justify-center py-12">
          <p className="text-muted-foreground">病例不存在</p>
          <Button onClick={() => navigate('/case')} className="mt-4">
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
          <Button variant="ghost" size="sm" onClick={() => navigate('/case')}>
            <ArrowLeft className="h-4 w-4" />
          </Button>
          <div>
            <h1 className="text-3xl font-bold">病例详情</h1>
            <p className="text-muted-foreground mt-1">病例ID：{currentCase.id}</p>
          </div>
        </div>
        <div className="flex gap-2">
          <Button variant="outline" onClick={() => navigate(`/case/${id}/edit`)}>
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
              <div className="text-sm text-muted-foreground mb-1">病例ID</div>
              <div className="font-medium">{currentCase.id}</div>
            </div>
            <div>
              <div className="text-sm text-muted-foreground mb-1">生猪耳标号</div>
              <div className="font-medium">
                {currentCase.pigEarTagNumber ? (
                  <Button
                    variant="link"
                    className="p-0 h-auto"
                    onClick={() => navigate(`/pig/${currentCase.pigId}`)}
                  >
                    {currentCase.pigEarTagNumber}
                  </Button>
                ) : (
                  '-'
                )}
              </div>
            </div>
            <div>
              <div className="text-sm text-muted-foreground mb-1">病例状态</div>
              <div>
                <Badge>
                  {currentCase.statusName || CaseStatus[currentCase.status as keyof typeof CaseStatus]}
                </Badge>
              </div>
            </div>
            <div>
              <div className="text-sm text-muted-foreground mb-1">创建时间</div>
              <div className="font-medium">{currentCase.createTime}</div>
            </div>
            <div>
              <div className="text-sm text-muted-foreground mb-1">更新时间</div>
              <div className="font-medium">{currentCase.updateTime}</div>
            </div>
          </div>
        </CardContent>
      </Card>

      {/* 症状描述 */}
      <Card>
        <CardHeader>
          <CardTitle>症状描述</CardTitle>
        </CardHeader>
        <CardContent>
          <div className="text-sm whitespace-pre-wrap">{currentCase.symptomDescription}</div>
        </CardContent>
      </Card>

      {/* 诊断结果 */}
      {currentCase.diagnosisResult && (
        <Card>
          <CardHeader>
            <CardTitle>诊断结果</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-sm whitespace-pre-wrap">{currentCase.diagnosisResult}</div>
          </CardContent>
        </Card>
      )}

      {/* 治疗方案 */}
      {currentCase.treatmentPlan && (
        <Card>
          <CardHeader>
            <CardTitle>治疗方案</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-sm whitespace-pre-wrap">{currentCase.treatmentPlan}</div>
          </CardContent>
        </Card>
      )}

      {/* 用药记录 */}
      {currentCase.medicationRecord && (
        <Card>
          <CardHeader>
            <CardTitle>用药记录</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-sm whitespace-pre-wrap">{currentCase.medicationRecord}</div>
          </CardContent>
        </Card>
      )}

      {/* 治疗效果 */}
      {currentCase.treatmentEffect && (
        <Card>
          <CardHeader>
            <CardTitle>治疗效果</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-sm whitespace-pre-wrap">{currentCase.treatmentEffect}</div>
          </CardContent>
        </Card>
      )}

      {/* 备注 */}
      {currentCase.remark && (
        <Card>
          <CardHeader>
            <CardTitle>备注</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-sm whitespace-pre-wrap">{currentCase.remark}</div>
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
            <Button onClick={() => navigate(`/treatment-record/create?caseId=${id}`)}>
              添加治疗记录
            </Button>
            <Button variant="outline" onClick={() => navigate(`/pig/${currentCase.pigId}`)}>
              查看生猪详情
            </Button>
            <Button variant="outline" onClick={() => navigate(`/treatment-record?caseId=${id}`)}>
              查看治疗记录
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
              此操作将永久删除该病例记录（ID：{currentCase.id}），是否继续？
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
