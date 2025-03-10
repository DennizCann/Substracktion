import * as functions from 'firebase-functions';
import * as admin from 'firebase-admin';
import * as nodemailer from 'nodemailer';

admin.initializeApp();

// Gmail için transporter oluştur
const transporter = nodemailer.createTransport({
    service: 'gmail',
    auth: {
        user: functions.config().email.user,
        pass: functions.config().email.pass
    }
});

export const sendEmailNotification = functions.firestore
    .document('notifications/{notificationId}')
    .onCreate(async (snap, context) => {
        const notification = snap.data();
        
        try {
            // Kullanıcı bilgilerini al
            const userDoc = await admin.firestore()
                .collection('users')
                .doc(notification.userId)
                .get();
            
            const user = userDoc.data();
            
            // E-posta bildirimleri kapalıysa gönderme
            if (!user?.emailNotifications) {
                console.log('Email notifications disabled for user:', notification.userId);
                return null;
            }

            // Mail gönder
            const mailOptions = {
                from: '"Substracktion" <noreply@substracktion.com>',
                to: user.email,
                subject: notification.title,
                html: `
                    <h2>${notification.title}</h2>
                    <p>${notification.body}</p>
                    <hr>
                    <small>Bu bir otomatik bildirimdir.</small>
                `
            };

            await transporter.sendMail(mailOptions);
            console.log('Email sent to:', user.email);
            return null;

        } catch (error) {
            console.error('Error sending email:', error);
            return null;
        }
    }); 