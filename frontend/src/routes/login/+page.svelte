<script lang="ts">
	import type { PageData } from './$types';
	import LoginForm from './form.svelte';
	export let data: PageData;
</script>

<LoginForm form={data.form} />
