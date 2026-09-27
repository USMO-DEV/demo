<script setup>
import { ref, onMounted } from 'vue'

const tasks = ref([])
const title = ref('')
const toast = ref('')
const busy = ref(false)

function showToast(text) {
  toast.value = text
  setTimeout(() => (toast.value = ''), 2500)
}

async function load() {
  try {
    tasks.value = await (await fetch('/api/tasks')).json()
  } catch {
    showToast('任务看板加载失败')
  }
}

async function add() {
  const t = title.value.trim()
  if (!t) return
  busy.value = true
  try {
    const res = await fetch('/api/tasks', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ title: t })
    })
    const data = await res.json()
    if (res.ok) {
      title.value = ''
      showToast(data.message)
      await load()
    } else {
      showToast(data.error || '添加失败')
    }
  } finally {
    busy.value = false
  }
}

async function toggle(task) {
  const res = await fetch(`/api/tasks/${task.id}/toggle`, { method: 'POST' })
  const data = await res.json()
  if (res.ok) {
    showToast(data.message)
    await load()
  }
}

async function remove(task) {
  const res = await fetch(`/api/tasks/${task.id}`, { method: 'DELETE' })
  const data = await res.json()
  if (res.ok) {
    showToast(data.message)
    await load()
  }
}

onMounted(load)
</script>

<template>
  <div class="card">
    <h2>📋 冒险任务看板
      <span class="badge b-blue">{{ tasks.length }} 个任务</span>
      <span class="badge b-green">{{ tasks.filter(t => t.done).length }} 已完成</span>
    </h2>

    <div class="add-row">
      <input class="input" v-model="title" placeholder="输入新任务，比如：给宠物买蛋糕 🎂" @keyup.enter="add" />
      <button class="btn blue" :disabled="busy" @click="add">➕ 添加</button>
    </div>

    <div class="toast anim-pop" v-if="toast">{{ toast }}</div>

    <ul class="task-list" v-if="tasks.length">
      <li v-for="t in tasks" :key="t.id" class="task" :class="{ done: t.done }">
        <label class="tick" @click="toggle(t)">
          <span class="box">{{ t.done ? '✔️' : '' }}</span>
        </label>
        <div class="body">
          <span class="title">{{ t.title }}</span>
          <span class="meta">创建于 {{ t.createdAt }} · #{{ t.id }}</span>
        </div>
        <button class="btn small red" @click="remove(t)">🗑️</button>
      </li>
    </ul>

    <div class="empty" v-else>
      <span class="anim-bounce" style="font-size: 44px">🍃</span>
      <p>看板空空的，去添加第一个冒险任务吧！</p>
    </div>
  </div>
</template>

<style scoped>
.add-row { display: flex; gap: 10px; margin-bottom: 14px; }
.add-row .input { flex: 1; }

.toast {
  background: var(--yellow);
  border: 3px solid var(--ink);
  border-radius: 14px;
  padding: 8px 14px;
  display: inline-block;
  font-weight: bold;
  margin-bottom: 14px;
}

.task-list { list-style: none; }
.task {
  display: flex;
  align-items: center;
  gap: 12px;
  border: 3px solid var(--ink);
  border-radius: 16px;
  padding: 10px 14px;
  margin: 10px 0;
  background: #fff;
  box-shadow: 4px 4px 0 rgba(43, 43, 43, 0.15);
}
.task.done { background: #eaffea; opacity: 0.85; }
.task.done .title { text-decoration: line-through; }

.tick { cursor: pointer; }
.box {
  display: inline-flex;
  width: 30px; height: 30px;
  border: 3px solid var(--ink);
  border-radius: 10px;
  align-items: center;
  justify-content: center;
  background: #fff;
  font-size: 16px;
}

.body { flex: 1; display: flex; flex-direction: column; }
.title { font-weight: bold; }
.meta { font-size: 0.75rem; opacity: 0.55; margin-top: 2px; }

.empty { text-align: center; padding: 24px 0; opacity: 0.8; }
</style>
