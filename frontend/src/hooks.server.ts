import type { HandleFetch } from '@sveltejs/kit';

export const handleFetch: HandleFetch = async ({ event, request, fetch }) => {
	const cookie = event.request.headers.get('cookie');
	if (cookie) request.headers.set('cookie', cookie);
	console.log(JSON.stringify(cookie, null, 2));
	console.log(JSON.stringify(event.request.headers.entries(), null, 2));
	return await fetch(request);
};
