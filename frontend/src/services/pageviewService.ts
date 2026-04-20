import { api } from "@/services/api";

export interface Pageview {
  id: string;
  pageviews?: number | null;
  illnessId?: string | null;
  illnessName?: string | null;
  updateTime?: string;
}

export interface PageResult<T> {
  records: T[];
  total: number;
  size: number;
  current: number;
  pages: number;
}

export interface HotIllness {
  illnessId: string;
  illnessName: string;
  pageviews: number;
}

export const getPageviews = async (
  current = 1,
  size = 10
): Promise<PageResult<Pageview>> => {
  return api.get<PageResult<Pageview>, PageResult<Pageview>>("/pageviews", {
    params: { current, size }
  });
};

export const getPageview = async (id: string): Promise<Pageview> => {
  return api.get<Pageview, Pageview>(`/pageviews/${id}`);
};

export const incrementPageview = async (illnessId: string): Promise<void> => {
  await api.post(`/pageviews/${illnessId}/increment`);
};

export const getHotIllnesses = async (limit = 10): Promise<HotIllness[]> => {
  return api.get<HotIllness[], HotIllness[]>("/pageviews/hot-illnesses", {
    params: { limit }
  });
};

export const getPageviewStatistics = async (): Promise<{
  totalPageviews: number;
  todayPageviews: number;
  topIllnesses: HotIllness[];
}> => {
  return api.get("/pageviews/statistics");
};
