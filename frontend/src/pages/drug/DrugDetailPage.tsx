import React, { useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { useDrugStore } from '@/stores/drugStore';
import { Button } from '@/components/ui/button';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Badge } from '@/components/ui/badge';
import { ArrowLeft } from 'lucide-react';
import { DrugType } from '@/types/pig';

export function DrugDetailPage() {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const { currentDrug, isLoading, fetchDrugById } = useDrugStore();

  useEffect(() => {
    if (id) {
      fetchDrugById(Number(id));
    }
  }, [id]);

  if (isLoading) {
    return (
      <div className="container mx-auto py-6">
        <div className="flex items-center justify-center py-12">
          <div className="text-muted-foreground">加载中...</div>
        </div>
      </div>
    );
  }

  if (!currentDrug) {
    return (
      <div className="container mx-auto py-6">
        <div className="flex flex-col items-center justify-center py-12">
          <p className="text-muted-foreground">兽药不存在</p>
          <Button onClick={() => navigate('/drug')} className="mt-4">
            返回列表
          </Button>
        </div>
      </div>
    );
  }

  return (
    <div className="container mx-auto py-6 space-y-6">
      {/* 页面标题 */}
      <div className="flex items-center justify-between">
        <div className="flex items-center gap-4">
          <Button variant="ghost" size="sm" onClick={() => navigate('/drug')}>
            <ArrowLeft className="h-4 w-4" />
          </Button>
          <div>
            <h1 className="text-3xl font-bold">{currentDrug.name}</h1>
            <p className="text-muted-foreground mt-1">兽药详细信息</p>
          </div>
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
              <div className="text-sm text-muted-foreground mb-1">药品名称</div>
              <div className="font-medium">{currentDrug.name}</div>
            </div>
            <div>
              <div className="text-sm text-muted-foreground mb-1">药品类型</div>
              <div>
                <Badge>
                  {currentDrug.typeName || DrugType[currentDrug.type as keyof typeof DrugType]}
                </Badge>
              </div>
            </div>
            <div>
              <div className="text-sm text-muted-foreground mb-1">生产厂家</div>
              <div className="font-medium">{currentDrug.manufacturer || '-'}</div>
            </div>
            <div>
              <div className="text-sm text-muted-foreground mb-1">批准文号</div>
              <div className="font-medium">{currentDrug.approvalNumber || '-'}</div>
            </div>
            <div>
              <div className="text-sm text-muted-foreground mb-1">规格</div>
              <div className="font-medium">{currentDrug.specification || '-'}</div>
            </div>
          </div>
        </CardContent>
      </Card>

      {/* 主要成分 */}
      {currentDrug.ingredients && (
        <Card>
          <CardHeader>
            <CardTitle>主要成分</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-sm whitespace-pre-wrap">{currentDrug.ingredients}</div>
          </CardContent>
        </Card>
      )}

      {/* 适应症 */}
      <Card>
        <CardHeader>
          <CardTitle>适应症</CardTitle>
        </CardHeader>
        <CardContent>
          <div className="text-sm whitespace-pre-wrap">{currentDrug.indications}</div>
        </CardContent>
      </Card>

      {/* 用法用量 */}
      {currentDrug.dosage && (
        <Card>
          <CardHeader>
            <CardTitle>用法用量</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-sm whitespace-pre-wrap">{currentDrug.dosage}</div>
          </CardContent>
        </Card>
      )}

      {/* 不良反应 */}
      {currentDrug.adverseReactions && (
        <Card>
          <CardHeader>
            <CardTitle>不良反应</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-sm whitespace-pre-wrap">{currentDrug.adverseReactions}</div>
          </CardContent>
        </Card>
      )}

      {/* 禁忌 */}
      {currentDrug.contraindications && (
        <Card>
          <CardHeader>
            <CardTitle>禁忌</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-sm whitespace-pre-wrap">{currentDrug.contraindications}</div>
          </CardContent>
        </Card>
      )}

      {/* 注意事项 */}
      {currentDrug.precautions && (
        <Card>
          <CardHeader>
            <CardTitle>注意事项</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-sm whitespace-pre-wrap">{currentDrug.precautions}</div>
          </CardContent>
        </Card>
      )}

      {/* 休药期 */}
      {currentDrug.withdrawalPeriod && (
        <Card>
          <CardHeader>
            <CardTitle>休药期</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-sm whitespace-pre-wrap">{currentDrug.withdrawalPeriod}</div>
          </CardContent>
        </Card>
      )}

      {/* 贮藏 */}
      {currentDrug.storage && (
        <Card>
          <CardHeader>
            <CardTitle>贮藏</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-sm whitespace-pre-wrap">{currentDrug.storage}</div>
          </CardContent>
        </Card>
      )}

      {/* 备注 */}
      {currentDrug.remark && (
        <Card>
          <CardHeader>
            <CardTitle>备注</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-sm whitespace-pre-wrap">{currentDrug.remark}</div>
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
            <Button onClick={() => navigate('/diagnosis')}>
              智能诊断
            </Button>
            <Button variant="outline" onClick={() => navigate('/disease')}>
              查看相关疾病
            </Button>
            <Button variant="outline" onClick={() => navigate('/article?category=MEDICATION_GUIDE')}>
              查看用药指南
            </Button>
          </div>
        </CardContent>
      </Card>
    </div>
  );
}
