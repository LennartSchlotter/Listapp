import { createTheme } from '@mui/material';
import { teal, indigo } from '@mui/material/colors';

export const baseTheme = createTheme({
  palette: {
    primary: {
      main: teal[500],
      light: teal[300],
      dark: teal[700],
    },
    secondary: {
      main: indigo[400],
      light: indigo[200],
      dark: indigo[600],
    },
    background: {
      default: '#F5F9FB',
      paper: '#FFFFFF',
    },
  },
  typography: {
    fontFamily: ['Inter', 'Roboto', '"Helvetica Neue"'].join(','),
    h1: {
      fontSize: '2.75rem',
      fontWeight: 600,
    },
    h2: {
      fontSize: '2.25rem',
      fontWeight: 600,
    },
    body1: {
      fontSize: '1rem',
      fontWeight: 400,
    },
    body2: {
      fontSize: '0.875rem',
      fontWeight: 400,
    },
  },
});
