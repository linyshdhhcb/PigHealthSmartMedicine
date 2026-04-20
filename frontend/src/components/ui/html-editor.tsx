import { useEffect, useRef } from "react";
import { Bold, Italic, List, ListOrdered, Redo2, Underline, Undo2 } from "lucide-react";

import { cn } from "@/lib/utils";

type HtmlEditorProps = {
  value: string;
  onChange: (value: string) => void;
  className?: string;
  minHeightClassName?: string;
  placeholder?: string;
};

type CommandButton = {
  icon: typeof Bold;
  title: string;
  command: string;
};

const COMMANDS: CommandButton[] = [
  { icon: Bold, title: "加粗", command: "bold" },
  { icon: Italic, title: "斜体", command: "italic" },
  { icon: Underline, title: "下划线", command: "underline" },
  { icon: List, title: "无序列表", command: "insertUnorderedList" },
  { icon: ListOrdered, title: "有序列表", command: "insertOrderedList" },
  { icon: Undo2, title: "撤销", command: "undo" },
  { icon: Redo2, title: "重做", command: "redo" }
];

export function HtmlEditor({
  value,
  onChange,
  className,
  minHeightClassName = "min-h-[180px]",
  placeholder = "请输入内容"
}: HtmlEditorProps) {
  const editorRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    const el = editorRef.current;
    if (!el) return;
    if (el.innerHTML !== value) {
      el.innerHTML = value || "";
    }
  }, [value]);

  const exec = (command: string) => {
    const el = editorRef.current;
    if (!el) return;
    el.focus();
    document.execCommand(command);
    onChange(el.innerHTML);
  };

  return (
    <div className={cn("rounded-xl border border-emerald-100 bg-white", className)}>
      <div className="flex flex-wrap items-center gap-1 border-b border-emerald-100 bg-emerald-50/70 p-2">
        {COMMANDS.map(({ icon: Icon, title, command }) => (
          <button
            key={command}
            type="button"
            title={title}
            className="inline-flex h-8 w-8 items-center justify-center rounded-lg text-teal-700 transition-all duration-200 hover:bg-emerald-100 hover:text-emerald-700"
            onClick={() => exec(command)}
          >
            <Icon className="h-4 w-4" />
          </button>
        ))}
      </div>
      <div
        ref={editorRef}
        contentEditable
        suppressContentEditableWarning
        className={cn(
          "w-full rounded-b-xl bg-white px-3 py-2 text-sm text-teal-900 outline-none",
          "empty:before:pointer-events-none empty:before:text-teal-700/50 empty:before:content-[attr(data-placeholder)]",
          minHeightClassName
        )}
        data-placeholder={placeholder}
        onInput={(e) => onChange((e.currentTarget as HTMLDivElement).innerHTML)}
      />
    </div>
  );
}
