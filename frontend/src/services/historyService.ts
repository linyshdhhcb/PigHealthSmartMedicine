import { api } from "@/services/api";

export interface History {
  id: string;
  userId?: string | null;
  keyword?: string | null;
  operateType?: number | null;
  createTime?: string;
}

export interface PageResult<T> {
  records: T[];
  total: number;
  size: number;
  current: number;
  pages: number;
}

export const OPERATE_TYPE_OPTIONS = [
  { value: 1, label: "搜索" },
  { value: 2, label: "科目" },
  { value: 3, label: "药品" }
];

export const getHistories = async (
  current = 1,
  size = 10,
  operateType?: number,
  keyword?: string
): Promise<PageResult<History>> => {
  return api.get<PageResult<History>, PageResult<History>>("/histories", {
    params: { current, size, operateType, keyword }
  });
};

export const getHistory = async (id: string): Promise<History> => {
  return api.get<History, History>(`/histories/${id}`);
};

export const deleteHistory = async (id: string): Promise<void> => {
  await api.delete(`/histories/${id}`);
};

export const clearHistories = async (): Promise<void> => {
  await api.delete("/histories/clear");
};

export const recordHistory = async (data: { keyword: string; operateType: number }): Promise<void> => {
  await api.post("/histories/record", data);
};
