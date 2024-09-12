<script setup lang="ts">
const username: Ref<string> = ref("");
const password: Ref<string> = ref("");
const passwordAgain: Ref<string> = ref("");
const pending: Ref<boolean> = ref(false);
const showError: Ref<boolean> = ref(false);
const errorMessage: Ref<string> = ref("");

const router: Router = useRouter();

async function submitForm(): Promise<void> {
  pending.value = true;
  showError.value = false;
  errorMessage.value = "";

  if (password.value !== passwordAgain.value) {
    showError.value = true;
    errorMessage.value = "Passwords do not match";
    pending.value = false;
    return;
  }

  try {
    const response = await $fetch("http://localhost:8080/account/signup", {
      method: "POST",
      body: {
        username: username.value,
        password: password.value,
        passwordAgain: passwordAgain.value
      }
    });

    console.log(response);
    // Handle successful signup (e.g., redirect to login page)
    console.log("Signup successful");
    await router.push('/login');
  } catch (error) {
    console.error(error);
    showError.value = true;
    errorMessage.value = "Signup failed. Please try again.";
  } finally {
    pending.value = false;
  }
}
</script>

<template>
  <div class="w-full md:w-1/2 lg:w-1/3 p-10 md:p-0">
    <h3 class="text-2xl font-semibold text-center mb-6 text-gray-800">
      Sign Up
      <span class="block w-24 h-1 bg-blue-700 mx-auto mt-2"></span>
    </h3>
    <form @submit.prevent="submitForm">
      <div class="mb-4">
        <label for="username" class="block mb-2 text-sm">Username</label>
        <input v-model="username" type="text" id="username" name="username"
               class="bg-gray-50 border border-gray-300 text-gray-900 block w-full p-2.5" required>
      </div>
      <div class="mb-4">
        <label for="password" class="block mb-2 text-sm">Password</label>
        <input v-model="password" type="password" id="password" name="password"
               class="bg-gray-50 border border-gray-300 text-gray-900 block w-full p-2.5" required>
      </div>
      <div class="mb-4">
        <label for="passwordAgain" class="block mb-2 text-sm">Confirm Password</label>
        <input v-model="passwordAgain" type="password" id="passwordAgain" name="passwordAgain"
               class="bg-gray-50 border border-gray-300 text-gray-900 block w-full p-2.5" required>
      </div>
      <div class="mt-6">
        <input type="submit" value="Sign Up"
               class="text-white bg-blue-700 hover:bg-blue-800 font-medium text-sm px-5 py-2.5 me-2 mb-2 focus:outline-none w-full"/>
      </div>
      <div class="mt-4 text-center">
        <p class="text-sm text-gray-600">
          Already have an account? 
          <NuxtLink to="/login" class="text-blue-700 hover:underline">Login here</NuxtLink>
        </p>
      </div>
    </form>

    <div class="flex flex-col items-center justify-center mx-auto mt-4">
      <Spinner :show="pending"/>
      <Alert :show="showError" :message="errorMessage" type="error"/>
    </div>
  </div>
</template>
