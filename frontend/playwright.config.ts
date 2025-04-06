import type { PlaywrightTestConfig } from '@playwright/test';

const config: PlaywrightTestConfig = {
	webServer: {
		command: 'pnpm run build && pnpm run preview',
		env: {
			BACKEND_URI: 'http://localhost:8080'
		},
		port: 4173
	},
	testDir: 'tests/playwright',
	testMatch: /(.+\.)?(test|spec)\.ts/
};

export default config;
