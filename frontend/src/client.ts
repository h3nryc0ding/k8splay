import { HoudiniClient, subscription } from '$houdini';
import { createClient } from 'graphql-ws';
import { browser, dev } from '$app/environment';
import { env } from '$env/dynamic/public';

const graphqlUrl = () => {
	if (dev) return 'http://localhost:8080/graphql';
	if (browser) return `https://${env.PUBLIC_BACKEND_DOMAIN}/graphql`;
	return 'http://backend:80/graphql';
};
const subscriptionUrl = () => {
	if (dev) return 'ws://localhost:8080/subscriptions';
	return `wss://${env.PUBLIC_BACKEND_DOMAIN}/subscriptions`;
};

export default new HoudiniClient({
	url: graphqlUrl(),
	plugins: [
		subscription(() =>
			createClient({
				url: subscriptionUrl()
			})
		)
	]
});
