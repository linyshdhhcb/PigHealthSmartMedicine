import React, { useEffect } from 'react';
import { useParams, useNavigate, useSearchParams } from 'react-router-dom';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import * as z from 'zod';
import { useCaseStore } from '@/stores/caseStore';
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
  pigId: z.string().min(1, '请输入生猪ID'),
  symptomDescription: z.string().min(1, '请输入症状描述').max(2000, '症状描述不能超过2000个字符'),
  diagnosisResult: z.string().optional(),
  treatmentPlan: z.string().optional(),
  medicationRecord: z.string().optional(),
  treatmentEffect: z.string().optional(),
  status: z.string().optional(),
  remark: z.string().optional(),
});

type FormValues = z.infer<typeof formSchema>;

export function CaseFormPage() {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();
  const pigIdFromQuery = searchParams.get('pigId');

  const { currentCase, isLoading, fetchCaseById, createCase, updateCase } = useCaseStore();
  const isEdit = !!id;

  const form = useForm<FormValues>({
    resolver: zodResolver(formSchema),
    defaultValues: {
      pigId: pigIdFromQuery || '',
      symptomDescription: '',
      diagnosisResult: '',
      treatmentPlan: '',
      medicationRecord: '',
      treatmentEffect: '',
      status: 'PENDING',
      remark: '',
    },
  });

  // 编辑模式：加载数据
  useEffect(() => {
    if (isEdit && id) {
      fetchCaseById(Number(id));
    }
  }, [isEdit, id]);

  // 填充表单数据
  useEffect(() => {
    if (isEdit && currentCase) {
      form.reset({
        pigId: String(currentCase.pigId),
        symptomDescription: currentCase.symptomDescription,
        diagnosisResult: currentCase.diagnosisResult || '',
        treatmentPlan: currentCase.treatmentPlan || '',
        medicationRecord: currentCase.medicationRecord || '',
        treatmentEffect: currentCase.treatmentEffect || '',
        status: currentCase.status || 'PENDING',
        remark: currentCase.remark || '',
      });
    }
  }, [currentCase, isEdit]);

  const onSubmit = async (values: FormValues) => {
    try {
      const data = {
        pigId: Number(values.pigId),
        symptomDescription: values.symptomDescription,
        diagnosisResult: values.diagnosisResult || undefined,
        treatmentPlan: values.treatmentPlan || undefined,
        medicationRecord: values.medicationRecord || undefined,
        treatmentEffect: values.treatmentEffect || undefined,
        status: values.status || undefined,
        remark: values.remark || undefined,
      };

      if (isEdit && id) {
        await updateCase(Number(id), data);
      } else {
        await createCase(data);
      }

      navigate('/case');
    } catch (error) {
      // 错误已在store中处理
    }
  };

  return (
    <div className="container mx-auto py-6 space-y-6">
      {/* 页面标题 */}
      <div className="flex items-center gap-4">
        <Button variant="ghost" size="sm" onClick={() => navigate('/case')}>
          <ArrowLeft className="h-4 w-4" />
        </Button>
        <div>
          <h1 className="text-3xl font-bold">{isEdit ? '编辑病例' : '创建病例'}</h1>
          <p className="text-muted-foreground mt-1">
            {isEdit ? '修改病例信息' : '添加新的病例记录'}
          </p>
        </div>
      </div>

      {/* 表单 */}
      <Card>
        <CardHeader>
          <CardTitle>病例信息</CardTitle>
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
                      <FormDescription>请输入生猪的ID</FormDescription>
                      <FormMessage />
                    </FormItem>
                  )}
                />

                {/* 病例状态 */}
                <FormField
                  control={form.control}
                  name="status"
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel>病例状态</FormLabel>
                      <Select onValueChange={field.onChange} value={field.value}>
                        <FormControl>
                          <SelectTrigger>
                            <SelectValue placeholder="请选择状态" />
                          </SelectTrigger>
                        </FormControl>
                        <SelectContent>
                          <SelectItem value="PENDING">待处理</SelectItem>
                          <SelectItem value="IN_PROGRESS">处理中</SelectItem>
                          <SelectItem value="COMPLETED">已完成</SelectItem>
                          <SelectItem value="CLOSED">已关闭</SelectItem>
                        </SelectContent>
                      </Select>
                      <FormMessage />
                    </FormItem>
                  )}
                />
              </div>

              {/* 症状描述 */}
              <FormField
                control={form.control}
                name="symptomDescription"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>症状描述 *</FormLabel>
                    <FormControl>
                      <Textarea
                        placeholder="详细描述生猪的症状表现..."
                        className="min-h-[120px]"
                        {...field}
                      />
                    </FormControl>
                    <FormDescription>请详细描述生猪的症状表现</FormDescription>
                    <FormMessage />
                  </FormItem>
                )}
              />

              {/* 诊断结果 */}
              <FormField
                control={form.control}
                name="diagnosisResult"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>诊断结果</FormLabel>
                    <FormControl>
                      <Textarea
                        placeholder="输入诊断结果..."
                        className="min-h-[100px]"
                        {...field}
                      />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />

              {/* 治疗方案 */}
              <FormField
                control={form.control}
                name="treatmentPlan"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>治疗方案</FormLabel>
                    <FormControl>
                      <Textarea
                        placeholder="输入治疗方案..."
                        className="min-h-[100px]"
                        {...field}
                      />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />

              {/* 用药记录 */}
              <FormField
                control={form.control}
                name="medicationRecord"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>用药记录</FormLabel>
                    <FormControl>
                      <Textarea
                        placeholder="输入用药记录..."
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
                name="treatmentEffect"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>治疗效果</FormLabel>
                    <FormControl>
                      <Textarea
                        placeholder="输入治疗效果..."
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
                <Button type="button" variant="outline" onClick={() => navigate('/case')}>
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
