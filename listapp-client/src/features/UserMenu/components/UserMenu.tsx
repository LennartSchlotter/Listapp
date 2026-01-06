import React from 'react';
import CustomAvatar from './CustomAvatar';
import { MenuItem, IconButton, Menu, ListItemIcon } from '@mui/material';
import LogoutIcon from '@mui/icons-material/Logout';

export default function UserMenu() {
  const [anchorElement, setAnchorElement] = React.useState<null | HTMLElement>(
    null
  );
  const open = Boolean(anchorElement);

  const handleClick = (event: React.MouseEvent<HTMLElement>) => {
    setAnchorElement(event.currentTarget);
  };

  const handleClose = () => {
    setAnchorElement(null);
  };

  const handleLogout = () => {
    handleClose();
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
            handleLogout();
          }}
          className="box-border flex justify-center items-center text-center"
        >
          Log out
          <ListItemIcon onClick={handleLogout}>
            <LogoutIcon />
          </ListItemIcon>
        </MenuItem>
      </Menu>
    </div>
  );
}
