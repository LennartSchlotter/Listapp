import { createList } from '../api/sdk.gen';
import { apiClient } from '../lib/apiClient';

export function CreateListButton() {
  const create = async () => {
    try {
      await createList({
        client: apiClient,
        body: { title: 'Test List' },
        throwOnError: true,
      });

      alert('List created');
    } catch (e) {
      console.error(e);
      alert('Failed');
    }
  };

  return <button onClick={create}>Create List</button>;
}
