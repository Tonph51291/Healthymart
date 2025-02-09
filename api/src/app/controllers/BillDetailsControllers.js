const BillDetails = require("../models/billDetails"); // Import BillDetails model
const Product = require("../models/products"); // Import Product model

const showView = async (req, res) => {
  try {
    BillDetails.find({})
      .populate("ProductID")
      .then((bills) => {
        bills = bills.map((bill) => bill.toObject());
        res.render("donhang", { bills });
      })
      .catch((err) => console.log(err));
  } catch (error) {
    console.error("Error fetching bills:", error);
    res.status(500).send("Internal server error");
  }
};

const addBillDetail = async (req, res) => {
  const { ProductID, Quantity, SoDienThoai, DiaChi } = req.body;

  console.log(req.body);

  // Kiểm tra nếu thiếu trường nào
  // if (!ProductID || !Quantity || !SoDienThoai || !DiaChi) {
  //   return res.json({
  //     status: 400,
  //     message:
  //       "All fields are required: ProductID, Quantity, SoDienThoai, DiaChi",
  //   });
  // }

  try {
    // Kiểm tra sản phẩm tồn tại
    const product = await Product.findById(ProductID);
    if (!product) {
      return res.json({
        status: 404,
        message: "Product not found",
      });
    }

    // Tính tổng tiền
    const TongTien = product.Price * Quantity;

    // Tạo bản ghi mới
    const newBillDetail = new BillDetails({
      ProductID,
      Quantity,
      SoDienThoai,
      DiaChi,
      TongTien,
    });

    // Lưu vào cơ sở dữ liệu
    await newBillDetail.save();

    return res.json({
      status: 200,
      message: "Bill detail added successfully",
      data: newBillDetail,
    });
  } catch (error) {
    console.error("Error adding bill detail:", error);

    // Xử lý lỗi
    return res.json({
      status: 500,
      message: "Internal server error",
      error: error.message,
    });
  }
};

const getListBillDetails = async (req, res) => {
  try {
    // Fetch all BillDetails with Product information
    const billDetails = await BillDetails.find().populate(
      "ProductID", // Tên trường ProductID trong BillDetails
      "ProductName Price ProductImg Quantity" // Các trường cần lấy từ Product
    );

    return res.json({
      status: 200,
      message: "Success",
      data: billDetails,
    });
  } catch (error) {
    console.error(error);
    return res.status(500).json({ message: "Internal server error" });
  }
};

// Export the controller functions
module.exports = {
  addBillDetail,
  getListBillDetails,
  showView,
};
