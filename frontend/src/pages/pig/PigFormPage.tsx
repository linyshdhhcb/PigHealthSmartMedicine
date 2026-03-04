import React, { useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import * as z from 'zod';
import { usePigStore } from '@/stores/pigStore';
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
  earTagNumber: z.string().min(1, '请输入耳标号').max(50, '耳标号不能超过50个字符'),
  breed: z.string().min(1, '请输入品种'),
  gender: z.string().min(1, '请选择性别'),
  birthDate: z.string().min(1, '请选择出生日期'),
  weight: z.string().optional(),
  penNumber: z.string().optional(),
  feedStatus: z.string().optional(),
  remark: z.string().optional(),
});

type FormValues = z.infer<typeof formSchema>;

export function PigFormPage() {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const { currentPig, isLoading, fetchPigById, createPig, updatePig } = usePigStore();
  const isEdit = !!id;

  const form = useForm<FormValues>({
    resolver: zodResolver(formSchema),
    defaultValues: {
      earTagNumber: '',
      breed: '',
      gender: '',
      birthDate: '',
      weight: '',
      penNumber: '',
      feedStatus: 'NORMAL',
      remark: '',
    },
  });

  // 编辑模式：加载数据
  useEffect(() => {
    if (isEdit && id) {
      fetchPigById(Number(id));
    }
  }, [isEdit, id]);

  // 填充表单数据
  useEffect(() => {
    if (isEdit && currentPig) {
      form.reset({
        earTagNumber: currentPig.earTagNumber,
        breed: currentPig.breed,
        gender: String(currentPig.gender),
        birthDate: currentPig.birthDate,
        weight: currentPig.weight ? String(currentPig.weight) : '',
        penNumber: currentPig.penNumber || '',
        feedStatus: currentPig.feedStatus || 'NORMAL',
        remark: currentPig.remark || '',
      });
    }
  }, [currentPig, isEdit]);

  const onSubmit = async (values: FormValues) => {
    try {
      const data = {
        earTagNumber: values.earTagNumber,
        breed: values.breed,
        gender: Number(values.gender),
        birthDate: values.birthDate,
        weight: values.weight ? Number(values.weight) : undefined,
        penNumber: values.penNumber || undefined,
        feedStatus: values.feedStatus || undefined,
        remark: values.remark || undefined,
      };

      if (isEdit && id) {
        await updatePig(Number(id), data);
      } else {
        await createPig(data);
      }

      navigate('/pig');
    } catch (error) {
      // 错误已在store中处理
    }
  };

  return (
    <div className="container mx-auto py-6 space-y-6">
      {/* 页面标题 */}
      <div className="flex items-center gap-4">
        <Button variant="ghost" size="sm" onClick={() => navigate('/pig')}>
          <ArrowLeft className="h-4 w-4" />
        </Button>
        <div>
          <h1 className="text-3xl font-bold">{isEdit ? '编辑生猪' : '新增生猪'}</h1>
          <p className="text-muted-foreground mt-1">
            {isEdit ? '修改生猪信息' : '添加新的生猪到系统'}
          </p>
        </div>
      </div>

      {/* 表单 */}
      <Card>
        <CardHeader>
          <CardTitle>基本信息</CardTitle>
        </CardHeader>
        <CardContent>
          <Form {...form}>
            <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-6">
              <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                {/* 耳标号 */}
                <FormField
                  control={form.control}
                  name="earTagNumber"
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel>耳标号 *</FormLabel>
                      <FormControl>
                        <Input placeholder="例如：PIG001" {...field} />
                      </FormControl>
                      <FormDescription>生猪的唯一标识</FormDescription>
                      <FormMessage />
                    </FormItem>
                  )}
                />

                {/* 品种 */}
                <FormField
                  control={form.control}
                  name="breed"
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel>品种 *</FormLabel>
                      <FormControl>
                        <Input placeholder="例如：长白猪" {...field} />
                      </FormControl>
                      <FormMessage />
                    </FormItem>
                  )}
                />

                {/* 性别 */}
                <FormField
                  control={form.control}
                  name="gender"
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel>性别 *</FormLabel>
                      <Select onValueChange={field.onChange} value={field.value}>
                        <FormControl>
                          <SelectTrigger>
                            <SelectValue placeholder="请选择性别" />
                          </SelectTrigger>
                        </FormControl>
                        <SelectContent>
                          <SelectItem value="0">母猪</SelectItem>
                          <SelectItem value="1">公猪</SelectItem>
                        </SelectContent>
                      </Select>
                      <FormMessage />
                    </FormItem>
                  )}
                />

                {/* 出生日期 */}
                <FormField
                  control={form.control}
                  name="birthDate"
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel>出生日期 *</FormLabel>
                      <FormControl>
                        <Input type="date" {...field} />
                      </FormControl>
                      <FormMessage />
                    </FormItem>
                  )}
                />

                {/* 体重 */}
                <FormField
                  control={form.control}
                  name="weight"
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel>体重（kg）</FormLabel>
                      <FormControl>
                        <Input type="number" step="0.1" placeholder="例如：80.5" {...field} />
                      </FormControl>
                      <FormMessage />
                    </FormItem>
                  )}
                />

                {/* 猪栏编号 */}
                <FormField
                  control={form.control}
                  name="penNumber"
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel>猪栏编号</FormLabel>
                      <FormControl>
                        <Input placeholder="例如：A-01" {...field} />
                      </FormControl>
                      <FormMessage />
                    </FormItem>
                  )}
                />

                {/* 饲养状态 */}
                <FormField
                  control={form.control}
                  name="feedStatus"
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel>饲养状态</FormLabel>
                      <Select onValueChange={field.onChange} value={field.value}>
                        <FormControl>
                          <SelectTrigger>
                            <SelectValue placeholder="请选择饲养状态" />
                          </SelectTrigger>
                        </FormControl>
                        <SelectContent>
                          <SelectItem value="NORMAL">正常</SelectItem>
                          <SelectItem value="WEANING">断奶</SelectItem>
                          <SelectItem value="FATTENING">育肥</SelectItem>
                        </SelectContent>
                      </Select>
                      <FormMessage />
                    </FormItem>
                  )}
                />
              </div>

              {/* 备注 */}
              <FormField
                control={form.control}
                name="remark"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>备注</FormLabel>
                    <FormControl>
                      <Textarea
                        placeholder="输入备注信息..."
                        className="min-h-[100px]"
                        {...field}
                      />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />

              {/* 操作按钮 */}
              <div className="flex justify-end gap-4">
                <Button type="button" variant="outline" onClick={() => navigate('/pig')}>
                  取消
                </Button>
                <Button type="submit" disabled={isLoading}>
                  {isLoading ? '提交中...' : isEdit ? '保存' : '创建'}
                </Button>
              </div>
            </form>
          </Form>
        </CardContent>
      </Card>
    </div>
  );
}
