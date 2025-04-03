<script lang="ts">
	import type { PageData } from './$houdini';
	import { Input } from '$lib/components/ui/input';
	import { Label } from '$lib/components/ui/label';

	export let data: PageData;
	$: ({ User } = data);
</script>

<form class="grid w-full items-center gap-4">
	<Label for="id">ID</Label>
	<Input id="id" disabled value={$User.data?.user?.id} />
	<Label for="email">Email</Label>
	<Input id="email" disabled value={$User.data?.user?.email} />
	<Label for="username">Username</Label>
	<Input id="username" disabled value={$User.data?.user?.preferredUsername} />
</form>
