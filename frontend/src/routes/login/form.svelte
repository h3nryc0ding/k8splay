<script lang="ts">
	import * as Form from '$lib/components/ui/form';
	import { formSchema, type FormSchema } from './schema';
	import type { SuperValidated } from 'sveltekit-superforms';
	import { Loader2, Github, Linkedin, Code2 } from 'lucide-svelte';
	import { Button } from '$lib/components/ui/button';
	import * as AlertDialog from '$lib/components/ui/alert-dialog';
	export let form: SuperValidated<FormSchema>;
	const isLoading = false;
	let showNotImplemented = false;
</script>

<Form.Root method="POST" {form} schema={formSchema} let:config>
	<Form.Field {config} name="username">
		<Form.Item>
			<Form.Label>Email</Form.Label>
			<Form.Input
				placeholder="name@example.com"
				type="email"
				autocapitalize="none"
				autocomplete="email"
				autocorrect="off"
				disabled={isLoading}
			/>
			<Form.Validation />
		</Form.Item>
	</Form.Field>
	<Form.Field {config} name="password">
		<Form.Item>
			<Form.Label>Password</Form.Label>
			<Form.Input
				placeholder="*********"
				type="password"
				autocapitalize="none"
				autocomplete="current-password"
				autocorrect="off"
			/>
			<Form.Validation />
		</Form.Item>
	</Form.Field>
	<Form.Button disabled={isLoading} class="w-full">
		{#if isLoading}
			<Loader2 class="mr-2 h-4 w-4 animate-spin" />
		{/if}
		Sign In with Email
	</Form.Button>
	<Form.Field {config} name="error">
		<Form.Item>
			<Form.Validation class="text-center" />
		</Form.Item>
	</Form.Field>
</Form.Root>
<div class="relative">
	<div class="absolute inset-0 flex items-center">
		<span class="w-full border-t" />
	</div>
	<div class="relative flex justify-center text-xs uppercase">
		<span class="bg-background px-2 text-muted-foreground"> Or continue with </span>
	</div>
</div>
<div class="mx-auto flex w-full flex-col justify-center space-y-2 sm:w-[350px]">
	<Button
		on:click={() => (showNotImplemented = true)}
		variant="outline"
		type="button"
		disabled={isLoading}
	>
		{#if isLoading}
			<Loader2 class="mr-2 h-4 w-4 animate-spin" />
		{:else}
			<Github class="mr-2 h-4 w-4" />
		{/if}
		GitHub
	</Button>
	<Button
		on:click={() => (showNotImplemented = true)}
		variant="outline"
		type="button"
		disabled={isLoading}
	>
		{#if isLoading}
			<Loader2 class="mr-2 h-4 w-4 animate-spin" />
		{:else}
			<Linkedin class="mr-2 h-4 w-4" />
		{/if}
		Linkedin
	</Button>
</div>

<AlertDialog.Root open={showNotImplemented}>
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
				<Button on:click={() => (showNotImplemented = false)}>Continue</Button>
			</AlertDialog.Action>
		</AlertDialog.Footer>
	</AlertDialog.Content>
</AlertDialog.Root>
