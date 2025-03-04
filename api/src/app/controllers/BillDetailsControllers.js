const mongoose = require("mongoose");
const BillDetails = require("../models/billDetails");
const Bill = require("../models/bills");
const Product = require("../models/products");

// Lấy danh sách tất cả BillDetails
const getListBillDetails = async (req, res) => {
  try {
    const billDetails = await BillDetails.find();
    res.status(200).json(billDetails);
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
};

// Lấy chi tiết BillDetails theo ID
const getBillDetails = async (req, res) => {
  try {
    const { id } = req.params;
    const billDetail = await BillDetails.findById(id);
    if (!billDetail) {
      return res.status(404).json({ message: "BillDetails not found" });
    }
    res.status(200).json(billDetail);
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
};

// Thêm mới BillDetails
const addBillDetail = async (req, res) => {
  try {
    const { billId, productId, quantity } = req.body;
    console.log(req.body);

    // Kiểm tra xem hóa đơn có tồn tại không
    const bill = await Bill.findById(billId);
    if (!bill) {
      return res.status(404).json({ message: "Bill not found" });
    }

    // Kiểm tra sản phẩm có tồn tại không
    const product = await Product.findById(productId);
    console.log(product);
    if (!product) {
      return res.status(404).json({ message: "Product not found" });
    }

    // Kiểm tra sản phẩm đã tồn tại trong giỏ hàng chưa
    let existingBillDetail = await BillDetails.findOne({ billId, productId });

    if (existingBillDetail) {
      // Nếu sản phẩm đã có, tăng quantity lên 1
      existingBillDetail.quantity += quantity; // Thêm số lượng theo yêu cầu
      await existingBillDetail.save();
      return res.status(201).json(existingBillDetail);
    } else {
      // Nếu chưa có, tạo mới BillDetails
      const newBillDetail = new BillDetails({ billId, productId, quantity });
      await newBillDetail.save();

      // Lấy chi tiết BillDetails kèm thông tin sản phẩm
      const populatedBillDetail = await newBillDetail.populate("productId");
      return res.status(201).json(populatedBillDetail);
    }
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
};

// Cập nhật BillDetails
const updateBillDetail = async (req, res) => {
  try {
    const { id } = req.params;
    const { billId, productId, quantity } = req.body;

    const updatedBillDetail = await BillDetails.findByIdAndUpdate(
      id,
      { billId, productId, quantity },
      { new: true }
    );
    if (!updatedBillDetail) {
      return res.status(404).json({ message: "BillDetails not found" });
    }
    res.status(200).json(updatedBillDetail);
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
};

// Xóa BillDetails
const deleteBillDetail = async (req, res) => {
  try {
    const { id } = req.params;
    const deletedBillDetail = await BillDetails.findByIdAndDelete(id);
    if (!deletedBillDetail) {
      return res.status(404).json({ message: "BillDetails not found" });
    }
    res.status(200).json({ message: "BillDetails deleted successfully" });
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
};
const getBillDetailsByBillId = async (req, res) => {
  try {
    const { billId } = req.params;
    console.log("Received billId:", billId);

    // Kiểm tra nếu billId không phải ObjectId hợp lệ
    if (!mongoose.Types.ObjectId.isValid(billId)) {
      return res.status(400).json({ error: "Invalid billId format" });
    }

    // Convert billId thành ObjectId
    const billDetails = await BillDetails.find({
      billId: new mongoose.Types.ObjectId(billId),
    });

    if (!billDetails.length) {
      return res
        .status(404)
        .json({ message: "No BillDetails found for this billId" });
    }

    return res.status(200).json(billDetails);
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
};

const updateQuantityById = async (req, res) => {
  const { id } = req.params;
  const { quantity } = req.body;
  console.log("Id", id);
  console.log("quantity", quantity);
  try {
    if (!quantity || quantity < 1) {
      return res.status(400).json({ message: "Số lượng không hợp lệ!" });
    }

    const billDetail = await BillDetails.findById(id);
    if (!billDetail) {
      return res
        .status(404)
        .json({ message: "Không tìm thấy chi tiết hóa đơn!" });
    }

    billDetail.quantity = quantity;
    await billDetail.save();

    return res
      .status(200)
      .json({ message: "Cập nhật số lượng thành công!", data: billDetail });
  } catch (error) {
    console.error("Lỗi cập nhật số lượng:", error);
    return res.status(500).json({ message: "Lỗi server!" });
  }
};

module.exports = {
  getListBillDetails,
  getBillDetails,
  addBillDetail,
  updateBillDetail,
  deleteBillDetail,
  getBillDetailsByBillId,
  updateQuantityById,
};
