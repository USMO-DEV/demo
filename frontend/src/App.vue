<script setup>
import { ref, onMounted, onUnmounted } from 'vue'

const time = ref('--:--:--')
const status = ref('加载中...')
let timer = null

async function refreshTime() {
  try {
    const res = await fetch('/api/time')
    const data = await res.json()
    time.value = data.time
    status.value = '已连接后端'
  } catch (e) {
    time.value = '请求失败'
    status.value = '后端不可用'
  }
}

onMounted(() => {
  refreshTime()
  timer = setInterval(refreshTime, 1000)
})

onUnmounted(() => clearInterval(timer))
</script>

<template>
  <div class="page">
    <div class="card">
      <h1>你好，Vue 3 + Java</h1>
      <p>前端 Vue 3（Vite 构建），后端 Java HttpServer</p>
      <div class="time-box">{{ time }}</div>
      <button @click="refreshTime">刷新服务器时间</button>
      <div class="tip">数据来自后端接口 /api/time · {{ status }}</div>
    </div>
  </div>
</template>

<style>
* { margin: 0; padding: 0; box-sizing: border-box; }
body {
  font-family: "Segoe UI", "Microsoft YaHei", sans-serif;
  min-height: 100vh;
}
.page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
}
.card {
  background: rgba(255, 255, 255, 0.12);
  backdrop-filter: blur(10px);
  border-radius: 20px;
  padding: 48px 56px;
  text-align: center;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.25);
}
h1 { font-size: 2.2rem; margin-bottom: 12px; }
p { opacity: 0.85; margin-bottom: 28px; }
.time-box {
  font-size: 3rem;
  font-weight: 700;
  letter-spacing: 4px;
  margin-bottom: 28px;
  font-variant-numeric: tabular-nums;
}
button {
  padding: 12px 32px;
  font-size: 1rem;
  border: none;
  border-radius: 999px;
  cursor: pointer;
  background: #fff;
  color: #5a4fcf;
  font-weight: 600;
  transition: transform 0.15s, box-shadow 0.15s;
}
button:hover { transform: translateY(-2px); box-shadow: 0 8px 20px rgba(0, 0, 0, 0.2); }
.tip { margin-top: 20px; font-size: 0.85rem; opacity: 0.7; }
</style>
