import { useState } from "react";
import { toast } from "sonner";

import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Textarea } from "@/components/ui/textarea";
import { submitFeedback } from "@/services/feedbackService";
import { useAuthStore } from "@/stores/authStore";
import { getErrorMessage } from "@/utils/error";

export function FeedbackSubmitPage() {
  const user = useAuthStore((s) => s.user);
  const [name, setName] = useState(user?.username?.slice(0, 11) || "");
  const [email, setEmail] = useState("");
  const [title, setTitle] = useState("");
  const [content, setContent] = useState("");
  const [busy, setBusy] = useState(false);

  const onSubmit = async () => {
    if (!name.trim()) {
      toast.error("请输入姓名");
      return;
    }
    if (!email.includes("@")) {
      toast.error("请输入有效邮箱");
      return;
    }
    if (!title.trim()) {
      toast.error("请输入标题");
      return;
    }
    if (!content.trim()) {
      toast.error("请输入内容");
      return;
    }
    try {
      setBusy(true);
      await submitFeedback({
        name: name.trim(),
        email: email.trim(),
        title: title.trim(),
        content: content.trim()
      });
      toast.success("感谢您的反馈！");
      setTitle("");
      setContent("");
    } catch (e) {
      toast.error(getErrorMessage(e, "提交失败"));
    } finally {
      setBusy(false);
    }
  };

  return (
    <div className="mx-auto max-w-lg space-y-6 p-6">
      <Card>
        <CardHeader>
          <CardTitle>意见反馈</CardTitle>
        </CardHeader>
        <CardContent className="space-y-4">
          <div>
            <Label>姓名（≤11字）</Label>
            <Input value={name} maxLength={11} onChange={(e) => setName(e.target.value)} />
          </div>
          <div>
            <Label>邮箱</Label>
            <Input type="email" value={email} onChange={(e) => setEmail(e.target.value)} />
          </div>
          <div>
            <Label>标题</Label>
            <Input value={title} maxLength={255} onChange={(e) => setTitle(e.target.value)} />
          </div>
          <div>
            <Label>内容</Label>
            <Textarea rows={8} value={content} maxLength={5000} onChange={(e) => setContent(e.target.value)} />
          </div>
          <Button className="w-full" onClick={onSubmit} disabled={busy}>
            提交
          </Button>
        </CardContent>
      </Card>
    </div>
  );
}
