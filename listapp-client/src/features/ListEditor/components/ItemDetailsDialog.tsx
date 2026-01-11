import React from 'react';
import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  Button,
} from '@mui/material';
import type { ItemSummaryDto } from '../../../api';

interface ItemDetailsDialogProps {
  open: boolean;
  item: ItemSummaryDto;
  onClose: () => void;
}

export const ItemDetailsDialog: React.FC<ItemDetailsDialogProps> = ({
  open,
  item,
  onClose,
}) => {
  if (!item) return null;

  return (
    <Dialog open={open} onClose={onClose} fullWidth maxWidth="sm">
      <DialogTitle>{item.title} - Details</DialogTitle>
      <DialogContent dividers>
        <p>
          <strong>Notes: </strong>
          {item.notes || <em className="text-gray-500">No notes provided</em>}
        </p>
      </DialogContent>
      <DialogActions>
        <Button onClick={onClose} color="primary">
          Close
        </Button>
      </DialogActions>
    </Dialog>
  );
};
