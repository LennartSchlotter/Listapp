import { useQuery } from '@tanstack/react-query';
import { getUser } from '../../../api';
import { apiClient } from '../../../lib/apiClient';

export default function ProfileModule() {
  const user = useQuery({
    queryKey: ['user'],
    retry: false,
    queryFn: async () => {
      const response = await getUser({ client: apiClient, throwOnError: true });
      return response.data;
    },
  });

  return (
    <div className="grid overflow-hidden grid-cols-[1fr_3fr]">
      {user.data?.name}
    </div>
  );
}
