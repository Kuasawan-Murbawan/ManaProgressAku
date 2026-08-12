# Changelog

All notable changes to ManaProgressAku will be here.

- ref - [keep a changelog](https://keepachangelog.com/en/1.1.0/)

# Unreleased

- profile page
- better exercise information
- add user's weight and height to profile
- gmail registration and login

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
