import { getLists } from '../api/sdk.gen';
import { apiClient } from '../lib/apiClient';

export function GetListButton() {
  const get = async () => {
    try {
      await getLists({
        client: apiClient,
        throwOnError: true,
      });

      alert('Lists received');
    } catch (e) {
      console.error(e);
      alert('Failed');
    }
  };

  return <button onClick={get}>Get List</button>;
}
