import React from "react";
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

import { useSessionStore } from "../store/session";
import { useNavigate } from "react-router-dom";
import { useActivityStore } from "../store/activity";

const DeleteSessionDialog = ({ isOpen, onClose }) => {
  const cancelRef = useRef();
  const { deleteAllActivities } = useActivityStore();

  const navigate = useNavigate();
  const { deleteSession } = useSessionStore();

  const handleConfirm = async () => {
    const res = await deleteSession();

    if (res.success) {
      const result = await deleteAllActivities();
      if (result.success) {
        console.log("All activities deleted successfully");
      } else {
        console.log(result.message);
      }
      navigate("/");
    } else {
      alert("Failed to delete session");
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
            <AlertDialogHeader>Delete This Session?</AlertDialogHeader>
            <AlertDialogBody>
              This action <b>cannot</b> be undone.
            </AlertDialogBody>
            <AlertDialogFooter>
              <Button ref={cancelRef} onClick={onClose}>
                Cancel
              </Button>
              <Button colorScheme="red" onClick={handleConfirm} ml={3}>
                Yes, Delete
              </Button>
            </AlertDialogFooter>
          </AlertDialogContent>
        </AlertDialogOverlay>
      </AlertDialog>
    </div>
  );
};

export default DeleteSessionDialog;
