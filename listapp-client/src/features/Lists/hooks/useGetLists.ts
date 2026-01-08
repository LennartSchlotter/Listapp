import { useQuery } from '@tanstack/react-query';
import { getLists } from '../../../api';
import { apiClient } from '../../../lib/apiClient';

export const useGetLists = () => {
  return useQuery({
    queryKey: ['lists'],
    queryFn: async () => {
      const response = await getLists({
        client: apiClient,
        throwOnError: true,
      });
      return response.data;
    },
  });
};
