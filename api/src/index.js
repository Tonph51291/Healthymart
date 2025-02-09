const path = require("path");
const express = require("express");
const morgan = require("morgan");
const { engine } = require("express-handlebars");
const app = express();
const port = 3000;
const route = require("./routes");
const methodOverride = require("method-override");

app.use(express.static(path.join(__dirname, "resources/views")));
app.use("/uploads", express.static(path.join(__dirname, "public/uploads")));
app.use(express.urlencoded({ extended: true }));
app.use(express.json());
app.use(methodOverride("_method"));

// HTTP logger
app.use(morgan("combined"));

// Set up Handlebars as the template engine
app.engine("hbs", engine({ extname: ".hbs" }));
app.set("view engine", "hbs");
app.set("views", path.join(__dirname, "resources/views"));
app.use(express.static(path.join(__dirname, "public")));
app.use("/uploads", express.static(path.join(__dirname, "uploads")));
const db = require("./config/db");
db.connect();

route(app);

app.listen(port, () => {
  console.log(`Example app listening on port ${port}`);
});
