import { useQuery } from '@tanstack/react-query';
import { getListById } from '../../../api';
import { apiClient } from '../../../lib/apiClient';

export const useGetListById = (listId: string) => {
  return useQuery({
    queryKey: ['list', listId],
    queryFn: async () => {
      const response = await getListById({
        client: apiClient,
        throwOnError: true,
        path: { id: listId },
      });
      return response.data;
    },
  });
};
