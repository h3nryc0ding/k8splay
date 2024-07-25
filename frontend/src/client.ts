import { ErrorType, HoudiniClient, subscription, type ErrorType$options } from '$houdini';
import { createClient } from 'graphql-ws';
import { redirect } from '@sveltejs/kit';
import { graphqlUrl, subscriptionUrl } from '$lib/urls';

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

function isErrorType(value: string): value is ErrorType$options {
	return value in ErrorType;
}

function handleError(errorType: ErrorType$options) {
	switch (errorType) {
		case ErrorType.UNAUTHENTICATED:
			return redirect(302, 'auth/login');
		case ErrorType.PERMISSION_DENIED:
			return redirect(302, 'auth/login');
		default:
			// TODO: Handle other error types
			console.warn('Unhandled error type:', errorType);
	}
}
