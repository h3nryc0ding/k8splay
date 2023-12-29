import { describe, it, expect } from 'vitest';

describe('always pass test', () => {
	it('should always pass', () => {
		expect(true).to.equal(true);
	});
});
