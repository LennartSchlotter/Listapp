import type { FC } from 'react';
import { LoginButton } from '../components/LoginButton';

const LoginModule: FC = () => {
  return (
    <div className="min-h-screen bg-gray-50 flex flex-col justify-center py-12 sm:px-6 lg:px-8">
      <div className="sm:mx-auto sm:w-full sm:max-w-md">
        <div className="text-center">
          <h1 className="text-3xl font-bold text-gray-900 tracking-tight">
            Welcome
          </h1>
          <p className="mt-2 text-sm text-gray-600">Sign in to continue</p>
        </div>
      </div>

      <div className="mt-8 sm:mx-auto sm:w-full sm:max-w-md">
        <div className="bg-white py-10 px-6 shadow-sm sm:rounded-lg border border-gray-200">
          <div className="space-y-6">
            <div className="flex flex-col items-center gap-4">
              <p className="text-sm text-gray-500 text-center">
                Sign in with your Google account
              </p>
              <LoginButton />
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default LoginModule;
