import type { Metadata } from 'next';
import { Inter } from 'next/font/google';

import { ReduxProvider } from '@/lib/redux/store/ReduxProvider';
import './globals.scss';
import './App.scss';

const inter = Inter({ subsets: ['latin'] });

export const metadata: Metadata = {
  title: 'OU | Tư vấn tuyển sinh tự động',
  description:
    'Hệ thống tư vấn tuyển sinh tự động, trả lời các câu hỏi về tuyển sinh Khoa Công nghệ thông tin - Trường Đại học Mở TP.HCM',
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en">
      <body className={inter.className}>
        <ReduxProvider>{children}</ReduxProvider>
      </body>
    </html>
  );
}
