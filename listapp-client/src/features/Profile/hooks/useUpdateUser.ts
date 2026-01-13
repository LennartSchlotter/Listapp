import { useMutation, useQueryClient } from '@tanstack/react-query';
import { updateUser, type UserUpdateDto } from '../../../api';
import { apiClient } from '../../../lib/apiClient';

export const useUpdateUser = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: ({ dto }: { dto: UserUpdateDto }) =>
      updateUser({
        client: apiClient,
        throwOnError: true,
        body: dto,
      }),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['user'] });
    },
  });
};
