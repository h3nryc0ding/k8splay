import type { HandleFetch } from '@sveltejs/kit';

export const handleFetch: HandleFetch = async ({ event, request, fetch }) => {
	const cookie = event.request.headers.get('cookie');
	if (cookie) request.headers.set('cookie', cookie);
	console.log('cookie', JSON.stringify(cookie, null, 2));
	console.log('event', JSON.stringify(event, null, 2));
	console.log('request', JSON.stringify(request, null, 2));
	return await fetch(request);
};
