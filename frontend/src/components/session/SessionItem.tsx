import { Trash2 } from "lucide-react";

import { AlertDialog, AlertDialogAction, AlertDialogCancel, AlertDialogContent, AlertDialogDescription, AlertDialogFooter, AlertDialogHeader, AlertDialogTitle, AlertDialogTrigger } from "@/components/ui/alert-dialog";
import { Button } from "@/components/ui/button";
import { cn } from "@/lib/utils";
import { formatTimestamp, truncate } from "@/utils/helpers";
import type { Session } from "@/types";

interface SessionItemProps {
  session: Session;
  active: boolean;
  onSelect: () => void;
  onDelete: () => void;
}

export function SessionItem({ session, active, onSelect, onDelete }: SessionItemProps) {
  return (
    <div
      className={cn(
        "group flex cursor-pointer items-center justify-between rounded-2xl border border-transparent px-3 py-2 transition",
        active ? "border-emerald-200 bg-emerald-50/80" : "hover:bg-emerald-50/50"
      )}
      onClick={onSelect}
      role="button"
      tabIndex={0}
      onKeyDown={(event) => {
        if (event.key === "Enter") onSelect();
      }}
    >
      <div className="min-w-0">
        <p className="truncate text-sm font-medium text-emerald-800">{truncate(session.title || "新对话", 36)}</p>
        <p className="text-xs text-emerald-600">{formatTimestamp(session.lastTime)}</p>
      </div>
      <AlertDialog>
        <AlertDialogTrigger asChild>
          <Button
            variant="ghost"
            size="icon"
            className="opacity-0 transition group-hover:opacity-100 text-emerald-600 hover:bg-emerald-100"
            onClick={(event) => event.stopPropagation()}
            aria-label="删除会话"
          >
            <Trash2 className="h-4 w-4" />
          </Button>
        </AlertDialogTrigger>
        <AlertDialogContent className="rounded-2xl border-emerald-100">
          <AlertDialogHeader>
            <AlertDialogTitle className="text-emerald-800">删除该会话？</AlertDialogTitle>
            <AlertDialogDescription>
              会话与消息将被永久删除，无法恢复。
            </AlertDialogDescription>
          </AlertDialogHeader>
          <AlertDialogFooter>
            <AlertDialogCancel className="rounded-xl border-emerald-200 text-emerald-700 hover:bg-emerald-50">取消</AlertDialogCancel>
            <AlertDialogAction
              onClick={(event) => {
                event.stopPropagation();
                onDelete();
              }}
              className="bg-gradient-to-r from-red-500 to-rose-500 hover:from-red-600 hover:to-rose-600 text-white rounded-xl border-0 shadow-md"
            >
              删除
            </AlertDialogAction>
          </AlertDialogFooter>
        </AlertDialogContent>
      </AlertDialog>
    </div>
  );
}
