const mongoose = require("mongoose");

const BillSchema = new mongoose.Schema({
  userId: { type: String, required: true },
  Date: { type: Date, default: Date.now },
  totalPrice: { type: Number, default: 0 },
  diaChi: { type: String, default: "" },
  soDienThoai: { type: String, default: "" },
});

module.exports = mongoose.model("Bill", BillSchema);
