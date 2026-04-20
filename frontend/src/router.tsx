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
import { QueryTermMappingPage } from "@/pages/admin/query-term-mapping/QueryTermMappingPage";
import { UserListPage } from "@/pages/admin/users/UserListPage";
import { ArticleTypeListPage } from "@/pages/admin/article-types";
import { ArticleListPage } from "@/pages/admin/articles";
import { NewsArticleListPage } from "@/pages/admin/news-articles";
import { IllnessKindListPage } from "@/pages/admin/illness-kinds";
import { IllnessListPage } from "@/pages/admin/illnesses";
import { MedicineListPage } from "@/pages/admin/medicines";
import { IllnessMedicineListPage } from "@/pages/admin/illness-medicines";
import { FeedbackListPage } from "@/pages/admin/feedbacks";
import { HistoryListPage } from "@/pages/admin/histories";
import { PageviewStatisticsPage } from "@/pages/admin/pageviews";
import { FeedbackSubmitPage } from "@/pages/feedback";
import { PortalLayout } from "@/pages/portal/PortalLayout";
import { PortalHomePage } from "@/pages/portal/PortalHomePage";
import { ArticlePortalListPage } from "@/pages/portal/ArticlePortalListPage";
import { ArticlePortalDetailPage } from "@/pages/portal/ArticlePortalDetailPage";
import { NewsPortalListPage } from "@/pages/portal/NewsPortalListPage";
import { NewsPortalDetailPage } from "@/pages/portal/NewsPortalDetailPage";
import { IllnessPortalListPage } from "@/pages/portal/IllnessPortalListPage";
import { IllnessPortalDetailPage } from "@/pages/portal/IllnessPortalDetailPage";
import { MedicinePortalListPage } from "@/pages/portal/MedicinePortalListPage";
import { MedicinePortalDetailPage } from "@/pages/portal/MedicinePortalDetailPage";
import { useAuthStore } from "@/stores/authStore";

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

  if (user?.role !== "admin") {
    return <Navigate to="/chat" replace />;
  }

  return children;
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
  return <Navigate to={isAuthenticated ? "/chat" : "/login"} replace />;
}

export const router = createBrowserRouter([
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
    path: "/app",
    element: (
      <RequireAuth>
        <PortalLayout />
      </RequireAuth>
    ),
    children: [
      { index: true, element: <Navigate to="/app/home" replace /> },
      { path: "home", element: <PortalHomePage /> },
      { path: "ai", element: <Navigate to="/chat" replace /> },
      { path: "ai/:sessionId", element: <Navigate to="/chat" replace /> },
      { path: "articles", element: <ArticlePortalListPage /> },
      { path: "articles/:id", element: <ArticlePortalDetailPage /> },
      { path: "news", element: <NewsPortalListPage /> },
      { path: "news/:id", element: <NewsPortalDetailPage /> },
      { path: "illnesses", element: <IllnessPortalListPage /> },
      { path: "illnesses/:id", element: <IllnessPortalDetailPage /> },
      { path: "medicines", element: <MedicinePortalListPage /> },
      { path: "medicines/:id", element: <MedicinePortalDetailPage /> }
    ]
  },
  {
    path: "/feedback",
    element: (
      <RequireAuth>
        <FeedbackSubmitPage />
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
        path: "mappings",
        element: <QueryTermMappingPage />
      },
      {
        path: "users",
        element: <UserListPage />
      },
      {
        path: "article-types",
        element: <ArticleTypeListPage />
      },
      {
        path: "articles",
        element: <ArticleListPage />
      },
      {
        path: "news-articles",
        element: <NewsArticleListPage />
      },
      {
        path: "illness-kinds",
        element: <IllnessKindListPage />
      },
      {
        path: "illnesses",
        element: <IllnessListPage />
      },
      {
        path: "medicines",
        element: <MedicineListPage />
      },
      {
        path: "illness-medicines",
        element: <IllnessMedicineListPage />
      },
      {
        path: "feedbacks",
        element: <FeedbackListPage />
      },
      {
        path: "histories",
        element: <HistoryListPage />
      },
      {
        path: "pageviews",
        element: <PageviewStatisticsPage />
      }
    ]
  },
  {
    path: "*",
    element: <NotFoundPage />
  }
]);
