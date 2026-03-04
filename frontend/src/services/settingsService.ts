import { api } from "@/services/api";

export interface SystemSettings {
  rag: {
    default: {
      collectionName: string;
      dimension: number;
      metricType: string;
    };
    queryRewrite: {
      enabled: boolean;
      maxHistoryMessages: number;
      maxHistoryChars: number;
    };
    rateLimit: {
      global: {
        enabled: boolean;
        maxConcurrent: number;
        maxWaitSeconds: number;
        leaseSeconds: number;
        pollIntervalMs: number;
      };
    };
    memory: {
      historyKeepTurns: number;
      summaryStartTurns: number;
      summaryEnabled: boolean;
      ttlMinutes: number;
      summaryMaxChars: number;
      titleMaxLength: number;
    };
  };
  ai: {
    providers: Record<
      string,
      {
        url: string;
        apiKey?: string | null;
        endpoints: Record<string, string>;
      }
    >;
    selection: {
      failureThreshold: number;
      openDurationMs: number;
    };
    stream: {
      messageChunkSize: number;
    };
    chat: ModelGroup;
    embedding: ModelGroup;
    rerank: ModelGroup;
  };
}

export interface ModelGroup {
  defaultModel?: string | null;
  deepThinkingModel?: string | null;
  candidates: ModelCandidate[];
}

export interface ModelCandidate {
  id: string;
  provider: string;
  model: string;
  url?: string | null;
  dimension?: number | null;
  priority?: number | null;
  enabled?: boolean | null;
  supportsThinking?: boolean | null;
}

export async function getSystemSettings(): Promise<SystemSettings> {
  return api.get<SystemSettings, SystemSettings>("/rag/settings");
}
