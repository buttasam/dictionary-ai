<script setup lang="ts">
</script>
<template>
  <div class="min-h-screen flex flex-col">
    <AppHeader/>
    <div class="flex-grow">
      <section class="bg-gray-100 pt-6 ms:pt-0">
        <div class="flex flex-col items-center justify-center mx-auto min-h-screen">
          <slot/>
        </div>
      </section>
    </div>
  </div>
</template>