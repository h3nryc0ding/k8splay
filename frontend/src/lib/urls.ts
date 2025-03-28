import { browser, dev } from '$app/environment';
import { env } from '$env/dynamic/public';

export const backendUrl = () => {
	if (dev) return 'http://localhost:8080';
	if (browser) return `https://${env.BACKEND_DOMAIN}`;
	return 'http://backend:80';
};

export const loginUrl = () => {
	let host: string;
	if (dev) {
		host = 'http://localhost:8080';
	} else {
		host = `https://${env.BACKEND_DOMAIN}`;
	}
	return `${host}/oauth2/authorization/keycloak`;
};

export const graphqlUrl = () => {
	return `${backendUrl()}/graphql`;
};

export const subscriptionUrl = () => {
	if (dev) return 'ws://localhost:8080/subscriptions';
	return `wss://${env.BACKEND_DOMAIN}/subscriptions`;
};
