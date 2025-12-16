import { Router } from "express";
import { getTodos } from "../controllers/todo.controller";

const router = Router();
router.get("/todos", getTodos);

export default router;
