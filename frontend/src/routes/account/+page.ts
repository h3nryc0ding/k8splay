import { redirect } from '@sveltejs/kit';
import type { AfterLoadEvent } from './$houdini';
import { loginUrl } from '../../client';

export function _houdini_afterLoad(event: AfterLoadEvent) {
	if (event.data.User.user == null) {
		throw redirect(307, loginUrl());
	}
}
