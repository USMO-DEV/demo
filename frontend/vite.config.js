import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

export default defineConfig({
  plugins: [vue()],
  server: {
    // 本地开发时把 /api 代理到 Java 后端
    proxy: {
      '/api': 'http://localhost:8080'
    }
  }
})
