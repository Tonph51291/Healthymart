const Category = require("../models/category");
const Product = require("../models/products");
const multer = require("multer");
const path = require("path");
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
class CreateProduct {
  create(req, res) {
    Product.find({})
      .populate("CateID")
      .then((products) => {
        products = products.map((product) => product.toObject());
        res.render("product", { products });
      })
      .catch((err) => {
        console.error(err);
        res.status(500).send("Lỗi khi tải danh sách sản phẩm.");
      });
  }
  async getListProduct(req, res) {
    try {
      const data = await Product.find({}).populate("CateID");
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

  showFormAddProduct(req, res) {
    Category.find({})
      .then((categories) => {
        categories = categories.map((category) => category.toObject());
        res.render("product_add", { categories }); // Truyền danh sách category vào view
      })
      .catch((err) => {
        console.error(err);
        res.status(500).send("Lỗi khi tải danh mục.");
      });
  }

  addProduct(req, res) {
    const { ProductName, Quantity, Price, CateID } = req.body;
    const ProductImg = req.file ? `/uploads/${req.file.filename}` : null;

    console.log("Product Data:", {
      ProductName,
      Quantity,
      Price,
      CateID,
      ProductImg,
    });

    if (!ProductName || !Quantity || !Price || !CateID || !ProductImg) {
      return res.status(400).send("Tất cả các trường là bắt buộc.");
    }

    const newProduct = new Product({
      ProductName,
      Quantity,
      Price,
      ProductImg,
      CateID,
    });

    newProduct
      .save()
      .then(() => {
        res.redirect("/product"); // Quay lại danh sách sản phẩm
      })
      .catch((err) => {
        console.error(err);
        res.status(500).send("Lỗi khi thêm sản phẩm.");
      });
  }
  updateProduct(req, res) {
    const productId = req.params.id;

    // Tìm sản phẩm theo ID và hiển thị form cập nhật
    Product.findById(productId)
      .populate("CateID") // Kết hợp với danh mục nếu cần
      .then((product) => {
        if (!product) {
          return res.status(404).send("Sản phẩm không tồn tại.");
        }

        // Chuyển đổi dữ liệu sang Object
        const productData = product.toObject();

        // Lấy danh mục sản phẩm
        Category.find({})
          .then((categories) => {
            categories = categories.map((category) => category.toObject());
            res.render("product_up", { product: productData, categories });
          })
          .catch((err) => {
            console.error(err);
            res.status(500).send("Lỗi khi tải danh mục.");
          });
      })
      .catch((err) => {
        console.error(err);
        res.status(500).send("Lỗi khi tải sản phẩm.");
      });
  }
  saveUpdatedProduct(req, res) {
    const productId = req.params.id;
    const { ProductName, Quantity, Price, CateID } = req.body;
    const ProductImg = req.file ? `/uploads/${req.file.filename}` : null;

    // Cập nhật sản phẩm
    const updatedData = { ProductName, Quantity, Price, CateID };
    if (ProductImg) {
      updatedData.ProductImg = ProductImg;
    }

    Product.findByIdAndUpdate(productId, updatedData, { new: true })
      .then((updatedProduct) => {
        if (!updatedProduct) {
          return res.status(404).send("Sản phẩm không tồn tại.");
        }
        res.redirect("/product"); // Quay lại danh sách sản phẩm sau khi cập nhật
      })
      .catch((err) => {
        console.error(err);
        res.status(500).send("Lỗi khi cập nhật sản phẩm.");
      });
  }
  deleteProduct(req, res) {
    const productId = req.params.id;

    Product.findByIdAndDelete(productId)
      .then((deletedProduct) => {
        if (!deletedProduct) {
          return res.status(404).send("Sản phẩm không tồn tại.");
        }
        res.redirect("/product"); // Quay lại danh sách sản phẩm sau khi xóa
        console.log("Co ra");
      })
      .catch((err) => {
        console.error(err);
        res.status(500).send("Lỗi khi xóa sản phẩm.");
      });
  }
}
const getProductById = async (req, res) => {
  try {
    const { id } = req.params;
    const product = await Product.findById(id).populate("CateID");

    if (!product) {
      return res.status(404).json({ message: "Product not found" });
    }

    res.status(200).json({
      status: 200,
      message: "Success",
      data: product,
    });
  } catch (error) {
    res.status(500).json({
      status: 500,
      message: "Server error",
      error: error.message,
    });
  }
};
module.exports = {
  getProductById,
  createProduct: new CreateProduct(),
};
