import React, { useState } from "react";
import {
  AlertDialog,
  AlertDialogOverlay,
  AlertDialogContent,
  AlertDialogHeader,
  AlertDialogBody,
  AlertDialogFooter,
  Button,
} from "@chakra-ui/react";
import { useRef } from "react";
import { useNavigate } from "react-router-dom";
import { useSessionStore } from "../store/session";

const StartSessionDialog = ({ isOpen, onClose }) => {
  const cancelRef = useRef();
  const navigate = useNavigate();
  const { createSession } = useSessionStore();

  const [isLoading, setIsLoading] = useState(false);

  const handleConfirm = async () => {
    setIsLoading(true);
    setTimeout(2000);
    const now = new Date();
    const date = now.toISOString().split("T")[0];
    const time = now.toTimeString().split(" ")[0];

    const res = await createSession({ date, time });

    setIsLoading(false);

    if (res.success) {
      navigate("/createSession");
    } else {
      alert("Failed to start session");
    }
  };

  return (
    <div>
      <AlertDialog
        isOpen={isOpen}
        leastDestructiveRef={cancelRef}
        onClose={onClose}
      >
        <AlertDialogOverlay>
          <AlertDialogContent>
            <AlertDialogHeader>Start a New Session?</AlertDialogHeader>
            <AlertDialogBody>
              Are you sure you want to create a new session now?
            </AlertDialogBody>
            <AlertDialogFooter>
              <Button ref={cancelRef} onClick={onClose}>
                Cancel
              </Button>
              <Button colorScheme="blue" onClick={handleConfirm} ml={3}>
                Yes, Start
              </Button>
            </AlertDialogFooter>
          </AlertDialogContent>
        </AlertDialogOverlay>
      </AlertDialog>
    </div>
  );
};

export default StartSessionDialog;
