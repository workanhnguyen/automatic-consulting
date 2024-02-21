'use client';

import { useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import Link from 'next/link';

import { Button } from '@mui/material';

import { loginThunk } from '@/lib/redux/actions/Auth';
import { UserLogin } from '@/lib/redux/module';
import { AppDispatch, RootState } from '@/lib/redux/store';
// @ts-ignore
import Cookies from 'js-cookie';

const LoginPage = () => {
  const dispatch = useDispatch<AppDispatch>();
  const {} = useSelector((state: RootState) => state.auth);

  useEffect(() => {
    const account: UserLogin = {
      email: 'an@gmail.com',
      password: '1234',
    };
    const token = Cookies.get('token');
    !token && dispatch(loginThunk(account));
  }, []);

  return (
    <div>
      <Link href="/">
        <Button variant="contained" color="primary">
          Home
        </Button>
      </Link>
    </div>
  );
};

export default LoginPage;
