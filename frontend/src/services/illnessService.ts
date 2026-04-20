import { api } from "@/services/api";
import type { IllnessKind } from "@/services/illnessKindService";

export interface Illness {
  id: string;
  kindId?: string | null;
  kindName?: string | null;
  illnessName?: string | null;
  includeReason?: string | null;
  illnessSymptom?: string | null;
  specialSymptom?: string | null;
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

export const getIllnesses = async (
  current = 1,
  size = 10,
  kindId?: string,
  keyword?: string
): Promise<PageResult<Illness>> => {
  return api.get<PageResult<Illness>, PageResult<Illness>>("/illnesses", {
    params: { current, size, kindId, keyword }
  });
};

export const getIllness = async (id: string): Promise<Illness> => {
  return api.get<Illness, Illness>(`/illnesses/${id}`);
};

export const createIllness = async (data: Partial<Illness>): Promise<string> => {
  return api.post<string, string>("/illnesses", data);
};

export const updateIllness = async (id: string, data: Partial<Illness>): Promise<void> => {
  await api.put(`/illnesses/${id}`, data);
};

export const deleteIllness = async (id: string): Promise<void> => {
  await api.delete(`/illnesses/${id}`);
};

export const getAllIllnessKinds = async (): Promise<IllnessKind[]> => {
  const result = await api.get<PageResult<IllnessKind>, PageResult<IllnessKind>>("/illness-kinds", {
    params: { current: 1, size: 1000 }
  });
  return result.records || [];
};
