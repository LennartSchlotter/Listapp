import React, { useState } from 'react';
import { ListItem, ListItemText, IconButton } from '@mui/material';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import type { ItemSummaryDto } from '../../../api';
import { useSortable } from '@dnd-kit/sortable';
import { CSS } from '@dnd-kit/utilities';
import { ItemDetailsDialog } from './ItemDetailsDialog';
import DragIndicatorIcon from '@mui/icons-material/DragIndicator';

interface ItemModuleProps {
  item: ItemSummaryDto;
  onEdit: (item: ItemSummaryDto) => void;
  onDelete: (item: ItemSummaryDto) => void;
}

const ItemModule: React.FC<ItemModuleProps> = ({ item, onEdit, onDelete }) => {
  const [openDialog, setOpenDialog] = useState(false);
  const handleOpenDialog = () => setOpenDialog(true);
  const { attributes, listeners, setNodeRef, transform, transition } =
    useSortable({ id: item.id! });
  const style = { transform: CSS.Transform.toString(transform), transition };

  return (
    <>
      <div ref={setNodeRef} style={style} {...attributes}>
        <div className="flex items-center border-b hover:bg-gray-50 transition-colors">
          <div className="w-12 text-center text-gray-500 font-medium py-4">
            {item.position! + 1}.
          </div>
          <ListItem
            className="flex-1 cursor-pointer"
            onClick={handleOpenDialog}
            secondaryAction={
              <div className="flex space-x-2 pr-4">
                <IconButton
                  size="large"
                  {...listeners}
                  onClick={(e) => e.stopPropagation()}
                  sx={{ cursor: 'grab' }}
                >
                  <DragIndicatorIcon />
                </IconButton>
                <IconButton
                  edge="end"
                  aria-label="edit"
                  onClick={(e) => {
                    e.stopPropagation();
                    onEdit(item);
                  }}
                >
                  <EditIcon className="text-blue-400" />
                </IconButton>
                <IconButton
                  edge="end"
                  aria-label="delete"
                  onClick={(e) => {
                    e.stopPropagation();
                    onDelete(item);
                  }}
                >
                  <DeleteIcon className="text-red-400" />
                </IconButton>
              </div>
            }
          >
            <ListItemText
              primary={item.title}
              slotProps={{ root: { className: 'text-center font-semibold' } }}
            />
          </ListItem>
        </div>
      </div>
      <ItemDetailsDialog
        open={openDialog}
        item={item}
        onClose={() => setOpenDialog(false)}
      />
    </>
  );
};

export default ItemModule;
