<script setup lang="ts">

import type {Ref} from "vue";

const BORDER_ICON = "ic:baseline-star-border";
const FILLED_ICON = "ic:baseline-star";

interface Props {
  wordId?: number
}

const props = defineProps<Props>()

const icon: Ref<string> = ref(BORDER_ICON);

async function saveToFavorite() {
  try {
    const response = await $fetch("http://localhost:8080/translator/favorite", {
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
    <Icon @click="saveToFavorite" :name="icon" class="text-3xl hover:cursor-pointer"
          @mouseover="() => icon = FILLED_ICON"
          @mouseleave="() => icon = BORDER_ICON"
    />
    <h1>Word id: {{ wordId }}</h1>
  </div>
</template>

<style scoped>

</style>