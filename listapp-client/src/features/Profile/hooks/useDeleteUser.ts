import { useMutation, useQueryClient } from '@tanstack/react-query';
import { deleteUser } from '../../../api';
import { apiClient } from '../../../lib/apiClient';

export const useDeleteUser = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: () =>
      deleteUser({
        client: apiClient,
        throwOnError: true,
      }),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['user'] }); //This makes no sense, I guess I would have to onSuccess redirect back to unauthenticated page?
    },
  });
};
