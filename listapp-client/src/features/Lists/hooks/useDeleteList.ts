import { useMutation, useQueryClient } from '@tanstack/react-query';
import { deleteList } from '../../../api';
import { apiClient } from '../../../lib/apiClient';

export const useDeleteList = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: ({ id }: { id: string }) =>
      deleteList({
        client: apiClient,
        throwOnError: true,
        path: {
          id,
        },
      }),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['lists'] });
    },
  });
};
