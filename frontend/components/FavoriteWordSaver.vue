<script setup lang="ts">

interface Props {
  wordId?: number
}

const props = defineProps<Props>()

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
    <Icon @click="saveToFavorite" name="material-symbols:star-outline-rounded" class="text-3xl hover:cursor-pointer"/>
    <h1>WordId: {{wordId}}</h1>
  </div>
</template>

<style scoped>

</style>