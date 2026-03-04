// 生猪诊疗系统类型定义

// ==================== 生猪管理 ====================

export interface Pig {
  id: number;
  farmId: number;
  userId: number;
  earTagNumber: string;
  breed: string;
  gender: number; // 0-母猪，1-公猪
  birthDate: string;
  weight: number;
  healthStatus: string; // HEALTHY, SICK, RECOVERING, DEAD
  healthStatusName?: string;
  penNumber: string;
  feedStatus: string; // NORMAL, WEANING, FATTENING
  feedStatusName?: string;
  immunizationStatus?: string;
  geneticInfo?: string;
  remark?: string;
  createTime: string;
  updateTime: string;
}

export interface PigFormData {
  farmId?: number;
  earTagNumber: string;
  breed: string;
  gender: number;
  birthDate: string;
  weight?: number;
  penNumber?: string;
  feedStatus?: string;
  remark?: string;
}

// ==================== 养殖场管理 ====================

export interface Farm {
  id: number;
  name: string;
  ownerId: number;
  ownerName?: string;
  address?: string;
  contactPhone?: string;
  scale: string; // SMALL, MEDIUM, LARGE
  scaleName?: string;
  totalPigs: number;
  createTime: string;
  updateTime: string;
}

export interface FarmFormData {
  name: string;
  address?: string;
  contactPhone?: string;
  scale: string;
}

// ==================== 病例管理 ====================

export interface Case {
  id: number;
  caseNumber: string;
  pigId: number;
  userId: number;
  veterinarianId?: number;
  symptoms: string;
  diagnosis?: string;
  diseaseName?: string;
  treatmentPlan?: string;
  prescribedDrugs?: string;
  status: string; // UNTREATED, TREATING, CURED, DEAD
  statusName?: string;
  diagnosisTime?: string;
  treatmentStartTime?: string;
  treatmentEndTime?: string;
  followUpInfo?: string;
  relatedPigs?: string;
  relatedCases?: string;
  aiDiagnosis?: string;
  conversationId?: string;
  createTime: string;
  updateTime: string;
}

export interface CaseFormData {
  pigId: number;
  symptoms: string;
  daysOfIllness?: number;
}

// ==================== 兽药信息 ====================

export interface Drug {
  id: number;
  name: string;
  drugType: string; // CHINESE, WESTERN
  drugTypeName?: string;
  manufacturer?: string;
  manufacturerAddress?: string;
  approvalNumber?: string;
  specification?: string;
  dosageForm: string; // TABLET, INJECTION, POWDER, LIQUID
  dosageFormName?: string;
  ingredients?: string;
  indication?: string;
  usage?: string;
  dosage?: string;
  contraindication?: string;
  adverseReaction?: string;
  drugInteraction?: string;
  storage?: string;
  storageConditions?: string;
  validityPeriod?: string;
  price?: number;
  stockQuantity: number;
  salesVolume: number;
  rating: number;
  reviewCount: number;
  imageUrl?: string;
  createTime: string;
  updateTime: string;
}

// ==================== 文章管理 ====================

export interface Article {
  id: number;
  title: string;
  category: string; // BREEDING, DISEASE, NUTRITION, MANAGEMENT, NEWS
  categoryName?: string;
  authorId: number;
  authorName?: string;
  summary?: string;
  content: string;
  coverImage?: string;
  tags?: string;
  viewCount: number;
  likeCount: number;
  status: string; // DRAFT, PUBLISHED, ARCHIVED
  statusName?: string;
  publishTime?: string;
  createTime: string;
  updateTime: string;
}

export interface ArticleFormData {
  title: string;
  category: string;
  summary?: string;
  content: string;
  coverImage?: string;
  tags?: string;
}

// ==================== 治疗记录 ====================

export interface TreatmentRecord {
  id: number;
  caseId: number;
  caseNumber?: string;
  pigId: number;
  earTagNumber?: string;
  treatmentDate: string;
  operatorId: number;
  operatorName?: string;
  treatmentType: string; // MEDICATION, SURGERY, NURSING
  treatmentTypeName?: string;
  drugsUsed?: string;
  dosageDetail?: string;
  treatmentEffect?: string; // EFFECTIVE, INEFFECTIVE, IMPROVED
  treatmentEffectName?: string;
  sideEffects?: string;
  notes?: string;
  createTime: string;
  updateTime: string;
}

export interface TreatmentRecordFormData {
  caseId: number;
  pigId: number;
  treatmentDate: string;
  treatmentType: string;
  drugsUsed?: string;
  dosageDetail?: string;
  treatmentEffect?: string;
  notes?: string;
}

// ==================== 健康监测 ====================

export interface HealthMonitor {
  id: number;
  pigId: number;
  earTagNumber?: string;
  monitorDate: string;
  temperature?: number;
  weight?: number;
  appetite?: string; // GOOD, NORMAL, POOR
  appetiteName?: string;
  activityLevel?: string; // ACTIVE, NORMAL, LETHARGIC
  activityLevelName?: string;
  fecesCondition?: string;
  respiratoryRate?: number;
  heartRate?: number;
  abnormalSigns?: string;
  monitorBy?: number;
  monitorByName?: string;
  createTime: string;
}

export interface HealthMonitorFormData {
  pigId: number;
  monitorDate: string;
  temperature?: number;
  weight?: number;
  appetite?: string;
  activityLevel?: string;
  fecesCondition?: string;
  respiratoryRate?: number;
  heartRate?: number;
  abnormalSigns?: string;
}

// ==================== 分页结果 ====================

export interface PageResult<T> {
  records: T[];
  total: number;
  current: number;
  size: number;
}

// ==================== 枚举常量 ====================

export const HealthStatus = {
  HEALTHY: '健康',
  SICK: '患病',
  RECOVERING: '康复中',
  DEAD: '死亡',
} as const;

export const FeedStatus = {
  NORMAL: '正常',
  WEANING: '断奶',
  FATTENING: '育肥',
} as const;

export const CaseStatus = {
  UNTREATED: '未治疗',
  TREATING: '治疗中',
  CURED: '已治愈',
  DEAD: '死亡',
} as const;

export const DrugType = {
  CHINESE: '中药',
  WESTERN: '西药',
} as const;

export const DosageForm = {
  TABLET: '片剂',
  INJECTION: '注射剂',
  POWDER: '粉剂',
  LIQUID: '液体',
} as const;

export const ArticleCategory = {
  BREEDING: '养殖知识',
  DISEASE: '疾病防治',
  NUTRITION: '营养饲料',
  MANAGEMENT: '管理技术',
  NEWS: '新闻资讯',
} as const;

export const ArticleStatus = {
  DRAFT: '草稿',
  PUBLISHED: '已发布',
  ARCHIVED: '已归档',
} as const;

export const FarmScale = {
  SMALL: '小型',
  MEDIUM: '中型',
  LARGE: '大型',
} as const;

export const Appetite = {
  GOOD: '良好',
  NORMAL: '正常',
  POOR: '差',
} as const;

export const ActivityLevel = {
  ACTIVE: '活跃',
  NORMAL: '正常',
  LETHARGIC: '嗜睡',
} as const;

export const TreatmentType = {
  MEDICATION: '药物治疗',
  SURGERY: '手术',
  NURSING: '护理',
} as const;

export const TreatmentEffect = {
  EFFECTIVE: '有效',
  INEFFECTIVE: '无效',
  IMPROVED: '好转',
} as const;
