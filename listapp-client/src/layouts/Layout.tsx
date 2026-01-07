import type { ReactNode } from 'react';
import Header from '../components/Header';

type childrenProps = {
  children: ReactNode;
};

export default function BaseLayout({ children }: childrenProps) {
  return (
    <div className="grid">
      <div>
        <Header />
      </div>
      <div className="overflow-y-auto">{children}</div>
    </div>
  );
}
