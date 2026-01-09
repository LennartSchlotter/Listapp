import { useState } from 'react';
import {
  Button,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  TextField,
} from '@mui/material';

interface ListDialogProps {
  open: boolean;
  onClose: () => void;
  onSubmit: (data: { title: string; description?: string | null }) => void;
  mode: 'create' | 'update';
  initialTitle?: string;
  initialDescription?: string | null;
}

export function ListDialog({
  open,
  onClose,
  onSubmit,
  mode,
  initialTitle = '',
  initialDescription = '',
}: ListDialogProps) {
  const [title, setTitle] = useState(() => initialTitle);
  const [description, setDescription] = useState(
    () => initialDescription ?? ''
  );

  const handleSubmit = () => {
    const trimmedTitle = title.trim();
    if (!trimmedTitle) return;

    const trimmedDescription = description.trim();
    let submitDescription: string | null | undefined;

    const initial = initialDescription ?? '';
    const current = trimmedDescription;

    if (mode === 'create') {
      submitDescription = trimmedDescription || undefined;
    } else {
      if (current === '') {
        submitDescription = null;
      } else if (current === initial) {
        submitDescription = undefined;
      } else {
        submitDescription = current;
      }
    }

    onSubmit({ title: trimmedTitle, description: submitDescription });
    onClose();
  };

  return (
    <Dialog open={open} onClose={onClose} maxWidth="sm" fullWidth>
      <DialogTitle>
        {mode === 'create' ? 'Create nw List' : 'Update List'}
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
          label="Description"
          fullWidth
          variant="outlined"
          value={description}
          onChange={(e) => setDescription(e.target.value)}
          slotProps={{ htmlInput: { maxLength: 2000 } }}
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
