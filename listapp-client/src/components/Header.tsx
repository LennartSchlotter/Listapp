import { baseTheme } from '../styles/muiThemes';
import LogoImage from '/vite.svg';
import UserMenu from '../features/UserMenu/components/UserMenu';

export default function Header() {
  return (
    <div
      className="app-header flex items-center justify-between w-full px-6 py-3"
      style={{ backgroundColor: baseTheme.palette.secondary.main }}
    >
      <img
        src={LogoImage}
        alt="Application Logo"
        className="h-13 w-auto ml-2"
      />
      <div className="font-bebas text-5xl ml-8 justify-between w-full">
        Listapp
      </div>
      <div className="mr-2">
        <UserMenu />
      </div>
    </div>
  );
}
