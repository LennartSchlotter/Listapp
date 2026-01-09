import { useMutation, useQueryClient } from '@tanstack/react-query';
import { createItem, type ItemCreateDto } from '../../../api';
import { apiClient } from '../../../lib/apiClient';

export const useCreateItem = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: ({ listId, dto }: { listId: string; dto: ItemCreateDto }) =>
      createItem({
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
