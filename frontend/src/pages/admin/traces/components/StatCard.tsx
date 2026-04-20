import type { ReactNode } from "react";

import { cn } from "@/lib/utils";

export type StatCardTone = "cyan" | "emerald" | "indigo" | "amber";

interface StatCardProps {
  title: string;
  value: string;
  unit?: string;
  icon: ReactNode;
  tone: StatCardTone;
}

const TONE_STYLES = {
  emerald: {
    bg: "from-emerald-50 to-teal-50",
    icon: "from-emerald-500 to-teal-500",
    text: "text-emerald-600",
    border: "border-emerald-200",
    shadow: "hover:shadow-emerald-500/20"
  },
  cyan: {
    bg: "from-cyan-50 to-teal-50",
    icon: "from-cyan-500 to-teal-500",
    text: "text-cyan-600",
    border: "border-cyan-200",
    shadow: "hover:shadow-cyan-500/20"
  },
  indigo: {
    bg: "from-indigo-50 to-blue-50",
    icon: "from-indigo-500 to-blue-500",
    text: "text-indigo-600",
    border: "border-indigo-200",
    shadow: "hover:shadow-indigo-500/20"
  },
  amber: {
    bg: "from-amber-50 to-orange-50",
    icon: "from-amber-500 to-orange-500",
    text: "text-amber-600",
    border: "border-amber-200",
    shadow: "hover:shadow-amber-500/20"
  }
};

export function StatCard({ title, value, unit, icon, tone }: StatCardProps) {
  const styles = TONE_STYLES[tone];

  return (
    <article className={cn(
      "relative overflow-hidden bg-white rounded-2xl border shadow-sm transition-all duration-300 hover:shadow-lg",
      styles.border,
      styles.shadow
    )}>
      <div className={cn("absolute inset-0 bg-gradient-to-br opacity-[0.03]", styles.bg)} />
      <div className="relative p-5 flex items-center gap-4">
        <div className={cn(
          "p-3 rounded-xl bg-gradient-to-br shadow-lg",
          styles.icon
        )}>
          <div className="text-white">
            {icon}
          </div>
        </div>
        <div className="flex-1 min-w-0">
          <p className="text-sm text-slate-500 font-medium truncate">{title}</p>
          <div className="flex items-baseline gap-1.5 mt-1">
            <p className="text-2xl font-bold text-slate-800">{value}</p>
            {unit ? <span className={cn("text-sm font-medium", styles.text)}>{unit}</span> : null}
          </div>
        </div>
      </div>
    </article>
  );
}
