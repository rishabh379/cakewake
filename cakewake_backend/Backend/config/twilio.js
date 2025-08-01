require('dotenv').config();
const twilio = require("twilio");

const accountSid = process.env.TWILIO_ACCOUNT_SID;
const authToken = process.env.TWILIO_AUTH_TOKEN;
const twilioPhoneNumber = process.env.TWILIO_PHONE_NUMBER;

const client = twilio(accountSid, authToken);

async function sendOTPViaSMS(mobileNumber, otp) {
    try {
        const message = await client.messages.create({
            body: `Your OTP is: ${otp}`,
            from: twilioPhoneNumber,
            to: `+91${mobileNumber}`,
        });
        console.log(`OTP sent successfully to ${mobileNumber}: ${message.sid}`);
    } catch (error) {
        console.log("Error occurred while sending OTP: ", error);
        throw error;
    }
}

module.exports = {
    client,
    twilioPhoneNumber,
    sendOTPViaSMS,
};