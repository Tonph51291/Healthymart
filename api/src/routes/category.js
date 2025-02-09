const express = require("express");
const router = express.Router();
const CategoryController = require("../app/controllers/CategoryControllers");
const upload = require("../app/middleware/upload");

// Hiển thị form thêm danh mục
router.get("", CategoryController.index);
router.get("/get_list_category", CategoryController.getList);
router.get("/add", CategoryController.showAddForm);

// Route xử lý thêm category
router.post("/add", upload.single("CateImg"), CategoryController.addCategory);
router.get("/:id/edit", CategoryController.showEditForm);

// Xử lý cập nhật category
router.post(
  "/:id/edit",
  upload.single("CateImg"),
  CategoryController.updateCategory
);
// Route xử lý xóa category
router.delete("/:id", CategoryController.deleteCategory);

module.exports = router;
