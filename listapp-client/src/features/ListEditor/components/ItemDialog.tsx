import { useState } from 'react';
import {
  Button,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  TextField,
} from '@mui/material';

interface ItemDialogProps {
  open: boolean;
  onClose: () => void;
  onSubmit: (data: {
    title: string;
    notes?: string | null;
    imagePath?: string | null;
  }) => void;
  mode: 'create' | 'update';
  initialTitle?: string;
  initialNotes?: string | null;
  initialImagePath?: string | null;
}

export function ItemDialog({
  open,
  onClose,
  onSubmit,
  mode,
  initialTitle = '',
  initialNotes = '',
  initialImagePath = '',
}: ItemDialogProps) {
  const [title, setTitle] = useState(() => initialTitle);
  const [notes, setNotes] = useState(() => initialNotes ?? '');
  const [imagePath, setImagePath] = useState(() => initialImagePath ?? '');

  const handleSubmit = () => {
    const trimmedTitle = title.trim();
    if (!trimmedTitle) return;

    const trimmedNotes = notes.trim();
    let submitNotes: string | null | undefined;

    const initialN = initialNotes ?? '';
    const currentN = trimmedNotes;

    if (mode === 'create') {
      submitNotes = trimmedNotes || undefined;
    } else {
      if (currentN === '') {
        submitNotes = null;
      } else if (currentN === initialN) {
        submitNotes = undefined;
      } else {
        submitNotes = currentN;
      }
    }

    const trimmedImagePath = imagePath.trim();
    let submitImagePath: string | null | undefined;

    const initialP = initialImagePath ?? '';
    const currentP = trimmedImagePath;

    if (mode === 'create') {
      submitImagePath = trimmedImagePath || undefined;
    } else {
      if (currentP === '') {
        submitImagePath = null;
      } else if (currentP === initialP) {
        submitImagePath = undefined;
      } else {
        submitImagePath = currentP;
      }
    }

    onSubmit({
      title: trimmedTitle,
      notes: submitNotes,
      imagePath: submitImagePath,
    });
    onClose();
  };

  return (
    <Dialog open={open} onClose={onClose} maxWidth="sm" fullWidth>
      <DialogTitle>
        {mode === 'create' ? 'Create new Item' : 'Update Item'}
      </DialogTitle>
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
          error={mode === 'update' && !title.trim()}
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
          slotProps={{ htmlInput: { maxLength: 1024 } }}
        />
      </DialogContent>
      <DialogActions>
        <Button onClick={onClose}>Cancel</Button>
        <Button
          onClick={handleSubmit}
          variant="contained"
          disabled={!title.trim()}
        >
          {mode === 'create' ? 'Create' : 'Update'}
        </Button>
      </DialogActions>
    </Dialog>
  );
}
