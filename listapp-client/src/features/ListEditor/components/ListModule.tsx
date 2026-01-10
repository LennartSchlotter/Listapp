import { useParams } from '@tanstack/react-router';
import { useGetListById } from '../hooks/useGetListById';
import type {
  ItemCreateDto,
  ItemSummaryDto,
  ItemUpdateDto,
} from '../../../api';
import ItemModule from './ItemModule';
import { useUpdateItem } from '../hooks/useUpdateItem';
import { useDeleteItem } from '../hooks/useDeleteItem';
import { useCreateItem } from '../hooks/useCreateItem';
import { Box, Button, Container } from '@mui/material';
import { useEffect, useEffectEvent, useState } from 'react';
import AddIcon from '@mui/icons-material/Add';
import { ItemDialog } from './ItemDialog';
import { DndContext, closestCenter, type DragEndEvent } from '@dnd-kit/core';
import {
  SortableContext,
  verticalListSortingStrategy,
  arrayMove,
} from '@dnd-kit/sortable';
import { useReorderItems } from '../hooks/useReorderItems';

export default function ListModule() {
  const { listId } = useParams({ from: '/app/_authenticated/lists/$listId' });
  const { data, isLoading, isError } = useGetListById(listId);
  const { mutate: updateItem } = useUpdateItem();
  const { mutate: deleteItem } = useDeleteItem();
  const { mutate: createItem } = useCreateItem();
  const { mutate: reorderItems } = useReorderItems();
  const [dialogOpen, setDialogOpen] = useState(false);
  const [dialogMode, setDialogMode] = useState<'create' | 'update'>('create');
  const [selectedItem, setSelectedItem] = useState<ItemSummaryDto | null>(null);
  const [items, setItems] = useState<ItemSummaryDto[]>([]);
  const updateItems = useEffectEvent((newItems: ItemSummaryDto[]) => {
    setItems(newItems);
  });

  useEffect(() => {
    if (data?.items) {
      updateItems(data.items);
    }
  }, [data]);

  const handleDelete = (item: ItemSummaryDto) => {
    deleteItem({
      listId: listId,
      id: item.id!,
    });
  };

  const handleOpenCreate = () => {
    setSelectedItem(null);
    setDialogMode('create');
    setDialogOpen(true);
  };

  const handleOpenUpdate = (item: ItemSummaryDto) => {
    setSelectedItem(item);
    setDialogMode('update');
    setDialogOpen(true);
  };

  const handleDialogSubmit = (data: {
    title: string;
    notes?: string | null;
    imagePath?: string | null;
  }) => {
    if (dialogMode === 'create') {
      const dto: ItemCreateDto = {
        title: data.title,
        ...(data.notes !== undefined && { notes: data.notes ?? undefined }),
        ...(data.imagePath !== undefined && {
          imagePath: data.imagePath ?? undefined,
        }),
      };
      createItem({ listId, dto });
    } else if (selectedItem) {
      const dto: Partial<ItemUpdateDto> = {};
      if (data.title !== selectedItem.title) dto.title = data.title;
      if (data.notes !== undefined) dto.notes = data.notes ?? undefined;
      if (data.imagePath !== undefined)
        dto.imagePath = data.imagePath ?? undefined;
      updateItem({ listId, id: selectedItem.id!, dto });
    }
  };

  const handleDragEnd = (event: DragEndEvent) => {
    const { active, over } = event;

    if (!over || active.id === over.id) return;

    setItems((prev) => {
      const oldIndex = prev.findIndex((i) => i.id === active.id);
      const newIndex = prev.findIndex((i) => i.id === over.id);
      const newItems = arrayMove(prev, oldIndex, newIndex);

      reorderItems({ listId, dto: { itemOrder: newItems.map((i) => i.id!) } });

      return newItems;
    });
  };

  return (
    <DndContext collisionDetection={closestCenter} onDragEnd={handleDragEnd}>
      {!isLoading && !isError && data ? (
        <Container maxWidth="md" sx={{ py: 4 }}>
          <Box
            sx={{
              display: 'flex',
              justifyContent: 'space-between',
              alignItems: 'center',
              mb: 4,
            }}
          >
            <h1 className="text-3xl font-bold text-center mb-8">
              {data.title}
            </h1>
            <Button
              variant="contained"
              color="success"
              startIcon={<AddIcon />}
              onClick={handleOpenCreate}
              size="large"
            >
              Create New Item
            </Button>
          </Box>
          <SortableContext
            items={items.map((i) => i.id!)}
            strategy={verticalListSortingStrategy}
          >
            {items.map((item) => (
              <ItemModule
                key={item.id}
                item={item}
                onEdit={handleOpenUpdate}
                onDelete={handleDelete}
              />
            ))}
          </SortableContext>
          {items.length === 0 && (
            <p className="text-center py-8 text-gray-500">
              No items in this list.
            </p>
          )}
        </Container>
      ) : (
        'Loading content'
      )}
      <ItemDialog
        key={dialogOpen ? `${dialogMode}-${selectedItem?.title}` : 'closed'}
        open={dialogOpen}
        onClose={() => setDialogOpen(false)}
        onSubmit={handleDialogSubmit}
        mode={dialogMode}
        initialTitle={selectedItem?.title}
        initialNotes={selectedItem?.notes}
        initialImagePath={selectedItem?.imagePath}
      />
    </DndContext>
  );
}
