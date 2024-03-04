<script lang="ts">
	import '../app.pcss';
	import type { LayoutData } from './$types';

	import SiteHeader from '$lib/SiteHeader.svelte';
	import SiteFooter from '$lib/SiteFooter.svelte';
	import { ModeWatcher } from 'mode-watcher';

	export let data: LayoutData;
</script>

<svelte:head>
	<script>
		if (/iPhone|iPad|iPod/.test(navigator.userAgent)) {
			const viewport = document.querySelector('meta[name=viewport]');
			viewport.setAttribute('content', 'width=device-width, initial-scale=1.0, maximum-scale=1.0');
		}
	</script>
</svelte:head>

<ModeWatcher />
<div class="relative flex min-h-screen flex-col" id="page">
	<SiteHeader isAuthenticated={data.isAuthenticated} />
	<div class="flex-1">
		<slot />
	</div>
	<SiteFooter />
</div>
