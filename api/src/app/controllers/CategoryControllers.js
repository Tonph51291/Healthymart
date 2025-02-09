const Category = require("../models/category");
const multer = require("multer");
const path = require("path");

// Cấu hình lưu trữ

const storage = multer.diskStorage({
  destination: (req, file, cb) => {
    cb(null, "uploads/");
  },
  filename: (req, file, cb) => {
    const uniqueSuffix = Date.now() + "-" + Math.round(Math.random() * 1e9);
    cb(null, uniqueSuffix + path.extname(file.originalname)); // Đặt tên file duy nhất
  },
});

// Middleware xử lý upload
const upload = multer({ storage: storage });

class CategoryController {
  // Hiển thị form thêm sản phẩm
  index(req, res) {
    Category.find({})
      .then((categorys) => {
        categorys = categorys.map((category) => category.toObject());
        res.render("category", { categorys });
      })
      .catch((err) => {
        console.log(err);
        res.status(500).send("Server error");
      });
  }
  async getList(req, res) {
    try {
      const data = await Category.find({});
      res.json({
        status: 200,
        message: "Success",
        data: data,
      });
    } catch (error) {
      res.json({
        status: 500,
        message: "Server error",
        error: error.message,
      });
    }
  }
  // Hiển thị form thêm sản phẩm
  showAddForm(req, res) {
    res.render("category_add"); // Render đến file .hbs của form thêm
  }

  // Xử lý thêm sản phẩm
  addCategory(req, res) {
    const { CateName } = req.body;
    const CateImg = `/uploads/${req.file.filename}`;

    const newCategory = new Category({ CateName, CateImg });

    newCategory
      .save()
      .then(() => {
        res.redirect("/category");
      })
      .catch((err) => {
        console.error(err);
        res.status(500).send("Lỗi khi thêm danh mục.");
      });
  }
  showEditForm(req, res) {
    Category.findById(req.params.id)
      .then((category) =>
        res.render("category_edit", {
          category: category.toObject(),
        })
      )
      .catch((err) => console.log(err));
  }

  // Xử lý cập nhật category
  updateCategory(req, res) {
    const { id } = req.params;
    const { CateName } = req.body;
    const updateData = { CateName };

    // Nếu có ảnh mới, cập nhật ảnh
    if (req.file) {
      updateData.CateImg = `/uploads/${req.file.filename}`;
    }

    Category.findOneAndUpdate({ _id: id }, updateData, { new: true })
      .then((updatedCategory) => {
        if (!updatedCategory) {
          return res.status(404).send("Category not found");
        }
        res.redirect("/category"); // Quay lại danh sách
      })
      .catch((err) => {
        console.error(err);
        res.status(500).send("Lỗi khi cập nhật danh mục");
      });
  }
  deleteCategory(req, res) {
    const { id } = req.params;

    Category.findByIdAndDelete(id)
      .then(() => {
        res.redirect("/category"); // Sau khi xóa, quay lại danh sách category
      })
      .catch((err) => {
        console.error(err);
        res.status(500).send("Lỗi khi xóa danh mục.");
      });
  }
}

module.exports = new CategoryController();
