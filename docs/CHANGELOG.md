# Changelog

All notable changes to ManaProgressAku will be here.

- ref - [keep a changelog](https://keepachangelog.com/en/1.1.0/)

# Unreleased

- better exercise information
- gmail registration and login

## [v1.4.0] - 2026-09-06

### Added

- User profile page (`/profile`) — users can optionally set weight, height, date of birth, and gender. All fields are optional; empty fields are visually flagged rather than blocking use of the app.
- Delete account flow, with password re-verification and an explicit warning that the action is permanent and irreversible. Deleting an account cascades to remove all of that user's sessions, activities, and sets at the database level.
- Bodyweight exercise support: exercises can be flagged `isBodyweight` by an admin (new toggle in Insert/Edit Exercise modals). When logging a set for a bodyweight exercise, weight autofills from the user's profile weight and is locked (reps remain editable); if no profile weight is on file, the field falls back to a normal editable input with a prompt linking to the profile page.
- Delete exercise, with confirmation dialog (admin only).
- `user_profile` table (1:1 with `users`), added via new `UserProfile` entity, repository, service, and `/getProfile`, `/updateProfile`, `/deleteAccount` endpoints.
- `is_bodyweight` column added to `exercise`.
- Expanded Postman/Newman API test suite covering profile CRUD and validation, the bodyweight admin toggle, account deletion (including token invalidation after delete), cross-user session access, unauthenticated access, and duplicate-active-session handling.

### Changed

- `Session.userId` changed from a raw column to a proper `@ManyToOne User` relationship, closing a gap where sessions could silently reference a deleted or nonexistent user with no database-level enforcement.
- Foreign keys across `session → users`, `activity → session`, `activityset → activity`, and `user_profile → users` now cascade on delete (`ON DELETE CASCADE`), so removing a user or a session cleanly removes everything beneath it.
- Admin-authenticated Postman requests now source credentials from a local `.env.local` file at test-run time instead of being stored in any committed or exported file.

### Fixed

- `GlobalExceptionHandler` was missing handlers for validation failures (`MethodArgumentNotValidException`) and malformed request bodies (`HttpMessageNotReadableException`); both previously went unhandled and were misreported as generic 401 Unauthorized responses instead of 400 Bad Request.
- `updateExercise`'s bodyweight flag was silently dropped on every request due to a getter/setter naming mismatch (`getBodyweight`/`setBodyweight` vs. the `isBodyweight` JSON key), so Jackson never bound the incoming value.
- `updateProfile` threw a NullPointerException on a user's first save (no existing profile row) instead of creating one; upsert logic corrected.
- `getProfile` returned 404 for any user without a profile yet instead of a valid empty response, breaking the profile page for all first-time visitors.
- Removed an orphaned `deleteExercise` code path that never refreshed the exercise list after a successful delete.
- Cleaned up pre-existing orphaned rows in `session` (and cascade-affected `activity`/`activityset` rows) that had accumulated due to the missing foreign key, across local and other environments.

## [v1.3.0] - 2026-08-20

### Added

- New app-wide navigation bar (`NavBar.jsx`) — bottom tab bar on mobile, top bar on desktop — linking Home, Exercise Library, Past Sessions, and Profile, with an elevated "Start Session" action in the center.
- "Start Session" from the navbar reuses the existing active-session check: resumes an in-progress session if one exists, otherwise opens the create-session dialog — available from any page, not just Home.

### Changed

- Active-session detection moved from `Home.jsx` into `App.jsx`, since the navbar now allows navigating to any page without visiting Home first. The check now re-runs on every route change (not just once on initial load), so an active session is still surfaced no matter where the user navigates.
- `Home.jsx` no longer owns active-session checking or `ActiveSessionDialog` — it now only handles its own setup (clearing stale activities, warming the exercise cache).
- Page content spacing (`pt`/`pb` in `App.jsx`) adjusted to account for the fixed navbar on both breakpoints.

### Fixed

- `ActiveSessionDialog`'s "Continue" action now correctly closes the dialog before navigating — previously it could remain "open" underneath the session page after navigating away.
- `finishSession` (session store) now returns a proper `{ success, message }` result on both failure paths (non-200 response and thrown error) instead of returning `undefined` — `ActiveSessionDialog`'s "Finish" action now surfaces an error toast on failure instead of silently closing as if it succeeded.
- Active-session check no longer re-prompts the user immediately after they choose "Continue" (excluded via route guard on `/createSession`).

## [v1.2.0] - 2026-08-13

### Added

- Full visual redesign across the app using a new design system (Tiber/Mist/Lime palette, Space Grotesk + Inter typography). Covers Home, Exercise List, New Session flow (New Exercise, Upper/Lower Body pickers, Current Activity), Past Sessions, Session Activities, Login, and Register.
- Ability to delete an unsaved set while logging an exercise (e.g. after accidentally clicking "+ Add Set").
- Saved sets are now locked from editing during active logging and sets can only be edited afterward via Edit Activity.
- Full-screen modals on mobile breakpoints for all modal dialogs (Exercise Detail, Edit/Insert Exercise, Upper/Lower Body pickers, Edit Activity).
- Cross-navigation links between Login and Register pages.
- Added design philosophy documentation to `docs/DESIGN_PHILOSOPHY.md` to guide future development and maintain visual consistency.

### Changed

- `ExerciseSummaryCard` set display converted from a wide horizontal table to a compact, scannable table optimized for mobile widths.
- All page backgrounds, cards, buttons, and form inputs unified under a single consistent visual language (see `docs/DESIGN_PHILOSOPHY.md`).

### Fixed

- Fixed a bug where finishing an exercise after deleting the in-progress (unsaved) set could re-submit an already-saved set, creating a duplicate entry.

## [v1.1.1] - 2026-08-06

### Features

- Added activity editing functionality.
  - Users can edit existing activity sets (weight, reps, and set number).
- Added exercise editing for administrators.
  - Admins can modify exercise name, description, and exercise type.

### Refactoring

- Refactored activity fetching architecture.
  - Standardized all activity retrieval through the Session Details endpoint.
  - Simplified state synchronization between Session and Activity stores.
- Reorganized frontend component structure.

### Improvements

- Reduced Zustand persistence.
  - Only persist data that should survive page refreshes (e.g. active session/activity IDs).
  - Prevent stale activity and session data after reloads.

### Bug Fixes

- Fixed password authentication by integrating Spring Security AuthenticationManager.
- Fixed activity deletion issues caused by Activity ↔ ActivitySet foreign key constraints.

## [v1.1.0] - 2026-05-25

### Added

- configure database table mapping (added activityset table)
- added checking for active session at Home page
- added session recovery after refresh
- configure UI to accommodate new table structure

## [v1.0.1] - 2026-05-20

Documentation and API testing

### Added

- Add Swagger dependencies and documentation for Controller and DTO
- Create smoke test files using Postman
- Added ReadMe, Architecture and ChangeLog
- Fetch all exercise when click "Add Activity"

---

# [v1.0.0] - 2026-05-18

Initial Production Release

### Added

- User authentication using JWT
- Login and registration
- Protected frontend routes
- Create workout sessions
- Add exercises
- Add workout activities
- View past sessions
- AWS deployment
