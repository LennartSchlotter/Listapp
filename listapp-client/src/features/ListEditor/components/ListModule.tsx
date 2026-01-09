import { useParams } from '@tanstack/react-router';
import { useGetListById } from '../hooks/useGetListById';
import type { ItemCreateDto, ItemSummaryDto } from '../../../api';
import ItemModule from './ItemModule';
import { useUpdateItem } from '../hooks/useUpdateItem';
import { useDeleteItem } from '../hooks/useDeleteItem';
import { useCreateItem } from '../hooks/useCreateItem';
import {
  Box,
  Button,
  Container,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
  TextField,
} from '@mui/material';
import { useState } from 'react';
import AddIcon from '@mui/icons-material/Add';

export default function ListModule() {
  const { listId } = useParams({ from: '/app/_authenticated/lists/$listId' });
  const { data, isLoading, isError } = useGetListById(listId);
  const { mutate: updateItem } = useUpdateItem();
  const { mutate: deleteItem } = useDeleteItem();
  const { mutate: createItem } = useCreateItem();
  const [open, setOpen] = useState(false);
  const [title, setTitle] = useState('');
  const [notes, setNotes] = useState('');
  const [imagePath, setImagePath] = useState('');

  const handleEdit = (item: ItemSummaryDto) => {
    updateItem({
      listId: listId,
      id: item.id!,
      dto: item,
    });
  };

  const handleDelete = (item: ItemSummaryDto) => {
    deleteItem({
      listId: listId,
      id: item.id!,
    });
  };

  const handleCreate = () => {
    if (!title.trim()) return;

    const newItem: ItemCreateDto = {
      title: title.trim(),
    };

    const trimmedNotes = notes.trim();
    if (trimmedNotes) {
      newItem.notes = trimmedNotes;
    }

    const trimmedImagePath = imagePath.trim();
    if (trimmedImagePath) {
      newItem.imagePath = trimmedImagePath;
    }

    createItem(
      {
        listId: listId,
        dto: newItem,
      },
      {
        onSuccess: () => {
          setTitle('');
          setNotes('');
          setImagePath('');
          setOpen(false);
        },
      }
    );
  };

  const handleClickCreate = () => {
    setOpen(true);
  };

  return (
    <div>
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
              onClick={handleClickCreate}
              size="large"
            >
              Create New Item
            </Button>
          </Box>
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
        </Container>
      ) : (
        'Loading content'
      )}
      <Dialog
        open={open}
        onClose={() => setOpen(false)}
        maxWidth="sm"
        fullWidth
      >
        <DialogTitle>Create New Item</DialogTitle>
        <DialogContent>
          <TextField
            autoFocus
            margin="dense"
            label="Title"
            fullWidth
            variant="outlined"
            value={title}
            onChange={(e) => setTitle(e.target.value)}
            slotProps={{ htmlInput: { maxLength: 100 } }}
          />
          <TextField
            autoFocus
            margin="dense"
            label="Notes"
            fullWidth
            variant="outlined"
            value={notes}
            onChange={(e) => setNotes(e.target.value)}
            slotProps={{ htmlInput: { maxLength: 2000 } }}
          />
          <TextField
            autoFocus
            margin="dense"
            label="Image Path"
            fullWidth
            variant="outlined"
            value={imagePath}
            onChange={(e) => setImagePath(e.target.value)}
            slotProps={{ htmlInput: { maxLength: 2000 } }}
          />
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setOpen(false)}>Cancel</Button>
          <Button
            onClick={handleCreate}
            variant="contained"
            disabled={!title.trim()}
          >
            {'Create'}
          </Button>
        </DialogActions>
      </Dialog>
    </div>
  );
}
