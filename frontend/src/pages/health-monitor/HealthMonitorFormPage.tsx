import React from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import * as z from 'zod';
import { useHealthMonitorStore } from '@/stores/healthMonitorStore';
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
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { ArrowLeft } from 'lucide-react';

const formSchema = z.object({
  pigId: z.string().min(1, '请输入生猪ID'),
  monitorDate: z.string().min(1, '请选择监测日期'),
  temperature: z.string().optional(),
  weight: z.string().optional(),
  appetite: z.string().optional(),
  mentalState: z.string().optional(),
  fecesCondition: z.string().optional(),
  respiratoryCondition: z.string().optional(),
  remark: z.string().optional(),
});

type FormValues = z.infer<typeof formSchema>;

export function HealthMonitorFormPage() {
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();
  const pigIdFromQuery = searchParams.get('pigId');

  const { isLoading, createHealthMonitor } = useHealthMonitorStore();

  const form = useForm<FormValues>({
    resolver: zodResolver(formSchema),
    defaultValues: {
      pigId: pigIdFromQuery || '',
      monitorDate: new Date().toISOString().split('T')[0],
      temperature: '',
      weight: '',
      appetite: '',
      mentalState: '',
      fecesCondition: '',
      respiratoryCondition: '',
      remark: '',
    },
  });

  const onSubmit = async (values: FormValues) => {
    try {
      const data = {
        pigId: Number(values.pigId),
        monitorDate: values.monitorDate,
        temperature: values.temperature ? Number(values.temperature) : undefined,
        weight: values.weight ? Number(values.weight) : undefined,
        appetite: values.appetite || undefined,
        mentalState: values.mentalState || undefined,
        fecesCondition: values.fecesCondition || undefined,
        respiratoryCondition: values.respiratoryCondition || undefined,
        remark: values.remark || undefined,
      };

      await createHealthMonitor(data);
      navigate('/health-monitor');
    } catch (error) {
      // 错误已在store中处理
    }
  };

  return (
    <div className="container mx-auto py-6 space-y-6">
      {/* 页面标题 */}
      <div className="flex items-center gap-4">
        <Button variant="ghost" size="sm" onClick={() => navigate('/health-monitor')}>
          <ArrowLeft className="h-4 w-4" />
        </Button>
        <div>
          <h1 className="text-3xl font-bold">添加健康监测</h1>
          <p className="text-muted-foreground mt-1">记录生猪健康状况</p>
        </div>
      </div>

      {/* 表单 */}
      <Card>
        <CardHeader>
          <CardTitle>监测信息</CardTitle>
        </CardHeader>
        <CardContent>
          <Form {...form}>
            <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-6">
              <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                {/* 生猪ID */}
                <FormField
                  control={form.control}
                  name="pigId"
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel>生猪ID *</FormLabel>
                      <FormControl>
                        <Input type="number" placeholder="例如：1" {...field} />
                      </FormControl>
                      <FormDescription>监测的生猪ID</FormDescription>
                      <FormMessage />
                    </FormItem>
                  )}
                />

                {/* 监测日期 */}
                <FormField
                  control={form.control}
                  name="monitorDate"
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel>监测日期 *</FormLabel>
                      <FormControl>
                        <Input type="date" {...field} />
                      </FormControl>
                      <FormMessage />
                    </FormItem>
                  )}
                />

                {/* 体温 */}
                <FormField
                  control={form.control}
                  name="temperature"
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel>体温（℃）</FormLabel>
                      <FormControl>
                        <Input type="number" step="0.1" placeholder="例如：38.5" {...field} />
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

                {/* 食欲 */}
                <FormField
                  control={form.control}
                  name="appetite"
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel>食欲</FormLabel>
                      <FormControl>
                        <Input placeholder="例如：正常、减退、废绝" {...field} />
                      </FormControl>
                      <FormMessage />
                    </FormItem>
                  )}
                />

                {/* 精神状态 */}
                <FormField
                  control={form.control}
                  name="mentalState"
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel>精神状态</FormLabel>
                      <FormControl>
                        <Input placeholder="例如：良好、沉郁、兴奋" {...field} />
                      </FormControl>
                      <FormMessage />
                    </FormItem>
                  )}
                />
              </div>

              {/* 粪便情况 */}
              <FormField
                control={form.control}
                name="fecesCondition"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>粪便情况</FormLabel>
                    <FormControl>
                      <Textarea
                        placeholder="描述粪便的颜色、形状、气味等..."
                        className="min-h-[100px]"
                        {...field}
                      />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />

              {/* 呼吸情况 */}
              <FormField
                control={form.control}
                name="respiratoryCondition"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>呼吸情况</FormLabel>
                    <FormControl>
                      <Textarea
                        placeholder="描述呼吸频率、是否有咳嗽等..."
                        className="min-h-[100px]"
                        {...field}
                      />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />

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
                <Button type="button" variant="outline" onClick={() => navigate('/health-monitor')}>
                  取消
                </Button>
                <Button type="submit" disabled={isLoading}>
                  {isLoading ? '提交中...' : '创建'}
                </Button>
              </div>
            </form>
          </Form>
        </CardContent>
      </Card>
    </div>
  );
}
