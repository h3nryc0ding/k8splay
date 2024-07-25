<script lang="ts">
	import { graphql } from '$houdini';
	import type { PageData } from './$houdini';
	import * as Dialog from '$lib/components/ui/dialog';
	import { Button } from '$lib/components/ui/button';
	import { Input } from '$lib/components/ui/input';
	import { onMount } from 'svelte';

	export let data: PageData;
	$: ({ AllMessage } = data);
	let username = '';
	let newMessage = '';
	let showDialog = false;

	const updates = graphql(`
		subscription MessageSent {
			messageSent {
				...All_Message_insert
			}
		}
	`);

	onMount(() => {
		updates.listen();
		return () => updates.unlisten();
	});

	const sendMessage = graphql(`
		mutation MessageSend($input: MessageInput!) {
			messageSend(input: $input) {
				...All_Message_insert
			}
		}
	`);

	async function handleSendMessage() {
		if (newMessage.trim() !== '' && username.trim() !== '') {
			await sendMessage.mutate({ input: { creator: username, text: newMessage } });
			newMessage = '';
		} else {
			showDialog = true;
		}
	}
</script>

<svelte:head>
	<title>Chat</title>
</svelte:head>

<Dialog.Root bind:open={showDialog}>
	<Dialog.Content>
		<Dialog.Header>
			<Dialog.Title>Please enter your username</Dialog.Title>
		</Dialog.Header>
		<div>
			<Input type="text" bind:value={username} placeholder="Username..." />
		</div>
		<Dialog.Footer>
			<Button variant="default" on:click={() => (showDialog = false)}>Confirm</Button>
			<Button
				variant="outline"
				on:click={() => {
					showDialog = false;
					username = '';
				}}
				>Dismiss
			</Button>
		</Dialog.Footer>
	</Dialog.Content>
</Dialog.Root>
<div class="flex h-[calc(100dvh-3.5rem-1px)] flex-col p-4">
	<div class="flex flex-1 flex-col items-center justify-center overflow-y-auto">
		<!-- Chat messages will go here -->
		<div class="flex flex-col">
			{#each $AllMessage.data?.messages || [] as message (message.id)}
				<!-- Individual chat message -->
				<div class="flex items-end justify-end">
					<div class="mx-2 my-1 max-w-xs rounded-lg bg-blue-600 p-2 text-white">
						<p>{message.text}</p>
						<p class="text-right text-sm italic">
							{message.creator} at {new Date(message.timestamp).toLocaleString('en-US')}
						</p>
					</div>
				</div>
			{/each}
		</div>
	</div>
	<!-- Message input field -->
	<div class="mt-4 flex-none">
		<div class="flex gap-x-2">
			<Input bind:value={newMessage} placeholder="Type your message..." />
			<Button on:click={handleSendMessage}>Send</Button>
		</div>
	</div>
</div>
