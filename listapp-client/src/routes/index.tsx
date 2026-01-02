import { createFileRoute } from '@tanstack/react-router';
import { LoginButton } from '../components/LoginButton';

export const Route = createFileRoute('/')({
  component: UnauthenticatedHome,
});

function UnauthenticatedHome() {
  return (
    <div style={{ padding: '2rem', textAlign: 'center' }}>
      <h1>Welcome</h1>
      <LoginButton />
    </div>
  );
}
