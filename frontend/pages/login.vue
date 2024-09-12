<script setup lang="ts">

interface LoginResponse {
    accessToken: string
}

const username: Ref<string> = ref("");
const password: Ref<string> = ref("");
const pending: Ref<boolean> = ref(false);
const showError: Ref<boolean> = ref(false);

const router: Router = useRouter();

const { login } = useAuth();


async function submitForm(): Promise<void> {
  pending.value = true;
  showError.value = false;
  try {
    const response: LoginResponse = await $fetch("http://localhost:8080/account/login", {
      method: "POST",
      body: {
        username: username.value,
        password: password.value
      }
    });

    login(response.accessToken);
    console.log("Login successful");
    router.push("/");
  } catch (error) {
    console.error(error);
    showError.value = true;
  } finally {
    pending.value = false;
  }
}
</script>

<template>
  <div class="w-full md:w-1/2 lg:w-1/3 p-10 md:p-0">
    <h3 class="text-2xl font-semibold text-center mb-6 text-gray-800">
      Login
      <span class="block w-24 h-1 bg-blue-700 mx-auto mt-2"></span>
    </h3>
    <form @submit.prevent="submitForm">
      <div class="mb-4">
        <label for="email" class="block mb-2 text-sm">Email</label>
        <input v-model="username" type="text" id="email" name="email"
               class="bg-gray-50 border border-gray-300 text-gray-900 block w-full p-2.5" required>
      </div>
      <div class="mb-4">
        <label for="password" class="block mb-2 text-sm">Password</label>
        <input v-model="password" type="password" id="password" name="password"
               class="bg-gray-50 border border-gray-300 text-gray-900 block w-full p-2.5" required>
      </div>
      <div class="mt-6">
        <input type="submit" value="Login"
               class="text-white bg-blue-700 hover:bg-blue-800 font-medium text-sm px-5 py-2.5 me-2 mb-2 focus:outline-none w-full"/>
      </div>
    </form>

    <div class="flex flex-col items-center justify-center mx-auto mt-4">
      <Spinner :show="pending"/>
      <Alert :show="showError" message="Login failed. Please try again." type="error"/>
    </div>
  </div>
</template>
