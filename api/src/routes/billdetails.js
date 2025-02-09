const express = require("express");
const router = express.Router();
const {
  addBillDetail,
  getListBillDetails,
  showView,
} = require("../app/controllers/BillDetailsControllers");
const { index } = require("../app/controllers/CategoryControllers");

// POST route to add a bill detail
router.get("/get_list_bill_details", getListBillDetails);
router.post("", addBillDetail);
router.get("", showView);

module.exports = router;
