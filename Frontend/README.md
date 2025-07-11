# React + Vite

This template provides a minimal setup to get React working in Vite with HMR and some ESLint rules.

Currently, two official plugins are available:

- [@vitejs/plugin-react](https://github.com/vitejs/vite-plugin-react/blob/main/packages/plugin-react) uses [Babel](https://babeljs.io/) for Fast Refresh
- [@vitejs/plugin-react-swc](https://github.com/vitejs/vite-plugin-react/blob/main/packages/plugin-react-swc) uses [SWC](https://swc.rs/) for Fast Refresh

## Expanding the ESLint configuration

If you are developing a production application, we recommend using TypeScript with type-aware lint rules enabled. Check out the [TS template](https://github.com/vitejs/vite/tree/main/packages/create-vite/template-react-ts) for information on how to integrate TypeScript and [`typescript-eslint`](https://typescript-eslint.io) in your project.

# NOTES

## Creating Activities Flow

1. User create session
2. Backend will create a session (return the created ID)
3. User create and save an exercise
4. Backend will save the exercise (while keeping track of the id)
5. User finish session

### foot notes

- JPA can handle many insertions well, no need to worry about performance
- Backend is already structured cleanly with Activity linked to Session and Exercise
- Avoid overengineering with batch logic unless you're seeing actual performance or UX issues
