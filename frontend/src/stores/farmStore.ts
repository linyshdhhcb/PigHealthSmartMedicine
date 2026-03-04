import { create } from 'zustand';
import type { Farm, PageResult } from '@/types/pig';
import { getFarmList, getFarmById, createFarm, updateFarm, deleteFarm } from '@/services/pigService';
import { toast } from 'sonner';

interface FarmState {
  // 状态
  farms: Farm[];
  currentFarm: Farm | null;
  total: number;
  pageNum: number;
  pageSize: number;
  isLoading: boolean;

  // 筛选条件
  filters: {
    ownerId?: number;
    scale?: string;
  };

  // 方法
  fetchFarms: () => Promise<void>;
  fetchFarmById: (id: number) => Promise<void>;
  setFilters: (filters: Partial<FarmState['filters']>) => void;
  setPageNum: (pageNum: number) => void;
  setPageSize: (pageSize: number) => void;
  createFarm: (data: any) => Promise<number>;
  updateFarm: (id: number, data: any) => Promise<void>;
  deleteFarm: (id: number) => Promise<void>;
  reset: () => void;
}

export const useFarmStore = create<FarmState>((set, get) => ({
  // 初始状态
  farms: [],
  currentFarm: null,
  total: 0,
  pageNum: 1,
  pageSize: 10,
  isLoading: false,
  filters: {},

  // 获取养殖场列表
  fetchFarms: async () => {
    const { pageNum, pageSize, filters } = get();
    set({ isLoading: true });

    try {
      const result: PageResult<Farm> = await getFarmList(
        filters.ownerId,
        filters.scale,
        pageNum,
        pageSize
      );

      set({
        farms: result.records,
        total: result.total,
        pageNum: result.current,
        isLoading: false,
      });
    } catch (error: any) {
      toast.error(error?.message || '获取养殖场列表失败');
      set({ isLoading: false });
      throw error;
    }
  },

  // 获取养殖场详情
  fetchFarmById: async (id: number) => {
    set({ isLoading: true });

    try {
      const farm = await getFarmById(id);
      set({ currentFarm: farm, isLoading: false });
    } catch (error: any) {
      toast.error(error?.message || '获取养殖场详情失败');
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

  // 创建养殖场
  createFarm: async (data) => {
    try {
      const id = await createFarm(data);
      toast.success('创建成功');
      await get().fetchFarms();
      return id;
    } catch (error: any) {
      toast.error(error?.message || '创建失败');
      throw error;
    }
  },

  // 更新养殖场
  updateFarm: async (id, data) => {
    try {
      await updateFarm(id, data);
      toast.success('更新成功');
      await get().fetchFarms();
    } catch (error: any) {
      toast.error(error?.message || '更新失败');
      throw error;
    }
  },

  // 删除养殖场
  deleteFarm: async (id) => {
    try {
      await deleteFarm(id);
      toast.success('删除成功');
      await get().fetchFarms();
    } catch (error: any) {
      toast.error(error?.message || '删除失败');
      throw error;
    }
  },

  // 重置状态
  reset: () => {
    set({
      farms: [],
      currentFarm: null,
      total: 0,
      pageNum: 1,
      pageSize: 10,
      filters: {},
    });
  },
}));
