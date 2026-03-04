import React, { useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import * as z from 'zod';
import { useArticleStore } from '@/stores/articleStore';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Textarea } from '@/components/ui/textarea';
import {
  Form,
  FormControl,
  FormDescription,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from '@/components/ui/form';
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '@/components/ui/select';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { ArrowLeft } from 'lucide-react';

const formSchema = z.object({
  title: z.string().min(1, '请输入标题').max(200, '标题不能超过200个字符'),
  category: z.string().min(1, '请选择分类'),
  summary: z.string().optional(),
  content: z.string().min(1, '请输入正文内容'),
  tags: z.string().optional(),
  status: z.string().optional(),
});

type FormValues = z.infer<typeof formSchema>;

export function ArticleFormPage() {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const { currentArticle, isLoading, fetchArticleById, createArticle, updateArticle } = useArticleStore();
  const isEdit = !!id;

  const form = useForm<FormValues>({
    resolver: zodResolver(formSchema),
    defaultValues: {
      title: '',
      category: '',
      summary: '',
      content: '',
      tags: '',
      status: 'DRAFT',
    },
  });

  // 编辑模式：加载数据
  useEffect(() => {
    if (isEdit && id) {
      fetchArticleById(Number(id));
    }
  }, [isEdit, id]);

  // 填充表单数据
  useEffect(() => {
    if (isEdit && currentArticle) {
      form.reset({
        title: currentArticle.title,
        category: currentArticle.category,
        summary: currentArticle.summary || '',
        content: currentArticle.content,
        tags: currentArticle.tags || '',
        status: currentArticle.status || 'DRAFT',
      });
    }
  }, [currentArticle, isEdit]);

  const onSubmit = async (values: FormValues) => {
    try {
      const data = {
        title: values.title,
        category: values.category,
        summary: values.summary || undefined,
        content: values.content,
        tags: values.tags || undefined,
        status: values.status || undefined,
      };

      if (isEdit && id) {
        await updateArticle(Number(id), data);
      } else {
        await createArticle(data);
      }

      navigate('/article');
    } catch (error) {
      // 错误已在store中处理
    }
  };

  return (
    <div className="container mx-auto py-6 space-y-6">
      {/* 页面标题 */}
      <div className="flex items-center gap-4">
        <Button variant="ghost" size="sm" onClick={() => navigate('/article')}>
          <ArrowLeft className="h-4 w-4" />
        </Button>
        <div>
          <h1 className="text-3xl font-bold">{isEdit ? '编辑文章' : '发布文章'}</h1>
          <p className="text-muted-foreground mt-1">
            {isEdit ? '修改文章内容' : '创建新的文章'}
          </p>
        </div>
      </div>

      {/* 表单 */}
      <Card>
        <CardHeader>
          <CardTitle>文章信息</CardTitle>
        </CardHeader>
        <CardContent>
          <Form {...form}>
            <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-6">
              <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                {/* 标题 */}
                <FormField
                  control={form.control}
                  name="title"
                  render={({ field }) => (
                    <FormItem className="md:col-span-2">
                      <FormLabel>标题 *</FormLabel>
                      <FormControl>
                        <Input placeholder="输入文章标题" {...field} />
                      </FormControl>
                      <FormMessage />
                    </FormItem>
                  )}
                />

                {/* 分类 */}
                <FormField
                  control={form.control}
                  name="category"
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel>分类 *</FormLabel>
                      <Select onValueChange={field.onChange} value={field.value}>
                        <FormControl>
                          <SelectTrigger>
                            <SelectValue placeholder="请选择分类" />
                          </SelectTrigger>
                        </FormControl>
                        <SelectContent>
                          <SelectItem value="DISEASE_PREVENTION">疾病防治</SelectItem>
                          <SelectItem value="BREEDING_TECHNOLOGY">养殖技术</SelectItem>
                          <SelectItem value="NUTRITION_FEED">营养饲料</SelectItem>
                          <SelectItem value="MEDICATION_GUIDE">用药指南</SelectItem>
                          <SelectItem value="POLICY_REGULATION">政策法规</SelectItem>
                          <SelectItem value="MARKET_ANALYSIS">市场分析</SelectItem>
                          <SelectItem value="OTHER">其他</SelectItem>
                        </SelectContent>
                      </Select>
                      <FormMessage />
                    </FormItem>
                  )}
                />

                {/* 状态 */}
                <FormField
                  control={form.control}
                  name="status"
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel>状态</FormLabel>
                      <Select onValueChange={field.onChange} value={field.value}>
                        <FormControl>
                          <SelectTrigger>
                            <SelectValue placeholder="请选择状态" />
                          </SelectTrigger>
                        </FormControl>
                        <SelectContent>
                          <SelectItem value="DRAFT">草稿</SelectItem>
                          <SelectItem value="PUBLISHED">发布</SelectItem>
                        </SelectContent>
                      </Select>
                      <FormDescription>草稿状态不会公开显示</FormDescription>
                      <FormMessage />
                    </FormItem>
                  )}
                />

                {/* 标签 */}
                <FormField
                  control={form.control}
                  name="tags"
                  render={({ field }) => (
                    <FormItem className="md:col-span-2">
                      <FormLabel>标签</FormLabel>
                      <FormControl>
                        <Input placeholder="输入标签，用逗号分隔，例如：猪病,防治,疫苗" {...field} />
                      </FormControl>
                      <FormDescription>多个标签用逗号分隔</FormDescription>
                      <FormMessage />
                    </FormItem>
                  )}
                />
              </div>

              {/* 摘要 */}
              <FormField
                control={form.control}
                name="summary"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>摘要</FormLabel>
                    <FormControl>
                      <Textarea
                        placeholder="输入文章摘要..."
                        className="min-h-[100px]"
                        {...field}
                      />
                    </FormControl>
                    <FormDescription>简要描述文章内容</FormDescription>
                    <FormMessage />
                  </FormItem>
                )}
              />

              {/* 正文 */}
              <FormField
                control={form.control}
                name="content"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>正文 *</FormLabel>
                    <FormControl>
                      <Textarea
                        placeholder="输入文章正文内容..."
                        className="min-h-[400px]"
                        {...field}
                      />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />

              {/* 操作按钮 */}
              <div className="flex justify-end gap-4">
                <Button type="button" variant="outline" onClick={() => navigate('/article')}>
                  取消
                </Button>
                <Button type="submit" disabled={isLoading}>
                  {isLoading ? '提交中...' : isEdit ? '保存' : '发布'}
                </Button>
              </div>
            </form>
          </Form>
        </CardContent>
      </Card>
    </div>
  );
}
