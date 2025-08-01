const cloudinary = require("../config/cloudinary");

const FOLDERS = {
  PROFILE_PICS: "CakeWake/User_profile",
  CAKE_IMAGE: "CakeWake/Bakery/Cake",
  CAKE_COMPONENT: "CakeWake/Bakery/Cake_Component",
};

function isFileTypeSupported(type, supportedTypes) {
  return supportedTypes.includes(type);
}

// Upload Profile Picture
exports.uploadProfilePic = async (file) => {
  try {
    const result = await cloudinary.uploader.upload(file.tempFilePath, {
      folder: FOLDERS.PROFILE_PICS,
      resource_type: "auto",
    });
    return result.secure_url;
  } catch (error) {
    throw error;
  }
};

// Upload Cake Image
exports.uploadCakeImage = async (file) => {
  try {
    const result = await cloudinary.uploader.upload(file.tempFilePath, {
      folder: FOLDERS.CAKE_IMAGE,
      resource_type: "auto",
    });
    return result.secure_url;
  } catch (error) {
    throw error;
  }
};

// Upload Cake Component Image
exports.uploadCakeComponent = async (file) => {
  try {
    const result = await cloudinary.uploader.upload(file.tempFilePath, {
      folder: FOLDERS.CAKE_COMPONENT,
      resource_type: "auto",
    });
    return result.secure_url;
  } catch (error) {
    throw error;
  }
};

// Delete image by public_id
exports.deleteFromCloudinary = async (imageUrl) => {
  try {
    if (!imageUrl) return;

    // Determine which folder the image is from
    let folder = null;
    if (imageUrl.includes(FOLDERS.PROFILE_PICS)) {
      folder = FOLDERS.PROFILE_PICS;
    } else if (imageUrl.includes(FOLDERS.CAKE_IMAGE)) {
      folder = FOLDERS.CAKE_IMAGE;
    } else if (imageUrl.includes(FOLDERS.CAKE_COMPONENT)) {
      folder = FOLDERS.CAKE_COMPONENT;
    } else {
      console.log("Invalid URL format or unknown folder");
      return;
    }

    // Extract the part after the folder path
    const urlParts = imageUrl.split(folder + "/");
    if (urlParts.length < 2) {
      console.log("Invalid URL format or wrong folder");
      return;
    }

    // Remove file extension to get public_id
    const filename = urlParts[1].split(".")[0];
    const public_id = `${folder}/${filename}`;

    console.log("Attempting to delete:", public_id);
    const result = await cloudinary.uploader.destroy(public_id);
    console.log("Delete result:", result);
    return result;
  } catch (error) {
    console.error("Delete error:", error);
    throw error;
  }
};

// Image upload handler
exports.imageUpload = async (req, res) => {
  try {
    const file = req.files && (req.files.file || req.files.profilePicture || req.files.cakeImage || req.files.cakeComponent);
    if (!file) {
      return res.status(400).json({
        success: false,
        message: "No files uploaded",
      });
    }

    // Supported types
    const supportedTypes = ["jpg", "jpeg", "png", "webp"];
    const fileType = file.name.split(".").pop().toLowerCase();

    if (!isFileTypeSupported(fileType, supportedTypes)) {
      return res.status(400).json({
        success: false,
        message: "File format not supported",
      });
    }

    let imageUrl;
    if (req.files.profilePicture) {
      imageUrl = await exports.uploadProfilePic(file);
    } else if (req.files.cakeImage) {
      imageUrl = await exports.uploadCakeImage(file);
    } else if (req.files.cakeComponent) {
      imageUrl = await exports.uploadCakeComponent(file);
    } else {
      // Default to profile pic if not specified
      imageUrl = await exports.uploadProfilePic(file);
    }

    res.json({
      success: true,
      imageUrl,
      message: "Image uploaded successfully",
    });
  } catch (error) {
    res.status(400).json({
      success: false,
      message: "Upload failed",
      error: error.message,
    });
  }
};