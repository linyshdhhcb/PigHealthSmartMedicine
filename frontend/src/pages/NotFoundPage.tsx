import { Link } from "react-router-dom";
import { MessageSquare, Home, ArrowLeft, HelpCircle, AlertCircle } from "lucide-react";

import { Button } from "@/components/ui/button";

export function NotFoundPage() {
  return (
      <div className="relative flex min-h-screen items-center justify-center bg-gradient-to-br from-emerald-50 via-white to-teal-50 px-4 overflow-hidden">
        {/* 装饰性背景元素 */}
        <div className="absolute inset-0 overflow-hidden">
          <div className="absolute -top-40 -right-40 h-80 w-80 rounded-full bg-emerald-400/10 blur-3xl" />
          <div className="absolute -bottom-40 -left-40 h-80 w-80 rounded-full bg-teal-400/10 blur-3xl" />
          <div className="absolute top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 h-96 w-96 rounded-full bg-emerald-300/5 blur-3xl" />
        </div>

        {/* 浮动装饰点 */}
        <div className="absolute top-20 left-10 h-2 w-2 rounded-full bg-emerald-400/60 animate-pulse" />
        <div className="absolute top-40 right-20 h-1.5 w-1.5 rounded-full bg-teal-400/60 animate-pulse delay-300" />
        <div className="absolute bottom-32 left-24 h-2.5 w-2.5 rounded-full bg-emerald-500/40 animate-pulse delay-700" />
        <div className="absolute bottom-20 right-16 h-2 w-2 rounded-full bg-teal-500/50 animate-pulse delay-500" />

        <div className="relative max-w-md w-full">
          {/* 主卡片 */}
          <div className="rounded-3xl border border-emerald-200/50 bg-white/90 p-8 text-center shadow-xl backdrop-blur-sm transition-all duration-500 hover:shadow-2xl hover:shadow-emerald-500/20 animate-in fade-in zoom-in duration-500">

            {/* 404 大标题 */}
            <div className="mb-6">
              <div className="relative inline-block">
                <h1 className="text-8xl font-black bg-gradient-to-r from-emerald-600 via-teal-600 to-emerald-600 bg-clip-text text-transparent animate-gradient">
                  404
                </h1>
                <div className="absolute -inset-1 bg-gradient-to-r from-emerald-500/20 to-teal-500/20 blur-xl rounded-full -z-10" />
              </div>
              <div className="mt-2 h-1 w-20 mx-auto bg-gradient-to-r from-emerald-500 to-teal-600 rounded-full" />
            </div>

            {/* 图标区域 */}
            <div className="mx-auto mb-6 flex h-20 w-20 items-center justify-center rounded-full bg-gradient-to-br from-emerald-500 to-teal-600 shadow-lg shadow-emerald-500/30 animate-bounce-in">
              <div className="relative">
                <MessageSquare className="h-10 w-10 text-white" />
                <div className="absolute -top-1 -right-1">
                  <AlertCircle className="h-4 w-4 text-amber-400 fill-amber-400" />
                </div>
              </div>
            </div>

            {/* 标题和描述 */}
            <div className="space-y-2">
              <p className="font-display text-2xl font-semibold text-emerald-900">
                页面不存在
              </p>
              <p className="text-sm text-emerald-600">
                抱歉，您访问的页面正在星际旅行中，暂时无法找到。
              </p>
              <p className="text-xs text-emerald-500">
                错误代码：404 - 资源未找到
              </p>
            </div>

            {/* 建议操作区域 */}
            <div className="mt-6 p-4 bg-emerald-50/50 rounded-xl border border-emerald-100">
              <p className="text-xs font-medium text-emerald-700 mb-2 flex items-center justify-center gap-1">
                <HelpCircle className="h-3 w-3" />
                您可以尝试以下操作：
              </p>
              <div className="flex flex-wrap gap-2 justify-center">
                <span className="text-xs px-2 py-1 bg-white rounded-md text-emerald-700 shadow-sm">检查网址是否正确</span>
                <span className="text-xs px-2 py-1 bg-white rounded-md text-emerald-700 shadow-sm">返回首页重新导航</span>
                <span className="text-xs px-2 py-1 bg-white rounded-md text-emerald-700 shadow-sm">联系技术支持</span>
              </div>
            </div>

            {/* 按钮组 */}
            <div className="mt-6 space-y-3">
              <Button
                  asChild
                  className="w-full bg-gradient-to-r from-emerald-600 to-teal-600 text-white shadow-md shadow-emerald-500/30 transition-all duration-200 hover:from-emerald-700 hover:to-teal-700 hover:shadow-lg hover:shadow-emerald-600/40 hover:-translate-y-0.5"
              >
                <Link to="/chat" className="flex items-center justify-center gap-2">
                  <MessageSquare className="h-4 w-4" />
                  返回聊天
                </Link>
              </Button>

              <div className="flex gap-3">
                <Button
                    asChild
                    variant="outline"
                    className="flex-1 border-emerald-200 text-emerald-700 hover:bg-emerald-50 hover:border-emerald-300 transition-all duration-200 hover:-translate-y-0.5"
                >
                  <Link to="/" className="flex items-center justify-center gap-2">
                    <Home className="h-4 w-4" />
                    首页
                  </Link>
                </Button>

                <Button
                    variant="ghost"
                    className="flex-1 text-teal-600 hover:text-teal-700 hover:bg-teal-50 transition-all duration-200 hover:-translate-y-0.5"
                    onClick={() => window.history.back()}
                >
                  <ArrowLeft className="h-4 w-4 mr-2" />
                  返回上页
                </Button>
              </div>
            </div>
          </div>

          {/* 底部提示 */}
          <div className="mt-6 text-center text-xs text-emerald-500">
            <p>如果问题持续存在，请联系管理员</p>
            <div className="mt-2 flex justify-center gap-4">
              <Link to="/help" className="text-emerald-600 hover:text-emerald-700 transition-colors hover:underline">
                帮助中心
              </Link>
              <span className="text-emerald-300">|</span>
              <Link to="/contact" className="text-teal-600 hover:text-teal-700 transition-colors hover:underline">
                联系我们
              </Link>
            </div>
          </div>
        </div>
      </div>
  );
}
