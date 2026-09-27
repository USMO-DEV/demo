<script setup>
import { ref, onMounted, onUnmounted } from 'vue'

const stats = ref(null)
let timer = null

async function load() {
  try {
    stats.value = await (await fetch('/api/stats')).json()
  } catch {
    stats.value = null
  }
}

onMounted(() => {
  load()
  timer = setInterval(load, 5000)
})
onUnmounted(() => clearInterval(timer))
</script>

<template>
  <div class="card" v-if="stats">
    <h2>📊 小岛情报站</h2>

    <div class="stat-grid">
      <div class="stat">
        <span class="icon anim-bounce">🐣</span>
        <span class="val">{{ stats.pet.level }}</span>
        <span class="key">宠物等级</span>
      </div>
      <div class="stat">
        <span class="icon">🍚</span>
        <span class="val">{{ stats.pet.feedCount }}</span>
        <span class="key">喂食次数</span>
      </div>
      <div class="stat">
        <span class="icon">🎾</span>
        <span class="val">{{ stats.pet.playCount }}</span>
        <span class="key">玩耍次数</span>
      </div>
      <div class="stat">
        <span class="icon">📋</span>
        <span class="val">{{ stats.tasks.total }}</span>
        <span class="key">任务总数</span>
      </div>
    </div>

    <div class="rate">
      <span>🏝️ 任务完成率</span>
      <div class="bar">
        <div class="bar-fill f-yellow" :style="{ width: stats.tasks.completionRate + '%' }"></div>
      </div>
      <span class="pct">{{ stats.tasks.completionRate }}%</span>
    </div>
  </div>

  <div class="card" v-else>
    <h2>📊 小岛情报站</h2>
    <p>情报员正在收集数据…</p>
  </div>
</template>

<style scoped>
.stat-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 12px;
  margin-bottom: 16px;
}
.stat {
  border: 3px solid var(--ink);
  border-radius: 16px;
  background: #fff;
  padding: 10px;
  text-align: center;
  display: flex;
  flex-direction: column;
  gap: 2px;
}
.stat .icon { font-size: 22px; }
.stat .val { font-size: 1.5rem; font-weight: bold; }
.stat .key { font-size: 0.75rem; opacity: 0.6; }

.rate {
  display: grid;
  grid-template-columns: auto 1fr auto;
  align-items: center;
  gap: 10px;
  font-size: 0.9rem;
  font-weight: bold;
}
.pct { font-variant-numeric: tabular-nums; }
</style>
