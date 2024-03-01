<script lang="ts">
	import type { PageData } from './$houdini';

	export let data: PageData;
	$: ({ User } = data);
</script>

<pre>
	{JSON.stringify($User.data?.user, null, 2)}
</pre>
