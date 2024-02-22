import { NextRequest, NextResponse } from 'next/server';

const privateRoutes = ['/', '/profile'];
const unauthorizedRoutes = ['/auth/login', '/auth/register'];

export default function middleware(request: NextRequest) {
  const token = request.cookies.get('token');
  if (!token && privateRoutes.includes(request.nextUrl.pathname)) {
    const absoluteUrl = new URL('/auth/login', request.nextUrl.origin);

    return NextResponse.redirect(absoluteUrl.toString());
  } else if (token && unauthorizedRoutes.includes(request.nextUrl.pathname)) {
    const absoluteUrl = new URL('/', request.nextUrl.origin);
    
    return NextResponse.redirect(absoluteUrl.toString());
  }
}
