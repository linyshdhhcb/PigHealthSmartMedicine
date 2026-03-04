import { create } from 'zustand';
import type { TreatmentRecord, PageResult } from '@/types/pig';
import {
  getTreatmentRecordList,
  getTreatmentRecordById,
  createTreatmentRecord,
  updateTreatmentRecord,
  deleteTreatmentRecord,
} from '@/services/pigService';
import { toast } from 'sonner';

interface TreatmentRecordState {
  // 状态
  treatmentRecords: TreatmentRecord[];
  currentTreatmentRecord: TreatmentRecord | null;
  total: number;
  pageNum: number;
  pageSize: number;
  isLoading: boolean;

  // 筛选条件
  filters: {
    caseId?: number;
    pigId?: number;
  };

  // 方法
  fetchTreatmentRecords: () => Promise<void>;
  fetchTreatmentRecordById: (id: number) => Promise<void>;
  setFilters: (filters: Partial<TreatmentRecordState['filters']>) => void;
  setPageNum: (pageNum: number) => void;
  setPageSize: (pageSize: number) => void;
  createTreatmentRecord: (data: any) => Promise<number>;
  updateTreatmentRecord: (id: number, data: any) => Promise<void>;
  deleteTreatmentRecord: (id: number) => Promise<void>;
  reset: () => void;
}

export const useTreatmentRecordStore = create<TreatmentRecordState>((set, get) => ({
  // 初始状态
  treatmentRecords: [],
  currentTreatmentRecord: null,
  total: 0,
  pageNum: 1,
  pageSize: 10,
  isLoading: false,
  filters: {},

  // 获取治疗记录列表
  fetchTreatmentRecords: async () => {
    const { pageNum, pageSize, filters } = get();
    set({ isLoading: true });

    try {
      const result: PageResult<TreatmentRecord> = await getTreatmentRecordList(
        filters.caseId,
        filters.pigId,
        pageNum,
        pageSize
      );

      set({
        treatmentRecords: result.records,
        total: result.total,
        pageNum: result.current,
        isLoading: false,
      });
    } catch (error: any) {
      toast.error(error?.message || '获取治疗记录列表失败');
      set({ isLoading: false });
      throw error;
    }
  },

  // 获取治疗记录详情
  fetchTreatmentRecordById: async (id: number) => {
    set({ isLoading: true });

    try {
      const record = await getTreatmentRecordById(id);
      set({ currentTreatmentRecord: record, isLoading: false });
    } catch (error: any) {
      toast.error(error?.message || '获取治疗记录详情失败');
      set({ isLoading: false });
      throw error;
    }
  },

  // 设置筛选条件
  setFilters: (filters) => {
    set((state) => ({
      filters: { ...state.filters, ...filters },
      pageNum: 1,
    }));
  },

  // 设置页码
  setPageNum: (pageNum) => {
    set({ pageNum });
  },

  // 设置每页数量
  setPageSize: (pageSize) => {
    set({ pageSize, pageNum: 1 });
  },

  // 创建治疗记录
  createTreatmentRecord: async (data) => {
    try {
      const id = await createTreatmentRecord(data);
      toast.success('创建成功');
      await get().fetchTreatmentRecords();
      return id;
    } catch (error: any) {
      toast.error(error?.message || '创建失败');
      throw error;
    }
  },

  // 更新治疗记录
  updateTreatmentRecord: async (id, data) => {
    try {
      await updateTreatmentRecord(id, data);
      toast.success('更新成功');
      await get().fetchTreatmentRecords();
    } catch (error: any) {
      toast.error(error?.message || '更新失败');
      throw error;
    }
  },

  // 删除治疗记录
  deleteTreatmentRecord: async (id) => {
    try {
      await deleteTreatmentRecord(id);
      toast.success('删除成功');
      await get().fetchTreatmentRecords();
    } catch (error: any) {
      toast.error(error?.message || '删除失败');
      throw error;
    }
  },

  // 重置状态
  reset: () => {
    set({
      treatmentRecords: [],
      currentTreatmentRecord: null,
      total: 0,
      pageNum: 1,
      pageSize: 10,
      filters: {},
    });
  },
}));
