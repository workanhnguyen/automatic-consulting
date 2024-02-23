import type { Metadata } from "next";

import { ReduxProvider } from "@/lib/redux/store/ReduxProvider";
import ThemeRegistry from "@/lib/theme/ThemeRegistry";
import CustomLayout from "@/lib/components/layouts";
import "./globals.scss";
import "./App.scss";

export const metadata: Metadata = {
  title: "OU | Tư vấn tuyển sinh tự động",
  description:
    "Hệ thống tư vấn tuyển sinh tự động, trả lời các câu hỏi về tuyển sinh Khoa Công nghệ thông tin - Trường Đại học Mở TP.HCM",
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
          <ReduxProvider>
            <CustomLayout>{children}</CustomLayout>
          </ReduxProvider>
        </body>
      </ThemeRegistry>
    </html>
  );
}
