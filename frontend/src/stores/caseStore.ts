import { create } from 'zustand';
import type { Case, PageResult } from '@/types/pig';
import { getCaseList, getCaseById, createCase, updateCase, deleteCase } from '@/services/pigService';
import { toast } from 'sonner';

interface CaseState {
  // 状态
  cases: Case[];
  currentCase: Case | null;
  total: number;
  pageNum: number;
  pageSize: number;
  isLoading: boolean;

  // 筛选条件
  filters: {
    pigId?: number;
    userId?: number;
    status?: string;
  };

  // 方法
  fetchCases: () => Promise<void>;
  fetchCaseById: (id: number) => Promise<void>;
  setFilters: (filters: Partial<CaseState['filters']>) => void;
  setPageNum: (pageNum: number) => void;
  setPageSize: (pageSize: number) => void;
  createCase: (data: any) => Promise<number>;
  updateCase: (id: number, data: any) => Promise<void>;
  deleteCase: (id: number) => Promise<void>;
  reset: () => void;
}

export const useCaseStore = create<CaseState>((set, get) => ({
  // 初始状态
  cases: [],
  currentCase: null,
  total: 0,
  pageNum: 1,
  pageSize: 10,
  isLoading: false,
  filters: {},

  // 获取病例列表
  fetchCases: async () => {
    const { pageNum, pageSize, filters } = get();
    set({ isLoading: true });

    try {
      const result: PageResult<Case> = await getCaseList(
        filters.pigId,
        filters.userId,
        filters.status,
        pageNum,
        pageSize
      );

      set({
        cases: result.records,
        total: result.total,
        pageNum: result.current,
        isLoading: false,
      });
    } catch (error: any) {
      toast.error(error?.message || '获取病例列表失败');
      set({ isLoading: false });
      throw error;
    }
  },

  // 获取病例详情
  fetchCaseById: async (id: number) => {
    set({ isLoading: true });

    try {
      const caseData = await getCaseById(id);
      set({ currentCase: caseData, isLoading: false });
    } catch (error: any) {
      toast.error(error?.message || '获取病例详情失败');
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

  // 创建病例
  createCase: async (data) => {
    try {
      const id = await createCase(data);
      toast.success('创建成功');
      await get().fetchCases();
      return id;
    } catch (error: any) {
      toast.error(error?.message || '创建失败');
      throw error;
    }
  },

  // 更新病例
  updateCase: async (id, data) => {
    try {
      await updateCase(id, data);
      toast.success('更新成功');
      await get().fetchCases();
    } catch (error: any) {
      toast.error(error?.message || '更新失败');
      throw error;
    }
  },

  // 删除病例
  deleteCase: async (id) => {
    try {
      await deleteCase(id);
      toast.success('删除成功');
      await get().fetchCases();
    } catch (error: any) {
      toast.error(error?.message || '删除失败');
      throw error;
    }
  },

  // 重置状态
  reset: () => {
    set({
      cases: [],
      currentCase: null,
      total: 0,
      pageNum: 1,
      pageSize: 10,
      filters: {},
    });
  },
}));
