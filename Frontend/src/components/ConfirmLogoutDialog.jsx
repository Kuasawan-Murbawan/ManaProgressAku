import {
  AlertDialog,
  AlertDialogHeader,
  AlertDialogContent,
  AlertDialogBody,
  AlertDialogFooter,
  AlertDialogOverlay,
  Button,
} from "@chakra-ui/react";
import React, { useRef } from "react";
import { useNavigate } from "react-router-dom";
import { useAuthStore } from "../store/auth";

const ConfirmLogoutDialog = ({ isOpen, onClose }) => {
  const cancelRef = useRef();
  const navigate = useNavigate();

  const handleConfirmLogout = async () => {
    setTimeout(2000);

    const authStore = useAuthStore.getState();
    authStore.logout();
  };

  return (
    <div>
      <AlertDialog
        leastDestructiveRef={cancelRef}
        isOpen={isOpen}
        onClose={onClose}
      >
        <AlertDialogOverlay>
          <AlertDialogContent>
            <AlertDialogHeader>Confirm Log Out?</AlertDialogHeader>
            <AlertDialogBody>Are you sure?</AlertDialogBody>
            <AlertDialogFooter>
              <Button ref={cancelRef} onClick={onClose}>
                Cancel
              </Button>
              <Button colorScheme="red" onClick={handleConfirmLogout}>
                Log Out
              </Button>
            </AlertDialogFooter>
          </AlertDialogContent>
        </AlertDialogOverlay>
      </AlertDialog>
    </div>
  );
};

export default ConfirmLogoutDialog;
