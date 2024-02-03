import type { PageServerLoad, Actions } from './$types';
import { fail, redirect } from '@sveltejs/kit';
import { superValidate, setError } from 'sveltekit-superforms/server';
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
		const res = await authenticate.mutate({ input: { username, password } }, { event });
		if (res.errors) {
			setError(form, 'error', 'Invalid username or password');
			return fail(400, {
				form,
				errors: res.errors
			});
		}
		throw redirect(303, 'account');
	}
};
