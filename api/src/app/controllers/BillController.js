const Bill = require("../models/bills");
const BillDetails = require("../models/billDetails");

async function createBill(req, res) {
  console.log("Request body:", req.body);

  try {
    const { Email, status = "pending", billDetails } = req.body; //Ở d
    console.log("Email:", Email);
    console.log("Status:", status);
    console.log("Bill Details:", billDetails);

    if (
      !billDetails ||
      !Array.isArray(billDetails) ||
      billDetails.length === 0
    ) {
      return res.json({
        status: 400,
        message: "Invalid bill details",
      });
    }

    // Lưu từng chi tiết hóa đơn và lưu ObjectId vào mảng
    const billDetailsIds = [];
    for (const detail of billDetails) {
      const newBillDetail = new BillDetails(detail);
      const savedBillDetail = await newBillDetail.save();
      billDetailsIds.push(savedBillDetail._id);
    }

    const newBill = new Bill({
      Email,
      status,
      BillDetails: billDetailsIds,
    });

    await newBill.save();

    res.json({
      status: 200,
      message: "Bill created successfully",
      data: newBill,
    });
  } catch (error) {
    console.error("Lỗi khi tạo Bill:", error);
    res.json({ message: error.message });
  }
}

module.exports = createBill;

// Get all bills
const getAllBills = async (req, res) => {
  try {
    const bills = await Bill.find();
    res.status(200).json(bills);
  } catch (error) {
    console.error(error);
    res.status(500).json({ message: "Error fetching bills", error });
  }
};
// Get bill by ID
const getBillById = async (req, res) => {
  try {
    const { id } = req.params;
    const bill = await Bill.findById(id);

    if (!bill) {
      return res.status(404).json({ message: "Bill not found" });
    }

    const billDetails = await BillDetails.find({ BillID: id })
      .populate("ProductID")
      .exec();

    res.status(200).json({ bill, billDetails });
  } catch (error) {
    console.error(error);
    res.json({ message: error.message });
  }
};

// Update bill
const updateBill = async (req, res) => {
  try {
    const { id } = req.params;
    const { status } = req.body;

    const updatedBill = await Bill.findByIdAndUpdate(
      id,
      { status },
      { new: true }
    );

    if (!updatedBill) {
      return res.status(404).json({ message: "Bill not found" });
    }

    res
      .status(200)
      .json({ message: "Bill updated successfully", bill: updatedBill });
  } catch (error) {
    console.error(error);
    res.status(500).json({ message: "Error updating bill", error });
  }
};

// Delete bill
const deleteBill = async (req, res) => {
  try {
    const { id } = req.params;

    // Xóa chi tiết hóa đơn liên quan
    await BillDetails.deleteMany({ BillID: id });

    // Xóa hóa đơn
    const deletedBill = await Bill.findByIdAndDelete(id);

    if (!deletedBill) {
      return res.status(404).json({ message: "Bill not found" });
    }

    res.status(200).json({ message: "Bill deleted successfully" });
  } catch (error) {
    console.error(error);
    res.status(500).json({ message: "Error deleting bill", error });
  }
};

// Export controller functions
module.exports = {
  createBill,
  getAllBills,
  getBillById,
  updateBill,
  deleteBill,
};
