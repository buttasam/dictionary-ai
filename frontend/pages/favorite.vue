<script setup lang="ts">

interface FavoriteResponse {
  wordId: number;
  word: string;
  translations: string[];
  fromLang: string;
  toLang: string;
}

const favoriteWords: Ref<Array<FavoriteResponse>> = ref([]);

const { getAccessToken } = useAuth();

onMounted(async () => {
  await loadData();
});

async function handleDeleteWord(wordId: number) {
  await deleteFromFavorite(wordId);
  favoriteWords.value = favoriteWords.value.filter(word => word.wordId !== wordId);
}

async function loadData() {
  favoriteWords.value = await $fetch(`${API_URL}/translator/favorite`,
    {
      headers: {
        'Authorization': `Bearer ${getAccessToken()}`
      }
    }
  );
}

</script>

<template>
  <h3 class="text-2xl font-semibold text-center mb-6 text-gray-800">
    Favorite Words
    <span class="block w-24 h-1 bg-blue-700 mx-auto mt-2"></span>
  </h3>

  <div class="container mx-auto px-4 py-8">
    <ul class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-4">
      <li v-for="favoriteWord in favoriteWords"
        class="bg-white shadow-md p-4 hover:shadow-lg transition-shadow duration-300 relative">
        <Icon @click="handleDeleteWord(favoriteWord.wordId)" name="ic:baseline-close"
          class="absolute top-2 right-2 text-gray-500 hover:text-gray-700 cursor-pointer text-xl" />
        <div class="text-lg font-semibold text-gray-800">{{ favoriteWord.word }}</div>
        <div class="text-sm text-gray-600">{{ favoriteWord.fromLang }} → {{ favoriteWord.toLang }}</div>
        <ul class="mt-2">
          <li v-for="translation in favoriteWord.translations" class="text-md text-gray-700">
            {{ translation }}
          </li>
        </ul>
      </li>
    </ul>
  </div>
</template>