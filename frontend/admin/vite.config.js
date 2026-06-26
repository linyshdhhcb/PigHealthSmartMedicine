import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import path from 'path'

const BACKEND_URL = 'http://127.0.0.1:19999'

export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: {
      '@': path.resolve(__dirname, 'src')
    }
  },
  define: {
    __BACKEND_URL__: JSON.stringify(BACKEND_URL)
  },
  server: {
    proxy: {
      '/conversation/': BACKEND_URL,
      '/knowledge/': BACKEND_URL,
      '/knowledgeBase/': BACKEND_URL,
      '/knowledgeChunk/': BACKEND_URL,
      '/user/': BACKEND_URL,
      '/illness/': BACKEND_URL,
      '/illnessKind/': BACKEND_URL,
      '/illnessMedicine/': BACKEND_URL,
      '/medicine/': BACKEND_URL,
      '/feedback/': BACKEND_URL,
      '/files/': BACKEND_URL,
      '/history/': BACKEND_URL,
      '/articles/': BACKEND_URL,
      '/articleTypes/': BACKEND_URL,
      '/newsArticles/': BACKEND_URL,
      '/travelNotes/': BACKEND_URL,
      '/pageview/': BACKEND_URL,
      '/operationLog/': BACKEND_URL,
      '/auth/': BACKEND_URL
    }
  }
})
