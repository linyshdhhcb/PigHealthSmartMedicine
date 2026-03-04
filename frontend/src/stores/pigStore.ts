import { create } from 'zustand';
import type { Pig, PageResult } from '@/types/pig';
import { getPigList, getPigById, createPig, updatePig, deletePig } from '@/services/pigService';
import { toast } from 'sonner';

interface PigState {
  // 状态
  pigs: Pig[];
  currentPig: Pig | null;
  total: number;
  pageNum: number;
  pageSize: number;
  isLoading: boolean;

  // 筛选条件
  filters: {
    farmId?: number;
    userId?: number;
    healthStatus?: string;
  };

  // 方法
  fetchPigs: () => Promise<void>;
  fetchPigById: (id: number) => Promise<void>;
  setFilters: (filters: Partial<PigState['filters']>) => void;
  setPageNum: (pageNum: number) => void;
  setPageSize: (pageSize: number) => void;
  createPig: (data: any) => Promise<number>;
  updatePig: (id: number, data: any) => Promise<void>;
  deletePig: (id: number) => Promise<void>;
  reset: () => void;
}

export const usePigStore = create<PigState>((set, get) => ({
  // 初始状态
  pigs: [],
  currentPig: null,
  total: 0,
  pageNum: 1,
  pageSize: 10,
  isLoading: false,
  filters: {},

  // 获取生猪列表
  fetchPigs: async () => {
    const { pageNum, pageSize, filters } = get();
    set({ isLoading: true });

    try {
      const result: PageResult<Pig> = await getPigList(
        filters.farmId,
        filters.userId,
        filters.healthStatus,
        pageNum,
        pageSize
      );

      set({
        pigs: result.records,
        total: result.total,
        pageNum: result.current,
        isLoading: false,
      });
    } catch (error: any) {
      toast.error(error?.message || '获取生猪列表失败');
      set({ isLoading: false });
      throw error;
    }
  },

  // 获取生猪详情
  fetchPigById: async (id: number) => {
    set({ isLoading: true });

    try {
      const pig = await getPigById(id);
      set({ currentPig: pig, isLoading: false });
    } catch (error: any) {
      toast.error(error?.message || '获取生猪详情失败');
      set({ isLoading: false });
      throw error;
    }
  },

  // 设置筛选条件
  setFilters: (filters) => {
    set((state) => ({
      filters: { ...state.filters, ...filters },
      pageNum: 1, // 重置页码
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

  // 创建生猪
  createPig: async (data) => {
    try {
      const id = await createPig(data);
      toast.success('创建成功');
      await get().fetchPigs(); // 刷新列表
      return id;
    } catch (error: any) {
      toast.error(error?.message || '创建失败');
      throw error;
    }
  },

  // 更新生猪
  updatePig: async (id, data) => {
    try {
      await updatePig(id, data);
      toast.success('更新成功');
      await get().fetchPigs(); // 刷新列表
    } catch (error: any) {
      toast.error(error?.message || '更新失败');
      throw error;
    }
  },

  // 删除生猪
  deletePig: async (id) => {
    try {
      await deletePig(id);
      toast.success('删除成功');
      await get().fetchPigs(); // 刷新列表
    } catch (error: any) {
      toast.error(error?.message || '删除失败');
      throw error;
    }
  },

  // 重置状态
  reset: () => {
    set({
      pigs: [],
      currentPig: null,
      total: 0,
      pageNum: 1,
      pageSize: 10,
      filters: {},
    });
  },
}));
