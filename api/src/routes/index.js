const homeControllers = require("../routes/home");
const createProduct = require("../routes/create");
const donhang = require("../routes/billdetails");
const category = require("../routes/category");
const account = require("../routes/account");
const favoriteProduct = require("../routes/favoriteProduct");
const bill = require("../routes/bill");

function route(app) {
  app.use("/account", account);
  app.use("/category", category);
  app.use("/billdetails", donhang);
  app.use("/favoriteProduct", favoriteProduct);
  app.use("/bill", bill);
  app.use("/product", createProduct);
  app.use("/", homeControllers);
}
module.exports = route;
