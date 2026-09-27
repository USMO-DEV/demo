<script setup>
import { ref, onMounted, onUnmounted } from 'vue'

const time = ref('--:--:--')
const ok = ref(false)
let timer = null

async function tick() {
  try {
    const res = await fetch('/api/time')
    time.value = (await res.json()).time
    ok.value = true
  } catch {
    time.value = '请求失败'
    ok.value = false
  }
}

onMounted(() => {
  tick()
  timer = setInterval(tick, 1000)
})
onUnmounted(() => clearInterval(timer))
</script>

<template>
  <div class="card">
    <h2>🕐 岛屿时间</h2>
    <div class="big">{{ time }}</div>
    <span class="badge" :class="ok ? 'b-green' : 'b-red'">
      {{ ok ? '● 后端在线' : '● 后端失联' }}
    </span>
  </div>
</template>

<style scoped>
.big {
  font-size: 2.4rem;
  font-weight: bold;
  letter-spacing: 3px;
  margin: 10px 0 16px;
  font-variant-numeric: tabular-nums;
}
</style>
