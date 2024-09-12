export function useAuth() {

    const ACCESS_TOKEN_KEY = 'accessToken';
    const isClient = import.meta.client;

    const isLoggedIn = useState('isLoggedIn', () => {
        return getAccessToken() != null;
    });

    onMounted(() => {
        isLoggedIn.value = !!getAccessToken();
    });

    const login = (token: string) => {
        if (isClient) {
            localStorage.setItem(ACCESS_TOKEN_KEY, token);
        }
        isLoggedIn.value = true
    }

    const logout = () => {
        if (isClient) {
            localStorage.removeItem(ACCESS_TOKEN_KEY);
        }
        isLoggedIn.value = false
    }

    function getAccessToken(): string | null {
        if (isClient) {
            return localStorage.getItem(ACCESS_TOKEN_KEY);
        }
        return null;
    }

    return {
        isLoggedIn: computed(() => isLoggedIn.value),
        login,
        logout,
        getAccessToken
    }
}