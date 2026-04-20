import { Link } from "react-router-dom";

const cards = [
  {
    title: "文章",
    desc: "查看医疗文章列表与详情",
    to: "/app/articles"
  },
  {
    title: "资讯",
    desc: "浏览最新新闻资讯",
    to: "/app/news"
  },
  {
    title: "疾病",
    desc: "查询疾病库信息",
    to: "/app/illnesses"
  },
  {
    title: "药品",
    desc: "查询药品库信息",
    to: "/app/medicines"
  },
  {
    title: "AI",
    desc: "进入 AI 对话助手",
    to: "/chat"
  }
];

export function PortalHomePage() {
  return (
    <div className="space-y-6">
      <div className="rounded-xl border border-emerald-100 bg-white p-6 shadow-sm">
        <h1 className="text-2xl font-semibold text-teal-900">欢迎使用 PHSM 健康门户</h1>
        <p className="mt-2 text-sm text-teal-700/80">你可以通过下方入口快速访问文章、资讯、疾病、药品与 AI 助手。</p>
      </div>
      <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-3">
        {cards.map((card) => (
          <Link
            key={card.title}
            to={card.to}
            className="rounded-xl border border-emerald-100 bg-white p-5 shadow-sm transition-all duration-200 hover:-translate-y-0.5 hover:shadow-md"
          >
            <h2 className="text-lg font-semibold text-teal-900">{card.title}</h2>
            <p className="mt-1 text-sm text-teal-700/80">{card.desc}</p>
          </Link>
        ))}
      </div>
    </div>
  );
}
