const mongoose = require("mongoose");

const BillSchema = new mongoose.Schema({
  Date: { type: Date, default: Date.now },
  Email: { type: String, ref: "Account", required: true }, // FK to Account
  status: { type: String, enum: ["pending", "completed"], default: "pending" },
  BillDetails: [{ type: mongoose.Schema.Types.ObjectId, ref: "BillDetails" }],
});

module.exports = mongoose.model("Bill", BillSchema);
