<template>
  <header class="bg-gray-200">

    <nav class="flex items-center justify-between flex-wrap">
      <div>
        <NuxtLink to="/">
          <a class="flex items-center text-2xl text-gray-900 m-2">
            <Icon name="material-symbols:dictionary-outline-rounded" class="mr-1 text-4xl"/>
            Dictionary AI
          </a>
        </NuxtLink>
      </div>

      <div class="block sm:hidden border border-black rounded px-2 py-1 mr-2">
        <button class="flex items-center font-bold text-2xl" @click="toggleMenu">
          <Icon name="ic:baseline-menu"/>
        </button>
      </div>

      <div :class="open ? 'block' : 'hidden'" class="w-full flex-grow sm:flex sm:items-center sm:w-auto">
        <div class="sm:flex-grow">
          <NuxtLink to="/" class="block sm:inline-block mr-2 p-2">Home</NuxtLink>
          <NuxtLink to="/favorite" class="block sm:inline-block mr-2 p-2">Favorite</NuxtLink>
        </div>
        <div class="p-2">
          <NuxtLink to="/favorite" class="mr-2">Login</NuxtLink>
        </div>
      </div>

    </nav>

  </header>
</template>
<script setup lang="ts">

import type {Ref} from "vue";

const open: Ref<boolean> = ref(false);

function toggleMenu() {
  open.value = !open.value;
}

</script>