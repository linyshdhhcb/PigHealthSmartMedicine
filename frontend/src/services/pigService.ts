import { api } from './api';
import type {
  Pig,
  PigFormData,
  Case,
  CaseFormData,
  Drug,
  Article,
  ArticleFormData,
  Farm,
  FarmFormData,
  TreatmentRecord,
  TreatmentRecordFormData,
  HealthMonitor,
  HealthMonitorFormData,
  PageResult,
} from '@/types/pig';

// ==================== 生猪管理 API ====================

export const getPigList = async (
  farmId?: number,
  userId?: number,
  healthStatus?: string,
  pageNum = 1,
  pageSize = 10
): Promise<PageResult<Pig>> => {
  return api.get('/pig/list', {
    params: { farmId, userId, healthStatus, pageNum, pageSize },
  });
};

export const getPigById = async (id: number): Promise<Pig> => {
  return api.get(`/pig/${id}`);
};

export const getPigByEarTag = async (earTagNumber: string): Promise<Pig> => {
  return api.get(`/pig/earTag/${earTagNumber}`);
};

export const createPig = async (data: PigFormData): Promise<number> => {
  return api.post('/pig', data);
};

export const updatePig = async (id: number, data: PigFormData): Promise<void> => {
  return api.put('/pig', { id, ...data });
};

export const deletePig = async (id: number): Promise<void> => {
  return api.delete(`/pig/${id}`);
};

// ==================== 养殖场管理 API ====================

export const getFarmList = async (
  ownerId?: number,
  pageNum = 1,
  pageSize = 10
): Promise<PageResult<Farm>> => {
  return api.get('/farm/list', {
    params: { ownerId, pageNum, pageSize },
  });
};

export const getFarmById = async (id: number): Promise<Farm> => {
  return api.get(`/farm/${id}`);
};

export const createFarm = async (data: FarmFormData): Promise<number> => {
  return api.post('/farm', data);
};

export const updateFarm = async (id: number, data: FarmFormData): Promise<void> => {
  return api.put('/farm', { id, ...data });
};

export const deleteFarm = async (id: number): Promise<void> => {
  return api.delete(`/farm/${id}`);
};

// ==================== 病例管理 API ====================

export const getCaseList = async (
  pigId?: number,
  userId?: number,
  status?: string,
  pageNum = 1,
  pageSize = 10
): Promise<PageResult<Case>> => {
  return api.get('/case/list', {
    params: { pigId, userId, status, pageNum, pageSize },
  });
};

export const getCaseById = async (id: number): Promise<Case> => {
  return api.get(`/case/${id}`);
};

export const getCaseByConversationId = async (conversationId: string): Promise<Case> => {
  return api.get(`/case/conversation/${conversationId}`);
};

export const createCase = async (data: CaseFormData): Promise<number> => {
  return api.post('/case', data);
};

export const updateCase = async (id: number, data: Partial<Case>): Promise<void> => {
  return api.put('/case', { id, ...data });
};

export const deleteCase = async (id: number): Promise<void> => {
  return api.delete(`/case/${id}`);
};

// ==================== 兽药信息 API ====================

export const getDrugList = async (
  name?: string,
  drugType?: string,
  pageNum = 1,
  pageSize = 10
): Promise<PageResult<Drug>> => {
  return api.get('/drug/list', {
    params: { name, drugType, pageNum, pageSize },
  });
};

export const getDrugById = async (id: number): Promise<Drug> => {
  return api.get(`/drug/${id}`);
};

export const searchDrugs = async (
  keyword: string,
  pageNum = 1,
  pageSize = 10
): Promise<PageResult<Drug>> => {
  return api.get('/drug/search', {
    params: { keyword, pageNum, pageSize },
  });
};

// ==================== 文章管理 API ====================

export const getArticleList = async (
  category?: string,
  status?: string,
  pageNum = 1,
  pageSize = 10
): Promise<PageResult<Article>> => {
  return api.get('/article/list', {
    params: { category, status, pageNum, pageSize },
  });
};

export const getArticleById = async (id: number): Promise<Article> => {
  return api.get(`/article/${id}`);
};

export const createArticle = async (data: ArticleFormData): Promise<number> => {
  return api.post('/article', data);
};

export const updateArticle = async (id: number, data: ArticleFormData): Promise<void> => {
  return api.put('/article', { id, ...data });
};

export const deleteArticle = async (id: number): Promise<void> => {
  return api.delete(`/article/${id}`);
};

export const publishArticle = async (id: number): Promise<void> => {
  return api.post(`/article/${id}/publish`);
};

export const likeArticle = async (id: number): Promise<void> => {
  return api.post(`/article/${id}/like`);
};

// ==================== 治疗记录 API ====================

export const getTreatmentRecordList = async (
  caseId?: number,
  pigId?: number,
  pageNum = 1,
  pageSize = 10
): Promise<PageResult<TreatmentRecord>> => {
  return api.get('/treatment-record/list', {
    params: { caseId, pigId, pageNum, pageSize },
  });
};

export const getTreatmentRecordById = async (id: number): Promise<TreatmentRecord> => {
  return api.get(`/treatment-record/${id}`);
};

export const createTreatmentRecord = async (data: TreatmentRecordFormData): Promise<number> => {
  return api.post('/treatment-record', data);
};

export const updateTreatmentRecord = async (
  id: number,
  data: TreatmentRecordFormData
): Promise<void> => {
  return api.put('/treatment-record', { id, ...data });
};

export const deleteTreatmentRecord = async (id: number): Promise<void> => {
  return api.delete(`/treatment-record/${id}`);
};

// ==================== 健康监测 API ====================

export const getHealthMonitorList = async (
  pigId?: number,
  pageNum = 1,
  pageSize = 10
): Promise<PageResult<HealthMonitor>> => {
  return api.get('/health-monitor/list', {
    params: { pigId, pageNum, pageSize },
  });
};

export const getHealthMonitorById = async (id: number): Promise<HealthMonitor> => {
  return api.get(`/health-monitor/${id}`);
};

export const createHealthMonitor = async (data: HealthMonitorFormData): Promise<number> => {
  return api.post('/health-monitor', data);
};

export const updateHealthMonitor = async (
  id: number,
  data: HealthMonitorFormData
): Promise<void> => {
  return api.put('/health-monitor', { id, ...data });
};

export const deleteHealthMonitor = async (id: number): Promise<void> => {
  return api.delete(`/health-monitor/${id}`);
};
