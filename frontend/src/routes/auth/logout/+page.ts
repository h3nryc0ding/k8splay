import { redirect } from '@sveltejs/kit';
import { logoutUrl } from '$lib/urls';

export function load() {
	throw redirect(302, logoutUrl());
}
