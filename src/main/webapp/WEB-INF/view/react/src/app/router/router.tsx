import { createBrowserRouter } from "react-router-dom";
import React from "react";
import Home from "../home/Home";

const router = createBrowserRouter([
	{
		path: "/",
		element: <Home />,
	},
]);

export default router;