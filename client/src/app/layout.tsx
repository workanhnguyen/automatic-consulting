import type { Metadata } from 'next';
import { Inter } from 'next/font/google';

import { ReduxProvider } from '@/lib/redux/store/ReduxProvider';
import ThemeRegistry from '@/lib/theme/ThemeRegistry';
import './globals.scss';
import './App.scss';

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
      <ThemeRegistry>
        <body>
          <ReduxProvider>{children}</ReduxProvider>
        </body>
      </ThemeRegistry>
    </html>
  );
}
