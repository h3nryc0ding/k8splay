import { test, expect } from '@playwright/test';

test('should have correct h1', async ({ page }) => {
	await page.goto('/');
	const text = await page.$eval('h1', (el) => el.textContent);
	expect(text).toBe('Welcome');
});
