import { useQuery } from '@tanstack/react-query';
import { getUser } from '../../../api';
import { apiClient } from '../../../lib/apiClient';

export const useGetUser = () => {
  return useQuery({
    queryKey: ['user'],
    queryFn: async () => {
      const response = await getUser({
        client: apiClient,
        throwOnError: true,
      });
      return response.data;
    },
  });
};
