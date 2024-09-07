<script setup lang="ts">

interface Props {
  show: boolean
}

defineProps<Props>();
</script>

<template>
  <div role="status" v-if="show">
    <Icon name="svg-spinners:3-dots-fade" class="w-8 h-8 text-blue-600" />
    <span class="sr-only">Loading...</span>
  </div>
</template>

<style scoped>

</style>