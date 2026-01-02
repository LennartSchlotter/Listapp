import { createClient } from '../api/client.gen';

export const apiClient = createClient({
  baseUrl: '',

  fetch: async (input, init) => {
    const csrfToken = getCsrfToken();

    return fetch(input, {
      ...init,
      credentials: 'include',
      headers: {
        'Content-Type': 'application/json',
        ...init?.headers,
        ...(csrfToken && init?.method !== 'GET'
          ? { 'X-XSRF-TOKEN': csrfToken }
          : {}),
      },
    });
  },
});

function getCsrfToken(): string | undefined {
  return document.cookie
    .split('; ')
    .find((c) => c.startsWith('XSRF-TOKEN='))
    ?.split('=')[1];
}
