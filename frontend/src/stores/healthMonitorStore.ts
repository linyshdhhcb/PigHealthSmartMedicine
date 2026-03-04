import { create } from 'zustand';
import type { HealthMonitor, PageResult } from '@/types/pig';
import {
  getHealthMonitorList,
  getHealthMonitorById,
  createHealthMonitor,
  updateHealthMonitor,
  deleteHealthMonitor,
} from '@/services/pigService';
import { toast } from 'sonner';

interface HealthMonitorState {
  // 状态
  healthMonitors: HealthMonitor[];
  currentHealthMonitor: HealthMonitor | null;
  total: number;
  pageNum: number;
  pageSize: number;
  isLoading: boolean;

  // 筛选条件
  filters: {
    pigId?: number;
  };

  // 方法
  fetchHealthMonitors: () => Promise<void>;
  fetchHealthMonitorById: (id: number) => Promise<void>;
  setFilters: (filters: Partial<HealthMonitorState['filters']>) => void;
  setPageNum: (pageNum: number) => void;
  setPageSize: (pageSize: number) => void;
  createHealthMonitor: (data: any) => Promise<number>;
  updateHealthMonitor: (id: number, data: any) => Promise<void>;
  deleteHealthMonitor: (id: number) => Promise<void>;
  reset: () => void;
}

export const useHealthMonitorStore = create<HealthMonitorState>((set, get) => ({
  // 初始状态
  healthMonitors: [],
  currentHealthMonitor: null,
  total: 0,
  pageNum: 1,
  pageSize: 10,
  isLoading: false,
  filters: {},

  // 获取健康监测列表
  fetchHealthMonitors: async () => {
    const { pageNum, pageSize, filters } = get();
    set({ isLoading: true });

    try {
      const result: PageResult<HealthMonitor> = await getHealthMonitorList(
        filters.pigId,
        pageNum,
        pageSize
      );

      set({
        healthMonitors: result.records,
        total: result.total,
        pageNum: result.current,
        isLoading: false,
      });
    } catch (error: any) {
      toast.error(error?.message || '获取健康监测列表失败');
      set({ isLoading: false });
      throw error;
    }
  },

  // 获取健康监测详情
  fetchHealthMonitorById: async (id: number) => {
    set({ isLoading: true });

    try {
      const monitor = await getHealthMonitorById(id);
      set({ currentHealthMonitor: monitor, isLoading: false });
    } catch (error: any) {
      toast.error(error?.message || '获取健康监测详情失败');
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

  // 创建健康监测
  createHealthMonitor: async (data) => {
    try {
      const id = await createHealthMonitor(data);
      toast.success('创建成功');
      await get().fetchHealthMonitors();
      return id;
    } catch (error: any) {
      toast.error(error?.message || '创建失败');
      throw error;
    }
  },

  // 更新健康监测
  updateHealthMonitor: async (id, data) => {
    try {
      await updateHealthMonitor(id, data);
      toast.success('更新成功');
      await get().fetchHealthMonitors();
    } catch (error: any) {
      toast.error(error?.message || '更新失败');
      throw error;
    }
  },

  // 删除健康监测
  deleteHealthMonitor: async (id) => {
    try {
      await deleteHealthMonitor(id);
      toast.success('删除成功');
      await get().fetchHealthMonitors();
    } catch (error: any) {
      toast.error(error?.message || '删除失败');
      throw error;
    }
  },

  // 重置状态
  reset: () => {
    set({
      healthMonitors: [],
      currentHealthMonitor: null,
      total: 0,
      pageNum: 1,
      pageSize: 10,
      filters: {},
    });
  },
}));
