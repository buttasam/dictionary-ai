<script setup lang="ts">

import type {Ref} from "vue";

const BORDER_ICON = "ic:baseline-star-border";
const FILLED_ICON = "ic:baseline-star";

const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080';

interface Props {
  wordId?: number,
  word?: string
}

const props = defineProps<Props>()

const icon: Ref<string> = ref(BORDER_ICON);

const isFavorite = ref(false);

const currentIcon = computed(() => isFavorite.value ? FILLED_ICON : BORDER_ICON);

async function saveToFavorite() {
  try {
    const response = await $fetch(`${API_URL}/translator/favorite`, {
      method: "POST",
      body: {
        wordId: props.wordId,
        userId: 1 // FIXME
      }
    })
    console.log(response);
  } catch (error) {
    console.log(error);
  }
}

</script>

<template>
  <div v-if="wordId">
    <div class="flex">
      <h1 class="text-xl">{{ word }}</h1>

      <Icon @click="saveToFavorite" :name="currentIcon" class="text-3xl hover:cursor-pointer"
            @mouseover="() => isFavorite = true"
            @mouseleave="() => isFavorite = false"
      />
    </div>
  </div>
</template>

<style scoped>

</style>