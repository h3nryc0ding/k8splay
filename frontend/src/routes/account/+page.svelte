<script lang="ts">
	import type { PageData } from './$houdini';
	export let data: PageData;
	$: ({ CurrentUser } = data);
</script>

<pre>
	{JSON.stringify($CurrentUser.data?.currentUser, null, 2)}
</pre>
