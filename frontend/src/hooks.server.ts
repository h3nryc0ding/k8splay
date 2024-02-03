import type { HandleFetch } from '@sveltejs/kit';

interface CookieAttributes {
	value: string;
	path: string;
	secure: boolean;
	httpOnly: boolean;
}

export const handleFetch: HandleFetch = async ({ event, request, fetch }) => {
	const response = await fetch(request);
	const setCookie = response.headers.get('Set-Cookie');
	if (!setCookie) {
		return response;
	}
	const parsedCookies = parseCookies(setCookie);
	for (const [cookieName, cookieValue] of Object.entries(parsedCookies)) {
		event.cookies.set(cookieName, cookieValue.value, {
			...cookieValue
		});
	}
	return response;
};

function parseCookies(str: string): Record<string, CookieAttributes> {
	const cookies = str.split(/,(?=\s[A-Za-z]+=)/);
	return cookies.reduce(
		(acc, cookie) => {
			const [firstPart, ...restParts] = cookie.split(';');
			const [cookieName, ...cookieValueParts] = firstPart.split('=');
			const cookieValue = cookieValueParts.join('=').trim();
			const cookieAttributes = restParts.map((part) => part.trim());
			acc[decodeURIComponent(cookieName.trim())] = {
				value: decodeURIComponent(cookieValue),
				path:
					cookieAttributes.find((attr) => attr.toLowerCase().startsWith('path='))?.split('=')[1] ||
					'/',
				secure: cookieAttributes.includes('Secure'),
				httpOnly: cookieAttributes.includes('HTTPOnly')
			};
			return acc;
		},
		{} as Record<string, CookieAttributes>
	);
}
