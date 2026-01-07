import React from 'react';
import CustomAvatar from './CustomAvatar';
import { MenuItem, IconButton, Menu, ListItemIcon } from '@mui/material';
import AccountCircleIcon from '@mui/icons-material/AccountCircle';
import { useRouter } from '@tanstack/react-router';

export default function UserMenu() {
  const [anchorElement, setAnchorElement] = React.useState<null | HTMLElement>(
    null
  );
  const open = Boolean(anchorElement);
  const router = useRouter();

  const handleClick = (event: React.MouseEvent<HTMLElement>) => {
    setAnchorElement(event.currentTarget);
  };

  const handleClose = () => {
    setAnchorElement(null);
  };

  const handleProfile = () => {
    handleClose();
    router.navigate({ to: '/app/profile' });
  };

  return (
    <div>
      <IconButton
        onClick={handleClick}
        className="scale-110 hover:scale-120 transition-transform"
        size="large"
      >
        <CustomAvatar />
      </IconButton>

      <Menu anchorEl={anchorElement} open={open} onClose={handleClose}>
        <MenuItem
          onClick={() => {
            handleProfile();
          }}
          className="box-border flex justify-center items-center text-center"
        >
          Profile
          <ListItemIcon className="ml-2" onClick={handleProfile}>
            <AccountCircleIcon />
          </ListItemIcon>
        </MenuItem>
      </Menu>
    </div>
  );
}
