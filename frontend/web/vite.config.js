// vite.config.js
import { defineConfig } from 'vite';
import vue from '@vitejs/plugin-vue';

export default defineConfig({
  plugins: [
    vue(),
  ],
  resolve: {
    alias: {
      '@': '/src'
    },
  },
  server: {
    proxy: {
      '/api': {
        target: 'http://127.0.0.1:9999',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, ''),
        secure: false, // 允许不安全的 SSL 证书
        headers: {
          'Access-Control-Allow-Credentials': 'true'
        },
        credentials: true // 允许发送 cookie
      }
    }
  }
});
