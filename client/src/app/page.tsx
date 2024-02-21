'use client';

import { loginThunk } from '@/lib/redux/actions/Auth';
import { UserLogin } from '@/lib/redux/module';
import { AppDispatch, RootState } from '@/lib/redux/store';
import Link from 'next/link';
import { useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';

const HomePage = () => {
  const dispatch = useDispatch<AppDispatch>();
  const {} = useSelector((state: RootState) => state.auth);

  useEffect(() => {
    const account: UserLogin = {
      email: 'an@gmail.com',
      password: '1234',
    };

    dispatch(loginThunk(account));
  }, []);

  return (
    <>
      <Link href="/about">About</Link>
      <Link href="/contact">Contact</Link>
    </>
  );
};

export default HomePage;
