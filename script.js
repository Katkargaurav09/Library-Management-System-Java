fetch("finesection")
  .then((response) => {
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }
    return response.json();
  })
  .then((data) => {
    console.log("Received data:", data); // Debugging output
  })
  .catch((error) => {
    console.error("Error fetching data:", error);
  });
