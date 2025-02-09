const jwt = require("jsonwebtoken");

const authenticateToken = (req, res, next) => {
  const authHeader = req.headers["authorization"];
  const token = authHeader && authHeader.split(" ")[1]; // Tách token từ header

  if (!token) {
    return res.status(401).json({ message: "Bạn cần đăng nhập để tiếp tục!" });
  }

  try {
    const decoded = jwt.verify(token, "secret_key"); // Giải mã token với secret_key
    req.user = decoded; // Gắn thông tin userId vào request
    next();
  } catch (err) {
    return res.status(403).json({ message: "Token không hợp lệ!" });
  }
};

module.exports = { authenticateToken };
