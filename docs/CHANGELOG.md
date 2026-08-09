# Changelog

All notable changes to ManaProgressAku will be here.

- ref - [keep a changelog](https://keepachangelog.com/en/1.1.0/)

# Unreleased

- profile page
- better exercise information
- add user's weight and height to profile

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
