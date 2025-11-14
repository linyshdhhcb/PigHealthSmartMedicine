<!-- components/IllnessCard.vue -->
<template>
  <div class="ill-card" @click="onClickCard" @mouseenter="hover = true" @mouseleave="hover = false">
    <div class="ill-left">
      <img
        :data-src="imageUrl"
        :alt="illness.illnessName"
        class="ill-img lazy"
        ref="imgRef"
      />
    </div>

    <div class="ill-right">
      <div class="top-row">
        <h3 class="title">{{ illness.illnessName }}</h3>
        <div class="views">👁️ {{ illness.pageviews ?? 0 }}</div>
      </div>

      <div class="meta">{{ illness.kindName }} · {{ illness.kindInfo }}</div>

      <div class="desc">
        <div class="desc-item">
          <span class="label">诱发原因：</span>
          <span class="val" v-html="short(illness.includeReason,40)"></span>
        </div>
        <div class="desc-item">
          <span class="label">主要症状：</span>
          <span class="val" v-html="short(illness.illnessSymptom,40)"></span>
        </div>
        <div class="desc-item">
          <span class="label">特殊症状：</span>
          <span class="val" v-html="short(illness.specialSymptom,40)"></span>
        </div>
      </div>

      <div class="actions">
        <el-button size="small" type="primary" @click.stop="viewDetail">查看详情</el-button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount } from 'vue'
const props = defineProps({
  illness: { type: Object, required: true },
  imageUrl: { type: String, default: '' }
})
const emits = defineEmits(['open-detail','card-click'])

const imgRef = ref(null)
let observer = null
const hover = ref(false)

const onClickCard = () => emits('card-click', props.illness)

const viewDetail = () => emits('open-detail', props.illness.id)

const short = (html, max = 50) => {
  if (!html) return ''
  // 如果已经是纯文本或安全HTML，截短文本
  const t = (new DOMParser()).parseFromString(html,'text/html').body.textContent || ''
  return t.length > max ? `${t.slice(0,max)}...` : t
}

onMounted(() => {
  // 简单懒加载（IntersectionObserver）
  if ('IntersectionObserver' in window && imgRef.value) {
    observer = new IntersectionObserver(entries => {
      entries.forEach(e => {
        if (e.isIntersecting) {
          const img = e.target
          const src = img.dataset.src
          if (src) img.src = src
          observer.unobserve(img)
        }
      })
    }, { rootMargin: '100px' })
    observer.observe(imgRef.value)
  } else if (imgRef.value) {
    // 回退：直接加载
    imgRef.value.src = imgRef.value.dataset.src
  }
})

onBeforeUnmount(()=> {
  observer && observer.disconnect()
})
</script>

<style scoped>
.ill-card{
  display:flex;
  background:#fff;
  border-radius:12px;
  box-shadow: 0 6px 18px rgba(0,0,0,0.06);
  overflow:hidden;
  transition: transform .18s ease, box-shadow .18s ease;
  cursor:pointer;
}
.ill-card:hover { transform: translateY(-6px); box-shadow: 0 12px 30px rgba(0,0,0,0.10); }
.ill-left { width:200px; flex-shrink:0; }
.ill-img { width:100%; height:100%; object-fit:cover; min-height:140px; display:block; }
.ill-right { padding:16px; flex:1; display:flex; flex-direction:column; justify-content:space-between; }
.top-row{ display:flex; justify-content:space-between; align-items:center; }
.title { font-size:18px; margin:0; color:#222; font-weight:600; }
.meta{ color:#7a7a7a; font-size:13px; margin:8px 0; }
.desc-item{ display:flex; gap:8px; margin-bottom:6px; align-items:flex-start; }
.label{ color:#888; min-width:90px; font-weight:500; }
.val{ color:#333; }
.actions{ text-align:right; }
</style>
