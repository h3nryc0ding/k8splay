<script>
	import '../app.pcss';
</script>

<svelte:head>
	<script>
		if (/iPhone|iPad|iPod/.test(navigator.userAgent)) {
			const viewport = document.querySelector('meta[name=viewport]');
			viewport.setAttribute('content', 'width=device-width, initial-scale=1.0, maximum-scale=1.0');
		}
	</script>
</svelte:head>

<slot />
