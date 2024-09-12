<script setup lang="ts">

const BORDER_ICON = "ic:baseline-star-border";
const FILLED_ICON = "ic:baseline-star";

interface Props {
  wordId?: number,
  word?: string
}

interface FavoriteResponse {
  isFavorite: boolean;
}

const props = defineProps<Props>()
const isFavorite = ref(false);
const currentIcon = computed(() => isFavorite.value ? FILLED_ICON : BORDER_ICON);
const { isLoggedIn , getAccessToken } = useAuth();

async function handleFavoriteIcon() {
  if (!props.wordId) return;

  if(isFavorite.value) {
    await deleteFromFavorite(props.wordId, 1);
    isFavorite.value = false;
  } else {
    await saveToFavorite(props.wordId, 1);
    isFavorite.value = true;
  }
}

async function fetchFavoriteStatus() {
  if (props.wordId) {
    try {
      const response: FavoriteResponse = await $fetch(`${API_URL}/translator/favorite/exists?userId=1&wordId=${props.wordId}`, {
        headers: {
          'Authorization': `Bearer ${getAccessToken()}`
        }
      });
      isFavorite.value = response.isFavorite;

      console.log(response);
    } catch (error) {
      console.error("Error fetching favorite status:", error);
    }
  }
}

onMounted(fetchFavoriteStatus);

</script>

<template>
  <div v-if="wordId && isLoggedIn">
    <div class="flex">
      <h1 class="text-xl">{{ word }}</h1>
      <Icon
        @click="handleFavoriteIcon" 
        :name="currentIcon" 
        class="text-3xl hover:cursor-pointer transition-transform duration-300 ease-in-out hover:scale-110 hover:text-yellow-500"
      />
    </div>
  </div>
</template>

<style scoped>

</style>