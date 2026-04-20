import { api } from "@/services/api";
import type { ArticleType } from "@/services/articleTypeService";

export interface Article {
  id: string;
  title: string;
  content: string;
  author?: string | null;
  typeId?: string | null;
  typeName?: string | null;
  createdBy?: string | null;
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

export const getArticles = async (
  current = 1,
  size = 10,
  typeId?: string,
  keyword?: string
): Promise<PageResult<Article>> => {
  return api.get<PageResult<Article>, PageResult<Article>>("/articles", {
    params: { current, size, typeId, keyword }
  });
};

export const getArticle = async (id: string): Promise<Article> => {
  return api.get<Article, Article>(`/articles/${id}`);
};

export const createArticle = async (data: Partial<Article>): Promise<string> => {
  return api.post<string, string>("/articles", data);
};

export const updateArticle = async (id: string, data: Partial<Article>): Promise<void> => {
  await api.put(`/articles/${id}`, data);
};

export const deleteArticle = async (id: string): Promise<void> => {
  await api.delete(`/articles/${id}`);
};

export const getAllArticleTypes = async (): Promise<ArticleType[]> => {
  const result = await api.get<PageResult<ArticleType>, PageResult<ArticleType>>("/article-types", {
    params: { current: 1, size: 1000 }
  });
  return result.records || [];
};
