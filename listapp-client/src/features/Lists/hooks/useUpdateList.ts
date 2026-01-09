import { useMutation, useQueryClient } from '@tanstack/react-query';
import { updateList, type ListUpdateDto } from '../../../api';
import { apiClient } from '../../../lib/apiClient';

export const useUpdateList = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: ({ id, dto }: { id: string; dto: ListUpdateDto }) =>
      updateList({
        client: apiClient,
        throwOnError: true,
        path: {
          id,
        },
        body: dto,
      }),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['lists'] });
    },
  });
};
