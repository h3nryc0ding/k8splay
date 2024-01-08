<script lang="ts">
	import { KeyRound, Moon, Sun } from 'lucide-svelte';
	import { Button } from '$lib/components/ui/button';
	import { toggleMode } from 'mode-watcher';

	const navItems: { name: string; href: string }[] = [
		{
			name: 'Chat',
			href: '/chat'
		}
	];
</script>

<header
	class="sticky top-0 z-50 w-full border-b bg-background/95 shadow-sm backdrop-blur supports-[backdrop-filter]:bg-background/60"
>
	<div class="container flex h-14 items-center">
		<nav class="flex items-center space-x-6 text-sm font-medium">
			<a href="/" class="mr-6 flex items-center space-x-2">
				<img class="h-8 w-8" src="/favicon.png" alt="project-b icon" />
			</a>
			{#each navItems as item}
				<a class="text-foreground/60 transition-colors hover:text-foreground/80" href={item.href}
					>{item.name}</a
				>
			{/each}
		</nav>
		<div class="flex flex-1 items-center justify-end space-x-2 sm:space-x-4">
			<nav class="flex items-center space-x-1">
				<Button on:click={toggleMode} variant="outline" size="icon">
					<Sun
						class="h-[1.2rem] w-[1.2rem] rotate-0 scale-100 transition-all dark:-rotate-90 dark:scale-0"
					/>
					<Moon
						class="absolute h-[1.2rem] w-[1.2rem] rotate-90 scale-0 transition-all dark:rotate-0 dark:scale-100"
					/>
					<span class="sr-only">Toggle theme</span>
				</Button>
				<Button href="/login" variant="ghost">
					<KeyRound class="mr-2 h-5 w-5" />
					Login
				</Button>
			</nav>
		</div>
	</div>
</header>
