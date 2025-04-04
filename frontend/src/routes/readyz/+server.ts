import { json } from '@sveltejs/kit';
import { BACKEND_URI } from '$lib/urls';

export async function GET() {
	try {
		const response = await fetch(BACKEND_URI, { signal: AbortSignal.timeout(3000) });

		if (!response.ok) {
			return json({ status: 'backend down' }, { status: 503 });
		}

		return json({ status: 'ready' }, { status: 200 });
	} catch (error) {
		return json({ status: 'backend unreachable' }, { status: 503 });
	}
}
