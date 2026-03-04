import React, { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useDrugStore } from '@/stores/drugStore';
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
import { Eye } from 'lucide-react';
import { DrugType } from '@/types/pig';

export function DrugListPage() {
  const navigate = useNavigate();

  const {
    drugs,
    total,
    pageNum,
    pageSize,
    isLoading,
    filters,
    fetchDrugs,
    setFilters,
    setPageNum,
    setPageSize,
  } = useDrugStore();

  const [keyword, setKeyword] = React.useState('');

  useEffect(() => {
    fetchDrugs();
  }, [pageNum, pageSize, filters]);

  const handleSearch = () => {
    setFilters({ keyword });
  };

  const getDrugTypeBadge = (type: string) => {
    const variants: Record<string, 'default' | 'secondary' | 'destructive' | 'outline'> = {
      ANTIBIOTIC: 'destructive',
      ANTIVIRAL: 'secondary',
      ANTIPARASITIC: 'default',
      VACCINE: 'outline',
      NUTRITIONAL: 'outline',
      OTHER: 'outline',
    };
    return variants[type] || 'default';
  };

  const totalPages = Math.ceil(total / pageSize);

  return (
    <div className="container mx-auto py-6 space-y-6">
      {/* 页面标题 */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-3xl font-bold">兽药展示</h1>
          <p className="text-muted-foreground mt-1">查看兽药信息和使用说明</p>
        </div>
      </div>

      {/* 筛选和搜索 */}
      <Card>
        <CardHeader>
          <CardTitle>筛选条件</CardTitle>
        </CardHeader>
        <CardContent>
          <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
            <div>
              <label className="text-sm font-medium mb-2 block">药品类型</label>
              <Select
                value={filters.type || 'all'}
                onValueChange={(value) =>
                  setFilters({ type: value === 'all' ? undefined : value })
                }
              >
                <SelectTrigger>
                  <SelectValue placeholder="选择类型" />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="all">全部</SelectItem>
                  <SelectItem value="ANTIBIOTIC">抗生素</SelectItem>
                  <SelectItem value="ANTIVIRAL">抗病毒药</SelectItem>
                  <SelectItem value="ANTIPARASITIC">抗寄生虫药</SelectItem>
                  <SelectItem value="VACCINE">疫苗</SelectItem>
                  <SelectItem value="NUTRITIONAL">营养补充剂</SelectItem>
                  <SelectItem value="OTHER">其他</SelectItem>
                </SelectContent>
              </Select>
            </div>
            <div>
              <label className="text-sm font-medium mb-2 block">关键词搜索</label>
              <Input
                placeholder="输入药品名称或成分"
                value={keyword}
                onChange={(e) => setKeyword(e.target.value)}
                onKeyDown={(e) => e.key === 'Enter' && handleSearch()}
              />
            </div>
            <div className="flex items-end gap-2">
              <Button onClick={handleSearch}>搜索</Button>
              <Button
                variant="outline"
                onClick={() => {
                  setKeyword('');
                  setFilters({});
                  fetchDrugs();
                }}
              >
                重置
              </Button>
            </div>
          </div>
        </CardContent>
      </Card>

      {/* 兽药列表 */}
      <Card>
        <CardHeader>
          <CardTitle>兽药列表（共 {total} 条）</CardTitle>
        </CardHeader>
        <CardContent>
          {isLoading ? (
            <div className="flex items-center justify-center py-8">
              <div className="text-muted-foreground">加载中...</div>
            </div>
          ) : drugs.length === 0 ? (
            <div className="flex flex-col items-center justify-center py-8">
              <p className="text-muted-foreground">暂无兽药记录</p>
            </div>
          ) : (
            <>
              <Table>
                <TableHeader>
                  <TableRow>
                    <TableHead>药品名称</TableHead>
                    <TableHead>类型</TableHead>
                    <TableHead>主要成分</TableHead>
                    <TableHead>适应症</TableHead>
                    <TableHead>生产厂家</TableHead>
                    <TableHead>操作</TableHead>
                  </TableRow>
                </TableHeader>
                <TableBody>
                  {drugs.map((drug) => (
                    <TableRow key={drug.id}>
                      <TableCell className="font-medium">{drug.name}</TableCell>
                      <TableCell>
                        <Badge variant={getDrugTypeBadge(drug.type)}>
                          {drug.typeName || DrugType[drug.type as keyof typeof DrugType]}
                        </Badge>
                      </TableCell>
                      <TableCell className="max-w-xs truncate">
                        {drug.ingredients || '-'}
                      </TableCell>
                      <TableCell className="max-w-xs truncate">
                        {drug.indications}
                      </TableCell>
                      <TableCell>{drug.manufacturer || '-'}</TableCell>
                      <TableCell>
                        <Button
                          variant="ghost"
                          size="sm"
                          onClick={() => navigate(`/drug/${drug.id}`)}
                        >
                          <Eye className="h-4 w-4" />
                        </Button>
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
    </div>
  );
}
