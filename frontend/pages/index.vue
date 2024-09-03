<script setup lang="ts">
import type {Ref} from "vue";
import Spinner from "~/components/Spinner.vue";

enum Language {
  EN = 'EN',
  CS = 'CS'
}

interface TranslateResponse {
  wordId?: number;
  translations: string[];
}

const input: Ref<string> = ref("");
const fromLang: Ref<Language> = ref(Language.EN);
const toLang: Ref<Language> = ref(Language.CS);
const translations: Ref<Array<string> | null> = ref(null);
const wordId: Ref<number | undefined> = ref(null);
const word: Ref<string | undefined> = ref(null);
const pending: Ref<boolean> = ref(false);
const showError: Ref<boolean> = ref(false);

async function submitForm(): Promise<void> {
  translations.value = null;
  pending.value = true;
  try {
    const response: TranslateResponse = await $fetch("http://localhost:8080/translator/translate", {
      method: "POST",
      body: {
        word: input.value,
        fromLang: fromLang.value,
        toLang: toLang.value,
      }
    })
    console.log(response);
    translations.value = response.translations;
    wordId.value = response.wordId;
    word.value = input.value;
  } catch (error) {
    console.log(error);
    showError.value = true;
  } finally {
    pending.value = false;
  }
}
</script>

<template>
  <div class="w-full md:w-1/2 lg:w-1/4 p-10 md:p-0">
    <form @submit.prevent="submitForm">
      <div>
        <label for="input" class="block mb-2 text-sm">Input</label>
        <input v-model="input" type="text" name="input" id="input"
               class="bg-gray-50 border border-gray-300 text-gray-900 block w-full p-2.5" required="">
      </div>
      <div class="flex space-x-5 w-full">
        <div class="w-full">
          <label for="from-lang" class="block mb-2 text-sm">From</label>
          <select v-model="fromLang" id="from-lang"
                  class="bg-gray-50 border border-gray-300 text-gray-900 text-sm block w-full p-2.5">
            <option selected value="EN">English</option>
            <option value="CS">Czech</option>
          </select>
        </div>
        <div class="w-full">
          <label for="to-lang" class="block mb-2 text-sm">To</label>
          <select v-model="toLang" id="to-lang"
                  class="bg-gray-50 border border-gray-300 text-gray-900 text-sm block w-full p-2.5">
            <option selected value="CS">Czech</option>
            <option value="EN">English</option>
          </select>
        </div>
      </div>
      <div class="mt-2">
        <input type="submit" value="Translate"
               class="text-white bg-blue-700 hover:bg-blue-800 font-medium text-sm px-5 py-2.5 me-2 mb-2 focus:outline-none w-full"/>
      </div>
    </form>

    <div class="flex flex-col items-center justify-center mx-auto ">

      <Spinner :show="pending"/>

      <div v-if="!pending" class="mt-4">
        <FavoriteWordSaver :word-id="wordId" :word="word"/>
        <ul class="list-decimal text-lg">
          <li v-for="translation in translations">{{ translation }}</li>
        </ul>
      </div>

      <Alert :show="showError" message="Something went wrong." type="error"/>
      <Alert :show="translations != null && translations.length == 0" message="Word not found!" type="warning"/>

    </div>
  </div>
</template>
