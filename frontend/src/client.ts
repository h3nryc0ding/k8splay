import { HoudiniClient, subscription } from '$houdini';
import { createClient } from 'graphql-ws';
import { browser, dev } from '$app/environment';
import { env } from '$env/dynamic/public';
import { redirect } from '@sveltejs/kit';

export const backendUrl = () => {
	if (dev) return 'http://localhost:8080';
	if (browser) return `https://${env.PUBLIC_BACKEND_DOMAIN}`;
	return 'http://backend:80';
};

export const loginUrl = () => {
	let host: string;
	if (dev) {
		host = 'http://localhost:8080';
	} else {
		host = `https://${env.PUBLIC_BACKEND_DOMAIN}`;
	}
	return `${host}/oauth2/authorization/keycloak`;
};

const graphqlUrl = () => {
	return `${backendUrl()}/graphql`;
};
const subscriptionUrl = () => {
	if (dev) return 'ws://localhost:8080/subscriptions';
	return `wss://${env.PUBLIC_BACKEND_DOMAIN}/subscriptions`;
};

export default new HoudiniClient({
	url: graphqlUrl(),
	fetchParams() {
		return {
			credentials: 'include'
		};
	},
	throwOnError: {
		operations: ['all'],
		error: (errors: { message: string; extensions?: { errorType?: string } }[]) => {
			if (
				errors &&
				errors.some(
					(error) =>
						error?.extensions?.errorType &&
						error?.extensions?.errorType in new Set(['PERMISSION_DENIED', 'UNAUTHENTICATED'])
				)
			) {
				return redirect(302, loginUrl());
			}
		}
	},
	plugins: [
		subscription(() =>
			createClient({
				url: subscriptionUrl()
			})
		)
	]
});
