import { redirect } from '@sveltejs/kit';
import { loginUrl } from '$lib/urls';

export function load() {
	throw redirect(302, loginUrl());
}
