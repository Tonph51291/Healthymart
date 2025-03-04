const bcrypt = require("bcrypt");
const jwt = require("jsonwebtoken");
const Account = require("../models/users"); // Model tài khoản
const FavoriteProduct = require("../models/favoriteProduct"); // Model yêu thích
const { mutipleMongooseToObject } = require("../../util/mongoose");

class AccountController {
  // Trang danh sách tài khoản
  async index(req, res) {
    try {
      const accounts = await Account.find();
      res.render("account", {
        accounts: mutipleMongooseToObject(accounts),
      });
    } catch (error) {
      console.error("Lỗi khi lấy danh sách tài khoản:", error);
      res.status(500).json({ message: "Lỗi server!" });
    }
  }

  // Đăng ký tài khoản
  async dangky(req, res) {
    try {
      const { name, email, password } = req.body;

      if (!name || !email || !password) {
        return res
          .status(400)
          .json({ message: "Vui lòng điền đầy đủ thông tin!" });
      }

      // Kiểm tra xem email đã tồn tại chưa
      const existingUser = await Account.findOne({ email });
      if (existingUser) {
        return res.status(400).json({ message: "Email đã được sử dụng!" });
      }

      const hashedPassword = await bcrypt.hash(password, 10);

      const newUser = new Account({
        name,
        email,
        password: hashedPassword,
      });

      await newUser.save();

      res.status(201).json({ message: "Đăng ký thành công!", user: newUser });
    } catch (error) {
      console.error("Lỗi đăng ký:", error);
      res.status(500).json({ message: "Lỗi server!", error });
    }
  }

  // Đăng nhập tài khoản
  async dangnhap(req, res) {
    try {
      const { email, password } = req.body;

      if (!email || !password) {
        return res
          .status(400)
          .json({ message: "Vui lòng nhập email và mật khẩu!" });
      }

      const user = await Account.findOne({ email });
      if (!user) {
        return res.status(404).json({ message: "Tài khoản không tồn tại!" });
      }

      const isPasswordValid = await bcrypt.compare(password, user.password);
      if (!isPasswordValid) {
        return res.status(401).json({ message: "Mật khẩu không chính xác!" });
      }

      const token = jwt.sign(
        { userId: user._id },
        process.env.JWT_SECRET || "secret_key",
        {
          expiresIn: "1h",
        }
      );

      res.status(200).json({
        message: "Đăng nhập thành công!",
        token,
        user: {
          _id: user._id,
          name: user.name,
          email: user.email,
        },
      });
    } catch (error) {
      console.error("Lỗi đăng nhập:", error);
      res.status(500).json({ message: "Lỗi server!", error });
    }
  }

  // Lấy danh sách sản phẩm yêu thích của người dùng
  async getFavorites(req, res) {
    try {
      const userId = req.user?.userId; // Lấy userId từ middleware xác thực

      if (!userId) {
        return res.status(401).json({ message: "Người dùng chưa đăng nhập!" });
      }

      const favorites = await FavoriteProduct.find({ userId }).populate(
        "productId"
      );

      res.status(200).json({
        message: "Danh sách sản phẩm yêu thích của bạn",
        favorites,
      });
    } catch (error) {
      console.error("Lỗi khi lấy sản phẩm yêu thích:", error);
      res.status(500).json({ message: "Lỗi server!", error });
    }
  }
}

module.exports = new AccountController();
