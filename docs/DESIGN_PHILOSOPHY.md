# ManaProgressAku: A Design Philosophy

This document is the visual reference for the ManaProgressAku frontend. If you're
adding a new page or component, check here first: the goal is that every screen
feels like it belongs to the same product, not like it was designed separately.

---

## 1. Direction

**Professional first, energetic second.**

ManaProgressAku should read as a modern fitness product. Not a school project,
not a generic Chakra UI demo, not a loud/colorful fitness app. Minimalism does
the "professional" work; the lime accent does the "energetic" work, used sparingly.

Avoid: gradients, emoji as UI elements, decorative-only icons, heavy shadows,
scale-heavy hover animations, more than two accent colors on one screen.

---

## 2. Color System

| Token       | Hex        | Use                                                                                      |
| ----------- | ---------- | ---------------------------------------------------------------------------------------- |
| `tiber.800` | `#063537`  | Brand base — headings, dark surfaces, headers, primary buttons                           |
| `tiber.900` | `#03201F`  | Deepest shade — strong text, high-contrast elements                                      |
| `tiber.600` | `#146059`  | Secondary accents, focus rings, upper-body indicator                                     |
| `tiber.500` | `#2A7D75`  | Borders, supporting elements                                                             |
| `lime.400`  | `#CCEE44`  | **Energetic accent — used sparingly.** CTA emphasis, active states, lower-body indicator |
| `mist.400`  | Background | Main page background — soft, warm-neutral, not stark white                               |
| `white`     | —          | Card/surface background, sits on top of `mist.400`                                       |
| `red.*`     | —          | Destructive actions only (delete, remove). Never used for branding.                      |

**Rule of three:** the working palette is Tiber, Mist, Lime — plus red reserved
for danger. Don't reach for Chakra's other color scales (`pink`, `purple`,
`blue`, `yellow`...) just because they exist. If a page ends up a different
color family from the rest of the app, that's a signal something drifted.

---

## 3. Typography

- **Headings** — Space Grotesk. Page titles, major headings, product labels.
  Gives the app a slightly technical, fitness-data feel.
- **Body** — Inter. Descriptions, labels, form fields, table values.
- Not every piece of text should be bold. Use weight to create hierarchy:
  bold/700 for titles, semibold/600 for important labels, regular for body copy.

---

## 4. Layout

Centered content area, generous margins on desktop, no content touching the
screen edges on any breakpoint.

```jsx
<Box
	minH="100vh"
	bg="mist.400"
	py={{ base: 6, md: 10 }}
	px={{ base: 4, md: 6 }}
>
	<Box w="100%" maxW="600px" mx="auto">
		{/* page content */}
	</Box>
</Box>
```

`maxW` varies by page (forms and single-column flows are narrower, ~380–480px;
list/dashboard pages can go up to ~900–1100px).

---

## 5. Cards

White surface, subtle shadow, rounded corners, a single **left accent spine**
instead of a colored background or gradient. This is the app's signature
pattern — it encodes meaning (category, status) without making the whole card
loud.

```jsx
<Box
  bg="white"
  borderRadius="xl"
  boxShadow="sm"
  borderLeft="5px solid"
  borderLeftColor="tiber.600" // or lime.400 — see "accent borders" below
>
```

Avoid `bgGradient` on cards. Avoid `boxShadow="2xl"` without a specific reason
— shadows should stay subtle (`sm`/`md`).

### Accent borders as a recurring language

Used consistently to encode category, not just decoration:

- **Upper body / primary flow** → `tiber.600`
- **Lower body / accent flow** → `lime.400`
- **Locked/inactive state** (e.g. a saved, non-editable set) → `mist.300`, plus
  reduced opacity on the whole card

---

## 6. Buttons

Three tiers, used consistently across the app:

| Style         | Look                                                          | When                                                                            |
| ------------- | ------------------------------------------------------------- | ------------------------------------------------------------------------------- |
| Primary       | `bg="tiber.800"` `color="white"`                              | Routine actions, "add", "begin", "log in"                                       |
| Energetic CTA | `bg="lime.400"` `color="tiber.900"`                           | Milestone / commit actions — "finish session", "create account", "save changes" |
| Destructive   | `variant="outline"` `borderColor="red.300"` `color="red.500"` | Delete, remove — always visually distinct from the other two                    |

Hover states are subtle — `translateY(-2px)` + a soft shadow bump, not scale
transforms. Avoid `transform: scale(1.05+)`; it reads as playful rather than
polished, and the app previously overused it.

---

## 7. Forms

White inputs, neutral border, Tiber focus ring, always a real `FormLabel` (not
just a placeholder — placeholders disappear once the user starts typing).

```jsx
<Input
	bg="white"
	borderColor="mist.300"
	borderRadius="lg"
	_hover={{ borderColor: "tiber.400" }}
	_focus={{ borderColor: "tiber.600", boxShadow: "0 0 0 1px #146059" }}
/>
```

---

## 8. Tables vs. stacked cards

Both are valid — the choice depends on what the user needs to _do_ with the data,
not a blanket rule.

- **Use a table** when the user needs to scan one attribute down a column to
  compare values at a glance (e.g. weight across sets, to spot a trend).
  Keep columns narrow and few (2–3) so nothing forces horizontal scroll at
  320px. Sets should be **rows**, not columns — a table with one column per
  set is the anti-pattern to avoid, since it grows unbounded and forces
  horizontal scroll as more sets are logged.
- **Use stacked cards/rows** when each item is a distinct, self-contained
  unit with its own actions (e.g. a session, an activity) — not a dense grid
  of comparable numbers.

---

## 9. Modals

Full-screen on mobile (`useBreakpointValue({ base: "full", md: "md" })`),
centered dialog on desktop. This is a settled convention — every modal in the
app follows it, so a new one should too.

Header uses Tiber (either as background for emphasis, or as text color on
white), footer buttons follow the same primary/CTA/destructive hierarchy as
regular buttons.

---

## 10. Loading & empty states

- Loading: `<Spinner color="tiber.600" />` with neutral supporting text at
  reduced opacity. Every page should use the same spinner color — don't let
  each page invent its own.
- Empty states: plain, no excessive decoration or emoji. State what's missing,
  and offer a clear next action if one exists.

```
No past sessions yet.
Start a workout session to see your progress here.
```

---

## 11. Icons

Use icons where they communicate meaning (edit, delete, back, chevron for
"opens something"). Don't add icons purely to decorate text that's already
clear on its own.

---

## 12. Before shipping a new screen

1. Does it use Tiber/Mist/Lime, and nothing outside that palette (aside from
   red for destructive actions)?
2. Are cards white with a left accent spine, not gradients or tinted backgrounds?
3. Does it work at 320–480px with no horizontal overflow?
4. Do modals go full-screen on mobile?
5. Do buttons follow the primary/CTA/destructive hierarchy?
6. Are loading and empty states styled consistently with the rest of the app?
7. Would this screen look out of place next to Exercise List, Home, or Past
   Sessions? If yes, figure out why before shipping.
