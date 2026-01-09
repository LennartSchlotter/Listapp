import {
  Button,
  Card,
  CardActionArea,
  CardContent,
  CardActions,
  Typography,
  Box,
} from '@mui/material';
import DeleteIcon from '@mui/icons-material/Delete';
import EditIcon from '@mui/icons-material/Edit';
import type { ListResponseDto, ListUpdateDto } from '../../../api';
import { Link } from '@tanstack/react-router';

interface ListEntryProps {
  list: ListResponseDto;
  onDelete: (list: ListResponseDto) => void;
  onUpdate: (list: ListUpdateDto) => void;
}

const ListEntry: React.FC<ListEntryProps> = ({ list, onDelete, onUpdate }) => {
  return (
    <Card sx={{ maxWidth: 1500, mb: 2, display: 'flex', alignItems: 'center' }}>
      <CardActionArea component={Link} to={`/app/lists/${list.id}`}>
        <CardContent>
          <Typography gutterBottom variant="h5" component="div">
            {list.title}
          </Typography>
          <Typography variant="body2" sx={{ color: 'text.secondary' }}>
            {list.description}
          </Typography>
        </CardContent>
      </CardActionArea>
      <Box sx={{ ml: 'auto' }}>
        <CardActions>
          <Button
            variant="contained"
            color="info"
            onClick={(e) => {
              e.stopPropagation();
              onUpdate(list);
            }}
            startIcon={<EditIcon />}
          >
            Edit
          </Button>
          <Button
            variant="contained"
            color="error"
            onClick={(e) => {
              e.stopPropagation();
              onDelete(list);
            }}
            startIcon={<DeleteIcon />}
          >
            Delete
          </Button>
        </CardActions>
      </Box>
    </Card>
  );
};

export default ListEntry;
