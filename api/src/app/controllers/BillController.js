const Bill = require("../models/bills");
const BillDetails = require("../models/billDetails");
const Product = require("../models/products");
const users = require("../models/users");

// Create a new bill and add bill details
async function createBill(req, res) {
  try {
    const data = req.body;
    const newBill = new Bill({
      userId: data.userId,
    });
    const result = await newBill.save();
    res.status("200").json({
      status: "200",
      message: "Thành công",
      data: result,
    });
  } catch (error) {
    console.log(error);
  }
}

async function getAllBill(req, res) {
  try {
    const bills = await Bill.find({}).lean(); // Lấy danh sách hóa đơn

    const billData = await Promise.all(
      bills.map(async (bill) => {
        const billDetails = await BillDetails.find({ billId: bill._id }).lean(); // Tìm chi tiết hóa đơn theo billId

        // Lấy thông tin khách hàng
        const customer = await users.findById(bill.userId).lean();

        const products = await Promise.all(
          billDetails.map(async (detail) => {
            const product = await Product.findById(detail.productId).lean(); // Lấy thông tin sản phẩm
            return {
              ...product,
              quantity: detail.quantity, // Gán số lượng từ BillDetails
            };
          })
        );

        return {
          ...bill,
          customerName: customer ? customer.name : "Không xác định", // Lấy tên khách hàng, nếu không có thì để "Không xác định"
          products, // Danh sách sản phẩm kèm quantity
        };
      })
    );

    res.render("bills", { bills: billData });
  } catch (error) {
    console.error(error);
    res.status(500).send("Lỗi khi lấy hóa đơn");
  }
}

async function getBillByUserId(req, res) {
  try {
    const { userId } = req.params; // Lấy userId từ URL

    console.log("User ID:", userId);

    if (!userId) {
      return res.status(400).json({ message: "userId is required" });
    }

    // Tìm hóa đơn theo userId
    const bills = await Bill.find({ userId });

    res.status(200).json({
      status: 200,
      message: "Success",
      data: bills,
    });
  } catch (error) {
    console.error("Error fetching bills:", error);
    res.status(500).json({
      status: 500,
      message: "Internal server error",
      error: error.message,
    });
  }
}

// Update bill status
const updateBill = async (req, res) => {
  try {
    const { id } = req.params;
    const { userId, totalPrice, diaChi, soDienThoai } = req.body;
    console.log(req.body);

    // Kiểm tra Bill có tồn tại không
    let bill = await Bill.findById(id);
    if (!bill) {
      return res.status(404).json({ message: "Bill không tồn tại" });
    }

    // Cập nhật thông tin Bill
    bill.userId = userId || bill.userId;
    bill.totalPrice = totalPrice || bill.totalPrice;
    bill.soDienThoai = soDienThoai || bill.soDienThoai;
    bill.diaChi = diaChi || bill.diaChi;

    // Lưu cập nhật vào Database
    await bill.save();

    res.status(200).json({ message: "Cập nhật thành công", bill });
  } catch (error) {
    console.error("Lỗi cập nhật Bill:", error);
    res.status(500).json({ message: "Lỗi Server", error });
  }
};

// Delete bill and related bill details
const deleteBill = async (req, res) => {
  try {
    const { id } = req.params;

    // Delete related bill details
    await BillDetails.deleteMany({ BillID: id });

    // Delete the bill
    const deletedBill = await Bill.findByIdAndDelete(id);
    if (!deletedBill) {
      return res.status(404).json({
        status: 404,
        message: "Bill not found",
        data: null,
      });
    }

    res.status(200).json({
      status: 200,
      message: "Bill deleted successfully",
      data: deletedBill,
    });
  } catch (error) {
    console.error("Error deleting bill:", error);
    res.status(500).json({
      status: 500,
      message: "Error deleting bill",
      error: error.message,
    });
  }
};
const getBillDetailsByBillId = async (req, res) => {
  try {
    const { billId } = req.params;

    // Populate nhiều sản phẩm
    const billDetails = await BillDetails.find({ billId }).populate("products");

    if (!billDetails.length) {
      return res
        .status(404)
        .json({ message: "No products found for this bill" });
    }

    res.status(200).json(billDetails);
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
};

// Export controller functions
module.exports = {
  createBill,
  getAllBill,
  getBillDetailsByBillId,
  updateBill,
  deleteBill,
  getBillByUserId,
};
