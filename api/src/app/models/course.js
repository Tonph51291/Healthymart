const { create } = require("express-handlebars");
const mongoose = require("mongoose");
const slug = require("mongoose-slug-generator");
mongoose.plugin(slug);
const Schema = mongoose.Schema;

const Course = new Schema({
  name: {
    type: String,
  },
  img: {
    type: String,
  },
  videoID: {
    type: String,
  },
  slug: {
    type: String,
    slug: "name",
  },

  createAt: {
    type: Date,
    default: Date.now,
  },
  updateAt: {
    type: Date,
    default: Date.now,
  },
});
module.exports = mongoose.model("Course", Course);
