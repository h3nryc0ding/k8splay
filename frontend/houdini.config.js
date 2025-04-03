import { BACKEND_URI } from './src/lib/urls';

/** @type {import('houdini').ConfigFile} */
const config = {
	watchSchema: {
		url: `${BACKEND_URI}/graphql`
	},
	plugins: {
		'houdini-svelte': {}
	}
};

export default config;
