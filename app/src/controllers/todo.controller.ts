export const getTodos = async (_: any, res: any) => {
  res.json([
    { id: 1, title: "Kerjain To-Do List", is_done: false },
    { id: 2, title: "Submit ALP", is_done: false }
  ]);
};