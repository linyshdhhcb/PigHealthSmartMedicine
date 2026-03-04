import React, { useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import * as z from 'zod';
import { useFarmStore } from '@/stores/farmStore';
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
  name: z.string().min(1, '请输入养殖场名称').max(100, '名称不能超过100个字符'),
  scale: z.string().min(1, '请选择养殖规模'),
  address: z.string().optional(),
  contactPhone: z.string().optional(),
  remark: z.string().optional(),
});

type FormValues = z.infer<typeof formSchema>;

export function FarmFormPage() {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const { currentFarm, isLoading, fetchFarmById, createFarm, updateFarm } = useFarmStore();
  const isEdit = !!id;

  const form = useForm<FormValues>({
    resolver: zodResolver(formSchema),
    defaultValues: {
      name: '',
      scale: '',
      address: '',
      contactPhone: '',
      remark: '',
    },
  });

  // 编辑模式：加载数据
  useEffect(() => {
    if (isEdit && id) {
      fetchFarmById(Number(id));
    }
  }, [isEdit, id]);

  // 填充表单数据
  useEffect(() => {
    if (isEdit && currentFarm) {
      form.reset({
        name: currentFarm.name,
        scale: currentFarm.scale,
        address: currentFarm.address || '',
        contactPhone: currentFarm.contactPhone || '',
        remark: currentFarm.remark || '',
      });
    }
  }, [currentFarm, isEdit]);

  const onSubmit = async (values: FormValues) => {
    try {
      const data = {
        name: values.name,
        scale: values.scale,
        address: values.address || undefined,
        contactPhone: values.contactPhone || undefined,
        remark: values.remark || undefined,
      };

      if (isEdit && id) {
        await updateFarm(Number(id), data);
      } else {
        await createFarm(data);
      }

      navigate('/farm');
    } catch (error) {
      // 错误已在store中处理
    }
  };

  return (
    <div className="container mx-auto py-6 space-y-6">
      {/* 页面标题 */}
      <div className="flex items-center gap-4">
        <Button variant="ghost" size="sm" onClick={() => navigate('/farm')}>
          <ArrowLeft className="h-4 w-4" />
        </Button>
        <div>
          <h1 className="text-3xl font-bold">{isEdit ? '编辑养殖场' : '新增养殖场'}</h1>
          <p className="text-muted-foreground mt-1">
            {isEdit ? '修改养殖场信息' : '添加新的养殖场'}
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
                {/* 养殖场名称 */}
                <FormField
                  control={form.control}
                  name="name"
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel>养殖场名称 *</FormLabel>
                      <FormControl>
                        <Input placeholder="例如：阳光养殖场" {...field} />
                      </FormControl>
                      <FormDescription>养殖场的名称</FormDescription>
                      <FormMessage />
                    </FormItem>
                  )}
                />

                {/* 养殖规模 */}
                <FormField
                  control={form.control}
                  name="scale"
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel>养殖规模 *</FormLabel>
                      <Select onValueChange={field.onChange} value={field.value}>
                        <FormControl>
                          <SelectTrigger>
                            <SelectValue placeholder="请选择规模" />
                          </SelectTrigger>
                        </FormControl>
                        <SelectContent>
                          <SelectItem value="SMALL">小型（500头以下）</SelectItem>
                          <SelectItem value="MEDIUM">中型（500-5000头）</SelectItem>
                          <SelectItem value="LARGE">大型（5000头以上）</SelectItem>
                        </SelectContent>
                      </Select>
                      <FormMessage />
                    </FormItem>
                  )}
                />

                {/* 联系电话 */}
                <FormField
                  control={form.control}
                  name="contactPhone"
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel>联系电话</FormLabel>
                      <FormControl>
                        <Input placeholder="例如：13800138000" {...field} />
                      </FormControl>
                      <FormMessage />
                    </FormItem>
                  )}
                />

                {/* 地址 */}
                <FormField
                  control={form.control}
                  name="address"
                  render={({ field }) => (
                    <FormItem className="md:col-span-2">
                      <FormLabel>地址</FormLabel>
                      <FormControl>
                        <Input placeholder="例如：XX省XX市XX区XX路XX号" {...field} />
                      </FormControl>
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
                <Button type="button" variant="outline" onClick={() => navigate('/farm')}>
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
