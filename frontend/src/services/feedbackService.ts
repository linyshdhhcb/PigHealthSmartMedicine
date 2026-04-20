import { api } from "@/services/api";

export interface Feedback {
  id: string;
  name?: string | null;
  email?: string | null;
  title?: string | null;
  content?: string | null;
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

export const getFeedbacks = async (
  current = 1,
  size = 10,
  keyword?: string
): Promise<PageResult<Feedback>> => {
  return api.get<PageResult<Feedback>, PageResult<Feedback>>("/feedbacks", {
    params: { current, size, keyword }
  });
};

export const getFeedback = async (id: string): Promise<Feedback> => {
  return api.get<Feedback, Feedback>(`/feedbacks/${id}`);
};

export const deleteFeedback = async (id: string): Promise<void> => {
  await api.delete(`/feedbacks/${id}`);
};

export const submitFeedback = async (data: Partial<Feedback>): Promise<string> => {
  return api.post<string, string>("/feedbacks", data);
};
