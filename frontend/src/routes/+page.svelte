<script lang="ts">
	import type { PageData } from './$houdini';
	import { graphql } from '$houdini';

	export let data: PageData;
	$: ({ AllMessage } = data);

	const updates = graphql(`
		subscription MessageSent {
			messageSent {
				id
				text
				creator
				timestamp
			}
		}
	`);
	$: updates.listen();
</script>

<pre>
    {JSON.stringify($AllMessage.data, null, 2)}
</pre>
<pre>
    {JSON.stringify($updates.data?.messageSent)}
</pre>
