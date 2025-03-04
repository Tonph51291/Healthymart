const express = require("express");
const router = express.Router();
const upload = require("../app/middleware/upload");

const {
  getProductById,
  createProduct,
} = require("../app/controllers/CreateProduct");
router.get("/add", createProduct.showFormAddProduct);
router.post("/add", upload.single("ProductImg"), createProduct.addProduct);
router.post(
  "/:id/edit",
  upload.single("ProductImg"),
  createProduct.saveUpdatedProduct
);
router.get("/:id/edit", createProduct.updateProduct);
router.get("", createProduct.create);
router.get("/get_list_product", createProduct.getListProduct);
router.get("/:id/delete", createProduct.deleteProduct);
router.get("/get_list_product/:id", getProductById);

module.exports = router;
