<script setup lang="ts">

import type {Ref} from "vue";

const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080';
const favoriteWords: Ref<Array<string>> = await $fetch(`${API_URL}/translator/favorite?userId=1`);

</script>

<template>
  <h3>Favorite words - English</h3>
  
  <div class="container mx-auto px-4 py-8">
    <ul class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-4">
      <li v-for="favoriteWord in favoriteWords" class="bg-white shadow-md p-4 hover:shadow-lg transition-shadow duration-300">
        <div class="text-lg font-semibold text-gray-800">{{ favoriteWord }}</div>
      </li>
    </ul>
  </div>
</template>