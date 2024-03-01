import type { HandleFetch } from '@sveltejs/kit';

export const handleFetch: HandleFetch = async ({ event, request, fetch }) => {
	const session = event.cookies.get('SESSION');
	if (session) request.headers.set('cookie', session);
	return await fetch(request);
};
