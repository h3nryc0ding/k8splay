<script lang="ts">
	import * as Card from '$lib/components/ui/card';
	import { Button } from '$lib/components/ui/button';
	import { backendUrl } from '../../client';
</script>

<Card.Root class="m-auto max-w-3xl">
	<Card.Header>
		<Card.Title>Your Account</Card.Title>
		<Card.Description>Check the data related to your Account.</Card.Description>
	</Card.Header>
	<Card.Content>
		<slot />
	</Card.Content>
	<Card.Footer>
		<Button class="ml-auto" href={`${backendUrl()}/logout`} variant="outline">Logout</Button>
	</Card.Footer>
</Card.Root>
