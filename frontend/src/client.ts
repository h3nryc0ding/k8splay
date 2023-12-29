import { HoudiniClient, subscription } from '$houdini';
import { createClient } from 'graphql-ws';

export default new HoudiniClient({
	url: 'http://api.130.61.237.219.nip.io/graphql',
	plugins: [
		subscription(() =>
			createClient({
				url: 'ws://api.130.61.237.219.nip.io/subscriptions'
			})
		)
	]
	// uncomment this to configure the network call (for things like authentication)
	// for more information, please visit here: https://www.houdinigraphql.com/guides/authentication
	// fetchParams({ session }) {
	//     return {
	//         headers: {
	//             Authentication: `Bearer ${session.token}`,
	//         }
	//     }
	// }
});
