import { api } from "@/services/api";

export interface Medicine {
  id: string;
  medicineName?: string | null;
  keyword?: string | null;
  medicineEffect?: string | null;
  medicineBrand?: string | null;
  interaction?: string | null;
  taboo?: string | null;
  usAge?: string | null;
  medicineType?: number | null;
  imgPath?: string | null;
  medicinePrice?: number | null;
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

export const MEDICINE_TYPE_OPTIONS = [
  { value: 0, label: "西药" },
  { value: 1, label: "中药" },
  { value: 2, label: "中成药" }
];

export const getMedicines = async (
  current = 1,
  size = 10,
  medicineType?: number,
  keyword?: string
): Promise<PageResult<Medicine>> => {
  return api.get<PageResult<Medicine>, PageResult<Medicine>>("/medicines", {
    params: { current, size, medicineType, keyword }
  });
};

export const getMedicine = async (id: string): Promise<Medicine> => {
  return api.get<Medicine, Medicine>(`/medicines/${id}`);
};

export const createMedicine = async (data: Partial<Medicine>): Promise<string> => {
  return api.post<string, string>("/medicines", data);
};

export const updateMedicine = async (id: string, data: Partial<Medicine>): Promise<void> => {
  await api.put(`/medicines/${id}`, data);
};

export const deleteMedicine = async (id: string): Promise<void> => {
  await api.delete(`/medicines/${id}`);
};

export const uploadMedicineImage = async (file: File): Promise<string> => {
  const formData = new FormData();
  formData.append("file", file);
  return api.post<string, string>("/medicines/upload-image", formData, {
    headers: {
      "Content-Type": "multipart/form-data"
    }
  });
};
