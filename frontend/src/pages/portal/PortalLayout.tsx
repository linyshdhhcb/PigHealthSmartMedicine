import { NavLink, Outlet } from "react-router-dom";

import { Button } from "@/components/ui/button";
import { cn } from "@/lib/utils";
import { useAuthStore } from "@/stores/authStore";

const navItems = [
  { to: "/app/home", label: "主页" },
  { to: "/app/articles", label: "文章" },
  { to: "/app/news", label: "资讯" },
  { to: "/app/illnesses", label: "疾病" },
  { to: "/app/medicines", label: "药品" },
  { to: "/chat", label: "AI" }
];

export function PortalLayout() {
  const logout = useAuthStore((state) => state.logout);
  const user = useAuthStore((state) => state.user);

  return (
    <div className="min-h-screen bg-gradient-to-b from-emerald-50 via-white to-white">
      <header className="sticky top-0 z-40 border-b border-emerald-100 bg-white/90 backdrop-blur">
        <div className="mx-auto flex h-14 max-w-7xl items-center justify-between px-4 sm:px-6 lg:px-8">
          <div className="flex items-center gap-2">
            <span className="inline-block h-2.5 w-2.5 rounded-full bg-emerald-600" />
            <span className="text-sm font-semibold tracking-wide text-teal-800">PHSM</span>
          </div>
          <nav className="flex items-center gap-1">
            {navItems.map((item) => (
              <NavLink
                key={item.to}
                to={item.to}
                className={({ isActive }) =>
                  cn(
                    "rounded-lg px-3 py-1.5 text-sm transition-colors",
                    isActive
                      ? "bg-emerald-600 text-white"
                      : "text-teal-700 hover:bg-emerald-50 hover:text-emerald-700"
                  )
                }
              >
                {item.label}
              </NavLink>
            ))}
          </nav>
          <div className="flex items-center gap-2">
            <span className="hidden text-xs text-teal-700/80 sm:inline">{user?.username || "用户"}</span>
            <Button
              variant="outline"
              size="sm"
              className="border-emerald-200 text-teal-800 hover:bg-emerald-50"
              onClick={logout}
            >
              退出
            </Button>
          </div>
        </div>
      </header>
      <main className="mx-auto max-w-7xl px-4 py-6 sm:px-6 lg:px-8">
        <Outlet />
      </main>
    </div>
  );
}
