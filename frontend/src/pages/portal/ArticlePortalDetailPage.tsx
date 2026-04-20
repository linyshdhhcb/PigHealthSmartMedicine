import { useEffect, useState } from "react";
import { Link, useParams } from "react-router-dom";

import { getArticle, type Article } from "@/services/articleService";

export function ArticlePortalDetailPage() {
  const { id } = useParams<{ id: string }>();
  const [detail, setDetail] = useState<Article | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (!id) return;
    const load = async () => {
      try {
        setLoading(true);
        setDetail(await getArticle(id));
      } finally {
        setLoading(false);
      }
    };
    void load();
  }, [id]);

  if (loading) return <p className="text-sm text-teal-700/75">加载中...</p>;
  if (!detail) return <p className="text-sm text-teal-700/75">未找到内容</p>;

  return (
    <article className="space-y-4 rounded-xl border border-emerald-100 bg-white p-6">
      <Link to="/app/articles" className="text-sm text-emerald-700 hover:underline">
        返回文章列表
      </Link>
      <h1 className="text-2xl font-semibold text-teal-900">{detail.title}</h1>
      <p className="text-sm text-teal-700/75">作者：{detail.author || "未知"} </p>
      <div className="prose prose-sm max-w-none text-teal-900" dangerouslySetInnerHTML={{ __html: detail.content || "" }} />
    </article>
  );
}
