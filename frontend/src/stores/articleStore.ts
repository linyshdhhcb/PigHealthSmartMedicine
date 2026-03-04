import { create } from 'zustand';
import type { Article, PageResult } from '@/types/pig';
import {
  getArticleList,
  getArticleById,
  createArticle,
  updateArticle,
  deleteArticle,
  publishArticle,
  likeArticle,
} from '@/services/pigService';
import { toast } from 'sonner';

interface ArticleState {
  // 状态
  articles: Article[];
  currentArticle: Article | null;
  total: number;
  pageNum: number;
  pageSize: number;
  isLoading: boolean;

  // 筛选条件
  filters: {
    category?: string;
    status?: string;
  };

  // 方法
  fetchArticles: () => Promise<void>;
  fetchArticleById: (id: number) => Promise<void>;
  setFilters: (filters: Partial<ArticleState['filters']>) => void;
  setPageNum: (pageNum: number) => void;
  setPageSize: (pageSize: number) => void;
  createArticle: (data: any) => Promise<number>;
  updateArticle: (id: number, data: any) => Promise<void>;
  deleteArticle: (id: number) => Promise<void>;
  publishArticle: (id: number) => Promise<void>;
  likeArticle: (id: number) => Promise<void>;
  reset: () => void;
}

export const useArticleStore = create<ArticleState>((set, get) => ({
  // 初始状态
  articles: [],
  currentArticle: null,
  total: 0,
  pageNum: 1,
  pageSize: 10,
  isLoading: false,
  filters: {},

  // 获取文章列表
  fetchArticles: async () => {
    const { pageNum, pageSize, filters } = get();
    set({ isLoading: true });

    try {
      const result: PageResult<Article> = await getArticleList(
        filters.category,
        filters.status,
        pageNum,
        pageSize
      );

      set({
        articles: result.records,
        total: result.total,
        pageNum: result.current,
        isLoading: false,
      });
    } catch (error: any) {
      toast.error(error?.message || '获取文章列表失败');
      set({ isLoading: false });
      throw error;
    }
  },

  // 获取文章详情
  fetchArticleById: async (id: number) => {
    set({ isLoading: true });

    try {
      const article = await getArticleById(id);
      set({ currentArticle: article, isLoading: false });
    } catch (error: any) {
      toast.error(error?.message || '获取文章详情失败');
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

  // 创建文章
  createArticle: async (data) => {
    try {
      const id = await createArticle(data);
      toast.success('创建成功');
      await get().fetchArticles();
      return id;
    } catch (error: any) {
      toast.error(error?.message || '创建失败');
      throw error;
    }
  },

  // 更新文章
  updateArticle: async (id, data) => {
    try {
      await updateArticle(id, data);
      toast.success('更新成功');
      await get().fetchArticles();
    } catch (error: any) {
      toast.error(error?.message || '更新失败');
      throw error;
    }
  },

  // 删除文章
  deleteArticle: async (id) => {
    try {
      await deleteArticle(id);
      toast.success('删除成功');
      await get().fetchArticles();
    } catch (error: any) {
      toast.error(error?.message || '删除失败');
      throw error;
    }
  },

  // 发布文章
  publishArticle: async (id) => {
    try {
      await publishArticle(id);
      toast.success('发布成功');
      await get().fetchArticles();
    } catch (error: any) {
      toast.error(error?.message || '发布失败');
      throw error;
    }
  },

  // 点赞文章
  likeArticle: async (id) => {
    try {
      await likeArticle(id);
      // 更新当前文章的点赞数
      const { currentArticle } = get();
      if (currentArticle && currentArticle.id === id) {
        set({
          currentArticle: {
            ...currentArticle,
            likeCount: currentArticle.likeCount + 1,
          },
        });
      }
    } catch (error: any) {
      toast.error(error?.message || '点赞失败');
      throw error;
    }
  },

  // 重置状态
  reset: () => {
    set({
      articles: [],
      currentArticle: null,
      total: 0,
      pageNum: 1,
      pageSize: 10,
      filters: {},
    });
  },
}));
