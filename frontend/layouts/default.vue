<script setup lang="ts">
</script>
<template>
  <div class="min-h-screen flex flex-col">
    <AppHeader />
    <div class="flex-grow">
      <section class="bg-gray-100">
        <div class="flex flex-col items-center sm:justify-center mx-auto min-h-screen pb-24">
          <slot />
        </div>
      </section>
    </div>
  </div>
</template>