import { api } from "@/services/api";

export interface IllnessKind {
  id: string;
  name?: string | null;
  info?: string | null;
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

export const getIllnessKinds = async (
  current = 1,
  size = 10,
  name?: string
): Promise<PageResult<IllnessKind>> => {
  return api.get<PageResult<IllnessKind>, PageResult<IllnessKind>>("/illness-kinds", {
    params: { current, size, name }
  });
};

export const getIllnessKind = async (id: string): Promise<IllnessKind> => {
  return api.get<IllnessKind, IllnessKind>(`/illness-kinds/${id}`);
};

export const createIllnessKind = async (data: Partial<IllnessKind>): Promise<string> => {
  return api.post<string, string>("/illness-kinds", data);
};

export const updateIllnessKind = async (id: string, data: Partial<IllnessKind>): Promise<void> => {
  await api.put(`/illness-kinds/${id}`, data);
};

export const deleteIllnessKind = async (id: string): Promise<void> => {
  await api.delete(`/illness-kinds/${id}`);
};

export const getAllIllnessKinds = async (): Promise<IllnessKind[]> => {
  const result = await api.get<PageResult<IllnessKind>, PageResult<IllnessKind>>("/illness-kinds", {
    params: { current: 1, size: 1000 }
  });
  return result.records || [];
};
