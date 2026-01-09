import { useMutation, useQueryClient } from '@tanstack/react-query';
import { deleteItem } from '../../../api';
import { apiClient } from '../../../lib/apiClient';

export const useDeleteItem = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: ({ listId, id }: { listId: string; id: string }) =>
      deleteItem({
        client: apiClient,
        throwOnError: true,
        path: {
          listId,
          id,
        },
      }),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['list'] });
    },
  });
};
