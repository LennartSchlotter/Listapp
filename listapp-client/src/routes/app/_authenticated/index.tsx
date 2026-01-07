import { createFileRoute } from '@tanstack/react-router';
import AllListsModule from '../../../features/Lists/components/AllListsModule';

export const Route = createFileRoute('/app/_authenticated/')({
  component: AllLists,
});

function AllLists() {
  return <AllListsModule />;
}
