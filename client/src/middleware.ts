import { NextRequest, NextResponse } from 'next/server';

export function middleware(request: NextRequest, response: NextResponse) {
  // const cookie = request.cookies.get('token')?.value;
  return NextResponse.next();
}

export const config = {
  matcher: "/"
}
