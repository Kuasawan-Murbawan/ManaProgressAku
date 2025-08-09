// import { React, useState, useCallback, useEffect } from "react";
// import { useBlocker } from "react-router-dom";

// export default function useNavigationBlocker(
//   when = true,
//   message = "Are you sure you want to leave this page?"
// ) {
//   // 1. Block in-app navigation (router links, back button)
//   useBlocker(({ retry }) => {
//     if (window.confirm(message)) {
//       retry(); // Allow navigation
//     }
//     // If user clicks cancel, navigation is prevented automatically
//   }, when);

//   // 2. Block browser tab close, refresh, or direct URL change
//   useEffect(() => {
//     if (!when) return;

//     const handleBeforeUnload = (event) => {
//       event.preventDefault();
//       event.returnValue = message; // Standard way to trigger browser prompt
//       return message;
//     };

//     window.addEventListener("beforeunload", handleBeforeUnload);
//     return () => window.removeEventListener("beforeunload", handleBeforeUnload);
//   }, [when, message]);
// }

import { useEffect } from "react";
import { useBeforeUnload, useLocation, useNavigate } from "react-router-dom";

export default function useNavigationBlocker(when = true) {
  const navigate = useNavigate();
  const location = useLocation();

  useEffect(() => {
    if (!when) return;

    const handleClick = (e) => {
      const target = e.target.closest("a[href]");
      if (target && target.href !== window.location.href) {
        const ok = window.confirm(
          "You have unsaved changes. Are you sure you want to leave?"
        );
        if (!ok) {
          e.preventDefault();
        }
      }
    };

    document.addEventListener("click", handleClick);
    return () => document.removeEventListener("click", handleClick);
  }, [when, location, navigate]);

  // Handle refresh / tab close
  useBeforeUnload(
    when
      ? (e) => {
          e.preventDefault();
          e.returnValue = "";
        }
      : null
  );
}
