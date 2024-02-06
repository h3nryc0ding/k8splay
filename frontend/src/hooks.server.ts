import type { HandleFetch } from '@sveltejs/kit';
import { parse } from 'cookie';
import type { CookieSerializeOptions } from 'cookie';

export const handleFetch: HandleFetch = async ({ event, request, fetch }) => {
	const response = await fetch(request);
	const cookies = response.headers.getSetCookie();
	if (cookies) {
		for (const cookie of cookies) {
			const parsed = parse(cookie);
			const cookieName = Object.keys(parsed)[0];
			const options: CookieSerializeOptions & { path: string } = {
				path: parsed['Path'] || '/',
				expires: parsed['Expires'] ? new Date(parsed['Expires']) : undefined,
				maxAge: parsed['Max-Age'] ? parseInt(parsed['Max-Age'], 10) : undefined,
				domain: parsed['Domain'],
				sameSite: parsed['SameSite'] as CookieSerializeOptions['sameSite'],
				secure: cookie.toLowerCase().includes('secure'),
				httpOnly: cookie.toLowerCase().includes('httponly')
			};
			if (
				options.maxAge === 0 ||
				(options.expires && options.expires <= new Date()) ||
				parsed[cookieName] === ''
			) {
				event.cookies.delete(cookieName, options);
			} else {
				event.cookies.set(cookieName, parsed[cookieName], options);
			}
		}
	}

	return response;
};
