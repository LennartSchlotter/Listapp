import { createFileRoute } from '@tanstack/react-router';
import { useEffect } from 'react';
import { useRouter } from '@tanstack/react-router';

export const Route = createFileRoute('/oauth-callback')({
  component: OAuthCallback,
});

function OAuthCallback() {
  const router = useRouter();

  useEffect(() => {
    router.navigate({ to: '/' });
  }, [router]);

  return <div>Logging in...</div>;
}
