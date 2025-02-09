const mongoose = require("mongoose");
const Schema = mongoose.Schema;
const Users = new Schema({
  email: {
    type: String,
    required: true,
  },
  name: {
    type: String,
  },
  password: {
    type: String,
    unique: true,
  },
});
module.exports = mongoose.model("Users", Users);
//
