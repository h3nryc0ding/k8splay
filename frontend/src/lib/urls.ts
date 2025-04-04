import { env } from '$env/dynamic/public';

export const BACKEND_URI = new URL(
	env.npm_lifecycle_event != 'build'
		? env.BACKEND_URI ||
			(() => {
				throw new Error(`Environment variable 'BACKEND_URI' is not set`);
			})()
		: 'http://localhost:8080' // Fails during `pnpm run build` if empty
);

export const loginUrl = () => {
	return new URL('/oauth2/authorization/default', BACKEND_URI);
};

export const logoutUrl = () => {
	return new URL('/oauth2/revocation/default', BACKEND_URI);
};

export const graphqlUrl = () => {
	return new URL('/graphql', BACKEND_URI);
};

export const subscriptionUrl = () => {
	const subscriptionUri = new URL(BACKEND_URI);
	subscriptionUri.protocol = subscriptionUri.protocol === 'https:' ? 'wss:' : 'ws:';
	return new URL('/subscriptions', subscriptionUri);
};
