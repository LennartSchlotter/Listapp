import { createFileRoute } from '@tanstack/react-router';
import BaseLayout from '../../../layouts/Layout';
import MainModule from '../../../features/UserMenu/components/Project/components/MainModule';

export const Route = createFileRoute('/app/_authenticated/')({
  component: MainApp,
});

function MainApp() {
  return (
    <BaseLayout>
      <MainModule />
    </BaseLayout>
  );
}
