import Avatar from '@mui/material/Avatar';
import { useQuery } from '@tanstack/react-query';
import { getUser } from '../../../api';
import { apiClient } from '../../../lib/apiClient';

export default function CustomAvatar() {
  const user = useQuery({
    queryKey: ['user'],
    retry: false,
    queryFn: async () => {
      const response = await getUser({ client: apiClient, throwOnError: true });
      return response.data;
    },
  });

  const userName = user.data?.name;

  function stringToColor(string: string) {
    let hash = 0;

    for (let i = 0; i < string.length; i += 1) {
      hash = string.charCodeAt(i) + ((hash << 5) - hash);
    }

    const hue = hash % 360;
    return `hsl(${hue}, 70%, 50%)`;
  }

  function initialsAvatar(name: string) {
    if (!name || name.trim() === '') {
      return { sx: { bgcolor: '#808080' }, children: '?' };
    }

    const parts = name.trim().split(/\s+/);
    let initials = parts[0][0].toUpperCase();

    if (parts.length > 1) {
      initials += parts[parts.length - 1][0].toUpperCase();
    } else if (parts[0].length > 1) {
      initials += parts[0][1].toUpperCase();
    }

    return {
      sx: { bgcolor: stringToColor(name) },
      children: initials,
    };
  }

  return <Avatar {...initialsAvatar(userName || 'User')} />;
}
