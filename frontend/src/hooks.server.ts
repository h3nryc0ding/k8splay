import type { HandleFetch, Handle } from '@sveltejs/kit';
import { redirect } from '@sveltejs/kit';
import { loginUrl } from './client';

const COOKIE_NAME = 'SESSION';
const protectedPaths = ['/account'];

export const handle: Handle = async ({ event, resolve }) => {
	const { cookies, locals } = event;
	locals.isAuthenticated = !!cookies.get(COOKIE_NAME);
	if (protectedPaths.includes(event.url.pathname) && !locals.isAuthenticated) {
		throw redirect(307, loginUrl());
	}
	return resolve(event);
};

export const handleFetch: HandleFetch = async ({ event, request, fetch }) => {
	const { cookies } = event;
	const session = cookies.get(COOKIE_NAME);
	if (session) request.headers.set('cookie', session);
	return await fetch(request);
};
