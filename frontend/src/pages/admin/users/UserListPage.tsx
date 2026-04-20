import { useEffect, useState } from "react";
import { Pencil, Plus, RefreshCw, Trash2, UserPlus } from "lucide-react";
import { toast } from "sonner";

import { AlertDialog, AlertDialogAction, AlertDialogCancel, AlertDialogContent, AlertDialogDescription, AlertDialogFooter, AlertDialogHeader, AlertDialogTitle } from "@/components/ui/alert-dialog";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardContent } from "@/components/ui/card";
import { Avatar } from "@/components/common/Avatar";
import { Dialog, DialogContent, DialogDescription, DialogFooter, DialogHeader, DialogTitle } from "@/components/ui/dialog";
import { Input } from "@/components/ui/input";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table";
import type { PageResult, UserItem, UserCreatePayload, UserUpdatePayload } from "@/services/userService";
import { createUser, deleteUser, getUsersPage, updateUser } from "@/services/userService";
import { getErrorMessage } from "@/utils/error";

const PAGE_SIZE = 10;

const roleOptions = [
  { value: "admin", label: "管理员" },
  { value: "user", label: "成员" }
];

const buildEmptyForm = () => ({
  username: "",
  password: "",
  role: "user",
  avatar: ""
});

export function UserListPage() {
  const [pageData, setPageData] = useState<PageResult<UserItem> | null>(null);
  const [loading, setLoading] = useState(true);
  const [searchInput, setSearchInput] = useState("");
  const [keyword, setKeyword] = useState("");
  const [pageNo, setPageNo] = useState(1);
  const [deleteTarget, setDeleteTarget] = useState<UserItem | null>(null);
  const [dialogState, setDialogState] = useState<{ open: boolean; mode: "create" | "edit"; user: UserItem | null }>({
    open: false,
    mode: "create",
    user: null
  });
  const [form, setForm] = useState(buildEmptyForm());

  const users = pageData?.records || [];

  const loadUsers = async (current = pageNo, name = keyword) => {
    try {
      setLoading(true);
      const data = await getUsersPage(current, PAGE_SIZE, name || undefined);
      setPageData(data);
    } catch (error) {
      toast.error(getErrorMessage(error, "加载用户列表失败"));
      console.error(error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadUsers();
  }, [pageNo, keyword]);

  const handleSearch = () => {
    setPageNo(1);
    setKeyword(searchInput.trim());
  };

  const handleRefresh = () => {
    setPageNo(1);
    loadUsers(1, keyword);
  };

  const handleDelete = async () => {
    if (!deleteTarget) return;
    try {
      await deleteUser(deleteTarget.id);
      toast.success("删除成功");
      setDeleteTarget(null);
      setPageNo(1);
      await loadUsers(1, keyword);
    } catch (error) {
      toast.error(getErrorMessage(error, "删除失败"));
      console.error(error);
    } finally {
      setDeleteTarget(null);
    }
  };

  const openCreateDialog = () => {
    setForm(buildEmptyForm());
    setDialogState({ open: true, mode: "create", user: null });
  };

  const openEditDialog = (user: UserItem) => {
    setForm({
      username: user.username || "",
      password: "",
      role: user.role || "user",
      avatar: user.avatar || ""
    });
    setDialogState({ open: true, mode: "edit", user });
  };

  const handleSave = async () => {
    const trimmedUsername = form.username.trim();
    const trimmedPassword = form.password.trim();
    if (!trimmedUsername) {
      toast.error("请输入用户名");
      return;
    }

    try {
      if (dialogState.mode === "create") {
        if (!trimmedPassword) {
          toast.error("请输入初始密码");
          return;
        }
        const payload: UserCreatePayload = {
          username: trimmedUsername,
          password: trimmedPassword,
          role: form.role || "user",
          avatar: form.avatar?.trim() || undefined
        };
        await createUser(payload);
        toast.success("创建成功");
        setPageNo(1);
        await loadUsers(1, keyword);
      } else if (dialogState.user) {
        const payload: UserUpdatePayload = {
          username: trimmedUsername,
          role: form.role || "user",
          avatar: form.avatar?.trim() || undefined,
          password: trimmedPassword || undefined
        };
        await updateUser(dialogState.user.id, payload);
        toast.success("更新成功");
        await loadUsers(pageNo, keyword);
      }
      setDialogState({ open: false, mode: "create", user: null });
    } catch (error) {
      toast.error(getErrorMessage(error, "保存失败"));
      console.error(error);
    }
  };

  const formatDate = (dateStr?: string | null) => {
    if (!dateStr) return "-";
    return new Date(dateStr).toLocaleString("zh-CN");
  };

  const isProtectedAdmin = (user: UserItem) => user.username === "admin";

  return (
    <div className="admin-page">
      <div className="admin-page-header">
        <div>
          <h1 className="admin-page-title">用户管理</h1>
          <p className="admin-page-subtitle">管理后台账号与角色权限</p>
        </div>
        <div className="admin-page-actions flex flex-wrap items-center gap-3">
          <div className="relative">
            <Input
              value={searchInput}
              onChange={(event) => setSearchInput(event.target.value)}
              placeholder="搜索用户名或角色"
              className="w-[220px] pl-10 h-10 bg-white border-emerald-200 focus:border-emerald-400 focus:ring-emerald-400/20 rounded-xl transition-all duration-200"
            />
            <svg className="absolute left-3 top-1/2 -translate-y-1/2 h-4 w-4 text-emerald-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
            </svg>
          </div>
          <Button variant="outline" onClick={handleSearch} className="h-10 px-4 border-emerald-200 text-emerald-700 hover:bg-emerald-50 hover:border-emerald-300 rounded-xl transition-all duration-200">
            搜索
          </Button>
          <Button variant="outline" onClick={handleRefresh} className="h-10 px-4 border-emerald-200 text-emerald-700 hover:bg-emerald-50 hover:border-emerald-300 rounded-xl transition-all duration-200">
            <RefreshCw className="w-4 h-4 mr-2" />
            刷新
          </Button>
          <Button className="h-10 px-5 bg-gradient-to-r from-emerald-500 to-emerald-600 hover:from-emerald-600 hover:to-emerald-700 text-white shadow-lg shadow-emerald-500/25 hover:shadow-emerald-600/30 rounded-xl transition-all duration-200 font-medium" onClick={openCreateDialog}>
            <UserPlus className="w-4 h-4 mr-2" />
            新增用户
          </Button>
        </div>
      </div>

      <Card className="border-emerald-100 shadow-sm overflow-hidden rounded-2xl">
        <CardContent className="p-0">
          {loading ? (
            <div className="text-center py-12 text-muted-foreground">加载中...</div>
          ) : users.length === 0 ? (
            <div className="text-center py-12 text-muted-foreground">
              <svg className="mx-auto h-12 w-12 text-emerald-300 mb-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={1.5} d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z" />
              </svg>
              暂无用户
            </div>
          ) : (
            <Table className="min-w-[860px]">
              <TableHeader>
                <TableRow className="bg-gradient-to-r from-emerald-50 to-teal-50 border-b border-emerald-100">
                  <TableHead className="w-[240px] font-semibold text-emerald-700 py-4">用户</TableHead>
                  <TableHead className="w-[140px] font-semibold text-emerald-700">角色</TableHead>
                  <TableHead className="w-[180px] font-semibold text-emerald-700">创建时间</TableHead>
                  <TableHead className="w-[180px] font-semibold text-emerald-700">更新时间</TableHead>
                  <TableHead className="w-[160px] text-left font-semibold text-emerald-700">操作</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {users.map((user, index) => {
                  const isProtected = isProtectedAdmin(user);
                  const roleLabel = user.role === "admin" ? "管理员" : "成员";
                  return (
                    <TableRow key={user.id} className={`${index % 2 === 0 ? 'bg-white' : 'bg-gradient-to-r from-emerald-50/30 to-teal-50/30'} hover:bg-emerald-50/60 transition-colors duration-200 border-b border-emerald-50`}>
                      <TableCell>
                        <div className="flex items-center gap-3">
                          <Avatar
                            name={user.username || "用户"}
                            src={user.avatar?.trim() || undefined}
                            className="h-9 w-9 border-2 border-emerald-200 bg-gradient-to-br from-emerald-50 to-teal-50 text-xs font-semibold text-emerald-600"
                          />
                          <div>
                            <div className="font-medium text-slate-800">{user.username || "-"}</div>
                            {isProtected ? (
                              <div className="text-xs text-emerald-500 flex items-center gap-1">
                                <svg className="h-3 w-3" fill="currentColor" viewBox="0 0 20 20">
                                  <path fillRule="evenodd" d="M2.166 4.999A11.954 11.954 0 0010 1.944 11.954 11.954 0 0017.834 5c.11.65.166 1.32.166 2.001 0 5.225-3.34 9.67-8 11.317C5.34 16.67 2 12.225 2 7c0-.682.057-1.35.166-2.001zm11.541 3.708a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z" clipRule="evenodd" />
                                </svg>
                                默认管理员
                              </div>
                            ) : null}
                          </div>
                        </div>
                      </TableCell>
                      <TableCell>
                        <Badge variant={user.role === "admin" ? "default" : "secondary"} className={user.role === "admin" ? "bg-gradient-to-r from-emerald-500 to-teal-500 text-white border-0" : "bg-teal-50 text-teal-700 border-teal-200"}>
                          {roleLabel}
                        </Badge>
                      </TableCell>
                      <TableCell className="text-slate-500">{formatDate(user.createTime)}</TableCell>
                      <TableCell className="text-slate-500">{formatDate(user.updateTime)}</TableCell>
                      <TableCell className="text-center">
                        <div className="flex justify-center gap-2">
                          <Button
                            variant="outline"
                            size="sm"
                            disabled={isProtected}
                            onClick={() => openEditDialog(user)}
                            className="h-8 px-3 border-emerald-200 text-emerald-600 hover:bg-emerald-50 hover:border-emerald-300 rounded-lg transition-all duration-200"
                          >
                            <Pencil className="w-3.5 h-3.5 mr-1" />
                            编辑
                          </Button>
                          <Button
                            variant="ghost"
                            size="sm"
                            className="h-8 px-3 text-red-500 hover:text-red-600 hover:bg-red-50 rounded-lg transition-all duration-200"
                            disabled={isProtected}
                            onClick={() => setDeleteTarget(user)}
                          >
                            <Trash2 className="w-3.5 h-3.5 mr-1" />
                            删除
                          </Button>
                        </div>
                      </TableCell>
                    </TableRow>
                  );
                })}
              </TableBody>
            </Table>
          )}
        </CardContent>
      </Card>

      <AlertDialog open={!!deleteTarget} onOpenChange={() => setDeleteTarget(null)}>
        <AlertDialogContent>
          <AlertDialogHeader>
            <AlertDialogTitle>确认删除</AlertDialogTitle>
            <AlertDialogDescription>
              此操作将永久删除该用户，无法恢复。确定要继续吗？
            </AlertDialogDescription>
          </AlertDialogHeader>
          <AlertDialogFooter>
            <AlertDialogCancel>取消</AlertDialogCancel>
            <AlertDialogAction onClick={handleDelete} className="bg-destructive text-destructive-foreground">
              删除
            </AlertDialogAction>
          </AlertDialogFooter>
        </AlertDialogContent>
      </AlertDialog>

      <Dialog open={dialogState.open} onOpenChange={(open) => setDialogState((prev) => ({ ...prev, open }))}>
        <DialogContent className="sm:max-w-[420px]">
          <DialogHeader>
            <DialogTitle>{dialogState.mode === "create" ? "新增用户" : "编辑用户"}</DialogTitle>
            <DialogDescription>
              {dialogState.mode === "create" ? "配置账号基本信息" : "更新账号信息，密码留空则不修改"}
            </DialogDescription>
          </DialogHeader>
          <div className="space-y-3">
            <div className="space-y-2">
              <label className="text-sm font-medium">用户名</label>
              <Input
                value={form.username}
                onChange={(event) => setForm((prev) => ({ ...prev, username: event.target.value }))}
                placeholder="请输入用户名"
              />
            </div>
            <div className="space-y-2">
              <label className="text-sm font-medium">密码</label>
              <Input
                type="password"
                value={form.password}
                onChange={(event) => setForm((prev) => ({ ...prev, password: event.target.value }))}
                placeholder={dialogState.mode === "create" ? "设置初始密码" : "留空则不修改"}
              />
            </div>
            <div className="space-y-2">
              <label className="text-sm font-medium">角色</label>
              <Select value={form.role} onValueChange={(value) => setForm((prev) => ({ ...prev, role: value }))}>
                <SelectTrigger>
                  <SelectValue placeholder="请选择角色" />
                </SelectTrigger>
                <SelectContent>
                  {roleOptions.map((option) => (
                    <SelectItem key={option.value} value={option.value}>
                      {option.label}
                    </SelectItem>
                  ))}
                </SelectContent>
              </Select>
            </div>
            <div className="space-y-2">
              <label className="text-sm font-medium">头像</label>
              <Input
                value={form.avatar}
                onChange={(event) => setForm((prev) => ({ ...prev, avatar: event.target.value }))}
                placeholder="可选，填写头像 URL"
              />
            </div>
          </div>
          <DialogFooter>
            <Button variant="outline" onClick={() => setDialogState({ open: false, mode: "create", user: null })}>
              取消
            </Button>
            <Button onClick={handleSave}>
              {dialogState.mode === "create" ? (
                <>
                  <Plus className="mr-2 h-4 w-4" />
                  创建
                </>
              ) : (
                <>
                  <Pencil className="mr-2 h-4 w-4" />
                  保存
                </>
              )}
            </Button>
          </DialogFooter>
        </DialogContent>
      </Dialog>

      {pageData ? (
        <div className="mt-4 flex flex-wrap items-center justify-between gap-2 text-sm text-slate-500">
          <span>共 {pageData.total} 条</span>
          <div className="flex items-center gap-2">
            <Button
              variant="outline"
              size="sm"
              onClick={() => setPageNo((prev) => Math.max(1, prev - 1))}
              disabled={pageData.current <= 1}
            >
              上一页
            </Button>
            <span>
              {pageData.current} / {pageData.pages}
            </span>
            <Button
              variant="outline"
              size="sm"
              onClick={() => setPageNo((prev) => Math.min(pageData.pages || 1, prev + 1))}
              disabled={pageData.current >= pageData.pages}
            >
              下一页
            </Button>
          </div>
        </div>
      ) : null}
    </div>
  );
}
