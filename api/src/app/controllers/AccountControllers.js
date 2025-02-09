const bcrypt = require("bcrypt");
const jwt = require("jsonwebtoken");
const Account = require("../models/users"); // Model tài khoản

const fs = require("fs");
const path = require("path");

const {
  mutipleMongooseToObject,
  mongooseToObject,
} = require("../../util/mongoose");
class AccountController {
  // Trang tài khoản
  // AccountController.js
  index(req, res) {
    Account.find()
      .then((accounts) => {
        console.log(accounts); // Add this line to check if accounts are being fetched
        res.render("account", {
          accounts: mutipleMongooseToObject(accounts),
        });
      })
      .catch((err) => {
        console.log(err);
        res.status(500).send("Server error");
      });
  }

  // Đăng ký tài khoản
  async dangky(req, res) {
    try {
      const { name, email, password } = req.body;

      // Kiểm tra thông tin
      if (!name || !email || !password) {
        return res
          .status(400)
          .json({ message: "Vui lòng điền đầy đủ thông tin!" });
      }

      // Hash mật khẩu
      const hashedPassword = await bcrypt.hash(password, 10);

      // Tạo tài khoản mới
      const newUser = new Account({
        name,
        email,
        password: hashedPassword,
      });

      await newUser.save();

      return res
        .status(201)
        .json({ message: "Đăng ký thành công!", user: newUser });
    } catch (error) {
      return res.status(500).json({ message: "Lỗi server!", error });
    }
  }

  // Đăng nhập
  async dangnhap(req, res) {
    try {
      const { email, password } = req.body;

      // Kiểm tra thông tin
      if (!email || !password) {
        return res
          .status(400)
          .json({ message: "Vui lòng nhập email và mật khẩu!" });
      }

      // Tìm tài khoản theo email
      const user = await Account.findOne({ email });
      if (!user) {
        return res.status(404).json({ message: "Tài khoản không tồn tại!" });
      }

      // Kiểm tra mật khẩu
      const isPasswordValid = await bcrypt.compare(password, user.password);
      if (!isPasswordValid) {
        return res.status(401).json({ message: "Mật khẩu không chính xác!" });
      }

      // Tạo token
      const token = jwt.sign({ userId: user._id }, "secret_key", {
        expiresIn: "1h",
      });

      return res.status(200).json({
        message: "Đăng nhập thành công!",
        token,
        user: {
          id: user._id,
          name: user.name,
          email: user.email,
        },
      });
    } catch (error) {
      return res.status(500).json({ message: "Lỗi server!", error });
    }
  }
  async getFavorites(req, res) {
    try {
      const userId = req.user.userId; // Middleware thêm userId vào req
      const favorites = await FavoriteProduct.find({ userId }).populate(
        "productId"
      );

      return res.status(200).json({
        message: "Danh sách sản phẩm yêu thích của bạn",
        favorites,
      });
    } catch (error) {
      return res.status(500).json({ message: "Lỗi server!", error });
    }
  }
}

module.exports = new AccountController();
