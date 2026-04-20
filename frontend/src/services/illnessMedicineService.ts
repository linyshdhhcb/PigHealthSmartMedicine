import { api } from "@/services/api";
import type { Illness } from "@/services/illnessService";
import type { Medicine } from "@/services/medicineService";

export interface IllnessMedicine {
  id: string;
  illnessId?: string | null;
  illnessName?: string | null;
  medicineId?: string | null;
  medicineName?: string | null;
  createTime?: string;
}

export interface PageResult<T> {
  records: T[];
  total: number;
  size: number;
  current: number;
  pages: number;
}

export const getIllnessMedicines = async (
  current = 1,
  size = 10,
  illnessId?: string,
  medicineId?: string
): Promise<PageResult<IllnessMedicine>> => {
  return api.get<PageResult<IllnessMedicine>, PageResult<IllnessMedicine>>("/illness-medicines", {
    params: { current, size, illnessId, medicineId }
  });
};

export const getIllnessMedicine = async (id: string): Promise<IllnessMedicine> => {
  return api.get<IllnessMedicine, IllnessMedicine>(`/illness-medicines/${id}`);
};

export const createIllnessMedicine = async (data: { illnessId: string; medicineId: string }): Promise<string> => {
  return api.post<string, string>("/illness-medicines", data);
};

export const deleteIllnessMedicine = async (id: string): Promise<void> => {
  await api.delete(`/illness-medicines/${id}`);
};

export const batchCreateIllnessMedicine = async (data: {
  illnessId: string;
  medicineIds: string[];
}): Promise<void> => {
  await api.post("/illness-medicines/batch", data);
};

export const batchDeleteIllnessMedicine = async (ids: string[]): Promise<void> => {
  await api.delete("/illness-medicines/batch", { data: { ids } });
};

export const getMedicinesByIllness = async (illnessId: string): Promise<Medicine[]> => {
  return api.get<Medicine[], Medicine[]>(`/illnesses/${illnessId}/medicines`);
};

export const getIllnessesByMedicine = async (medicineId: string): Promise<Illness[]> => {
  return api.get<Illness[], Illness[]>(`/medicines/${medicineId}/illnesses`);
};
