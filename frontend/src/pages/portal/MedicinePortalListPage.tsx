import { useEffect, useState } from "react";
import { Link } from "react-router-dom";

import { Input } from "@/components/ui/input";
import { MEDICINE_TYPE_OPTIONS, getMedicines, type Medicine } from "@/services/medicineService";

export function MedicinePortalListPage() {
  const [items, setItems] = useState<Medicine[]>([]);
  const [loading, setLoading] = useState(true);
  const [kw, setKw] = useState("");

  useEffect(() => {
    const load = async () => {
      try {
        setLoading(true);
        const res = await getMedicines(1, 200, undefined, kw || undefined);
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
        <h1 className="text-xl font-semibold text-teal-800">药品库</h1>
        <Input
          value={kw}
          onChange={(e) => setKw(e.target.value)}
          placeholder="搜索药品名称/关键字"
          className="w-full max-w-xs border-emerald-200"
        />
      </div>
      <div className="grid gap-3 sm:grid-cols-2 lg:grid-cols-3">
        {loading ? (
          <p className="text-sm text-teal-700/75">加载中...</p>
        ) : items.length ? (
          items.map((item) => (
            <Link
              key={item.id}
              to={`/app/medicines/${item.id}`}
              className="rounded-xl border border-emerald-100 bg-white p-4 transition hover:-translate-y-0.5 hover:shadow-md"
            >
              <div className="flex items-center justify-between">
                <h3 className="font-medium text-teal-900">{item.medicineName || "-"}</h3>
                {item.imgPath ? <img src={item.imgPath} alt="" className="h-10 w-10 rounded-lg border border-emerald-100 object-cover" /> : null}
              </div>
              <p className="mt-1 text-xs text-teal-700/70">类型：{MEDICINE_TYPE_OPTIONS.find((x) => x.value === item.medicineType)?.label || "-"}</p>
              <p className="mt-1 line-clamp-2 text-sm text-teal-700/80">{item.medicineEffect || "暂无功效描述"}</p>
            </Link>
          ))
        ) : (
          <p className="text-sm text-teal-700/75">暂无数据</p>
        )}
      </div>
    </div>
  );
}
