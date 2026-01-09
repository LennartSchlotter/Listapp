import { useMutation, useQueryClient } from '@tanstack/react-query';
import { createList, type ListCreateDto } from '../../../api';
import { apiClient } from '../../../lib/apiClient';

export const useCreateList = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: ({ dto }: { dto: ListCreateDto }) =>
      createList({
        client: apiClient,
        throwOnError: true,
        body: dto,
      }),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['lists'] });
    },
  });
};
