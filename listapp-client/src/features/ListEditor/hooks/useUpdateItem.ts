import { useMutation, useQueryClient } from '@tanstack/react-query';
import { updateItem, type ItemUpdateDto } from '../../../api';
import { apiClient } from '../../../lib/apiClient';

export const useUpdateItem = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: ({
      listId,
      id,
      dto,
    }: {
      listId: string;
      id: string;
      dto: ItemUpdateDto;
    }) =>
      updateItem({
        client: apiClient,
        throwOnError: true,
        path: {
          listId,
          id,
        },
        body: dto,
      }),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['list'] });
    },
  });
};
