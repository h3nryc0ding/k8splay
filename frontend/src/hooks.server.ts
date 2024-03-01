import type { HandleFetch } from '@sveltejs/kit';

export const handleFetch: HandleFetch = async ({ event, request, fetch }) => {
	console.log('cookies', event.cookies.getAll());
	const session = event.cookies.get('SESSION');
	console.log('session', session);
	if (session) request.headers.set('cookie', session);
	return await fetch(request);
};
