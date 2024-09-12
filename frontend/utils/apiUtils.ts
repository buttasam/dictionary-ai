export const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080';

export async function saveToFavorite(wordId: number): Promise<any> {
  const { getAccessToken } = useAuth();
  try {
    return await $fetch(`${API_URL}/translator/favorite?wordId=${wordId}`, {
      method: "POST",
      headers: {
        'Authorization': `Bearer ${getAccessToken()}`
      }
    });
  } catch (error) {
    console.error("Error saving favorite:", error);
    throw error;
  }
}

export async function deleteFromFavorite(wordId: number) {
  const { getAccessToken } = useAuth();
  try {
    await $fetch(`${API_URL}/translator/favorite?wordId=${wordId}`, {
      method: "DELETE",
      headers: {
        'Authorization': `Bearer ${getAccessToken()}`
      }
    });
  } catch (error) {
    console.log(error);
  }
}