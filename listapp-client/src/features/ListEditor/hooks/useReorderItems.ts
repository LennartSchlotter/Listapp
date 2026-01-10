import { useMutation, useQueryClient } from '@tanstack/react-query';
import { reorderItems, type ItemReorderDto } from '../../../api';
import { apiClient } from '../../../lib/apiClient';

export const useReorderItems = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: ({ listId, dto }: { listId: string; dto: ItemReorderDto }) =>
      reorderItems({
        client: apiClient,
        throwOnError: true,
        path: {
          listId,
        },
        body: dto,
      }),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['list'] });
    },
  });
};
