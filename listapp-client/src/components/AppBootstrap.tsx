import { useEffect, useState } from 'react';
import { getUser } from '../api/sdk.gen';
import { apiClient } from '../lib/apiClient';

export function AppBootstrap() {
  const [, setReady] = useState(false);

  useEffect(() => {
    getUser({ client: apiClient })
      .catch(() => {})
      .finally(() => setReady(true));
  }, []);

  return null;
}
