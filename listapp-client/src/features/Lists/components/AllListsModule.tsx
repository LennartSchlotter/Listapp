import type { ListResponseDto } from '../../../api';
import { useGetLists } from '../hooks/useGetLists';
import ListEntry from './ListEntry';

export default function AllListsModule() {
  const { isLoading, isError, data } = useGetLists();

  const handleDelete = (list: ListResponseDto) => {
    console.log('Delete list', list);
  };

  return (
    <div>
      {!isLoading && !isError && data ? (
        <div className="justify-center items-center mt-1">
          {data?.map((list) => (
            <ListEntry key={list.id} list={list} onDelete={handleDelete} />
          ))}
        </div>
      ) : (
        'Loading content...'
      )}
    </div>
  );
}
