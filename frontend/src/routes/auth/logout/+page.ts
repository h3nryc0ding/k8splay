import { logout } from '$lib/auth';
import { redirect } from '@sveltejs/kit';

export function load() {
	logout();
	throw redirect(302, '/');
}