<template>
  <div class="navbar" :class="'nav' + navType">
    <hamburger id="hamburger-container" :is-active="sidebar.opened" class="hamburger-container" @toggleClick="toggleSideBar" />
    <breadcrumb v-if="navType == 1" id="breadcrumb-container" class="breadcrumb-container" />
    <top-nav v-if="navType == 2" id="topmenu-container" class="topmenu-container" />
    <template v-if="navType == 3">
      <logo v-show="showLogo" :collapse="false"></logo>
      <top-bar id="topbar-container" class="topbar-container" />
    </template>
    <div class="right-menu">
      <template v-if="device!=='mobile'">
        <search id="header-search" class="right-menu-item" />
        <el-tooltip content="源码地址" effect="dark" placement="bottom">
          <ruo-yi-git id="ruoyi-git" class="right-menu-item hover-effect" />
        </el-tooltip>
        <el-tooltip content="文档地址" effect="dark" placement="bottom">
          <ruo-yi-doc id="ruoyi-doc" class="right-menu-item hover-effect" />
        </el-tooltip>
        <screenfull id="screenfull" class="right-menu-item hover-effect" />
        <el-tooltip content="布局大小" effect="dark" placement="bottom">
          <size-select id="size-select" class="right-menu-item hover-effect" />
        </el-tooltip>
      </template>
      <el-dropdown class="avatar-container right-menu-item hover-effect" trigger="hover">
        <div class="avatar-wrapper">
          <img :src="avatar" class="user-avatar">
          <span class="user-nickname">{{ nickName }}</span>
          <i class="el-icon-arrow-down"></i>
        </div>
        <el-dropdown-menu slot="dropdown">
          <router-link to="/user/profile">
            <el-dropdown-item><i class="el-icon-user"></i> 个人中心</el-dropdown-item>
          </router-link>
          <el-dropdown-item @click.native="setLayout" v-if="setting">
            <i class="el-icon-setting"></i> 布局设置
          </el-dropdown-item>
          <el-dropdown-item divided @click.native="logout">
            <i class="el-icon-switch-button"></i> 退出登录
          </el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import Breadcrumb from '@/components/Breadcrumb'
import TopNav from '@/components/TopNav'
import TopBar from './TopBar'
import Logo from './Sidebar/Logo'
import Hamburger from '@/components/Hamburger'
import Screenfull from '@/components/Screenfull'
import SizeSelect from '@/components/SizeSelect'
import Search from '@/components/HeaderSearch'
import RuoYiGit from '@/components/RuoYi/Git'
import RuoYiDoc from '@/components/RuoYi/Doc'

export default {
  emits: ['setLayout'],
  components: { Breadcrumb, Logo, TopNav, TopBar, Hamburger, Screenfull, SizeSelect, Search, RuoYiGit, RuoYiDoc },
  computed: {
    ...mapGetters(['sidebar', 'avatar', 'device', 'nickName']),
    setting: { get() { return this.$store.state.settings.showSettings } },
    navType: { get() { return this.$store.state.settings.navType } },
    showLogo: { get() { return this.$store.state.settings.sidebarLogo } }
  },
  methods: {
    toggleSideBar() { this.$store.dispatch('app/toggleSideBar') },
    setLayout(event) { this.$emit('setLayout') },
    logout() {
      this.$confirm('确定注销并退出系统吗？', '提示', { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' }).then(() => {
        this.$store.dispatch('LogOut').then(() => { location.href = '/index' })
      }).catch(() => {})
    }
  }
}
</script>

<style lang="scss" scoped>
.navbar.nav3 { .hamburger-container { display: none !important; } }
.navbar {
  height: 64px; overflow: hidden; position: relative; background: #FFFFFF; box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05); display: flex; align-items: center; box-sizing: border-box; border-bottom: 1px solid #F1F5F9;
  .hamburger-container { line-height: 64px; height: 100%; cursor: pointer; transition: all 0.3s; -webkit-tap-highlight-color: transparent; display: flex; align-items: center; flex-shrink: 0; margin-right: 8px; padding: 0 16px; border-radius: 8px; margin-left: 8px; &:hover { background: #F1F5F9; } }
  .breadcrumb-container { flex-shrink: 0; }
  .topmenu-container { position: absolute; left: 50px; }
  .topbar-container { flex: 1; min-width: 0; display: flex; align-items: center; overflow: hidden; margin-left: 8px; }
  .errLog-container { display: inline-block; vertical-align: top; }
  .right-menu {
    height: 100%; line-height: 64px; display: flex; align-items: center; margin-left: auto; padding-right: 16px;
    &:focus { outline: none; }
    .right-menu-item { display: inline-flex; align-items: center; padding: 0 12px; height: 40px; font-size: 18px; color: #64748B; vertical-align: text-bottom; border-radius: 8px; margin: 0 2px;
      &.hover-effect { cursor: pointer; transition: all 0.3s; &:hover { background: #F1F5F9; color: #0891B2; } }
    }
    .avatar-container {
      margin-right: 0px; padding: 0 12px; height: 44px; border-radius: 22px; background: #F8FAFC; transition: all 0.3s;
      &:hover { background: #F1F5F9; }
      .avatar-wrapper { display: flex; align-items: center; gap: 10px;
        .user-avatar { cursor: pointer; width: 32px; height: 32px; border-radius: 50%; border: 2px solid #E2E8F0; }
        .user-nickname { font-size: 14px; font-weight: 500; color: #1E293B; max-width: 100px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
        .el-icon-arrow-down { font-size: 12px; color: #94A3B8; transition: transform 0.3s; }
      }
    }
  }
}
</style>
