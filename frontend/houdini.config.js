/** @type {import('houdini').ConfigFile} */
const config = {
	watchSchema: {
		url: `${process.env.BACKEND_URI}/graphql`
	},
	plugins: {
		'houdini-svelte': {}
	}
};

export default config;
