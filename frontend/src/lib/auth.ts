import { loginUrl, logoutUrl } from './urls';

export function login() {
	window.location.replace(loginUrl());
}

export function logout() {
	window.location.replace(logoutUrl());
}
