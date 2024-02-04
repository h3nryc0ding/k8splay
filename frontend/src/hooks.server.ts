import type { HandleFetch } from '@sveltejs/kit';
import { parseCookies } from '$lib/utils';

export const handleFetch: HandleFetch = async ({ event, request, fetch }) => {
	const response = await fetch(request);
	const setCookie = response.headers.get('Set-Cookie');
	if (!setCookie) {
		return response;
	}
	const parsedCookies = parseCookies(setCookie);
	for (const [cookieName, cookieValue] of Object.entries(parsedCookies)) {
		event.cookies.set(cookieName, cookieValue.value, {
			...cookieValue,
			path: cookieValue.path || '/'
		});
	}
	return response;
};
