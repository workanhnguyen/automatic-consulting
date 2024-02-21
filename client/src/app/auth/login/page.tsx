'use client';

import { useEffect } from 'react';
import { useDispatch } from 'react-redux';

import { loginThunk } from '@/lib/redux/actions/Auth';
import { UserLogin } from '@/lib/redux/module';
import { AppDispatch } from '@/lib/redux/store';

const LoginPage = () => {
  const dispatch = useDispatch<AppDispatch>();

  useEffect(() => {
    const userAccount: UserLogin = {
      email: 'an@gmail.com',
      password: '1234',
    };

    dispatch(loginThunk(userAccount));
  }, []);

  return <div>LoginPage</div>;
};

export default LoginPage;
