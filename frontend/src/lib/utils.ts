import { type ClassValue, clsx } from 'clsx';
import { twMerge } from 'tailwind-merge';
import { cubicOut } from 'svelte/easing';
import type { TransitionConfig } from 'svelte/transition';
import type { CookieSerializeOptions } from 'cookie';

export function cn(...inputs: ClassValue[]) {
	return twMerge(clsx(inputs));
}

type FlyAndScaleParams = {
	y?: number;
	x?: number;
	start?: number;
	duration?: number;
};

export const flyAndScale = (
	node: Element,
	params: FlyAndScaleParams = { y: -8, x: 0, start: 0.95, duration: 150 }
): TransitionConfig => {
	const style = getComputedStyle(node);
	const transform = style.transform === 'none' ? '' : style.transform;

	const scaleConversion = (valueA: number, scaleA: [number, number], scaleB: [number, number]) => {
		const [minA, maxA] = scaleA;
		const [minB, maxB] = scaleB;

		const percentage = (valueA - minA) / (maxA - minA);
		const valueB = percentage * (maxB - minB) + minB;

		return valueB;
	};

	const styleToString = (style: Record<string, number | string | undefined>): string => {
		return Object.keys(style).reduce((str, key) => {
			if (style[key] === undefined) return str;
			return str + `${key}:${style[key]};`;
		}, '');
	};

	return {
		duration: params.duration ?? 200,
		delay: 0,
		css: (t) => {
			const y = scaleConversion(t, [0, 1], [params.y ?? 5, 0]);
			const x = scaleConversion(t, [0, 1], [params.x ?? 0, 0]);
			const scale = scaleConversion(t, [0, 1], [params.start ?? 0.95, 1]);

			return styleToString({
				transform: `${transform} translate3d(${x}px, ${y}px, 0) scale(${scale})`,
				opacity: t
			});
		},
		easing: cubicOut
	};
};

export function parseCookies(
	str: string
): Record<string, CookieSerializeOptions & { value: string }> {
	const cookies = str.split(/,/);
	return cookies.reduce(
		(acc, cookie) => {
			const [firstPart, ...restParts] = cookie.split(';');
			const [cookieName, ...cookieValueParts] = firstPart.split('=');
			const cookieValue = cookieValueParts.join('=').trim();
			const cookieAttributes = restParts.map((part) => part.trim());

			const attributes: CookieSerializeOptions & { value: string } = {
				value: decodeURIComponent(cookieValue),
				path: '/',
				secure: false,
				httpOnly: false
			};

			cookieAttributes.forEach((attr) => {
				if (attr.toLowerCase().startsWith('path=')) {
					attributes.path = attr.split('=')[1];
				} else if (attr.toLowerCase().startsWith('domain=')) {
					attributes.domain = attr.split('=')[1];
				} else if (attr.toLowerCase().startsWith('expires=')) {
					// fixme: this does not correctly parse the date
					attributes.expires = new Date(attr.split('=')[1]);
				} else if (attr.toLowerCase().startsWith('max-age=')) {
					attributes.maxAge = parseInt(attr.split('=')[1]);
				} else if (attr.toLowerCase() === 'secure') {
					attributes.secure = true;
				} else if (attr.toLowerCase() === 'httponly') {
					attributes.httpOnly = true;
				} else if (attr.toLowerCase().startsWith('samesite=')) {
					attributes.sameSite = attr.split('=')[1].toLowerCase() as 'strict' | 'lax' | 'none';
				}
			});

			acc[decodeURIComponent(cookieName.trim())] = attributes;
			return acc;
		},
		{} as Record<string, CookieSerializeOptions & { value: string }>
	);
}
