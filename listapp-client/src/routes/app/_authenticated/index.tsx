import { createFileRoute } from '@tanstack/react-router';
import { CreateListButton } from '../../../components/CreateListButton';
import { GetListButton } from '../../../components/GetListsButton';

export const Route = createFileRoute('/app/_authenticated/')({
  component: MainApp,
});

function MainApp() {
  return (
    <div>
      <h1>Main Application</h1>
      <CreateListButton />
      <GetListButton />
    </div>
  );
}
