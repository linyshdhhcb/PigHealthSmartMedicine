import { useEffect, useState } from "react";
import { Link } from "react-router-dom";

import { Input } from "@/components/ui/input";
import { getArticles, type Article } from "@/services/articleService";

export function ArticlePortalListPage() {
  const [items, setItems] = useState<Article[]>([]);
  const [loading, setLoading] = useState(true);
  const [kw, setKw] = useState("");

  useEffect(() => {
    const load = async () => {
      try {
        setLoading(true);
        const res = await getArticles(1, 100, undefined, kw || undefined);
        setItems(res.records || []);
      } finally {
        setLoading(false);
      }
    };
    void load();
  }, [kw]);

  return (
    <div className="space-y-4">
      <div className="flex items-center justify-between gap-3">
        <h1 className="text-xl font-semibold text-teal-800">文章列表</h1>
        <Input
          value={kw}
          onChange={(e) => setKw(e.target.value)}
          placeholder="搜索标题/内容"
          className="w-full max-w-xs border-emerald-200"
        />
      </div>
      <div className="grid gap-3">
        {loading ? (
          <p className="text-sm text-teal-700/75">加载中...</p>
        ) : items.length ? (
          items.map((item) => (
            <Link
              key={item.id}
              to={`/app/articles/${item.id}`}
              className="rounded-xl border border-emerald-100 bg-white p-4 transition hover:-translate-y-0.5 hover:shadow-md"
            >
              <h3 className="font-medium text-teal-900">{item.title}</h3>
              <p className="mt-1 line-clamp-2 text-sm text-teal-700/80">
                {(item.content || "").replace(/<[^>]+>/g, "").slice(0, 120) || "暂无内容"}
              </p>
            </Link>
          ))
        ) : (
          <p className="text-sm text-teal-700/75">暂无数据</p>
        )}
      </div>
    </div>
  );
}
