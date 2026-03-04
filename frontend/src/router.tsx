import { Navigate, createBrowserRouter } from "react-router-dom";

import { LoginPage } from "@/pages/LoginPage";
import { ChatPage } from "@/pages/ChatPage";
import { NotFoundPage } from "@/pages/NotFoundPage";
import { AdminLayout } from "@/pages/admin/AdminLayout";
import { DashboardPage } from "@/pages/admin/dashboard/DashboardPage";
import { KnowledgeListPage } from "@/pages/admin/knowledge/KnowledgeListPage";
import { KnowledgeDocumentsPage } from "@/pages/admin/knowledge/KnowledgeDocumentsPage";
import { KnowledgeChunksPage } from "@/pages/admin/knowledge/KnowledgeChunksPage";
import { IntentTreePage } from "@/pages/admin/intent-tree/IntentTreePage";
import { IntentListPage } from "@/pages/admin/intent-tree/IntentListPage";
import { IntentEditPage } from "@/pages/admin/intent-tree/IntentEditPage";
import { IngestionPage } from "@/pages/admin/ingestion/IngestionPage";
import { RagTracePage } from "@/pages/admin/traces/RagTracePage";
import { RagTraceDetailPage } from "@/pages/admin/traces/RagTraceDetailPage";
import { SystemSettingsPage } from "@/pages/admin/settings/SystemSettingsPage";
import { SampleQuestionPage } from "@/pages/admin/sample-questions/SampleQuestionPage";
import { UserListPage } from "@/pages/admin/users/UserListPage";
import { useAuthStore } from "@/stores/authStore";

// Pig Health Diagnosis System Pages
import { PigListPage } from "@/pages/pig/PigListPage";
import { PigDetailPage } from "@/pages/pig/PigDetailPage";
import { PigFormPage } from "@/pages/pig/PigFormPage";
import { CaseListPage } from "@/pages/case/CaseListPage";
import { CaseDetailPage } from "@/pages/case/CaseDetailPage";
import { CaseFormPage } from "@/pages/case/CaseFormPage";
import { DrugListPage } from "@/pages/drug/DrugListPage";
import { DrugDetailPage } from "@/pages/drug/DrugDetailPage";
import { ArticleListPage } from "@/pages/article/ArticleListPage";
import { ArticleDetailPage } from "@/pages/article/ArticleDetailPage";
import { ArticleFormPage } from "@/pages/article/ArticleFormPage";
import { FarmListPage } from "@/pages/farm/FarmListPage";
import { FarmDetailPage } from "@/pages/farm/FarmDetailPage";
import { FarmFormPage } from "@/pages/farm/FarmFormPage";
import { TreatmentRecordListPage } from "@/pages/treatment-record/TreatmentRecordListPage";
import { TreatmentRecordFormPage } from "@/pages/treatment-record/TreatmentRecordFormPage";
import { HealthMonitorListPage } from "@/pages/health-monitor/HealthMonitorListPage";
import { HealthMonitorFormPage } from "@/pages/health-monitor/HealthMonitorFormPage";

function RequireAuth({ children }: { children: JSX.Element }) {
  const isAuthenticated = useAuthStore((state) => state.isAuthenticated);
  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }
  return children;
}

function RequireAdmin({ children }: { children: JSX.Element }) {
  const user = useAuthStore((state) => state.user);
  const isAuthenticated = useAuthStore((state) => state.isAuthenticated);

  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }

  // 允许 ADMIN, VETERINARIAN, FARMER 访问管理后台
  // 菜单会根据角色自动过滤
  if (user?.role === "admin" || user?.role === "ADMIN" ||
      user?.role === "VETERINARIAN" || user?.role === "FARMER") {
    return children;
  }

  // 其他角色跳转到聊天页面
  return <Navigate to="/chat" replace />;
}

function RedirectIfAuth({ children }: { children: JSX.Element }) {
  const isAuthenticated = useAuthStore((state) => state.isAuthenticated);
  if (isAuthenticated) {
    return <Navigate to="/chat" replace />;
  }
  return children;
}

function HomeRedirect() {
  const isAuthenticated = useAuthStore((state) => state.isAuthenticated);
  const user = useAuthStore((state) => state.user);

  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }

  // 根据角色跳转到不同的默认页面
  if (user?.role === 'ADMIN') {
    return <Navigate to="/admin/dashboard" replace />;
  } else if (user?.role === 'VETERINARIAN') {
    return <Navigate to="/article" replace />;
  } else if (user?.role === 'FARMER') {
    return <Navigate to="/pig" replace />;
  }

  // 默认跳转到聊天页面
  return <Navigate to="/chat" replace />;
}

export const router = createBrowserRouter(
  [
  {
    path: "/",
    element: <HomeRedirect />
  },
  {
    path: "/login",
    element: (
      <RedirectIfAuth>
        <LoginPage />
      </RedirectIfAuth>
    )
  },
  {
    path: "/chat",
    element: (
      <RequireAuth>
        <ChatPage />
      </RequireAuth>
    )
  },
  {
    path: "/chat/:sessionId",
    element: (
      <RequireAuth>
        <ChatPage />
      </RequireAuth>
    )
  },
  {
    path: "/admin",
    element: (
      <RequireAdmin>
        <AdminLayout />
      </RequireAdmin>
    ),
    children: [
      {
        index: true,
        element: <Navigate to="/admin/dashboard" replace />
      },
      {
        path: "dashboard",
        element: <DashboardPage />
      },
      {
        path: "knowledge",
        element: <KnowledgeListPage />
      },
      {
        path: "knowledge/:kbId",
        element: <KnowledgeDocumentsPage />
      },
      {
        path: "knowledge/:kbId/docs/:docId",
        element: <KnowledgeChunksPage />
      },
      {
        path: "intent-tree",
        element: <IntentTreePage />
      },
      {
        path: "intent-list",
        element: <IntentListPage />
      },
      {
        path: "intent-list/:id/edit",
        element: <IntentEditPage />
      },
      {
        path: "ingestion",
        element: <IngestionPage />
      },
      {
        path: "traces",
        element: <RagTracePage />
      },
      {
        path: "traces/:traceId",
        element: <RagTraceDetailPage />
      },
      {
        path: "settings",
        element: <SystemSettingsPage />
      },
      {
        path: "sample-questions",
        element: <SampleQuestionPage />
      },
      {
        path: "users",
        element: <UserListPage />
      }
    ]
  },
  // Pig Health Diagnosis System Routes - 嵌套在 admin 路由下
  {
    path: "/pig",
    element: (
      <RequireAuth>
        <AdminLayout />
      </RequireAuth>
    ),
    children: [
      {
        index: true,
        element: <PigListPage />
      },
      {
        path: ":id",
        element: <PigDetailPage />
      },
      {
        path: "create",
        element: <PigFormPage />
      },
      {
        path: ":id/edit",
        element: <PigFormPage />
      }
    ]
  },
  {
    path: "/case",
    element: (
      <RequireAuth>
        <AdminLayout />
      </RequireAuth>
    ),
    children: [
      {
        index: true,
        element: <CaseListPage />
      },
      {
        path: ":id",
        element: <CaseDetailPage />
      },
      {
        path: "create",
        element: <CaseFormPage />
      },
      {
        path: ":id/edit",
        element: <CaseFormPage />
      }
    ]
  },
  {
    path: "/drug",
    element: (
      <RequireAuth>
        <AdminLayout />
      </RequireAuth>
    ),
    children: [
      {
        index: true,
        element: <DrugListPage />
      },
      {
        path: ":id",
        element: <DrugDetailPage />
      }
    ]
  },
  {
    path: "/article",
    element: (
      <RequireAuth>
        <AdminLayout />
      </RequireAuth>
    ),
    children: [
      {
        index: true,
        element: <ArticleListPage />
      },
      {
        path: ":id",
        element: <ArticleDetailPage />
      },
      {
        path: "create",
        element: <ArticleFormPage />
      },
      {
        path: ":id/edit",
        element: <ArticleFormPage />
      }
    ]
  },
  {
    path: "/farm",
    element: (
      <RequireAuth>
        <AdminLayout />
      </RequireAuth>
    ),
    children: [
      {
        index: true,
        element: <FarmListPage />
      },
      {
        path: ":id",
        element: <FarmDetailPage />
      },
      {
        path: "create",
        element: <FarmFormPage />
      },
      {
        path: ":id/edit",
        element: <FarmFormPage />
      }
    ]
  },
  {
    path: "/treatment-record",
    element: (
      <RequireAuth>
        <AdminLayout />
      </RequireAuth>
    ),
    children: [
      {
        index: true,
        element: <TreatmentRecordListPage />
      },
      {
        path: "create",
        element: <TreatmentRecordFormPage />
      }
    ]
  },
  {
    path: "/health-monitor",
    element: (
      <RequireAuth>
        <AdminLayout />
      </RequireAuth>
    ),
    children: [
      {
        index: true,
        element: <HealthMonitorListPage />
      },
      {
        path: "create",
        element: <HealthMonitorFormPage />
      }
    ]
  },
  {
    path: "*",
    element: <NotFoundPage />
  }
],
{
  future: {
    v7_startTransition: true,
  },
});
