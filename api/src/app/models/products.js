const mongoose = require("mongoose");

const ProductSchema = new mongoose.Schema({
  ProductImg: { type: String, required: true }, // Đường dẫn ảnh
  ProductName: { type: String, required: true }, // Tên sản phẩm
  Price: { type: Number, required: true }, // Giá
  Quantity: { type: Number, required: true }, // Số lượng
  CateID: {
    type: mongoose.Schema.Types.ObjectId,
    ref: "Category",
    required: true,
  }, // Loại sản phẩm
});

module.exports = mongoose.model("Product", ProductSchema);
