import { useEffect, useEffectEvent, useState } from 'react';
import { useGetUser } from '../hooks/useGetUser';
import { useUpdateUser } from '../hooks/useUpdateUser';
import { useDeleteUser } from '../hooks/useDeleteUser';
import { useNavigate } from '@tanstack/react-router';

import Container from '@mui/material/Container';
import Paper from '@mui/material/Paper';
import Typography from '@mui/material/Typography';
import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button';
import Box from '@mui/material/Box';
import Divider from '@mui/material/Divider';
import Card from '@mui/material/Card';
import CardContent from '@mui/material/CardContent';
import Alert from '@mui/material/Alert';

export default function ProfileModule() {
  const navigate = useNavigate();
  const { isError, data } = useGetUser();
  const { mutate: updateUser } = useUpdateUser();
  const { mutate: deleteUser } = useDeleteUser();

  const [name, setName] = useState(data?.name ?? '');
  const [email, setEmail] = useState(data?.email ?? '');
  const [showDeleteConfirm, setShowDeleteConfirm] = useState(false);

  const updateFormFields = useEffectEvent((newData: typeof data) => {
    setName(newData!.name ?? '');
    setEmail(newData!.email ?? '');
  });

  useEffect(() => {
    if (data) {
      updateFormFields(data);
    }
  }, [data]);

  if (isError || !data) {
    return (
      <Container maxWidth="md" sx={{ py: 4 }}>
        <Alert severity="error">Could not load profile information.</Alert>
      </Container>
    );
  }

  const hasChanges = name !== (data.name ?? '') || email !== (data.email ?? '');

  const handleSave = () => {
    if (!hasChanges) return;
    updateUser({
      dto: {
        name: name.trim() || undefined,
        email: email.trim() || undefined,
      },
    });
  };

  const handleDelete = () => {
    deleteUser(undefined, {
      onSuccess: () => {
        navigate({ to: '/' });
      },
    });
  };

  return (
    <Container maxWidth="md" sx={{ py: 4 }}>
      <Box sx={{ mb: 5 }}>
        <Typography variant="h4" component="h1" gutterBottom>
          Profile
        </Typography>
        <Typography
          variant="body2"
          color="text.secondary"
          className="font-bebas"
        >
          Edit your personal information
        </Typography>
      </Box>
      <Paper variant="outlined" sx={{ mb: 5 }}>
        <Box
          sx={{
            p: 3,
            bgcolor: 'grey.50',
            borderBottom: 1,
            borderColor: 'divider',
          }}
        >
          <Typography variant="h6" component="h2">
            Profile Information
          </Typography>
        </Box>
        <Box sx={{ p: 3 }}>
          <TextField
            label="Display Name"
            fullWidth
            margin="normal"
            value={name}
            onChange={(e) => setName(e.target.value)}
            variant="outlined"
          />
          <TextField
            label="Email Address"
            fullWidth
            margin="normal"
            type="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            variant="outlined"
          />
          <Divider sx={{ my: 3 }} />
          <Typography variant="body2" color="text.secondary">
            Member since:{' '}
            {data.createdAt
              ? new Date(data.createdAt).toLocaleDateString()
              : '—'}
          </Typography>
          <Typography variant="body2" color="text.secondary" sx={{ mt: 1 }}>
            Last updated:{' '}
            {data.updatedAt
              ? new Date(data.updatedAt).toLocaleDateString()
              : '—'}
          </Typography>
        </Box>
        <Box
          sx={{
            p: 3,
            bgcolor: 'grey.50',
            borderTop: 1,
            borderColor: 'divider',
            textAlign: 'right',
          }}
        >
          <Button
            variant="contained"
            color="primary"
            disabled={!hasChanges}
            onClick={handleSave}
          >
            Save Changes
          </Button>
        </Box>
      </Paper>
      <Card variant="outlined" sx={{ borderColor: 'error.light' }}>
        <Box
          sx={{
            p: 3,
            bgcolor: 'error.50',
            borderBottom: 1,
            borderColor: 'error.light',
          }}
        >
          <Typography variant="h6" color="error.dark">
            Danger Zone
          </Typography>
        </Box>
        <CardContent sx={{ p: 3 }}>
          <Typography variant="body2" color="text.secondary" paragraph>
            Deleting your account is permanent and cannot be undone. All your
            data will be removed.
          </Typography>
          {!showDeleteConfirm ? (
            <Button
              variant="outlined"
              color="error"
              onClick={() => setShowDeleteConfirm(true)}
            >
              Delete Account
            </Button>
          ) : (
            <Box sx={{ mt: 2 }}>
              <Typography
                variant="body2"
                color="error.main"
                fontWeight="medium"
                gutterBottom
              >
                Are you sure? This action cannot be undone.
              </Typography>

              <Box sx={{ mt: 2, display: 'flex', gap: 2 }}>
                <Button
                  variant="outlined"
                  onClick={() => setShowDeleteConfirm(false)}
                >
                  Cancel
                </Button>
                <Button
                  variant="contained"
                  color="error"
                  onClick={handleDelete}
                >
                  Yes, confirm deletion
                </Button>
              </Box>
            </Box>
          )}
        </CardContent>
      </Card>
    </Container>
  );
}
