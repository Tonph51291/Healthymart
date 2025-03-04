const express = require("express");
const router = express.Router();
const BillController = require("../app/controllers/BillController");

// Define routes
router.post("/bills", BillController.createBill);
router.get("/bills", BillController.getAllBill);
router.get("/bills/:userId", BillController.getBillByUserId);
router.put("/bills/:id", BillController.updateBill);
router.delete("/bills/:id", BillController.deleteBill);

module.exports = router;
