const express = require("express");
const router = express.Router();

const newHome = require("../app/controllers/HomeControllers");
router.get("/home", newHome.home);
router.get("", newHome.home);

module.exports = router;
