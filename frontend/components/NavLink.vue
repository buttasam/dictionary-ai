<template>
    <NuxtLink
      :to="to"
      :class="[
        'block sm:inline-block mr-2 p-2 hover:text-gray-600 transition-colors duration-200',
        { 'font-bold': $route.path === to },
        additionalClasses
      ]"
    >
      <slot></slot>
    </NuxtLink>
  </template>
  
  <script setup lang="ts">
  defineProps<{
    to: string;
    additionalClasses?: string | string[];
  }>();
  </script>