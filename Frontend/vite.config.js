// import { defineConfig } from "vite";
// import react from "@vitejs/plugin-react";

// export default defineConfig({
//   plugins: [react()],
//   server: {
//     port: 5173,
//     proxy: {
//       "/api": {
//         target: "http://localhost:8080", // backend port
//         changeOrigin: true,
//         secure: false,
//         rewrite: (path) => path.replace(/^\/api/, "/mana-progress-aku"),
//       },
//     },
//   },
// });
import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";

export default defineConfig({
  plugins: [react()],
  server: {
    port: 5173,
    proxy: {
      "/api": {
        target: "http://localhost:8080", // Backend base URL
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, "/mana-progress-aku"),
      },
    },
  },
});
