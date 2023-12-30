<script lang="ts">
	import type { PageData } from './$houdini';
	import { graphql } from '$houdini';
	import * as Dialog from '$lib/components/ui/dialog';
	import { Button } from '$lib/components/ui/button';
	import { Input } from '$lib/components/ui/input';

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
	$: updates.listen();
	const sendMessage = graphql(`
		mutation MessageSend($input: MessageInput!) {
			messageSend(input: $input) {
				...All_Message_insert
			}
		}
	`);

	// Function to handle sending messages
	async function handleSendMessage() {
		if (newMessage.trim() !== '' && username.trim() !== '') {
			await sendMessage.mutate({ input: { creator: username, text: newMessage } });
			newMessage = '';
		} else {
			showDialog = true;
		}
	}
</script>

<Dialog.Root bind:open={showDialog}>
	<Dialog.Content>
		<Dialog.Header>
			<Dialog.Title>Please enter your username</Dialog.Title>
		</Dialog.Header>
		<div>
			<Input type="text" bind:value={username} placeholder="Username..." />
		</div>
		<Dialog.Footer>
			<Button on:click={() => (showDialog = false)}>Confirm</Button>
			<Button
				on:click={() => {
					showDialog = false;
					username = '';
				}}
				>Dismiss
			</Button>
		</Dialog.Footer>
	</Dialog.Content>
</Dialog.Root>
<div class="flex h-dvh flex-col bg-gray-200 p-3">
	<div class="flex flex-1 flex-col items-center justify-center overflow-y-auto">
		<!-- Chat messages will go here -->
		<div class="flex flex-col">
			{#each $AllMessage.data?.messages || [] as message (message.id)}
				<!-- Individual chat message -->
				<div class="flex items-end justify-end">
					<div class="mx-2 my-1 max-w-xs rounded-lg bg-blue-600 p-2 text-white">
						<p>{message.text}</p>
						<p class="text-sm">-{message.creator} at {message.timestamp}</p>
					</div>
				</div>
			{/each}
		</div>
	</div>
	<!-- Message input field -->
	<div class="mt-4 flex-none">
		<div class="flex">
			<Input
				bind:value={newMessage}
				class="mr-3 w-full rounded-lg border-2 bg-white px-4 py-2"
				placeholder="Type your message..."
			/>
			<Button on:click={handleSendMessage} class="rounded-lg bg-blue-600 px-4 py-2 text-white"
				>Send
			</Button>
		</div>
	</div>
</div>
