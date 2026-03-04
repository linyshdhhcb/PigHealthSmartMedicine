import React, { useEffect } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';
import { useArticleStore } from '@/stores/articleStore';
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
import { Plus, Eye, Edit, Trash2, ThumbsUp } from 'lucide-react';
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
import { ArticleCategory } from '@/types/pig';

export function ArticleListPage() {
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();
  const categoryFromQuery = searchParams.get('category');

  const {
    articles,
    total,
    pageNum,
    pageSize,
    isLoading,
    filters,
    fetchArticles,
    setFilters,
    setPageNum,
    setPageSize,
    deleteArticle,
    publishArticle,
  } = useArticleStore();

  const [deleteId, setDeleteId] = React.useState<number | null>(null);

  useEffect(() => {
    if (categoryFromQuery) {
      setFilters({ category: categoryFromQuery });
    }
    fetchArticles();
  }, [pageNum, pageSize, filters]);

  const handleDelete = async () => {
    if (deleteId) {
      try {
        await deleteArticle(deleteId);
        setDeleteId(null);
      } catch (error) {
        // 错误已在store中处理
      }
    }
  };

  const handlePublish = async (id: number) => {
    try {
      await publishArticle(id);
    } catch (error) {
      // 错误已在store中处理
    }
  };

  const getArticleCategoryBadge = (category: string) => {
    const variants: Record<string, 'default' | 'secondary' | 'destructive' | 'outline'> = {
      DISEASE_PREVENTION: 'destructive',
      BREEDING_TECHNOLOGY: 'default',
      NUTRITION_FEED: 'secondary',
      MEDICATION_GUIDE: 'outline',
      POLICY_REGULATION: 'outline',
      MARKET_ANALYSIS: 'outline',
      OTHER: 'outline',
    };
    return variants[category] || 'default';
  };

  const totalPages = Math.ceil(total / pageSize);

  return (
    <div className="container mx-auto py-6 space-y-6">
      {/* 页面标题 */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-3xl font-bold">文章资讯</h1>
          <p className="text-muted-foreground mt-1">查看和管理养殖相关文章</p>
        </div>
        <Button onClick={() => navigate('/article/create')}>
          <Plus className="mr-2 h-4 w-4" />
          发布文章
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
              <label className="text-sm font-medium mb-2 block">文章分类</label>
              <Select
                value={filters.category || 'all'}
                onValueChange={(value) =>
                  setFilters({ category: value === 'all' ? undefined : value })
                }
              >
                <SelectTrigger>
                  <SelectValue placeholder="选择分类" />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="all">全部</SelectItem>
                  <SelectItem value="DISEASE_PREVENTION">疾病防治</SelectItem>
                  <SelectItem value="BREEDING_TECHNOLOGY">养殖技术</SelectItem>
                  <SelectItem value="NUTRITION_FEED">营养饲料</SelectItem>
                  <SelectItem value="MEDICATION_GUIDE">用药指南</SelectItem>
                  <SelectItem value="POLICY_REGULATION">政策法规</SelectItem>
                  <SelectItem value="MARKET_ANALYSIS">市场分析</SelectItem>
                  <SelectItem value="OTHER">其他</SelectItem>
                </SelectContent>
              </Select>
            </div>
            <div>
              <label className="text-sm font-medium mb-2 block">发布状态</label>
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
                  <SelectItem value="DRAFT">草稿</SelectItem>
                  <SelectItem value="PUBLISHED">已发布</SelectItem>
                </SelectContent>
              </Select>
            </div>
            <div className="flex items-end">
              <Button
                variant="outline"
                onClick={() => {
                  setFilters({});
                  fetchArticles();
                }}
              >
                重置筛选
              </Button>
            </div>
          </div>
        </CardContent>
      </Card>

      {/* 文章列表 */}
      <Card>
        <CardHeader>
          <CardTitle>文章列表（共 {total} 条）</CardTitle>
        </CardHeader>
        <CardContent>
          {isLoading ? (
            <div className="flex items-center justify-center py-8">
              <div className="text-muted-foreground">加载中...</div>
            </div>
          ) : articles.length === 0 ? (
            <div className="flex flex-col items-center justify-center py-8">
              <p className="text-muted-foreground">暂无文章</p>
              <Button onClick={() => navigate('/article/create')} className="mt-4">
                发布第一篇文章
              </Button>
            </div>
          ) : (
            <>
              <Table>
                <TableHeader>
                  <TableRow>
                    <TableHead>标题</TableHead>
                    <TableHead>分类</TableHead>
                    <TableHead>作者</TableHead>
                    <TableHead>状态</TableHead>
                    <TableHead>浏览/点赞</TableHead>
                    <TableHead>发布时间</TableHead>
                    <TableHead>操作</TableHead>
                  </TableRow>
                </TableHeader>
                <TableBody>
                  {articles.map((article) => (
                    <TableRow key={article.id}>
                      <TableCell className="font-medium max-w-xs truncate">
                        {article.title}
                      </TableCell>
                      <TableCell>
                        <Badge variant={getArticleCategoryBadge(article.category)}>
                          {article.categoryName || ArticleCategory[article.category as keyof typeof ArticleCategory]}
                        </Badge>
                      </TableCell>
                      <TableCell>{article.authorName || '-'}</TableCell>
                      <TableCell>
                        <Badge variant={article.status === 'PUBLISHED' ? 'default' : 'outline'}>
                          {article.status === 'PUBLISHED' ? '已发布' : '草稿'}
                        </Badge>
                      </TableCell>
                      <TableCell>
                        <div className="flex items-center gap-2">
                          <span>{article.viewCount}</span>
                          <span>/</span>
                          <span className="flex items-center gap-1">
                            <ThumbsUp className="h-3 w-3" />
                            {article.likeCount}
                          </span>
                        </div>
                      </TableCell>
                      <TableCell>{article.publishTime || '-'}</TableCell>
                      <TableCell>
                        <div className="flex gap-2">
                          <Button
                            variant="ghost"
                            size="sm"
                            onClick={() => navigate(`/article/${article.id}`)}
                          >
                            <Eye className="h-4 w-4" />
                          </Button>
                          <Button
                            variant="ghost"
                            size="sm"
                            onClick={() => navigate(`/article/${article.id}/edit`)}
                          >
                            <Edit className="h-4 w-4" />
                          </Button>
                          {article.status === 'DRAFT' && (
                            <Button
                              variant="ghost"
                              size="sm"
                              onClick={() => handlePublish(article.id)}
                            >
                              发布
                            </Button>
                          )}
                          <Button
                            variant="ghost"
                            size="sm"
                            onClick={() => setDeleteId(article.id)}
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
              此操作将永久删除该文章，是否继续？
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
