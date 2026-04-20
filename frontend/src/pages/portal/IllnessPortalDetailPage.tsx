import { useEffect, useState } from "react";
import { Link, useParams } from "react-router-dom";

import { getIllness, type Illness } from "@/services/illnessService";

export function IllnessPortalDetailPage() {
  const { id } = useParams<{ id: string }>();
  const [detail, setDetail] = useState<Illness | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (!id) return;
    const load = async () => {
      try {
        setLoading(true);
        setDetail(await getIllness(id));
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
      <Link to="/app/illnesses" className="text-sm text-emerald-700 hover:underline">
        返回疾病列表
      </Link>
      <h1 className="text-2xl font-semibold text-teal-900">{detail.illnessName || "-"}</h1>
      <p className="text-sm text-teal-700/75">分类：{detail.kindName || detail.kindId || "-"}</p>
      <section className="space-y-2">
        <h2 className="text-base font-medium text-teal-800">诱发因素</h2>
        <div className="prose prose-sm max-w-none rounded-lg border border-emerald-100 bg-emerald-50/40 p-3 text-teal-900" dangerouslySetInnerHTML={{ __html: detail.includeReason || "-" }} />
      </section>
      <section className="space-y-2">
        <h2 className="text-base font-medium text-teal-800">疾病症状</h2>
        <div className="prose prose-sm max-w-none rounded-lg border border-emerald-100 bg-emerald-50/40 p-3 text-teal-900" dangerouslySetInnerHTML={{ __html: detail.illnessSymptom || "-" }} />
      </section>
      <section className="space-y-2">
        <h2 className="text-base font-medium text-teal-800">特殊症状</h2>
        <div className="prose prose-sm max-w-none rounded-lg border border-emerald-100 bg-emerald-50/40 p-3 text-teal-900" dangerouslySetInnerHTML={{ __html: detail.specialSymptom || "-" }} />
      </section>
    </article>
  );
}
