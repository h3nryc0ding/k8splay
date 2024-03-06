<article class="container prose my-8 max-w-4xl dark:prose-invert">
	<h1>MicroK8s Playground</h1>
	<h2>Motivation</h2>
	<p>
		This repository is a playground for me to experiment with Kubernetes and its ecosystem. It is a
		full stack application with a Spring Boot backend, a SvelteKit frontend, and a Keycloak server
		for authentication.
	</p>
	<h2>Features</h2>
	<ul>
		<li>GraphQL API</li>
		<li>SSO Authentication</li>
		<li>SSL Encryption using Let&#39;s Encrypt and cert-manager</li>
		<li>WebSocket based live chat</li>
	</ul>
	<h3>Backend</h3>
	<p>Deployed at <a href="https://api.k8splay.xyz/graphiql">api.k8splay.xyz</a></p>
	<ul>
		<li>Reactive Spring Boot with WebFlux and Kotlin</li>
		<li>Persistence with R2DBC and MongoDB</li>
		<li>Authentication and Authorization using Keycloak and oAuth2</li>
		<li>GraphiQL available at <code>/graphiql</code></li>
	</ul>
	<h3>Frontend</h3>
	<p>Deployed at <a href="https://k8splay.xyz">k8splay.xyz</a></p>
	<ul>
		<li>SvelteKit</li>
		<li>ShadCN and TailwindCSS for styling</li>
		<li>Houdini GraphQL client for SSR</li>
		<li>Authenticated routes</li>
		<li>All pages support SSR with in-cluster backend requests</li>
	</ul>
	<h3>Keycloak</h3>
	<p>Deployed at <a href="https://accounts.k8splay.xyz">accounts.k8splay.xyz</a></p>
	<ul>
		<li>Optimized Docker image</li>
		<li>Persistence with PostgreSQL</li>
		<li>Custom theme</li>
	</ul>
	<h3>DevOps</h3>
	<p>
		Deployed at <code>&lt;PR&gt;.test.k8splay.xyz</code>. Please replace <code>&lt;PR&gt;</code>
		with the PR number, e.g. <code>pr-1.test.k8splay.xyz</code>. The API is deployed at
		<code>api.&lt;PR&gt;.test.k8splay.xyz</code>.
		<br />The deployment is automatically deleted when the PR is closed.
	</p>
	<p>Implemented using GitHub Actions:</p>
	<ul>
		<li>Automated prod deployment</li>
		<li>Automated test deployments on pull requests</li>
		<li>Automated linting and testing</li>
	</ul>
</article>
