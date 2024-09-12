<script setup lang="ts">
const open: Ref<boolean> = ref(false);

function toggleMenu() {
  open.value = !open.value;
}

const { isLoggedIn, logout } = useAuth();
</script>

<template>
  <header class="bg-gray-200 shadow-md">

    <nav class="container mx-auto px-4 py-2 flex items-center justify-between flex-wrap">
      <div>
        <NuxtLink to="/">
          <a class="flex items-center text-2xl text-gray-900 m-2">
            <Icon name="material-symbols:dictionary-outline-rounded" class="mr-1 text-4xl" />
            Dictionary AI
          </a>
        </NuxtLink>
      </div>

      <div class="block sm:hidden border border-black rounded px-2 py-1 mr-2">
        <button class="flex items-center font-bold text-2xl" @click="toggleMenu">
          <Icon name="ic:baseline-menu" />
        </button>
      </div>

      <div :class="open ? 'block' : 'hidden'" class="w-full flex-grow sm:flex sm:items-center sm:w-auto">
        <div class="sm:flex-grow">
          <NavLink to="/">Home</NavLink>
          <NavLink to="/favorite" v-if="isLoggedIn">Favorite</NavLink>
        </div>
        <div class="p-2">
          <NavLink :to="'/login'" additional-classes="pl-0" @click="!isLoggedIn ? null : logout()">
            {{ isLoggedIn ? 'Logout' : 'Login' }}
          </NavLink>
        </div>
      </div>

    </nav>
  </header>
</template>