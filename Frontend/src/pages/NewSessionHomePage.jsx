import { Button, Text, useDisclosure } from "@chakra-ui/react";
import React from "react";
import { useNavigate } from "react-router-dom";
import DeleteSessionDialog from "../components/DeleteSessionDialog";

const NewSessionHomePage = () => {
  const navigate = useNavigate();

  const {
    isOpen: deleteSessionIsOpen,
    onOpen: deleteSessionOnOpen,
    onClose: deleteSessionOnClose,
  } = useDisclosure();

  return (
    <div>
      <Text fontSize={"4xl"} fontWeight={"bold"}>
        Hello Din
      </Text>
      <Text>What do you want to do</Text>
      <Button onClick={() => navigate("/newExercise")}>Add Exercise</Button>
      <Button onClick={deleteSessionOnOpen}>Delete Session</Button>

      <DeleteSessionDialog
        isOpen={deleteSessionIsOpen}
        onClose={deleteSessionOnClose}
      />
    </div>
  );
};

export default NewSessionHomePage;
