import { api } from "@/services/api";

export interface NewsArticle {
  id: string;
  url?: string | null;
  title: string;
  content: string;
  author?: string | null;
  publishTime?: string;
  source?: string | null;
  summary?: string | null;
  createTime?: string;
  updateTime?: string;
}

export interface PageResult<T> {
  records: T[];
  total: number;
  size: number;
  current: number;
  pages: number;
}

export const getNewsArticles = async (
  current = 1,
  size = 10,
  keyword?: string,
  source?: string
): Promise<PageResult<NewsArticle>> => {
  return api.get<PageResult<NewsArticle>, PageResult<NewsArticle>>("/news-articles", {
    params: { current, size, keyword, source }
  });
};

export const getNewsArticle = async (id: string): Promise<NewsArticle> => {
  return api.get<NewsArticle, NewsArticle>(`/news-articles/${id}`);
};

export const createNewsArticle = async (data: Partial<NewsArticle>): Promise<string> => {
  return api.post<string, string>("/news-articles", data);
};

export const updateNewsArticle = async (id: string, data: Partial<NewsArticle>): Promise<void> => {
  await api.put(`/news-articles/${id}`, data);
};

export const deleteNewsArticle = async (id: string): Promise<void> => {
  await api.delete(`/news-articles/${id}`);
};
