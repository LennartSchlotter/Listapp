import React, { useState } from 'react';
import {
  Card,
  CardMedia,
  CardContent,
  Typography,
  IconButton,
  Box,
} from '@mui/material';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import type { ItemSummaryDto } from '../../../api';
import { useSortable } from '@dnd-kit/sortable';
import { CSS } from '@dnd-kit/utilities';
import { ItemDetailsDialog } from './ItemDetailsDialog';

interface ImageItemModuleProps {
  item: ItemSummaryDto;
  onEdit: (item: ItemSummaryDto) => void;
  onDelete: (item: ItemSummaryDto) => void;
}

const ImageItemModule: React.FC<ImageItemModuleProps> = ({
  item,
  onEdit,
  onDelete,
}) => {
  const [openDialog, setOpenDialog] = useState(false);
  const { attributes, listeners, setNodeRef, transform, transition } =
    useSortable({ id: item.id! });

  const style = {
    transform: CSS.Transform.toString(transform),
    transition,
  };

  return (
    <>
      <div ref={setNodeRef} style={style} {...attributes} {...listeners}>
        <Card
          sx={{
            height: 240,
            position: 'relative',
            cursor: 'pointer',
            boxShadow: 3,
            overflow: 'hidden',
            '&:hover': { boxShadow: 6 },
          }}
          onClick={() => setOpenDialog(true)}
        >
          <CardMedia
            component="img"
            image={item.imagePath}
            alt={item.title}
            sx={{
              height: '200px',
              width: '100%',
              objectFit: 'contain',
              backgroundColor: 'grey.200',
            }}
          />
          <CardContent
            sx={{
              height: 40,
              padding: 1,
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'center',
            }}
          >
            <Typography variant="subtitle2" align="center" fontWeight="medium">
              {item.title}
            </Typography>
          </CardContent>
          <Box
            sx={{
              position: 'absolute',
              top: 8,
              left: 8,
              backgroundColor: 'rgba(0, 0, 0, 0.5)',
              color: 'white',
              padding: '4px 8px',
              borderRadius: 1,
            }}
          >
            {item.position! + 1}.
          </Box>
          <Box
            sx={{
              position: 'absolute',
              top: 8,
              right: 8,
              display: 'flex',
              gap: 1,
            }}
          >
            <IconButton
              size="small"
              aria-label="edit"
              onClick={(e) => {
                e.stopPropagation();
                onEdit(item);
              }}
              sx={{
                color: 'blue.400',
                backgroundColor: 'white',
                '&:hover': { backgroundColor: 'grey.100' },
              }}
            >
              <EditIcon fontSize="small" />
            </IconButton>
            <IconButton
              size="small"
              aria-label="delete"
              onClick={(e) => {
                e.stopPropagation();
                onDelete(item);
              }}
              sx={{
                color: 'red.400',
                backgroundColor: 'white',
                '&:hover': { backgroundColor: 'grey.100' },
              }}
            >
              <DeleteIcon fontSize="small" />
            </IconButton>
          </Box>
        </Card>
      </div>

      <ItemDetailsDialog
        open={openDialog}
        item={item}
        onClose={() => setOpenDialog(false)}
      />
    </>
  );
};

export default ImageItemModule;
