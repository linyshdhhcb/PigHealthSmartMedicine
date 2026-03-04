import React from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import * as z from 'zod';
import { useTreatmentRecordStore } from '@/stores/treatmentRecordStore';
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
  caseId: z.string().min(1, '请输入病例ID'),
  pigId: z.string().min(1, '请输入生猪ID'),
  treatmentDate: z.string().min(1, '请选择治疗日期'),
  treatmentMethod: z.string().optional(),
  medication: z.string().optional(),
  dosage: z.string().optional(),
  effect: z.string().optional(),
  remark: z.string().optional(),
});

type FormValues = z.infer<typeof formSchema>;

export function TreatmentRecordFormPage() {
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();
  const caseIdFromQuery = searchParams.get('caseId');
  const pigIdFromQuery = searchParams.get('pigId');

  const { isLoading, createTreatmentRecord } = useTreatmentRecordStore();

  const form = useForm<FormValues>({
    resolver: zodResolver(formSchema),
    defaultValues: {
      caseId: caseIdFromQuery || '',
      pigId: pigIdFromQuery || '',
      treatmentDate: new Date().toISOString().split('T')[0],
      treatmentMethod: '',
      medication: '',
      dosage: '',
      effect: '',
      remark: '',
    },
  });

  const onSubmit = async (values: FormValues) => {
    try {
      const data = {
        caseId: Number(values.caseId),
        pigId: Number(values.pigId),
        treatmentDate: values.treatmentDate,
        treatmentMethod: values.treatmentMethod || undefined,
        medication: values.medication || undefined,
        dosage: values.dosage || undefined,
        effect: values.effect || undefined,
        remark: values.remark || undefined,
      };

      await createTreatmentRecord(data);
      navigate('/treatment-record');
    } catch (error) {
      // 错误已在store中处理
    }
  };

  return (
    <div className="container mx-auto py-6 space-y-6">
      {/* 页面标题 */}
      <div className="flex items-center gap-4">
        <Button variant="ghost" size="sm" onClick={() => navigate('/treatment-record')}>
          <ArrowLeft className="h-4 w-4" />
        </Button>
        <div>
          <h1 className="text-3xl font-bold">添加治疗记录</h1>
          <p className="text-muted-foreground mt-1">记录治疗过程和效果</p>
        </div>
      </div>

      {/* 表单 */}
      <Card>
        <CardHeader>
          <CardTitle>治疗信息</CardTitle>
        </CardHeader>
        <CardContent>
          <Form {...form}>
            <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-6">
              <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                {/* 病例ID */}
                <FormField
                  control={form.control}
                  name="caseId"
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel>病例ID *</FormLabel>
                      <FormControl>
                        <Input type="number" placeholder="例如：1" {...field} />
                      </FormControl>
                      <FormDescription>关联的病例ID</FormDescription>
                      <FormMessage />
                    </FormItem>
                  )}
                />

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
                      <FormDescription>治疗的生猪ID</FormDescription>
                      <FormMessage />
                    </FormItem>
                  )}
                />

                {/* 治疗日期 */}
                <FormField
                  control={form.control}
                  name="treatmentDate"
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel>治疗日期 *</FormLabel>
                      <FormControl>
                        <Input type="date" {...field} />
                      </FormControl>
                      <FormMessage />
                    </FormItem>
                  )}
                />

                {/* 用药剂量 */}
                <FormField
                  control={form.control}
                  name="dosage"
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel>用药剂量</FormLabel>
                      <FormControl>
                        <Input placeholder="例如：10mg/kg" {...field} />
                      </FormControl>
                      <FormMessage />
                    </FormItem>
                  )}
                />
              </div>

              {/* 治疗方法 */}
              <FormField
                control={form.control}
                name="treatmentMethod"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>治疗方法</FormLabel>
                    <FormControl>
                      <Textarea
                        placeholder="描述治疗方法..."
                        className="min-h-[100px]"
                        {...field}
                      />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />

              {/* 用药情况 */}
              <FormField
                control={form.control}
                name="medication"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>用药情况</FormLabel>
                    <FormControl>
                      <Textarea
                        placeholder="描述用药情况..."
                        className="min-h-[100px]"
                        {...field}
                      />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />

              {/* 治疗效果 */}
              <FormField
                control={form.control}
                name="effect"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>治疗效果</FormLabel>
                    <FormControl>
                      <Textarea
                        placeholder="描述治疗效果..."
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
                <Button type="button" variant="outline" onClick={() => navigate('/treatment-record')}>
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
