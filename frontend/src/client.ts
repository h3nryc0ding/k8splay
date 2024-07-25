import { ErrorType, HoudiniClient, subscription } from '$houdini';
import { createClient } from 'graphql-ws';
import { redirect } from '@sveltejs/kit';
import { graphqlUrl, loginUrl, subscriptionUrl } from '$lib/urls';

export default new HoudiniClient({
	url: graphqlUrl(),
	fetchParams() {
		return {
			credentials: 'include'
		};
	},
	throwOnError: {
		operations: ['all'],
		error: async (errors: { message: string; extensions?: { errorType?: string } }[]) => {
			for (const error of errors) {
				if (error.extensions?.errorType && isErrorType(error.extensions.errorType)) {
					return handleError(error.extensions.errorType);
				}
			}
		}
	},
	plugins: [
		subscription(() =>
			createClient({
				url: subscriptionUrl()
			})
		)
	]
});

type ErrorType = 'UNAUTHENTICATED' | 'PERMISSION_DENIED';

function isErrorType(value: string): value is ErrorType {
	return value === 'UNAUTHENTICATED' || value === 'PERMISSION_DENIED';
}

function handleError(errorType: ErrorType) {
	switch (errorType) {
		// TODO: Handle other error types
		case 'UNAUTHENTICATED':
			return redirect(302, "auth/login");
		case 'PERMISSION_DENIED':
			return redirect(302, 'auth/login');
	}
}
