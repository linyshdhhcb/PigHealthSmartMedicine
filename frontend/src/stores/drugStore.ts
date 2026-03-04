import { create } from 'zustand';
import type { Drug, PageResult } from '@/types/pig';
import { getDrugList, getDrugById } from '@/services/pigService';
import { toast } from 'sonner';

interface DrugState {
  // 状态
  drugs: Drug[];
  currentDrug: Drug | null;
  total: number;
  pageNum: number;
  pageSize: number;
  isLoading: boolean;

  // 筛选条件
  filters: {
    type?: string;
    keyword?: string;
  };

  // 方法
  fetchDrugs: () => Promise<void>;
  fetchDrugById: (id: number) => Promise<void>;
  setFilters: (filters: Partial<DrugState['filters']>) => void;
  setPageNum: (pageNum: number) => void;
  setPageSize: (pageSize: number) => void;
  reset: () => void;
}

export const useDrugStore = create<DrugState>((set, get) => ({
  // 初始状态
  drugs: [],
  currentDrug: null,
  total: 0,
  pageNum: 1,
  pageSize: 10,
  isLoading: false,
  filters: {},

  // 获取兽药列表
  fetchDrugs: async () => {
    const { pageNum, pageSize, filters } = get();
    set({ isLoading: true });

    try {
      const result: PageResult<Drug> = await getDrugList(
        filters.type,
        filters.keyword,
        pageNum,
        pageSize
      );

      set({
        drugs: result.records,
        total: result.total,
        pageNum: result.current,
        isLoading: false,
      });
    } catch (error: any) {
      toast.error(error?.message || '获取兽药列表失败');
      set({ isLoading: false });
      throw error;
    }
  },

  // 获取兽药详情
  fetchDrugById: async (id: number) => {
    set({ isLoading: true });

    try {
      const drug = await getDrugById(id);
      set({ currentDrug: drug, isLoading: false });
    } catch (error: any) {
      toast.error(error?.message || '获取兽药详情失败');
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

  // 重置状态
  reset: () => {
    set({
      drugs: [],
      currentDrug: null,
      total: 0,
      pageNum: 1,
      pageSize: 10,
      filters: {},
    });
  },
}));
