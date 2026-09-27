<script setup>
import { ref, onMounted, onUnmounted } from 'vue'

const pet = ref(null)
const food = ref('')
const msg = ref('和宠物打个招呼吧！')
const busy = ref(false)
let timer = null

async function load() {
  try {
    pet.value = await (await fetch('/api/pet')).json()
  } catch {
    msg.value = '后端失联，宠物走丢了…'
  }
}

async function act(path, body) {
  if (busy.value) return
  busy.value = true
  try {
    const res = await fetch(path, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(body || {})
    })
    const data = await res.json()
    msg.value = data.message
    pet.value = data.pet || pet.value
  } catch {
    msg.value = '请求失败，再试一次？'
  } finally {
    busy.value = false
  }
}

function feed() {
  act('/api/pet/feed', { food: food.value })
  food.value = ''
}
function play() {
  act('/api/pet/play')
}

onMounted(() => {
  load()
  timer = setInterval(load, 10000)
})
onUnmounted(() => clearInterval(timer))
</script>

<template>
  <div class="card pet-card" v-if="pet">
    <h2>🐣 我的宠物</h2>

    <div class="pet-stage">
      <div class="pet-face" :class="{ 'anim-bounce': pet.mood >= 80, 'anim-wiggle': pet.satiety < 25 }">
        {{ pet.face }}
      </div>
      <div class="pet-name">
        {{ pet.name }} <span class="badge b-blue">Lv.{{ pet.level }}</span>
        <span class="badge b-yellow">{{ pet.state }}</span>
      </div>
      <div class="bubble">{{ msg }}</div>
    </div>

    <div class="bars">
      <div class="row">
        <span class="label">🍚 饱食度</span>
        <div class="bar"><div class="bar-fill f-green" :style="{ width: pet.satiety + '%' }"></div></div>
        <span class="num">{{ pet.satiety }}</span>
      </div>
      <div class="row">
        <span class="label">💗 心情</span>
        <div class="bar"><div class="bar-fill f-pink" :style="{ width: pet.mood + '%' }"></div></div>
        <span class="num">{{ pet.mood }}</span>
      </div>
      <div class="row">
        <span class="label">⭐ 经验</span>
        <div class="bar"><div class="bar-fill f-yellow" :style="{ width: (pet.exp / pet.expMax * 100) + '%' }"></div></div>
        <span class="num">{{ pet.exp }}/{{ pet.expMax }}</span>
      </div>
    </div>

    <div class="actions">
      <input class="input" v-model="food" placeholder="想喂点什么？蛋糕 / 苹果…" @keyup.enter="feed" />
      <button class="btn green" :disabled="busy" @click="feed">🍓 喂食</button>
      <button class="btn pink" :disabled="busy" @click="play">🎾 玩耍</button>
    </div>
  </div>

  <div class="card" v-else>
    <h2>🐣 我的宠物</h2>
    <p>宠物正在赶来岛上…</p>
  </div>
</template>

<style scoped>
.pet-stage { text-align: center; margin-bottom: 14px; }
.pet-face { font-size: 84px; line-height: 1.1; }
.pet-name { font-size: 1.1rem; font-weight: bold; margin: 6px 0; display: flex; justify-content: center; gap: 8px; align-items: center; flex-wrap: wrap; }
.bubble {
  display: inline-block;
  background: #fff;
  border: 3px solid var(--ink);
  border-radius: 16px;
  padding: 8px 14px;
  font-size: 0.9rem;
  margin-top: 8px;
  position: relative;
  max-width: 100%;
}
.bubble::before {
  content: '';
  position: absolute;
  top: -12px; left: 24px;
  border: 6px solid transparent;
  border-bottom-color: var(--ink);
}
.bars .row {
  display: grid;
  grid-template-columns: 72px 1fr 64px;
  align-items: center;
  gap: 10px;
  margin: 10px 0;
}
.bars .label { font-size: 0.9rem; font-weight: bold; }
.bars .num { font-size: 0.85rem; text-align: right; font-variant-numeric: tabular-nums; }
.actions { display: flex; gap: 10px; margin-top: 16px; flex-wrap: wrap; }
.actions .input { flex: 1; min-width: 140px; }
</style>
