const mongoose = require("mongoose");
const BillDetailsSchema = new mongoose.Schema({
  billId: { type: mongoose.Schema.Types.ObjectId, ref: "Bill", required: true }, // FK to Bill

  productId: {
    type: mongoose.Schema.Types.ObjectId,
    ref: "Product",
    required: true,
  },
  quantity: { type: Number, required: true },
  totalPrice: { type: Number, default: 0 },
});

module.exports = mongoose.model("BillDetails", BillDetailsSchema);
