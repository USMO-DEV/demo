<script setup>
import ClockCard from './components/ClockCard.vue'
import PetCard from './components/PetCard.vue'
import TaskBoard from './components/TaskBoard.vue'
import StatsPanel from './components/StatsPanel.vue'
</script>

<template>
  <div class="sky">
    <!-- 漂浮的云朵装饰 -->
    <div class="cloud c1">☁️</div>
    <div class="cloud c2">🎈</div>
    <div class="cloud c3">☁️</div>
    <div class="cloud c4">⭐</div>

    <header class="hero">
      <div class="hero-emoji anim-bounce">🏝️</div>
      <h1>宠物小岛大冒险</h1>
      <p class="slogan">喂宠物 · 做任务 · 升级打怪，前后端分离全家桶！</p>
    </header>

    <main class="grid">
      <PetCard />
      <ClockCard />
      <StatsPanel />
      <TaskBoard class="wide" />
    </main>

    <footer class="footer">
      <span>🐱 Vue 3 前端</span>
      <span>⚡ Java HttpServer 后端</span>
      <span>🐳 Docker + Nginx + Jenkins 部署</span>
    </footer>
  </div>
</template>

<style scoped>
.sky {
  max-width: 1100px;
  margin: 0 auto;
  padding: 36px 20px 20px;
  min-height: 100vh;
  position: relative;
}

/* 云朵 */
.cloud {
  position: fixed;
  font-size: 56px;
  opacity: 0.55;
  pointer-events: none;
  animation: floatX linear infinite;
  z-index: 0;
}
.c1 { top: 6%;  animation-duration: 55s; }
.c2 { top: 18%; font-size: 40px; animation-duration: 70s; animation-delay: -30s; }
.c3 { top: 55%; font-size: 44px; animation-duration: 60s; animation-delay: -15s; }
.c4 { top: 75%; font-size: 36px; animation-duration: 80s; animation-delay: -50s; }

/* 标题区 */
.hero { text-align: center; margin-bottom: 32px; position: relative; z-index: 1; }
.hero-emoji { font-size: 64px; }
.hero h1 {
  font-size: 2.4rem;
  margin: 8px 0;
  color: var(--ink);
  text-shadow: 3px 3px 0 var(--yellow);
}
.slogan { font-size: 1rem; opacity: 0.75; }

/* 布局 */
.grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 28px;
  position: relative;
  z-index: 1;
}
.wide { grid-column: 1 / -1; }

.footer {
  margin-top: 36px;
  text-align: center;
  display: flex;
  justify-content: center;
  flex-wrap: wrap;
  gap: 8px 24px;
  font-size: 0.85rem;
  opacity: 0.6;
}
</style>
