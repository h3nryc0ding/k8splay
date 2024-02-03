import type { PageServerLoad, Actions } from './$types';
import { fail, redirect } from '@sveltejs/kit';
import { setError, superValidate } from 'sveltekit-superforms/server';
import { formSchema } from './schema';
import { graphql } from '$houdini';

export const load: PageServerLoad = async () => {
	return {
		form: await superValidate(formSchema)
	};
};
export const actions: Actions = {
	default: async (event) => {
		const form = await superValidate(event, formSchema);
		if (!form.valid) {
			return fail(400, {
				form
			});
		}

		const authenticate = graphql(`
			mutation Authenticate($input: AuthenticationInput!) {
				authenticate(input: $input)
			}
		`);
		const { username, password } = form.data;
		await authenticate.mutate({ input: { username, password } }, { event });
		throw redirect(303, 'account');
	}
};
