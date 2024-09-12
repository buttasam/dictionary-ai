export const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080';

export async function saveToFavorite(wordId: number, userId: number): Promise<any> {

  const { getAccessToken } = useAuth();

  try {
    return await $fetch(`${API_URL}/translator/favorite`, {
      method: "POST",
      body: {
        wordId,
        userId
      },
      headers: {
        'Authorization': `Bearer ${getAccessToken()}`
      }
    });
  } catch (error) {
    console.error("Error saving favorite:", error);
    throw error;
  }
}

export async function deleteFromFavorite(wordId: number, userId: number) {
    try {
      await $fetch(`${API_URL}/translator/favorite`, {
        method: "DELETE",
        body: {
          wordId: wordId,
          userId: userId
        }
      });
    } catch(error) {
      console.log(error);
    }
  }