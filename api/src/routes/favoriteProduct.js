const express = require("express");
const router = express.Router();
const FavoriteProductController = require("../app/controllers/FavoriteProductControllers");
const { authenticateToken } = require("../app/middleware/auth");

// API endpoints
router.get("/", authenticateToken, FavoriteProductController.getFavorites); // Lấy danh sách yêu thích
router.post("/add", authenticateToken, FavoriteProductController.addFavorite); // Thêm sản phẩm yêu thích
router.delete(
  "/remove",
  authenticateToken,
  FavoriteProductController.removeFavorite
); // Xóa sản phẩm yêu thích


module.exports = router;
