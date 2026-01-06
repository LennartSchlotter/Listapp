import { createFileRoute, useRouter } from '@tanstack/react-router';
import { LoginButton } from '../components/LoginButton';
import { useEffect } from 'react';
import { getUser } from '../api';
import { apiClient } from '../lib/apiClient';

export const Route = createFileRoute('/')({
  component: UnauthenticatedHome,
});

function UnauthenticatedHome() {
  const router = useRouter();

  useEffect(() => {
    const redirect = async () => {
      try {
        await getUser({ client: apiClient, throwOnError: true });
        router.navigate({ to: '/app' });
      } catch {
        /* unauthenticated */
      }
    };

    redirect();
  }, [router]);

  return (
    <div>
      <h1>Welcome</h1>
      <LoginButton />
    </div>
  );
}
