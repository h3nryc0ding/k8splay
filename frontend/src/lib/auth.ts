import { goto } from '$app/navigation';
import { backendUrl, loginUrl } from './urls';

export async function logout() {
	await fetch(`${backendUrl()}/logout`, {
		method: 'POST',
		credentials: 'include',
		mode: 'no-cors'
	});
	await goto('/', { invalidateAll: true });
}

export function login() {
	window.location.href = loginUrl();
}
