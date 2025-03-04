const mongoose = require("mongoose");
const OderSchema = new mongoose.Schema({
  billId: { type: mongoose.Schema.Types.ObjectId, ref: "Bill", required: true }, // FK to Bill
  totalPrice: { type: Number, default: 0 },
});

module.exports = mongoose.model("Oder", OderSchema);
