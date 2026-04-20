import { useEffect, useState } from "react";
import { Link, useParams } from "react-router-dom";

import { MEDICINE_TYPE_OPTIONS, getMedicine, type Medicine } from "@/services/medicineService";

export function MedicinePortalDetailPage() {
  const { id } = useParams<{ id: string }>();
  const [detail, setDetail] = useState<Medicine | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (!id) return;
    const load = async () => {
      try {
        setLoading(true);
        setDetail(await getMedicine(id));
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
      <Link to="/app/medicines" className="text-sm text-emerald-700 hover:underline">
        返回药品列表
      </Link>
      <div className="flex items-start justify-between gap-4">
        <div>
          <h1 className="text-2xl font-semibold text-teal-900">{detail.medicineName || "-"}</h1>
          <p className="mt-1 text-sm text-teal-700/75">
            类型：{MEDICINE_TYPE_OPTIONS.find((x) => x.value === detail.medicineType)?.label || "-"} · 品牌：{detail.medicineBrand || "-"}
          </p>
        </div>
        {detail.imgPath ? <img src={detail.imgPath} alt="" className="h-20 w-20 rounded-lg border border-emerald-100 object-cover" /> : null}
      </div>
      <p className="text-sm text-teal-700/80">关键字：{detail.keyword || "-"}</p>
      <section className="space-y-2">
        <h2 className="text-base font-medium text-teal-800">功效</h2>
        <p className="whitespace-pre-wrap rounded-lg border border-emerald-100 bg-emerald-50/40 p-3 text-sm text-teal-900">{detail.medicineEffect || "-"}</p>
      </section>
      <section className="space-y-2">
        <h2 className="text-base font-medium text-teal-800">相互作用</h2>
        <p className="whitespace-pre-wrap rounded-lg border border-emerald-100 bg-emerald-50/40 p-3 text-sm text-teal-900">{detail.interaction || "-"}</p>
      </section>
      <section className="space-y-2">
        <h2 className="text-base font-medium text-teal-800">禁忌</h2>
        <p className="whitespace-pre-wrap rounded-lg border border-emerald-100 bg-emerald-50/40 p-3 text-sm text-teal-900">{detail.taboo || "-"}</p>
      </section>
      <section className="space-y-2">
        <h2 className="text-base font-medium text-teal-800">用法用量</h2>
        <p className="whitespace-pre-wrap rounded-lg border border-emerald-100 bg-emerald-50/40 p-3 text-sm text-teal-900">{detail.usAge || "-"}</p>
      </section>
    </article>
  );
}
