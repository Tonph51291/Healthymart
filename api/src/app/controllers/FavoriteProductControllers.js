const FavoriteProduct = require("../models/favoriteProduct");

class FavoriteProductController {
  // Lấy danh sách yêu thích của người dùng
  async getFavorites(req, res) {
    try {
      const userId = req.user.userId; // Lấy userId từ middleware
      const favorites = await FavoriteProduct.find({ userId }).populate(
        "productId"
      );

      res.status(200).json({
        message: "Danh sách sản phẩm yêu thích",
        favorites,
      });
    } catch (error) {
      res.status(500).json({ message: "Lỗi server!", error });
    }
  }

  // Thêm sản phẩm vào danh sách yêu thích
  async addFavorite(req, res) {
    try {
      const userId = req.user.userId; // Lấy userId từ middleware
      const { productId } = req.body;

      // Kiểm tra nếu đã tồn tại
      const existingFavorite = await FavoriteProduct.findOne({
        userId,
        productId,
      });
      if (existingFavorite) {
        return res
          .status(400)
          .json({ message: "Sản phẩm đã trong danh sách yêu thích!" });
      }

      const newFavorite = new FavoriteProduct({ userId, productId });
      await newFavorite.save();

      res
        .status(201)
        .json({
          message: "Đã thêm vào danh sách yêu thích!",
          favorite: newFavorite,
        });
    } catch (error) {
      res.status(500).json({ message: "Lỗi server!", error });
    }
  }

  // Xóa sản phẩm khỏi danh sách yêu thích
  async removeFavorite(req, res) {
    try {
      const userId = req.user.userId; // Lấy userId từ middleware
      const { productId } = req.body;

      const result = await FavoriteProduct.findOneAndDelete({
        userId,
        productId,
      });
      if (!result) {
        return res
          .status(404)
          .json({ message: "Sản phẩm không nằm trong danh sách yêu thích!" });
      }

      res.status(200).json({ message: "Đã xóa khỏi danh sách yêu thích!" });
    } catch (error) {
      res.status(500).json({ message: "Lỗi server!", error });
    }
  }
}

module.exports = new FavoriteProductController();
