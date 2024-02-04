import { test, expect, describe } from 'vitest';
import { parseCookies } from './utils';

describe('parseCookies function', () => {
	describe('should extract properties correctly', () => {
		describe('should extract `Secure`', () => {
			test('should be secure', () => {
				const cookieStr = 'Key=value; Path=/; Secure';
				const result = parseCookies(cookieStr);
				expect(result['Key'].secure).toBe(true);
			});
			test('should not be secure', () => {
				const cookieStr = 'Key=value; Path=/';
				const result = parseCookies(cookieStr);
				expect(result['Key'].secure).toBe(false);
			});
		});
		describe('Should extract `HTTPOnly`', () => {
			test('should be HTTPOnly', () => {
				const cookieStr = 'Key=value; Path=/; HTTPOnly';
				const result = parseCookies(cookieStr);
				expect(result['Key'].httpOnly).toBe(true);
			});
			test('should not be `HTTPOnly`', () => {
				const cookie = 'Key=value; Path=/';
				const result = parseCookies(cookie);
				expect(result['Key'].httpOnly).toBe(false);
			});
		});
		test('should extract `Path`', () => {
			const cookieStr = 'Key=value; Path=/login';
			const result = parseCookies(cookieStr);
			expect(result['Key'].path).toBe('/login');
		});
		test('should extract `Value`', () => {
			const cookieStr = 'Key=value; Path=/';
			const result = parseCookies(cookieStr);
			expect(result['Key'].value).toBe('value');
		});
		// TODO: Add test for `Expires`
		test.fails('should extract `Expires`', () => {
			const cookieStr = 'Key=value; Path=/; Expires=Wed, 21 Oct 2015 07:28:00 GMT';
			const result = parseCookies(cookieStr);
			expect(result['Key'].expires?.getTime()).toBe(Date.parse('Wed, 21 Oct 2015 07:28:00 GMT'));
		});
		test('should extract `Max-Age`', () => {
			const cookieStr = 'Key=value; Path=/; Max-Age=3600';
			const result = parseCookies(cookieStr);
			expect(result['Key'].maxAge).toBe(3600);
		});
		test('should extract `Domain`', () => {
			const cookieStr = 'Key=value; Path=/; Domain=example.com';
			const result = parseCookies(cookieStr);
			expect(result['Key'].domain).toBe('example.com');
		});
		describe('should extract `SameSite`', () => {
			test('should be `Lax`', () => {
				const cookieStr = 'Key=value; Path=/; SameSite=Lax';
				const result = parseCookies(cookieStr);
				expect(result['Key'].sameSite).toBe('lax');
			});
			test('should be `Strict`', () => {
				const cookieStr = 'Key=value; Path=/; SameSite=Strict';
				const result = parseCookies(cookieStr);
				expect(result['Key'].sameSite).toBe('strict');
			});
			test('should be `None`', () => {
				const cookieStr = 'Key=value; Path=/; SameSite=None';
				const result = parseCookies(cookieStr);
				expect(result['Key'].sameSite).toBe('none');
			});
		});
	});
	test('should extract multiple cookies', () => {
		const cookie = 'Key1=value1; Path=/; Secure, Key2=value2; Path=/; Secure';
		const result = parseCookies(cookie);
		expect(result['Key1'].value).toBe('value1');
		expect(result['Key2'].value).toBe('value2');
	});
});
