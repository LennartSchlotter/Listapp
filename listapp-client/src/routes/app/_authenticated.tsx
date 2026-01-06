import { createFileRoute, Outlet, redirect } from '@tanstack/react-router';
import { useEffect, useState } from 'react';
import { getUser } from '../../api/sdk.gen';
import { apiClient } from '../../lib/apiClient';

export const Route = createFileRoute('/app/_authenticated')({
  beforeLoad: async () => {
    try {
      await getUser({ client: apiClient, throwOnError: true });
    } catch {
      throw redirect({ to: '/' });
    }
  },
  component: AuthenticatedLayout,
});

function AuthenticatedLayout() {
  const [bootstrapped, setBootstrapped] = useState(false);

  useEffect(() => {
    getUser({ client: apiClient, throwOnError: true }).finally(() =>
      setBootstrapped(true)
    );
  }, []);

  if (!bootstrapped) {
    return <div>Loading application...</div>;
  }

  return (
    <>
      {/* ThemeProvider, CssBaseline, Toaster, BaseLayout */}
      <Outlet />
    </>
  );
}
