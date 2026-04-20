import { api } from "@/services/api";

export interface ArticleType {
  id: string;
  typeName: string;
  createdBy?: string | null;
  updatedBy?: string | null;
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

export const getArticleTypes = async (
  current = 1,
  size = 10,
  typeName?: string
): Promise<PageResult<ArticleType>> => {
  return api.get<PageResult<ArticleType>, PageResult<ArticleType>>("/article-types", {
    params: { current, size, typeName }
  });
};

export const getArticleType = async (id: string): Promise<ArticleType> => {
  return api.get<ArticleType, ArticleType>(`/article-types/${id}`);
};

export const createArticleType = async (data: Partial<ArticleType>): Promise<string> => {
  return api.post<string, string>("/article-types", data);
};

export const updateArticleType = async (id: string, data: Partial<ArticleType>): Promise<void> => {
  await api.put(`/article-types/${id}`, data);
};

export const deleteArticleType = async (id: string): Promise<void> => {
  await api.delete(`/article-types/${id}`);
};
