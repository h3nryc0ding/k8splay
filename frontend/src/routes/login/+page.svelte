<script lang="ts">
	import * as AlertDialog from '$lib/components/ui/alert-dialog';
	import { Button } from '$lib/components/ui/button';
	import { Code2 } from 'lucide-svelte';
</script>

<svelte:head>
	<title>Login</title>
</svelte:head>

<AlertDialog.Root open={true}>
	<AlertDialog.Content>
		<AlertDialog.Header>
			<AlertDialog.Title>
				Under Construction
				<Code2 class="inline" />
			</AlertDialog.Title>
			<AlertDialog.Description>
				This feature is currently under development. Although you cannot log in at the moment, this
				functionality will be available in the future. Stay tuned for updates!
			</AlertDialog.Description>
		</AlertDialog.Header>
		<AlertDialog.Footer>
			<AlertDialog.Action asChild={true}>
				<Button href="/">Continue</Button>
			</AlertDialog.Action>
		</AlertDialog.Footer>
	</AlertDialog.Content>
</AlertDialog.Root>
