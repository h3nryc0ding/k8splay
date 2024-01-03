import { HoudiniClient, subscription } from '$houdini';
import { createClient } from 'graphql-ws';
import { browser, dev } from '$app/environment';

const graphqlUrl = () => {
	if (dev) return 'http://localhost:8080/graphql';
	if (browser) return 'https://api.130.61.237.219.nip.io/graphql';
	return 'http://backend:80/graphql';
};
const subscriptionUrl = () => {
	if (dev) return 'ws://localhost:8080/subscriptions';
	return 'ws://api.130.61.237.219.nip.io/subscriptions';
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
