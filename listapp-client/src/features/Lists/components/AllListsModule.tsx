import {
  type ListCreateDto,
  type ListResponseDto,
  type ListUpdateDto,
} from '../../../api';
import { useGetLists } from '../hooks/useGetLists';
import { useDeleteList } from '../hooks/useDeleteList';
import { useCreateList } from '../hooks/useCreateList';
import { useState } from 'react';
import { Button, Container, Box } from '@mui/material';
import AddIcon from '@mui/icons-material/Add';
import ListEntry from './ListEntry';
import { useUpdateList } from '../hooks/useUpdateList';
import { ListDialog } from './ListDialog';

export default function AllListsModule() {
  const { isLoading, isError, data } = useGetLists();
  const { mutate: deleteList } = useDeleteList();
  const { mutate: createList } = useCreateList();
  const { mutate: updateList } = useUpdateList();
  const [dialogOpen, setDialogOpen] = useState(false);
  const [dialogMode, setDialogMode] = useState<'create' | 'update'>('create');
  const [selectedList, setSelectedList] = useState<ListResponseDto | null>(
    null
  );

  const handleDelete = (list: ListResponseDto) => {
    deleteList({ id: list.id! });
  };

  const handleOpenCreate = () => {
    setSelectedList(null);
    setDialogMode('create');
    setDialogOpen(true);
  };

  const handleOpenUpdate = (list: ListResponseDto) => {
    setSelectedList(list);
    setDialogMode('update');
    setDialogOpen(true);
  };

  const handleDialogSubmit = (data: {
    title: string;
    description?: string | null;
  }) => {
    if (dialogMode === 'create') {
      const dto: ListCreateDto = {
        title: data.title,
        ...(data.description !== undefined && {
          description: data.description ?? undefined,
        }),
      };
      createList({ dto });
    } else if (selectedList) {
      const dto: Partial<ListUpdateDto> = {};
      if (data.title !== selectedList.title) dto.title = data.title;
      if (data.description !== undefined)
        dto.description = data.description ?? undefined;
      updateList({ id: selectedList.id!, dto });
    }
  };

  return (
    <>
      <Container maxWidth="md" sx={{ py: 4 }}>
        <Box
          sx={{
            display: 'flex',
            justifyContent: 'space-between',
            alignItems: 'center',
            mb: 4,
          }}
        >
          <h1 className="text-3xl font-bold text-center mb-8">My Lists</h1>
          <Button
            variant="contained"
            color="success"
            startIcon={<AddIcon />}
            onClick={handleOpenCreate}
            size="large"
          >
            Create New List
          </Button>
        </Box>
        {!isLoading && !isError && data ? (
          <Box
            sx={{
              display: 'flex',
              flexDirection: 'column',
              gap: 2,
            }}
          >
            {data?.map((list) => (
              <ListEntry
                key={list.id}
                list={list}
                onDelete={handleDelete}
                onUpdate={handleOpenUpdate}
              />
            ))}
          </Box>
        ) : (
          'Loading content...'
        )}
      </Container>
      <ListDialog
        key={dialogOpen ? `${dialogMode}-${selectedList?.title}` : 'closed'}
        open={dialogOpen}
        onClose={() => setDialogOpen(false)}
        onSubmit={handleDialogSubmit}
        mode={dialogMode}
        initialTitle={selectedList?.title}
        initialDescription={selectedList?.description}
      />
    </>
  );
}
