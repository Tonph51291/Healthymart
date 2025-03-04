const express = require("express");
const router = express.Router();
const {
  addBillDetail,
  getListBillDetails,
  getBillDetails,
  updateBillDetail,
  deleteBillDetail,
  getBillDetailsByBillId,
  updateQuantityById,
} = require("../app/controllers/BillDetailsControllers");

// Routes for BillDetails
router.get("", getListBillDetails);
router.get("/:id", getBillDetails);
router.get("/billDetails/:billId", getBillDetailsByBillId);
router.post("/add", addBillDetail);
router.put("/update/:id", updateBillDetail);
router.delete("/delete/:id", deleteBillDetail);
router.put("/:id", updateQuantityById);

module.exports = router;
