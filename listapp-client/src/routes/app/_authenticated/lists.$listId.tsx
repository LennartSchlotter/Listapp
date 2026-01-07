import { createFileRoute } from '@tanstack/react-router';
import ListModule from '../../../features/ListEditor/components/ListModule';

export const Route = createFileRoute('/app/_authenticated/lists/$listId')({
  component: ListComponent,
});

function ListComponent() {
  return <ListModule />;
}
