<script setup lang="ts">

import type {Ref} from "vue";

// FIXME
const favoriteWords: Ref<Array<string>> = await $fetch("http://localhost:8080/translator/favorite?userId=1");

</script>

<template>
  <h3>Favorite words - English</h3>
  <ul class="list-decimal">
    <li v-for="favoriteWord in favoriteWords">
      {{ favoriteWord }}
    </li>
  </ul>
</template>