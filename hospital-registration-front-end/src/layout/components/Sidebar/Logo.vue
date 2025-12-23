<template>
  <div class="sidebar-logo-container" :class="{'collapse':collapse}" :style="{ backgroundColor: sideTheme === 'theme-dark' && navType !== 3 ? 'transparent' : variables.menuLightBackground }">
    <transition name="sidebarLogoFade">
      <router-link v-if="collapse" key="collapse" class="sidebar-logo-link" to="/">
        <div class="logo-icon">
          <svg viewBox="0 0 32 32" fill="none" xmlns="http://www.w3.org/2000/svg">
            <rect width="32" height="32" rx="8" fill="rgba(255,255,255,0.15)"/>
            <path d="M16 8v8m0 0l6-4m-6 4l-6-4" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
            <circle cx="16" cy="20" r="3" fill="currentColor"/>
          </svg>
        </div>
      </router-link>
      <router-link v-else key="expand" class="sidebar-logo-link" to="/">
        <div class="logo-icon">
          <svg viewBox="0 0 32 32" fill="none" xmlns="http://www.w3.org/2000/svg">
            <rect width="32" height="32" rx="8" fill="rgba(255,255,255,0.15)"/>
            <path d="M16 8v8m0 0l6-4m-6 4l-6-4" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
            <circle cx="16" cy="20" r="3" fill="currentColor"/>
          </svg>
        </div>
        <h1 class="sidebar-title" :style="{ color: sideTheme === 'theme-dark' && navType !== 3 ? variables.logoTitleColor : variables.logoLightTitleColor }">{{ title }}</h1>
      </router-link>
    </transition>
  </div>
</template>

<script>
import logoImg from '@/assets/logo/logo.png'
import variables from '@/assets/styles/variables.scss'

export default {
  name: 'SidebarLogo',
  props: { collapse: { type: Boolean, required: true } },
  computed: {
    variables() { return variables },
    sideTheme() { return this.$store.state.settings.sideTheme },
    navType() { return this.$store.state.settings.navType }
  },
  data() { return { title: process.env.VUE_APP_TITLE, logo: logoImg } }
}
</script>

<style lang="scss" scoped>
.sidebarLogoFade-enter-active { transition: opacity 0.5s; }
.sidebarLogoFade-enter, .sidebarLogoFade-leave-to { opacity: 0; }
.sidebar-logo-container {
  position: relative; height: 64px; line-height: 64px; text-align: center; overflow: hidden; padding: 0 16px;
  .sidebar-logo-link { height: 100%; width: 100%; display: flex; align-items: center; justify-content: flex-start; gap: 12px;
    .logo-icon { width: 40px; height: 40px; flex-shrink: 0; color: #FFFFFF;
      svg { width: 100%; height: 100%; }
    }
    .sidebar-title { display: inline-block; margin: 0; color: #fff; font-weight: 600; line-height: 64px; font-size: 16px; font-family: 'Inter', -apple-system, BlinkMacSystemFont, sans-serif; vertical-align: middle; white-space: nowrap; overflow: hidden; text-overflow: ellipsis; letter-spacing: -0.3px; }
  }
  &.collapse { padding: 0;
    .sidebar-logo-link { justify-content: center; }
    .logo-icon { margin-right: 0; }
  }
}
</style>
