import * as React from "react";
import { Activity, Eye, EyeOff, Leaf, Lock, ShieldCheck, Sparkles, User } from "lucide-react";
import { useNavigate } from "react-router-dom";

import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Checkbox } from "@/components/ui/checkbox";
import { useAuthStore } from "@/stores/authStore";

const highlights = [
  {
    icon: Sparkles,
    title: "检索增强问答",
    desc: "融合知识库与业务数据，回答可追溯",
  },
  {
    icon: Activity,
    title: "生猪健康洞察",
    desc: "助力养殖场景下的风险识别与决策",
  },
  {
    icon: ShieldCheck,
    title: "安全可控",
    desc: "账号权限由管理员统一初始化与维护",
  },
];

export function LoginPage() {
  const navigate = useNavigate();
  const { login, isLoading } = useAuthStore();
  const [showPassword, setShowPassword] = React.useState(false);
  const [remember, setRemember] = React.useState(true);
  const [form, setForm] = React.useState({ username: "admin", password: "admin" });
  const [error, setError] = React.useState<string | null>(null);

  const handleSubmit = async (event: React.FormEvent) => {
    event.preventDefault();
    setError(null);
    if (!form.username.trim() || !form.password.trim()) {
      setError("请输入用户名和密码。");
      return;
    }
    try {
      await login(form.username.trim(), form.password.trim());
      if (!remember) {
        // 如需仅在内存中保存登录态，可在此扩展。
      }
      navigate("/chat");
    } catch (err) {
      setError((err as Error).message || "登录失败，请稍后重试。");
    }
  };

  return (
    <div className="relative min-h-screen w-full overflow-hidden bg-[#f4f9f7] dark:bg-slate-950">
      {/* 全局背景：网格 + 柔光 */}
      <div
        className="pointer-events-none absolute inset-0 opacity-[0.35] dark:opacity-20"
        style={{
          backgroundImage: `
            radial-gradient(circle at 20% 20%, rgba(16, 185, 129, 0.18), transparent 45%),
            radial-gradient(circle at 80% 10%, rgba(45, 212, 191, 0.14), transparent 40%),
            radial-gradient(circle at 50% 90%, rgba(5, 150, 105, 0.12), transparent 50%)
          `,
        }}
      />
      <div
        className="pointer-events-none absolute inset-0 opacity-[0.4] dark:opacity-[0.15]"
        style={{
          backgroundImage: `linear-gradient(rgba(15, 118, 110, 0.06) 1px, transparent 1px),
            linear-gradient(90deg, rgba(15, 118, 110, 0.06) 1px, transparent 1px)`,
          backgroundSize: "48px 48px",
        }}
      />

      <div className="relative z-10 mx-auto grid min-h-screen w-full max-w-[1200px] lg:grid-cols-2 lg:gap-0">
        {/* 左侧品牌区（大屏） */}
        <aside className="relative hidden overflow-hidden lg:flex lg:flex-col lg:justify-between lg:p-12 xl:p-14">
          <div className="absolute inset-0 bg-gradient-to-br from-emerald-700 via-teal-800 to-slate-900" />
          <div className="absolute -right-24 -top-24 h-72 w-72 rounded-full bg-emerald-400/25 blur-3xl" />
          <div className="absolute -bottom-16 -left-16 h-64 w-64 rounded-full bg-teal-300/20 blur-3xl" />
          <div className="absolute inset-0 bg-[url('data:image/svg+xml,%3Csvg width=%2260%22 height=%2260%22 viewBox=%220 0 60 60%22 xmlns=%22http://www.w3.org/2000/svg%22%3E%3Cg fill=%22none%22 fill-rule=%22evenodd%22%3E%3Cg fill=%22%23ffffff%22 fill-opacity=%220.06%22%3E%3Cpath d=%22M36 34v-4h-2v4h-4v2h4v4h2v-4h4v-2h-4zm0-30V0h-2v4h-4v2h4v4h2V6h4V4h-4zM6 34v-4H4v4H0v2h4v4h2v-4h4v-2H6zM6 4V0H4v4H0v2h4v4h2V6h4V4H6z%22/%3E%3C/g%3E%3C/g%3E%3C/svg%3E')] opacity-60" />

          <div className="relative space-y-8">
            <div className="flex items-center gap-4">
              <div className="flex h-14 w-14 items-center justify-center rounded-2xl bg-white/10 shadow-lg shadow-emerald-900/20 ring-1 ring-white/20 backdrop-blur">
                <img src="/piglogo.png" alt="" className="h-10 w-10 object-contain" />
              </div>
              <div>
                <p className="text-xs font-semibold uppercase tracking-[0.2em] text-emerald-100/90">智慧养殖</p>
                <p className="font-display text-2xl font-semibold tracking-tight text-white">生猪健康管理</p>
              </div>
            </div>
            <p className="max-w-sm text-base leading-relaxed text-emerald-50/90">
              基于 RAG 的生猪健康管理智慧医药系统，把文档知识与对话体验连在一起。
            </p>
          </div>

          <ul className="relative mt-auto space-y-5 pt-10">
            {highlights.map(({ icon: Icon, title, desc }) => (
              <li key={title} className="flex gap-4 rounded-2xl border border-white/10 bg-white/5 p-4 backdrop-blur-sm transition hover:bg-white/10">
                <span className="flex h-10 w-10 shrink-0 items-center justify-center rounded-xl bg-emerald-500/25 text-emerald-100">
                  <Icon className="h-5 w-5" strokeWidth={1.75} />
                </span>
                <div className="min-w-0 space-y-1">
                  <p className="font-medium text-white">{title}</p>
                  <p className="text-sm leading-snug text-emerald-100/75">{desc}</p>
                </div>
              </li>
            ))}
          </ul>

          <p className="relative pt-12 text-xs text-emerald-200/60">© {new Date().getFullYear()} · 企业内部使用</p>
        </aside>

        {/* 右侧 / 小屏：表单 */}
        <main className="relative flex min-h-screen flex-col items-center justify-center px-4 py-12 sm:px-8">
          {/* 小屏顶部的轻量品牌 */}
          <div className="mb-8 flex w-full max-w-md flex-col items-center gap-3 lg:hidden">
            <div className="flex h-16 w-16 items-center justify-center rounded-2xl bg-gradient-to-br from-emerald-500 to-teal-600 shadow-lg shadow-emerald-600/35 ring-4 ring-white/80 dark:ring-slate-800">
              <img src="/piglogo.png" alt="Logo" className="h-11 w-11 object-contain" />
            </div>
            <div className="text-center">
              <h1 className="font-display text-2xl font-bold tracking-tight text-emerald-900 dark:text-emerald-100">欢迎回来</h1>
              <p className="mt-1.5 text-sm text-emerald-700/85 dark:text-emerald-400/90">基于 RAG 的生猪健康管理智慧医药系统</p>
            </div>
            <div className="mt-1 flex flex-wrap justify-center gap-2">
              <span className="rounded-full border border-emerald-200/80 bg-white/70 px-2.5 py-0.5 text-[11px] font-medium text-emerald-800 shadow-sm dark:border-emerald-800 dark:bg-emerald-950/50 dark:text-emerald-200">
                知识检索
              </span>
              <span className="rounded-full border border-teal-200/80 bg-white/70 px-2.5 py-0.5 text-[11px] font-medium text-teal-800 shadow-sm dark:border-teal-800 dark:bg-teal-950/40 dark:text-teal-200">
                养殖场景
              </span>
            </div>
          </div>

          <div className="w-full max-w-md animate-fade-in">
            <div className="rounded-3xl border border-emerald-100/80 bg-white/85 p-8 shadow-[0_24px_80px_-24px_rgba(5,80,60,0.35)] backdrop-blur-xl dark:border-emerald-900/50 dark:bg-slate-900/80 dark:shadow-[0_24px_80px_-24px_rgba(0,0,0,0.65)]">
              <div className="mb-8 hidden text-center lg:block">
                <h2 className="font-display text-2xl font-semibold tracking-tight text-slate-900 dark:text-white">登录账户</h2>
                <p className="mt-2 text-sm text-slate-500 dark:text-slate-400">使用管理员分配的账号进入系统</p>
              </div>

              <form className="space-y-5" onSubmit={handleSubmit}>
                <div className="space-y-2">
                  <label
                    htmlFor="login-username"
                    className="text-xs font-medium uppercase tracking-wide text-emerald-800/90 dark:text-emerald-300"
                  >
                    用户名
                  </label>
                  <div className="group relative">
                    <User className="pointer-events-none absolute left-3.5 top-1/2 h-[1.125rem] w-[1.125rem] -translate-y-1/2 text-slate-400 transition-colors group-focus-within:text-emerald-600 dark:group-focus-within:text-emerald-400" />
                    <Input
                      id="login-username"
                      placeholder="请输入用户名"
                      value={form.username}
                      onChange={(event) => setForm((prev) => ({ ...prev, username: event.target.value }))}
                      className="h-12 rounded-xl border-emerald-100/90 bg-white/90 pl-11 shadow-inner shadow-emerald-900/[0.03] transition-[box-shadow,border-color] placeholder:text-slate-400 focus-visible:border-emerald-400/90 focus-visible:ring-emerald-500/25 dark:border-emerald-900/60 dark:bg-slate-950/40"
                      autoComplete="username"
                    />
                  </div>
                </div>

                <div className="space-y-2">
                  <label
                    htmlFor="login-password"
                    className="text-xs font-medium uppercase tracking-wide text-emerald-800/90 dark:text-emerald-300"
                  >
                    密码
                  </label>
                  <div className="group relative">
                    <Lock className="pointer-events-none absolute left-3.5 top-1/2 h-[1.125rem] w-[1.125rem] -translate-y-1/2 text-slate-400 transition-colors group-focus-within:text-emerald-600 dark:group-focus-within:text-emerald-400" />
                    <Input
                      id="login-password"
                      type={showPassword ? "text" : "password"}
                      placeholder="请输入密码"
                      value={form.password}
                      onChange={(event) => setForm((prev) => ({ ...prev, password: event.target.value }))}
                      className="h-12 rounded-xl border-emerald-100/90 bg-white/90 pl-11 pr-12 shadow-inner shadow-emerald-900/[0.03] transition-[box-shadow,border-color] placeholder:text-slate-400 focus-visible:border-emerald-400/90 focus-visible:ring-emerald-500/25 dark:border-emerald-900/60 dark:bg-slate-950/40"
                      autoComplete="current-password"
                    />
                    <button
                      type="button"
                      onClick={() => setShowPassword((prev) => !prev)}
                      className="absolute right-2.5 top-1/2 -translate-y-1/2 rounded-lg p-2 text-slate-400 transition-colors hover:bg-emerald-50 hover:text-emerald-700 dark:hover:bg-emerald-950/50 dark:hover:text-emerald-300"
                      aria-label="显示或隐藏密码"
                    >
                      {showPassword ? <EyeOff className="h-5 w-5" /> : <Eye className="h-5 w-5" />}
                    </button>
                  </div>
                </div>

                <div className="flex flex-col gap-3 sm:flex-row sm:items-center sm:justify-between sm:gap-2">
                  <label className="flex cursor-pointer items-center gap-2.5 text-sm text-emerald-700 transition-colors hover:text-emerald-900 dark:text-emerald-400 dark:hover:text-emerald-200">
                    <Checkbox
                      checked={remember}
                      onCheckedChange={(value) => setRemember(Boolean(value))}
                      className="border-emerald-300 data-[state=checked]:border-emerald-600 data-[state=checked]:bg-emerald-600"
                    />
                    <span className="select-none">记住我</span>
                  </label>
                  <span className="text-xs text-slate-500 dark:text-slate-500 sm:text-right">账号由管理员初始化</span>
                </div>

                {error && (
                  <div className="animate-shake rounded-xl border border-red-200/90 bg-red-50/95 px-3.5 py-3 text-sm text-red-700 dark:border-red-900/60 dark:bg-red-950/40 dark:text-red-300">
                    {error}
                  </div>
                )}

                <Button
                  type="submit"
                  className="h-12 w-full rounded-xl bg-gradient-to-r from-emerald-600 via-emerald-600 to-teal-700 text-base font-semibold text-white shadow-lg shadow-emerald-700/25 transition-[transform,box-shadow] hover:shadow-xl hover:shadow-emerald-800/30 active:scale-[0.99] disabled:pointer-events-none disabled:opacity-60 dark:from-emerald-500 dark:via-emerald-600 dark:to-teal-600"
                  disabled={isLoading}
                >
                  {isLoading ? (
                    <span className="inline-flex items-center gap-2">
                      <svg className="h-5 w-5 animate-spin" viewBox="0 0 24 24" aria-hidden>
                        <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4" fill="none" />
                        <path
                          className="opacity-75"
                          fill="currentColor"
                          d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"
                        />
                      </svg>
                      正在登录...
                    </span>
                  ) : (
                    "登录"
                  )}
                </Button>
              </form>
            </div>

            <div className="mt-8 flex flex-wrap items-center justify-center gap-x-6 gap-y-2 text-xs text-slate-500 dark:text-slate-500">
              <span className="inline-flex items-center gap-1.5">
                <ShieldCheck className="h-3.5 w-3.5 text-emerald-600 dark:text-emerald-400" />
                传输与存储按企业规范加固
              </span>
              <span className="hidden h-3 w-px bg-slate-300 sm:block dark:bg-slate-600" />
              <span className="inline-flex items-center gap-1.5">
                <Leaf className="h-3.5 w-3.5 text-emerald-600 dark:text-emerald-400" />
                安全 · 高效 · 智能
              </span>
            </div>
          </div>
        </main>
      </div>
    </div>
  );
}
