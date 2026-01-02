export function LoginButton() {
  const login = () => {
    window.location.href = '/oauth2/authorization/google';
  };

  return <button onClick={login}>Login with Google</button>;
}
