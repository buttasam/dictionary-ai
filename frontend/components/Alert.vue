<script setup lang="ts">

interface Props {
  show: boolean,
  message: string,
  type: 'info' | 'warning' | 'error' | 'success'
}

defineProps<Props>();

const icons = {
  success: "ep:success-filled",
  info: "ic:baseline-info",
  error: "material-symbols:amp-error",
  warning: "material-symbols:warning",
}

const classes = {
  info: 'text-blue-800 border-blue-300 bg-blue-50',
  success: 'text-green-800 border-green-300 bg-green-50',
  warning: 'text-yellow-800 border-yellow-300 bg-yellow-50',
  error: 'text-red-800 border-red-300 bg-red-50',
}

</script>

<template>
  <div v-if="show" :class="['w-full flex items-center p-4 mb-4 text-sm border', classes[type]]" role="alert">
    <Icon :name="icons[type]" class="mr-1" />
    <span class="sr-only">Info</span>
    <div>
      <span class="font-medium">{{ message }}</span>
    </div>
  </div>
</template>