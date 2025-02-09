const mongoose = require("mongoose");

const BillDetailsSchema = new mongoose.Schema({
  // FK to Bill
  ProductID: {
    type: mongoose.Schema.Types.ObjectId,
    ref: "Product",
  }, // FK to Product
  Quantity: { type: Number, required: true },

  SoDienThoai: {
    type: String,
    required: true,
  },
  DiaChi: {
    type: String,
    required: true,
  },
  TongTien: {
    type: Number,
    required: true,
  },
});

module.exports = mongoose.model("BillDetails", BillDetailsSchema);
