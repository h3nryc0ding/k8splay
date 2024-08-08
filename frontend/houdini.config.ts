import type { ConfigFile } from 'houdini';

const config: ConfigFile = {
	watchSchema: {
		url: 'http://localhost:8080/graphql'
	},
	plugins: {
		'houdini-svelte': {}
	}
};

export default config;
