import { createFileRoute } from '@tanstack/react-router';
import ProfileModule from '../../../features/Profile/components/ProfileModule';

export const Route = createFileRoute('/app/_authenticated/profile')({
  component: ProfileComponent,
});

function ProfileComponent() {
  return <ProfileModule />;
}
