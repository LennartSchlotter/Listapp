import type { FC } from 'react';

export const LoginButton: FC = () => {
  const handleLogin = () => {
    window.location.href = '/oauth2/authorization/google';
  };

  return (
    <button
      type="button"
      onClick={handleLogin}
      className="flex items-center justify-center gap-3 px-5 py-2.5 bg-white border border-gray-300 text-gray-800 text-sm font-medium rounded-md hover:bg-gray-50"
    >
      Sign in with Google
    </button>
  );
};
