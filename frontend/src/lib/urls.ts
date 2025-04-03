import { env } from '$env/dynamic/public';

export const BACKEND_URI = new URL(env.BACKEND_URI);

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
