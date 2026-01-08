import { useParams } from '@tanstack/react-router';
import { useGetListById } from '../hooks/useGetListById';
import type { ItemSummaryDto } from '../../../api';
import ItemModule from './ItemModule';

export default function ListModule() {
  const { listId } = useParams({ from: '/app/_authenticated/lists/$listId' });
  const { data, isLoading, isError } = useGetListById(listId);

  const handleEdit = (item: ItemSummaryDto) => {
    console.log('Edit item:', item);
  };

  const handleDelete = (item: ItemSummaryDto) => {
    console.log('Delete item:', item);
  };

  return (
    <div>
      {!isLoading && !isError && data ? (
        <div className="max-w-4xl mx-auto p-6">
          <h1 className="text-3xl font-bold text-center mb-8">{data?.title}</h1>
          <div className="border border-gray-100 rounded-lg overflow-hidden">
            {data!.items?.map((item: ItemSummaryDto, index: number) => (
              <ItemModule
                key={item.id}
                item={item}
                index={index}
                onEdit={handleEdit}
                onDelete={handleDelete}
              />
            ))}
            {(!data!.items ||
              data!.items.length === 0 ||
              data!.items === undefined) && (
              <p className="text-center py-8 text-gray-500">
                No items in this list.
              </p>
            )}
          </div>
        </div>
      ) : (
        'Loading content'
      )}
    </div>
  );
}
