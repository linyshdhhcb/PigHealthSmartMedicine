import * as React from "react";
import { useNavigate, useParams } from "react-router-dom";

import { ChatInput } from "@/components/chat/ChatInput";
import { MessageList } from "@/components/chat/MessageList";
import { useChatStore } from "@/stores/chatStore";

export function PortalAiPage() {
  const navigate = useNavigate();
  const { sessionId } = useParams<{ sessionId: string }>();
  const {
    messages,
    isLoading,
    isStreaming,
    currentSessionId,
    sessions,
    isCreatingNew,
    fetchSessions,
    selectSession,
    createSession
  } = useChatStore();

  const showWelcome = messages.length === 0 && !isLoading;
  const [sessionsReady, setSessionsReady] = React.useState(false);
  const sessionExists = React.useMemo(() => {
    if (!sessionId) return false;
    return sessions.some((session) => session.id === sessionId);
  }, [sessionId, sessions]);

  React.useEffect(() => {
    let active = true;
    fetchSessions()
      .catch(() => null)
      .finally(() => {
        if (active) setSessionsReady(true);
      });
    return () => {
      active = false;
    };
  }, [fetchSessions]);

  React.useEffect(() => {
    if (sessionId) {
      if (sessionsReady && !sessionExists) {
        createSession().catch(() => null);
        navigate("/app/ai", { replace: true });
        return;
      }
      selectSession(sessionId).catch(() => null);
      return;
    }
    if (!sessionsReady || isCreatingNew || currentSessionId) return;
    createSession().catch(() => null);
  }, [sessionId, sessionsReady, sessionExists, isCreatingNew, currentSessionId, selectSession, createSession, navigate]);

  React.useEffect(() => {
    if (currentSessionId && currentSessionId !== sessionId) {
      navigate(`/app/ai/${currentSessionId}`, { replace: true });
    }
  }, [currentSessionId, sessionId, navigate]);

  return (
    <div className="flex h-[calc(100vh-110px)] flex-col rounded-xl border border-emerald-100 bg-white shadow-sm">
      <div className="flex-1 min-h-0">
        <MessageList
          messages={messages}
          isLoading={isLoading}
          isStreaming={isStreaming}
          sessionKey={currentSessionId}
        />
      </div>
      {showWelcome ? null : (
        <div className="relative z-20 bg-gradient-to-t from-white via-white to-transparent backdrop-blur-sm">
          <div className="mx-auto max-w-[900px] px-4 pt-2 pb-4">
            <ChatInput />
          </div>
        </div>
      )}
    </div>
  );
}
