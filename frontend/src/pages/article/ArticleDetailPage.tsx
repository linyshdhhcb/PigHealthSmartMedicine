import React, { useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { useArticleStore } from '@/stores/articleStore';
import { Button } from '@/components/ui/button';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Badge } from '@/components/ui/badge';
import { ArrowLeft, Edit, Trash2, ThumbsUp, Eye } from 'lucide-react';
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

export function ArticleDetailPage() {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const { currentArticle, isLoading, fetchArticleById, deleteArticle, likeArticle } = useArticleStore();
  const [showDeleteDialog, setShowDeleteDialog] = React.useState(false);

  useEffect(() => {
    if (id) {
      fetchArticleById(Number(id));
    }
  }, [id]);

  const handleDelete = async () => {
    if (id) {
      try {
        await deleteArticle(Number(id));
        navigate('/article');
      } catch (error) {
        // 错误已在store中处理
      }
    }
  };

  const handleLike = async () => {
    if (id) {
      try {
        await likeArticle(Number(id));
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

  if (!currentArticle) {
    return (
      <div className="container mx-auto py-6">
        <div className="flex flex-col items-center justify-center py-12">
          <p className="text-muted-foreground">文章不存在</p>
          <Button onClick={() => navigate('/article')} className="mt-4">
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
          <Button variant="ghost" size="sm" onClick={() => navigate('/article')}>
            <ArrowLeft className="h-4 w-4" />
          </Button>
          <div>
            <h1 className="text-3xl font-bold">{currentArticle.title}</h1>
            <div className="flex items-center gap-4 mt-2 text-sm text-muted-foreground">
              <span>作者：{currentArticle.authorName || '未知'}</span>
              <span>发布时间：{currentArticle.publishTime || '-'}</span>
              <span className="flex items-center gap-1">
                <Eye className="h-3 w-3" />
                {currentArticle.viewCount}
              </span>
              <span className="flex items-center gap-1">
                <ThumbsUp className="h-3 w-3" />
                {currentArticle.likeCount}
              </span>
            </div>
          </div>
        </div>
        <div className="flex gap-2">
          <Button variant="outline" onClick={handleLike}>
            <ThumbsUp className="mr-2 h-4 w-4" />
            点赞
          </Button>
          <Button variant="outline" onClick={() => navigate(`/article/${id}/edit`)}>
            <Edit className="mr-2 h-4 w-4" />
            编辑
          </Button>
          <Button variant="destructive" onClick={() => setShowDeleteDialog(true)}>
            <Trash2 className="mr-2 h-4 w-4" />
            删除
          </Button>
        </div>
      </div>

      {/* 文章信息 */}
      <Card>
        <CardHeader>
          <div className="flex items-center justify-between">
            <CardTitle>文章信息</CardTitle>
            <div className="flex gap-2">
              <Badge>
                {currentArticle.categoryName || ArticleCategory[currentArticle.category as keyof typeof ArticleCategory]}
              </Badge>
              <Badge variant={currentArticle.status === 'PUBLISHED' ? 'default' : 'outline'}>
                {currentArticle.status === 'PUBLISHED' ? '已发布' : '草稿'}
              </Badge>
            </div>
          </div>
        </CardHeader>
        <CardContent>
          <div className="grid grid-cols-2 md:grid-cols-3 gap-6">
            <div>
              <div className="text-sm text-muted-foreground mb-1">文章ID</div>
              <div className="font-medium">{currentArticle.id}</div>
            </div>
            <div>
              <div className="text-sm text-muted-foreground mb-1">作者</div>
              <div className="font-medium">{currentArticle.authorName || '-'}</div>
            </div>
            <div>
              <div className="text-sm text-muted-foreground mb-1">浏览量</div>
              <div className="font-medium">{currentArticle.viewCount}</div>
            </div>
            <div>
              <div className="text-sm text-muted-foreground mb-1">点赞数</div>
              <div className="font-medium">{currentArticle.likeCount}</div>
            </div>
            <div>
              <div className="text-sm text-muted-foreground mb-1">发布时间</div>
              <div className="font-medium">{currentArticle.publishTime || '-'}</div>
            </div>
            <div>
              <div className="text-sm text-muted-foreground mb-1">更新时间</div>
              <div className="font-medium">{currentArticle.updateTime}</div>
            </div>
          </div>
        </CardContent>
      </Card>

      {/* 摘要 */}
      {currentArticle.summary && (
        <Card>
          <CardHeader>
            <CardTitle>摘要</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-sm text-muted-foreground whitespace-pre-wrap">
              {currentArticle.summary}
            </div>
          </CardContent>
        </Card>
      )}

      {/* 文章内容 */}
      <Card>
        <CardHeader>
          <CardTitle>正文</CardTitle>
        </CardHeader>
        <CardContent>
          <div className="prose max-w-none">
            <div className="text-sm whitespace-pre-wrap">{currentArticle.content}</div>
          </div>
        </CardContent>
      </Card>

      {/* 标签 */}
      {currentArticle.tags && (
        <Card>
          <CardHeader>
            <CardTitle>标签</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="flex flex-wrap gap-2">
              {currentArticle.tags.split(',').map((tag, index) => (
                <Badge key={index} variant="outline">
                  {tag.trim()}
                </Badge>
              ))}
            </div>
          </CardContent>
        </Card>
      )}

      {/* 删除确认对话框 */}
      <AlertDialog open={showDeleteDialog} onOpenChange={setShowDeleteDialog}>
        <AlertDialogContent>
          <AlertDialogHeader>
            <AlertDialogTitle>确认删除</AlertDialogTitle>
            <AlertDialogDescription>
              此操作将永久删除该文章（{currentArticle.title}），是否继续？
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
