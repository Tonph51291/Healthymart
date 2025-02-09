const express = require("express");
const router = express.Router();

const Account = require("../app/models/users");

const AccountController = require("../app/controllers/AccountControllers");

router.post("/dangky", AccountController.dangky);
router.post("/dangnhap", AccountController.dangnhap);

router.get("", AccountController.index);

module.exports = router;
